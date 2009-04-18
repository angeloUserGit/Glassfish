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
package com.sun.enterprise.resource.pool.monitor;

import com.sun.logging.LogDomains;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.flashlight.provider.ProbeProviderFactory;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.annotations.Service;


/**
 * Utility class to create providers for monitoring purposes.
 * 
 * @author shalini
 */
@Service
public class ConnectionPoolProbeProviderUtil {

    @Inject
    private ProbeProviderFactory probeProviderFactory;

    private JdbcConnPoolProbeProvider jdbcConnPoolProvider = null;
    //private static final JdbcConnPoolProbeProvider NO_OP_JDBC_CONN_POOL_PROBE_PROVIDER =
    //    (JdbcConnPoolProbeProvider) Proxy.newProxyInstance(
    //        JdbcConnPoolProbeProvider.class.getClassLoader(),
    //        new Class[] { JdbcConnPoolProbeProvider.class },
    //        new NoopInvocationHandler());    
    private Logger _logger = LogDomains.getLogger(ConnectionPoolProbeProviderUtil.class, LogDomains.RSR_LOGGER);
    
    /**
     * Create probe providers for jdbcPool related events.
     * 
     * The generated jdbcPool probe providers are shared by all 
     * jdbc connection pools. Each jdbc connection pool will qualify a 
     * probe event with its pool name.
     *
     */   
    public void createProbeProviders() {
        try {
            //jdbcConnPoolProvider = probeProviderFactory.getProbeProvider(
                //"jdbc-connection-pool", "jdbc-connection-pool", null, JdbcConnPoolProbeProvider.class);
            jdbcConnPoolProvider = new JdbcConnPoolProbeProvider();
            if (jdbcConnPoolProvider == null) {
                // Should never happen
                _logger.log(Level.WARNING,
                    "Unable to create probe provider for interface " +
                    JdbcConnPoolProbeProvider.class.getName() +
                    ", using no-op provider");
            }
        } catch (Exception e) {
            _logger.log(Level.SEVERE,
                        "Unable to create probe provider for interface " +
                        JdbcConnPoolProbeProvider.class.getName() +
                        ", using no-op provider",
                        e);
        }
        if (jdbcConnPoolProvider == null) {
            //jdbcConnPoolProvider = NO_OP_JDBC_CONN_POOL_PROBE_PROVIDER;
        }
    }

    /**
     * Get probe provider for jdbc connection pool related events
     * @return JdbcConnPoolProbeProvider
     */
    public JdbcConnPoolProbeProvider getJdbcConnPoolProvider() {
        return jdbcConnPoolProvider;
    }

    /**
     * Probe provider that implements each probe provider method as a 
     * no-op.
     */
    /*public static class NoopInvocationHandler implements InvocationHandler {

        public Object invoke(Object proxy, Method method, Object[] args) {
            // Deliberate no-op
            return null;
        }
    } */   
}