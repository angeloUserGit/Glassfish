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
 
/*
 * $Header: /cvs/glassfish/appserv-api/src/java/com/sun/appserv/management/util/jmx/ObjectNameQueryMBeanImpl.java,v 1.1 2006/12/02 06:04:21 llc Exp $
 * $Revision: 1.1 $
 * $Date: 2006/12/02 06:04:21 $
 */
package com.sun.appserv.management.util.jmx;

import java.util.Set;

import javax.management.StandardMBean;
import javax.management.ObjectName;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.NotCompliantMBeanException;

import javax.management.MalformedObjectNameException;
import javax.management.MBeanRegistration;

/**
	An MBean implementing ObjectNameQueryMBean
 */
public class ObjectNameQueryMBeanImpl
	extends StandardMBean
	implements ObjectNameQueryMBean, MBeanRegistration
{
	private MBeanServerConnection		mConn;
	private ObjectNameQueryImpl			mImpl;
	
		public
	ObjectNameQueryMBeanImpl()
		throws NotCompliantMBeanException
	{
		super( ObjectNameQueryMBean.class );
	}
	
		public Set<ObjectName>
	matchAll( ObjectName startingSetPattern, String [] regexNames, String [] regexValues )
				throws MalformedObjectNameException, java.io.IOException
	{
		final Set<ObjectName>	candidates	= JMXUtil.queryNames( mConn, startingSetPattern, null );
		
		return( mImpl.matchAll( candidates, regexNames, regexValues ) );
	}
	
		public Set<ObjectName>
	matchAll( Set<ObjectName> startingSet, String [] regexNames, String [] regexValues )
	{
		return( mImpl.matchAll( startingSet, regexNames, regexValues ) );
	}
				
		public Set<ObjectName>
	matchAny( ObjectName startingSetPattern, String [] regexNames, String [] regexValues )
				throws MalformedObjectNameException, java.io.IOException
	{
		final Set<ObjectName>	candidates	= JMXUtil.queryNames( mConn, startingSetPattern, null );
		
		return( mImpl.matchAny( candidates, regexNames, regexValues ) );
	}
				
		public Set<ObjectName>
	matchAny( Set<ObjectName> startingSet, String [] regexNames, String [] regexValues )
	{
		return( mImpl.matchAny( startingSet, regexNames, regexValues ) );
	}
	
	
	
		public ObjectName
	preRegister( final MBeanServer server, final ObjectName name)
	{
		mConn		= server;
		
		mImpl		= new ObjectNameQueryImpl( );
		return( name );
	}
	
		public void
	postRegister( Boolean registrationDone )
	{
	}
	
		public void
	preDeregister()
	{
		// nothing to do
	}
		public void
	postDeregister()
	{
		// nothing to do
	}

}






