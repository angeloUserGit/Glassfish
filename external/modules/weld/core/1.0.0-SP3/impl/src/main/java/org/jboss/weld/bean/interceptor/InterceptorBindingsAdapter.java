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

package org.jboss.weld.bean.interceptor;

import static org.jboss.weld.logging.messages.BeanMessage.INTERCEPTION_MODEL_NULL;
import static org.jboss.weld.logging.messages.BeanMessage.INTERCEPTION_TYPE_LIFECYCLE;
import static org.jboss.weld.logging.messages.BeanMessage.INTERCEPTION_TYPE_NOT_LIFECYCLE;
import static org.jboss.weld.logging.messages.BeanMessage.INTERCEPTION_TYPE_NULL;
import static org.jboss.weld.logging.messages.BeanMessage.METHOD_NULL;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.enterprise.inject.spi.InterceptionType;
import javax.enterprise.inject.spi.Interceptor;

import org.jboss.interceptor.model.InterceptionModel;
import org.jboss.weld.ForbiddenArgumentException;
import org.jboss.weld.ejb.spi.InterceptorBindings;
import org.jboss.weld.serialization.spi.helpers.SerializableContextual;

/**
 * @author Marius Bogoevici
 */
public class InterceptorBindingsAdapter implements InterceptorBindings
{

   private InterceptionModel<Class<?>, SerializableContextual<Interceptor<?>, ?>> interceptionModel;

   public InterceptorBindingsAdapter(InterceptionModel<Class<?>, SerializableContextual<Interceptor<?>, ?>> interceptionModel)
   {
      if (interceptionModel == null)
      {
         throw new ForbiddenArgumentException(INTERCEPTION_MODEL_NULL);
      }
      this.interceptionModel = interceptionModel;
   }

   public Collection<Interceptor<?>> getAllInterceptors()
   {
      Collection<SerializableContextual<Interceptor<?>, ?>> contextualSet = interceptionModel.getAllInterceptors();
      return toInterceptorList(contextualSet);
   }

   public List<Interceptor<?>> getMethodInterceptors(InterceptionType interceptionType, Method method)
   {
      if (interceptionType == null)
      {
         throw new ForbiddenArgumentException(INTERCEPTION_TYPE_NULL);
      }

      if (method == null)
      {
         throw new ForbiddenArgumentException(METHOD_NULL);
      }

      org.jboss.interceptor.model.InterceptionType internalInterceptionType = org.jboss.interceptor.model.InterceptionType.valueOf(interceptionType.name());

      if (internalInterceptionType.isLifecycleCallback())
      {
         throw new ForbiddenArgumentException(INTERCEPTION_TYPE_LIFECYCLE, interceptionType.name());
      }

      return toInterceptorList(interceptionModel.getInterceptors(internalInterceptionType, method));

   }

   public List<Interceptor<?>> getLifecycleInterceptors(InterceptionType interceptionType)
   {
      if (interceptionType == null)
      {
         throw new ForbiddenArgumentException(INTERCEPTION_TYPE_NULL);
      }

      org.jboss.interceptor.model.InterceptionType internalInterceptionType = org.jboss.interceptor.model.InterceptionType.valueOf(interceptionType.name());

      if (!internalInterceptionType.isLifecycleCallback())
      {
         throw new ForbiddenArgumentException(INTERCEPTION_TYPE_NOT_LIFECYCLE, interceptionType.name());
      }

      return toInterceptorList(interceptionModel.getInterceptors(internalInterceptionType, null));
   }

   private List<Interceptor<?>> toInterceptorList(Collection<SerializableContextual<Interceptor<?>, ?>> contextualSet)
   {
      ArrayList<Interceptor<?>> interceptors = new ArrayList<Interceptor<?>>();
      for (SerializableContextual<Interceptor<?>, ?> serializableContextual : contextualSet)
      {
         interceptors.add(serializableContextual.get());
      }
      return interceptors;
   }

}
