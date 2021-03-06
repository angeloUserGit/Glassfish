/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.weld.introspector.jlr;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;

import org.jboss.weld.exceptions.DefinitionException;
import org.jboss.weld.introspector.MethodSignature;
import org.jboss.weld.introspector.WeldClass;
import org.jboss.weld.introspector.WeldMethod;
import org.jboss.weld.introspector.WeldParameter;
import org.jboss.weld.logging.messages.ReflectionMessage;
import org.jboss.weld.resources.ClassTransformer;
import org.jboss.weld.util.collections.ArrayListSupplier;
import org.jboss.weld.util.reflection.HierarchyDiscovery;
import org.jboss.weld.util.reflection.Reflections;
import org.jboss.weld.util.reflection.SecureReflections;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;

/**
 * Represents an annotated method
 * 
 * This class is immutable and therefore threadsafe
 * 
 * @author Pete Muir
 * 
 * @param <T>
 */
public class WeldMethodImpl<T, X> extends AbstractWeldCallable<T, X, Method> implements WeldMethod<T, X>
{

   // The underlying method
   private final Method method;

   // The abstracted parameters
   private final List<WeldParameter<?, X>> parameters;
   // A mapping from annotation type to parameter abstraction with that
   // annotation present
   private final ListMultimap<Class<? extends Annotation>, WeldParameter<?, X>> annotatedParameters;

   // The property name
   private final String propertyName;

   private final MethodSignature signature;

   public static <T, X> WeldMethodImpl<T, X> of(Method method, WeldClass<X> declaringClass, ClassTransformer classTransformer)
   {
      return new WeldMethodImpl<T, X>(method, (Class<T>) method.getReturnType(), method.getGenericReturnType(), new HierarchyDiscovery(method.getGenericReturnType()).getTypeClosure(), null, buildAnnotationMap(method.getAnnotations()), buildAnnotationMap(method.getDeclaredAnnotations()), declaringClass, classTransformer);
   }

   public static <T, X> WeldMethodImpl<T, X> of(AnnotatedMethod<? super X> method, WeldClass<X> declaringClass, ClassTransformer classTransformer)
   {
      return new WeldMethodImpl<T, X>(method.getJavaMember(), (Class<T>) method.getJavaMember().getReturnType(), method.getBaseType(), method.getTypeClosure(), method, buildAnnotationMap(method.getAnnotations()), buildAnnotationMap(method.getAnnotations()), declaringClass, classTransformer);
   }

