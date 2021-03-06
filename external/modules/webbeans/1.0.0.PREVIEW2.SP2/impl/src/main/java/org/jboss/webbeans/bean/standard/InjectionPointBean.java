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
package org.jboss.webbeans.bean.standard;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.webbeans.BeanManagerImpl;

/**
 * Bean for InjectionPoint metadata
 * 
 * @author David Allen
 * 
 */
public class InjectionPointBean extends AbstractStandardBean<InjectionPoint>
{
   
   private static final Set<Type> TYPES = new HashSet<Type>(Arrays.asList(new Type[] { InjectionPoint.class }));

   /**
    * Creates an InjectionPoint Web Bean for the injection of the containing bean owning
    * the field, constructor or method for the annotated item
    * 
    * @param <T> must be InjectionPoint
    * @param <S>
    * @param field The annotated member field/parameter for the injection
    * @param manager The RI manager implementation
    * @return a new bean for this injection point
    */
   public static InjectionPointBean of(BeanManagerImpl manager)
   {
      return new InjectionPointBean(manager);
   }

   protected InjectionPointBean(BeanManagerImpl manager)
   {
      super(manager);
   }

   public InjectionPoint create(CreationalContext<InjectionPoint> creationalContext)
   {
      return getManager().getInjectionPoint();
   }
   
   public void destroy(InjectionPoint instance, CreationalContext<InjectionPoint> creationalContext) 
   {
      
   }

   @Override
   public Class<InjectionPoint> getType()
   {
      return InjectionPoint.class;
   }

   public Set<Type> getTypes()
   {
      return TYPES;
   }
   
   @Override
   public String toString()
   {
      return "Built-in javax.inject.manager.InjectionPoint bean";
   }
   
}
