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

package com.sun.enterprise.connectors.system;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.sun.enterprise.connectors.ConnectorRuntimeException;
import com.sun.logging.LogDomains;

/**
 * The <code>MQJMXConnectorInfo</code> holds MBean Server connection information
 * to a SJSMQ broker instance. This API is used by the admin infrastructure for
 * performing MQ administration/configuration operations on a broker instance.
 * 
 * @author Sivakumar Thyagarajan
 * @since SJSAS 9.0
 */
public class MQJMXConnectorInfo {
    private String jmxServiceURL = null;
    private Map<String,?> jmxConnectorEnv = null;
    private String asInstanceName = null;
    private String brokerInstanceName = null;
    private String brokerType = null;
    static Logger _logger = LogDomains.getLogger(LogDomains.RSR_LOGGER);
    private JMXConnector connector = null;

    public MQJMXConnectorInfo(String asInstanceName, String brokerInstanceName,
                              String brokerType, String jmxServiceURL, 
                                       Map<String, ?> jmxConnectorEnv) {
        this.brokerInstanceName = brokerInstanceName;
        this.asInstanceName = asInstanceName;
        this.jmxServiceURL = jmxServiceURL;
        this.brokerType = brokerType;
        this.jmxConnectorEnv = jmxConnectorEnv;
        if (_logger.isLoggable(Level.FINE)) {
            _logger.log(Level.FINE, "MQJMXConnectorInfo : brokerInstanceName " + 
                            brokerInstanceName + " ASInstanceName " + asInstanceName + 
                            " jmxServiceURL "  + jmxServiceURL +  " BrokerType " + brokerType 
                            + " jmxConnectorEnv " + jmxConnectorEnv);
        }
    }

    public String getBrokerInstanceName(){
        return this.brokerInstanceName;
    }

    public String getBrokerType(){
        return this.brokerType;
    }

    public String getASInstanceName(){
        return this.asInstanceName;
    }

    public String getJMXServiceURL(){
        _logger.log(Level.FINE,"MQJMXConnectorInfo :: JMXServiceURL is " + this.jmxServiceURL);
        return this.jmxServiceURL;
    }

    public Map<String, ?> getJMXConnectorEnv(){
        return this.jmxConnectorEnv;
    }
    
    /**
     * Returns an <code>MBeanServerConnection</code> representing the MQ broker instance's MBean
     * server. 
     * @return
     * @throws ConnectorRuntimeException
     */
    //XXX:Enhance to support SSL (once MQ team delivers support in the next drop)
    //XXX: Discuss how <code>ConnectionNotificationListeners</code> could 
    //be shared with the consumer of this API
    public MBeanServerConnection getMQMBeanServerConnection() throws ConnectorRuntimeException {
        try {
            if (_logger.isLoggable(Level.FINE)) {
                _logger.log(Level.FINE, 
                "creating MBeanServerConnection to MQ JMXServer with "+getJMXServiceURL());
            }
            JMXServiceURL jmxServiceURL = new JMXServiceURL(getJMXServiceURL());
            connector = JMXConnectorFactory.connect(jmxServiceURL, this.jmxConnectorEnv);
            //XXX: Do we need to pass in a Subject? 
            MBeanServerConnection mbsc = connector.getMBeanServerConnection();
            return mbsc;
        } catch (Exception e) {
            e.printStackTrace();
            ConnectorRuntimeException cre = new ConnectorRuntimeException(e.getMessage());
            cre.initCause(e);
            throw cre;
        }
    }

    public void closeMQMBeanServerConnection() throws ConnectorRuntimeException {
        try {
           if (connector != null) {
                 connector.close();
           }
        } catch (IOException e) {
            ConnectorRuntimeException cre = new ConnectorRuntimeException(e.getMessage());
            cre.initCause(e);
            throw cre;
        }
    }
 } 
