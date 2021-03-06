/* 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.felix.scr.impl;


import java.util.IdentityHashMap;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.ComponentInstance;
import org.osgi.service.log.LogService;


/**
 * The <code>ServiceFactoryComponentManager</code> TODO
 */
public class ServiceFactoryComponentManager extends ImmediateComponentManager implements ServiceFactory
{

    // maintain the map of ComponentContext objects created for the
    // service instances
    private IdentityHashMap serviceContexts = new IdentityHashMap();


    /**
     * @param activator
     * @param metadata
     */
    public ServiceFactoryComponentManager( BundleComponentActivator activator, ComponentMetadata metadata,
        long componentId )
    {
        super( activator, metadata, componentId );
    }


    /* (non-Javadoc)
     * @see org.apache.felix.scr.AbstractComponentManager#createComponent()
     */
    protected boolean createComponent()
    {
        // nothing to do, this is handled by getService
        return true;
    }


    /* (non-Javadoc)
     * @see org.apache.felix.scr.AbstractComponentManager#deleteComponent()
     */
    protected void deleteComponent()
    {
        // nothing to do, this is handled by ungetService
    }


    protected Object getService()
    {
        return this;
    }


    /* (non-Javadoc)
     * @see org.apache.felix.scr.AbstractComponentManager#getInstance()
     */
    public Object getInstance()
    {
        // this method is not expected to be called as the base call is
        // overwritten in the BundleComponentContext class
        return null;
    }


    /* (non-Javadoc)
     * @see org.osgi.framework.ServiceFactory#getService(org.osgi.framework.Bundle, org.osgi.framework.ServiceRegistration)
     */
    public Object getService( Bundle bundle, ServiceRegistration registration )
    {
        log( LogService.LOG_DEBUG, "DelayedServiceFactoryServiceFactory.getService()", getComponentMetadata(), null );

        // When the getServiceMethod is called, the implementation object must be created

        // private ComponentContext and implementation instances
        BundleComponentContext serviceContext = new BundleComponentContext( this, bundle );
        Object service = createImplementationObject( serviceContext );

        // register the components component context if successfull
        if ( service != null )
        {
            serviceContext.setImplementationObject( service );
            serviceContexts.put( service, serviceContext );

            // if this is the first use of this component, switch to ACTIVE state
            setStateConditional( STATE_REGISTERED, STATE_ACTIVE );
        }

        return service;
    }


    /* (non-Javadoc)
     * @see org.osgi.framework.ServiceFactory#ungetService(org.osgi.framework.Bundle, org.osgi.framework.ServiceRegistration, java.lang.Object)
     */
    public void ungetService( Bundle bundle, ServiceRegistration registration, Object service )
    {
        log( LogService.LOG_DEBUG, "DelayedServiceFactoryServiceFactory.ungetService()", getComponentMetadata(), null );

        // When the ungetServiceMethod is called, the implementation object must be deactivated

        // private ComponentContext and implementation instances
        ComponentContext serviceContext = ( ComponentContext ) serviceContexts.remove( service );
        disposeImplementationObject( service, serviceContext );

        // if this was the last use of the component, go back to REGISTERED state
        if ( serviceContexts.isEmpty() )
        {
            setStateConditional( STATE_ACTIVE, STATE_REGISTERED );
        }
    }

    private static class BundleComponentContext extends ComponentContextImpl implements ComponentInstance
    {

        private Bundle m_usingBundle;
        private Object m_implementationObject;


        BundleComponentContext( AbstractComponentManager componentManager, Bundle usingBundle )
        {
            super( componentManager );

            m_usingBundle = usingBundle;
        }


        private void setImplementationObject( Object implementationObject )
        {
            m_implementationObject = implementationObject;
        }


        public Bundle getUsingBundle()
        {
            return m_usingBundle;
        }


        public ComponentInstance getComponentInstance()
        {
            return this;
        }


        //---------- ComponentInstance interface ------------------------------

        public Object getInstance()
        {
            return m_implementationObject;
        }


        public void dispose()
        {
            getComponentManager().dispose();
        }
    }
}
