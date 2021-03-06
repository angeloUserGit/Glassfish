/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2009 Sun Microsystems, Inc. All rights reserved.
 *
 * Use is subject to license terms.
 *
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
package org.jboss.weld.context;

import java.lang.annotation.Annotation;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jboss.weld.context.api.BeanStore;

/**
 * The Application context
 * 
 * @author Nicklas Karlsson
 * 
 * @see org.jboss.weld.context.AbstractApplicationContext
 */
public abstract class AbstractApplicationContext extends AbstractMapContext
{

   // The beans
   private BeanStore beanStore;
   // Is the context active?
   private final AtomicBoolean active;

   /**
    * Constructor
    */
   public AbstractApplicationContext(Class<? extends Annotation> scope)
   {
      super(scope);
      this.active = new AtomicBoolean(false);
   }

   /**
    * Gets the bean store
    * 
    * @return The bean store
    */
   @Override
   public BeanStore getBeanStore()
   {
      return this.beanStore;
   }

   /**
    * Sets the bean store
    * 
    * @param applicationBeanStore The bean store
    */
   public void setBeanStore(BeanStore applicationBeanStore)
   {
      this.beanStore = applicationBeanStore;
   }

   /**
    * Indicates if the context is active
    * 
    * @return True if active, false otherwise
    */
   @Override
   public boolean isActive()
   {
      return active.get();
   }

   /**
    * Sets the active state of the context
    * 
    * @param active The new state
    */
   @Override
   public void setActive(boolean active)
   {
      this.active.set(active);
   }

   @Override
   public String toString()
   {
      String active = isActive() ? "Active " : "Inactive ";
      String beanStoreInfo = getBeanStore() == null ? "" : getBeanStore().toString();
      return active + "application context " + beanStoreInfo;
   }

   @Override
   protected boolean isCreationLockRequired()
   {
      return true;
   }

}
