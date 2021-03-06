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
import javax.enterprise.inject.spi.BeanManager;

import org.jboss.webbeans.BeanManagerImpl;

public class ManagerBean extends AbstractStandardBean<BeanManagerImpl>
{
   
   private static final Set<Type> TYPES = new HashSet<Type>(Arrays.asList(BeanManagerImpl.class, BeanManager.class));
   
   public static final ManagerBean of(BeanManagerImpl manager)
   {
      return new ManagerBean(manager);
   }
   
   protected ManagerBean(BeanManagerImpl manager)
   {
      super(manager);
   }

   public BeanManagerImpl create(CreationalContext<BeanManagerImpl> creationalContext)
   {
      return getManager().getCurrent();
   }

   @Override
   public Class<BeanManagerImpl> getType()
   {
      return BeanManagerImpl.class;
   }

   public Set<Type> getTypes()
   {
      return TYPES;
   }

   public void destroy(BeanManagerImpl instance, CreationalContext<BeanManagerImpl> creationalContext)
   {
      // No-op
   }
   
   @Override
   public boolean isSerializable()
   {
      return true;
   }
   
   @Override
   public String toString()
   {
      return "Built-in javax.inject.manager.Manager bean";
   }
   
   
}