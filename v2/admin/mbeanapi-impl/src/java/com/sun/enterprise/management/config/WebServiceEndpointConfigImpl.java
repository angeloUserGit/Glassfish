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
package com.sun.enterprise.management.config;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import javax.management.ObjectName;

import com.sun.appserv.management.base.XTypes;
import com.sun.appserv.management.util.misc.TypeCast;

import com.sun.appserv.management.config.TransformationRuleConfigKeys;
import com.sun.appserv.management.config.TransformationRuleConfig;


import com.sun.enterprise.admin.wsmgmt.WebServiceMgrBackEnd;

import com.sun.enterprise.management.support.Delegate;
import com.sun.enterprise.management.support.AMXAttributeNameMapper;


public final class WebServiceEndpointConfigImpl  extends AMXConfigImplBase
    implements ConfigFactoryCallback
    // implements WebServiceEndpointConfig
{
    	public
    WebServiceEndpointConfigImpl( final Delegate delegate )
    {
    	super( delegate );
    }

    // this method may be superfluous as it is probably handled automatically
     public Map<String,ObjectName> getTransformationRuleConfigObjectNameMap()
     {
          return(getContaineeObjectNameMap(
            XTypes.TRANSFORMATION_RULE_CONFIG));
     }

    // this method may be superfluous as it is probably handled automatically
        public Map<String,ObjectName>
    getRegistryLocationConfigObjectNameMap()
    {
       return (getContaineeObjectNameMap(XTypes.REGISTRY_LOCATION_CONFIG));
    }

     public List<ObjectName> getTransformationRuleConfigObjectNameList() {
        final String wsepName = getName();
        final String appId = getContainer().getName();
      
        final Map<String,ObjectName>    containeeMap    =
            getContaineeObjectNameMap(TransformationRuleConfig.J2EE_TYPE);
            
        final List<ObjectName>  objectNames = TypeCast.checkList( 
            WebServiceMgrBackEnd.getManager().
                getTransformationRuleConfigObjectNameList( appId, wsepName, containeeMap ),
                    ObjectName.class);
                
        return objectNames;
     }


    @Override
        protected void
    addCustomMappings( final AMXAttributeNameMapper mapper )
    {
        super.addCustomMappings( mapper );
          
    	mapper.matchName( "MonitoringLevel", "monitoring" );
    }
}















