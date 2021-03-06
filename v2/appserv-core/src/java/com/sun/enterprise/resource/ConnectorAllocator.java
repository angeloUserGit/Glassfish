/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.enterprise.resource;


import java.util.logging.Level;

import javax.transaction.xa.XAResource;
import javax.resource.spi.*;
import javax.resource.ResourceException;
import javax.security.auth.Subject;

import com.sun.enterprise.deployment.ConnectorDescriptor;
import com.sun.enterprise.PoolManager;


/**
 * @author Tony Ng
 */
public class ConnectorAllocator extends AbstractConnectorAllocator {

    private boolean shareable;
   

    class ConnectionListenerImpl extends ConnectionEventListener {
        private ResourceHandle resource;
        
        public ConnectionListenerImpl(ResourceHandle resource) {
            this.resource = resource;
        }

        public void connectionClosed(ConnectionEvent evt) {
            if (resource.hasConnectionErrorOccurred()) {
                return;
            }
            resource.decrementCount();
            if (resource.getShareCount() == 0) {
                poolMgr.resourceClosed(resource);
            }
        }

        /**
         * Resource adapters will signal that the connection being closed is bad.
         * @param evt ConnectionEvent
         */
        public void badConnectionClosed(ConnectionEvent evt){

            if (resource.hasConnectionErrorOccurred()) {
	            return;
    	    }
            resource.decrementCount();
            if (resource.getShareCount() == 0) {
                ManagedConnection mc = (ManagedConnection) evt.getSource();
                mc.removeConnectionEventListener(this);
                poolMgr.badResourceClosed(resource);
            }
        }
        
        public void connectionErrorOccurred(ConnectionEvent evt) {
            resource.setConnectionErrorOccurred();

            ManagedConnection mc = (ManagedConnection) evt.getSource();
            mc.removeConnectionEventListener(this);
            poolMgr.resourceErrorOccurred(resource);
            try {
                mc.destroy();
            } catch (Exception ex) {
                // ignore exception
            }
        }

        public void localTransactionStarted(ConnectionEvent evt) {
            // no-op
        }

        public void localTransactionCommitted(ConnectionEvent evt) {
            // no-op
        }

        public void localTransactionRolledback(ConnectionEvent evt) {
            // no-op
        }
    }

    public ConnectorAllocator(PoolManager poolMgr,
                              ManagedConnectionFactory mcf,
                              ResourceSpec spec,
                              Subject subject,
                              ConnectionRequestInfo reqInfo,
                              ClientSecurityInfo info,
                              ConnectorDescriptor desc,
                              boolean shareable) {
        super(poolMgr, mcf, spec, subject, reqInfo, info, desc);
        this.shareable = shareable;
    }

    
    public ResourceHandle createResource()
         throws PoolingException
    {
        try {
            ManagedConnection mc =
                mcf.createManagedConnection(subject, reqInfo);
            
            ResourceHandle resource =
                new ResourceHandle(mc, spec, this, info);
            ConnectionEventListener l = 
                new ConnectionListenerImpl(resource);
            mc.addConnectionEventListener(l);
            return resource;
        } catch (ResourceException ex) {
            throw new PoolingException(ex);
        }
    }

    public void fillInResourceObjects(ResourceHandle resource)
        throws PoolingException
    {
        try {
	    ManagedConnection mc = (ManagedConnection) resource.getResource();
	    Object con = mc.getConnection( subject, reqInfo );
	    resource.incrementCount();
            XAResource xares = mc.getXAResource();
            resource.fillInResourceObjects(con, xares);
        } catch( ResourceException ex ) {
	    throw new PoolingException( ex );
	}
    }

    public void destroyResource(ResourceHandle resource)
        throws PoolingException {

        try {
            closeUserConnection(resource);
        } catch (Exception ex) {
            // ignore error
        }
        
        try {
            ManagedConnection mc = (ManagedConnection) resource.getResource();
            mc.destroy();
        } catch (Exception ex) {
            _logger.log(Level.WARNING, ex.getMessage());
            throw new PoolingException(ex);
        }

    }

    public boolean shareableWithinComponent() {
        return shareable;
    }
}
