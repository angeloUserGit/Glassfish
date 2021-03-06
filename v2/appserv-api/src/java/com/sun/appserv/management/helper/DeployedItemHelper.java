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
 * $Header: /cvs/glassfish/appserv-api/src/java/com/sun/appserv/management/helper/DeployedItemHelper.java,v 1.1 2006/12/02 06:03:35 llc Exp $
 * $Revision: 1.1 $
 * $Date: 2006/12/02 06:03:35 $
 */
package com.sun.appserv.management.helper;

import java.util.Set;
import java.util.HashSet;

import javax.management.ObjectName;

import com.sun.appserv.management.base.AMX;
import com.sun.appserv.management.base.XTypes;
import com.sun.appserv.management.base.Util;
import com.sun.appserv.management.DomainRoot;
import com.sun.appserv.management.base.QueryMgr;

/**
	Helper for deployed items (modules).
 */
public final class DeployedItemHelper extends Helper
{
		public
	DeployedItemHelper( final DomainRoot	domainRoot )
	{
		super( domainRoot );
	}

	/**
		Get the ObjectNames of all deployed items in the specified server.
		To extract the names, use {@link Util}.getNamesSet(set).
		
		@param standaloneServerName
		@return Set of ObjectName for all modules
	 */
		public Set<ObjectName>
	queryStandaloneServerDeployedItemObjectNames(
		final String	standaloneServerName  )
	{
		// specify all STANDALONE_SERVER_DEPLOYED_ITEM_REF_CONFIG
		final String	refsProp	=
			Util.makeJ2EETypeProp( XTypes.DEPLOYED_ITEM_REF_CONFIG );
			
		// specify the server
		final String	serverProp	=
			Util.makeProp( XTypes.STANDALONE_SERVER_CONFIG, standaloneServerName );
			
		return( Util.toObjectNames( propsQuery( refsProp, serverProp ) ) );
	}
	
	/**
		Get the ObjectNames of all deployed items in the specified cluster.
		To extract the names, use {@link Util}.getNamesSet(set).
		
		@param clusterName
		@return Set of ObjectName for all modules
	 */
		public Set<ObjectName>
	queryClusterDeployedItemObjectNames(
		final String	clusterName  )
	{
		// specify all STANDALONE_SERVER_DEPLOYED_ITEM_REF_CONFIG
		final String	refsProp	= 
			Util.makeJ2EETypeProp( XTypes.DEPLOYED_ITEM_REF_CONFIG );
			
		// specify the server
		final String	clusterProp	=
			Util.makeProp( XTypes.CLUSTER_CONFIG, clusterName );
			
		return( Util.toObjectNames( propsQuery( refsProp, clusterProp ) ) );
	}
}