   /**
    * Constructor
    * 
    * Initializes the superclass with the built annotation map, sets the method
    * and declaring class abstraction and detects the actual type arguments
    * 
    * @param method The underlying method
    * @param declaringClass The declaring class abstraction
    */
   @SuppressWarnings("unchecked")
   private WeldMethodImpl(Method method, final Class<T> rawType, final Type type, Set<Type> typeClosure, AnnotatedMethod<? super X> annotatedMethod, Map<Class<? extends Annotation>, Annotation> annotationMap, Map<Class<? extends Annotation>, Annotation> declaredAnnotationMap, WeldClass<X> declaringClass, ClassTransformer classTransformer)
   {
      super(annotationMap, declaredAnnotationMap, classTransformer, method, rawType, type, typeClosure, declaringClass);
      this.method = method;
      this.parameters = new ArrayList<WeldParameter<?, X>>();
      this.annotatedParameters = Multimaps.newListMultimap(new HashMap<Class<? extends Annotation>, Collection<WeldParameter<?, X>>>(), ArrayListSupplier.<WeldParameter<?, X>>instance());

      if (annotatedMethod == null)
      {
         for (int i = 0; i < method.getParameterTypes().length; i++)
         {
            if (method.getParameterAnnotations()[i].length > 0)
            {
               Class<? extends Object> clazz = method.getParameterTypes()[i];
               Type parametertype = method.getGenericParameterTypes()[i];
               WeldParameter<?, X> parameter = WeldParameterImpl.of(method.getParameterAnnotations()[i], clazz, parametertype, this, i, classTransformer);
               this.parameters.add(parameter);
               for (Annotation annotation : parameter.getAnnotations())
               {
                  if (MAPPED_PARAMETER_ANNOTATIONS.contains(annotation.annotationType()))
                  {
                     annotatedParameters.put(annotation.annotationType(), parameter);
                  }
               }
            }
            else
            {
               Class<? extends Object> clazz = method.getParameterTypes()[i];
               Type parameterType = method.getGenericParameterTypes()[i];
               WeldParameter<?, X> parameter = WeldParameterImpl.of(new Annotation[0], (Class<Object>) clazz, parameterType, this, i, classTransformer);
               this.parameters.add(parameter);
            }
         }
      }
      else
      {
         if (annotatedMethod.getParameters().size() != method.getParameterTypes().length)
         {
            throw new DefinitionException(ReflectionMessage.INCORRECT_NUMBER_OF_ANNOTATED_PARAMETERS_METHOD, annotatedMethod.getParameters().size(), annotatedMethod, annotatedMethod.getParameters(), Arrays.asList(method.getParameterTypes()));
         }
         else
         {
            for (AnnotatedParameter<? super X> annotatedParameter : annotatedMethod.getParameters())
            {
               WeldParameter<?, X> parameter = WeldParameterImpl.of(annotatedParameter.getAnnotations(), method.getParameterTypes()[annotatedParameter.getPosition()], annotatedParameter.getBaseType(), this, annotatedParameter.getPosition(), classTransformer);
               this.parameters.add(parameter);
               for (Annotation annotation : parameter.getAnnotations())
               {
                  if (MAPPED_PARAMETER_ANNOTATIONS.contains(annotation.annotationType()))
                  {
                     annotatedParameters.put(annotation.annotationType(), parameter);
                  }
               }
            }
         }
         
      }

      

      String propertyName = Reflections.getPropertyName(getDelegate());
      if (propertyName == null)
      {
         this.propertyName = getName();
      }
      else
      {
         this.propertyName = propertyName;
      }
      this.signature = new MethodSignatureImpl(this);

   }

   @Override
   public Method getDelegate()
   {
      return method;
   }

   public List<WeldParameter<?, X>> getWeldParameters()
   {
      return Collections.unmodifiableList(parameters);
   }

   public Class<?>[] getParameterTypesAsArray()
   {
      return method.getParameterTypes();
   }

   public List<WeldParameter<?, X>> getWeldParameters(Class<? extends Annotation> annotationType)
   {
      return Collections.unmodifiableList(annotatedParameters.get(annotationType));
   }

   public boolean isEquivalent(Method method)
   {
      return this.getDeclaringType().isEquivalent(method.getDeclaringClass()) && this.getName().equals(method.getName()) && Arrays.equals(this.getParameterTypesAsArray(), method.getParameterTypes());
   }


   public T invokeOnInstance(Object instance, Object... parameters) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
   {
      Method method = SecureReflections.lookupMethod(instance.getClass(), getName(), getParameterTypesAsArray());
      @SuppressWarnings("unchecked")
      T result = (T) SecureReflections.invoke(instance, method, parameters);
      return result;
   }

   public T invoke(Object instance, Object... parameters) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
   {
      @SuppressWarnings("unchecked")
      T result = (T) SecureReflections.invoke(instance, method, parameters);
      return result;
   }

   public String getPropertyName()
   {
      return propertyName;
   }

   @Override
   public String toString()
   {
      return new StringBuilder().append(method.toString()).toString();
   }

   public MethodSignature getSignature()
   {
      return signature;
   }

   public List<AnnotatedParameter<X>> getParameters()
   {
      return Collections.unmodifiableList((List) parameters);
   }
   
   public boolean isGeneric()
   {
      return getJavaMember().getTypeParameters().length > 0;
   }

}
