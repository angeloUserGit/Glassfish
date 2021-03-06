/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
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

package org.jboss.interceptor.proxy;

import org.jboss.interceptor.model.metadata.ClassReference;
import org.jboss.interceptor.model.InterceptorMetadata;
import org.jboss.interceptor.InterceptorException;
import org.jboss.interceptor.util.ReflectionUtils;

/**
 * @author <a href="mailto:mariusb@redhat.com">Marius Bogoevici</a>
 */
public class DirectClassInterceptionHandler<I> extends AbstractClassInterceptionHandler
{

   private final Object interceptorInstance;

   public DirectClassInterceptionHandler(Object interceptorInstance, InterceptorMetadata interceptorMetadata)
   {
      super(interceptorMetadata);
      this.interceptorInstance = interceptorInstance;
   }

   public DirectClassInterceptionHandler(Class<?> simpleInterceptorClass, InterceptorMetadata interceptorMetadata)
   {
      super(interceptorMetadata);
      if (simpleInterceptorClass == null)
         throw new IllegalArgumentException("Class must not be null");
      try
      {
         this.interceptorInstance = simpleInterceptorClass.newInstance();
      } catch (Exception e)
      {
         throw new InterceptorException("Cannot create interceptor instance:", e);
      }

   }

   public Object getInterceptorInstance()
   {
      return interceptorInstance;
   }


}

