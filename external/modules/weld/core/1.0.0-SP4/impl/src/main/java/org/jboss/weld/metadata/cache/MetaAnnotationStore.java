/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.weld.metadata.cache;

import java.lang.annotation.Annotation;
import java.util.concurrent.Callable;

import org.jboss.weld.bootstrap.api.Service;
import org.jboss.weld.resources.ClassTransformer;
import org.jboss.weld.util.collections.ConcurrentCache;

/**
 * Metadata singleton for holding EJB metadata, scope models etc.
 * 
 * @author Pete Muir
 * 
 */
public class MetaAnnotationStore implements Service
{

   // The stereotype models
   private ConcurrentCache<Class<? extends Annotation>, StereotypeModel<?>> stereotypes = new ConcurrentCache<Class<? extends Annotation>, StereotypeModel<?>>();
   // The scope models
   private ConcurrentCache<Class<? extends Annotation>, ScopeModel<?>> scopes = new ConcurrentCache<Class<? extends Annotation>, ScopeModel<?>>();
   // The binding type models
   private ConcurrentCache<Class<? extends Annotation>, BindingTypeModel<?>> bindingTypes = new ConcurrentCache<Class<? extends Annotation>, BindingTypeModel<?>>();
   // the interceptor bindings
   private ConcurrentCache<Class<? extends Annotation>, InterceptorBindingModel<?>> interceptorBindings = new ConcurrentCache<Class<? extends Annotation>, InterceptorBindingModel<?>>();

   private final ClassTransformer classTransformer;

   public MetaAnnotationStore(ClassTransformer classTransformer)
   {
      this.classTransformer = classTransformer;
   }

   /**
    * Gets a stereotype model
    * 
    * Adds the model if it is not present.
    * 
    * @param <T> The type
    * @param stereotype The stereotype
    * @return The stereotype model
    */
   public <T extends Annotation> StereotypeModel<T> getStereotype(final Class<T> stereotype)
   {
      return stereotypes.putIfAbsent(stereotype, new Callable<StereotypeModel<T>>()
      {

         public StereotypeModel<T> call() throws Exception
         {
            return new StereotypeModel<T>(stereotype, classTransformer);
         }
      });
   }

   /**
    * Gets a scope model
    * 
    * Adds the model if it is not present.
    * 
    * @param <T> The type
    * @param scopeType The scope type
    * @return The scope type model
    */
   public <T extends Annotation> ScopeModel<T> getScopeModel(final Class<T> scopeType)
   {
      return scopes.putIfAbsent(scopeType, new Callable<ScopeModel<T>>()
      {

         public ScopeModel<T> call() throws Exception
         {
            return new ScopeModel<T>(scopeType, classTransformer);
         }

      });
   }

   /**
    * Gets a binding type model.
    * 
    * Adds the model if it is not present.
    * 
    * @param <T> The type
    * @param bindingType The binding type
    * @return The binding type model
    */
   public <T extends Annotation> BindingTypeModel<T> getBindingTypeModel(final Class<T> bindingType)
   {
      return bindingTypes.putIfAbsent(bindingType, new Callable<BindingTypeModel<T>>()
      {

         public BindingTypeModel<T> call() throws Exception
         {
            return new BindingTypeModel<T>(bindingType, classTransformer);
         }

      });
   }

   /**
    * Gets a string representation
    * 
    * @return A string representation
    */
   @Override
   public String toString()
   {
      StringBuilder buffer = new StringBuilder();
      buffer.append("Metadata cache\n");
      buffer.append("Registered binding type models: " + bindingTypes.size() + "\n");
      buffer.append("Registered scope type models: " + scopes.size() + "\n");
      buffer.append("Registered stereotype models: " + stereotypes.size() + "\n");
      buffer.append("Registered interceptor binding models: " + interceptorBindings.size() + "\n");
      return buffer.toString();
   }
   
   public void cleanup() 
   {
      this.bindingTypes.clear();
      this.scopes.clear();
      this.stereotypes.clear();
      this.interceptorBindings.clear();
   }

   public <T extends Annotation> InterceptorBindingModel getInterceptorBindingModel(final Class<T> interceptorBinding)
   {
      return interceptorBindings.putIfAbsent(interceptorBinding, new Callable<InterceptorBindingModel<T>>()
      {

         public InterceptorBindingModel<T> call() throws Exception
         {
            return new InterceptorBindingModel<T>(interceptorBinding, classTransformer);
         }

      });
   }
}
