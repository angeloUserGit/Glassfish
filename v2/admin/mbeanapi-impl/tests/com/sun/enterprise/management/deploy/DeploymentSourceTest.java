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
package com.sun.enterprise.management.deploy;

import java.util.Map;

import java.io.IOException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import com.sun.appserv.management.deploy.DeploymentSource;
import com.sun.appserv.management.deploy.DeploymentSourceImpl;


import com.sun.enterprise.management.AMXTestBase;
import com.sun.enterprise.management.Capabilities;

/**
 */
public final class DeploymentSourceTest extends junit.framework.TestCase
{
		public
	DeploymentSourceTest( )
	{
	}
	
		private DeploymentSourceImpl
	createDeploymentSource()
		throws IOException
	{
		final File	archiveFile	= File.createTempFile( "junk", "junk" );
		archiveFile.deleteOnExit();
		
		final String[]	entriesAdded	= new String[]	{ "hello", "there" };
		final String[]	entriesRemoved	= new String[0];
		final String[]	entriesDeleted	= new String[0];
		final DeploymentSourceImpl	ds	=
			new DeploymentSourceImpl( archiveFile.toString(),
				true,
				entriesAdded, entriesRemoved, entriesDeleted, null );
		
		return( ds );
	}
	
		public void
	testCreateDeploymentSource()
		throws IOException
	{
		createDeploymentSource();
	}
	
	
		public void
	testDeploymentSourceFromMap()
		throws IOException
	{
		final DeploymentSourceImpl	ds	= createDeploymentSource();
		final Map<String,Serializable>	data	= ds.asMap();
		
		final DeploymentSourceImpl ds2	= new DeploymentSourceImpl( data );
		
		assert( ds2.equals( ds ) );
		assert( ds.equals( ds2 ) );
	}
	
	
}


























