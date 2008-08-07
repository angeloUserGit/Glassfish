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
 
package com.sun.appserv.management.j2ee;

import com.sun.appserv.management.base.AMX;
import com.sun.appserv.management.util.misc.GSetUtil;

import java.util.Set;

/**
	See JSR 77.3-1.<br>
	Specific extensions to the JSR 77 model include:
	<ul>
	<li>{@link #J2EE_CLUSTER}</li>
	</ul>
	@see com.sun.appserv.management.base.XTypes
 */
public final class J2EETypes
{
	/**
		The javax.management.ObjectName property key denoting the type of the MBean.
	 */
	public final static String	J2EE_TYPE_KEY			= AMX.J2EE_TYPE_KEY;
	
	/**
		The ObjectName property key denoting the name of the MBean.
	 */
	public final static String	NAME_KEY			= AMX.NAME_KEY;
	
	public final static String	J2EE_DOMAIN					= "J2EEDomain";
	public final static String	J2EE_SERVER					= "J2EEServer";
	public final static String	J2EE_CLUSTER				= "X-J2EECluster";
	public final static String	J2EE_APPLICATION			= "J2EEApplication";
	public final static String	APP_CLIENT_MODULE			= "AppClientModule";
	public final static String	EJB_MODULE					= "EJBModule";
	public final static String	WEB_MODULE					= "WebModule";
	public final static String	RESOURCE_ADAPTER_MODULE		= "ResourceAdapterModule";
	public final static String	RESOURCE_ADAPTER		= "ResourceAdapter";
	public final static String	ENTITY_BEAN					= "EntityBean";
	public final static String	STATEFUL_SESSION_BEAN		= "StatefulSessionBean";
	public final static String	STATELESS_SESSION_BEAN		= "StatelessSessionBean";
	public final static String	MESSAGE_DRIVEN_BEAN			= "MessageDrivenBean";
	public final static String	SERVLET						= "Servlet";
	public final static String	JAVA_MAIL_RESOURCE			= "JavaMailResource";
	public final static String	JCA_RESOURCE				= "JCAResource";
	public final static String	JCA_CONNECTION_FACTORY		= "JCAConnectionFactory";
	public final static String	JCA_MANAGED_CONNECTION_FACTORY	= "JCAManagedConnectionFactory";
	public final static String	JDBC_RESOURCE				= "JDBCResource";
	public final static String	JDBC_DATA_SOURCE			= "JDBCDataSource";
	public final static String	JDBC_DRIVER					= "JDBCDriver";
	public final static String	JMS_RESOURCE				= "JMSResource";
	public final static String	JNDI_RESOURCE				= "JNDIResource";
	public final static String	JTA_RESOURCE				= "JTAResource";
	public final static String	RMI_IIOP_RESOURCE			= "RMI_IIOPResource";
	public final static String	URL_RESOURCE				= "URL_Resource";
	public final static String	JVM					= "JVM";
	
	
	/**
		@since AppServer 9.0
	 */
	public final static String	WEB_SERVICE_ENDPOINT	="WebServiceEndpoint";
	
	/** @deprecated do not use */
	public final static String	WEB_SERVICE				= WEB_SERVICE_ENDPOINT;
	
	/**
		Set consisting of all standard JSR 77 j2eeTypes
	 */
	public static final Set	ALL_STD	= 
	    GSetUtil.newUnmodifiableStringSet(
		J2EE_DOMAIN,
		J2EE_SERVER,
		J2EE_APPLICATION,
		APP_CLIENT_MODULE,
		EJB_MODULE,
		WEB_MODULE,
		RESOURCE_ADAPTER_MODULE,
		ENTITY_BEAN,
		STATEFUL_SESSION_BEAN,
		STATELESS_SESSION_BEAN,
		MESSAGE_DRIVEN_BEAN,
		SERVLET,
		JAVA_MAIL_RESOURCE,
		JCA_RESOURCE,
		JCA_CONNECTION_FACTORY,
		JCA_MANAGED_CONNECTION_FACTORY,
		JDBC_RESOURCE,
		JDBC_DATA_SOURCE,
		JDBC_DRIVER,
		JMS_RESOURCE,
		JNDI_RESOURCE,
		JTA_RESOURCE,
		RMI_IIOP_RESOURCE,
		URL_RESOURCE,
		JVM,
		WEB_SERVICE_ENDPOINT  );


}
