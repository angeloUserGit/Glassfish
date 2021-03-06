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
package org.jboss.weld.el;

import static org.jboss.weld.el.ELCreationalContextStack.getCreationalContextStore;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import java.util.concurrent.Callable;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.enterprise.inject.spi.Bean;

import org.jboss.weld.BeanManagerImpl;

/**
 * An EL-resolver against the named beans
 *  
 * @author Pete Muir
 */
public abstract class AbstractWeldELResolver extends ELResolver
{
   
   protected abstract BeanManagerImpl getManager(ELContext context);

   @Override
   public Class<?> getCommonPropertyType(ELContext context, Object base)
   {
      return null;
   }

   @Override
   public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base)
   {
      return null;
   }

   @Override
   public Class<?> getType(ELContext context, Object base, Object property)
   {
      return null;
   }

   @Override
   public Object getValue(final ELContext context, Object base, Object property)
   {
      if (property != null)
      {
         String propertyString = property.toString();
         Namespace namespace = null;
         if (base == null) 
         {
            if (getManager(context).getRootNamespace().contains(propertyString))
            {
               context.setPropertyResolved(true);
               return getManager(context).getRootNamespace().get(propertyString);
            }
         }
         else if (base instanceof Namespace)
         {
            namespace = (Namespace) base;
            // We're definitely the responsible party
            context.setPropertyResolved(true);
            if (namespace.contains(propertyString))
            {
               // There is a child namespace
               return namespace.get(propertyString);
            }
         }
         else
         {
            // let the standard EL resolver chain handle the property
            return null;
         }
         final String name;
         if (namespace != null)
         {
            // Try looking in the manager for a bean
            name = namespace.qualifyName(propertyString);
         }
         else
         {
            name = propertyString;
         }
         Object value = null;
         try
         {
            final Bean<?> bean = getManager(context).resolve(getManager(context).getBeans(name));
            final ELCreationalContext<?> creationalContext = getCreationalContextStore(context).peek();
            if (bean != null)
            {
               value = creationalContext.putIfAbsent(bean, new Callable<Object>()
               {
                  
                  public Object call() throws Exception
                  {
                     return getManager(context).getReference(bean, creationalContext);
                  }
                  
               });
            }
         }
         catch (Exception e)
         {
            throw new RuntimeException("Error resolving property " + propertyString + " against base " + base, e);
         }
         if (value != null)
         {
            context.setPropertyResolved(true);
            return value;
         }
      }
      return null;
   }

   @Override
   public boolean isReadOnly(ELContext context, Object base, Object property)
   {
      return false;
   }

   @Override
   public void setValue(ELContext context, Object base, Object property, Object value)
   {
   }

}

