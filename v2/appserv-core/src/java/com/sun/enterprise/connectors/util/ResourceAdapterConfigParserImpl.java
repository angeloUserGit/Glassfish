
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

package com.sun.enterprise.connectors.util;

import com.sun.enterprise.deployment.*;
import com.sun.enterprise.config.serverbeans.ElementProperty;
import com.sun.enterprise.connectors.*;
import com.sun.enterprise.util.*;
import com.sun.logging.LogDomains;
import java.util.logging.*;
import java.util.*;
import java.lang.*;
import java.lang.reflect.*;
import java.io.IOException;
import org.xml.sax.SAXParseException;

/**
 *  This is Resource Adapter configuration parser. It parses the
 *  ra.xml file for the Resources adapter javabean properties 
 *
 *  @author      Srikanth P
 *
 */

public class ResourceAdapterConfigParserImpl implements ConnectorConfigParser {

    static Logger _logger = LogDomains.getLogger(LogDomains.RSR_LOGGER);
    
    /**
     *  Default constructor.
     *
     */

    public ResourceAdapterConfigParserImpl() {

    }

    /** Parses the ra.xml for the Resource Adapter javabean properties.
     *  Here the second parameter connectionDefName is not used and can
     *  be null or any value.
     *
     *  It throws ConnectorRuntimeException if module dir is null or
     *  corresponing rar is not deployed i.e invalid moduleDir parameter.
     *
     *  @param desc ConnectorDescriptor pertaining to rar.
     *  @param  Not used. Can be null or any value,
     *  @return Javabean properties with the propety names and values
     *          of propeties. The property values will be the values
     *          mentioned in ra.xml if present. Otherwise it will be the
     *          default values obtained by introspecting the javabean.
     *          In both the case if no value is present, empty String is
     *          returned as the value.
     *  @throws  ConnectorRuntimeException if moduleDir is null .
     *           If corresponding rar is not deployed i.e moduleDir is invalid.
     */

    public Properties getJavaBeanProps(ConnectorDescriptor desc,
              String connectionDefName, String rarName) throws ConnectorRuntimeException
    {

        if(desc == null) {
            throw new ConnectorRuntimeException("Invalid arguments");
        }

        /* ddVals           -> Properties present in ra.xml
        *  introspectedVals -> All properties with values
        *                                 obtained by introspection of resource
        *                                  adapter javabean
        *  mergedVals       -> merged props of raConfigPros and
        *                                 allraConfigPropsWithDefVals
        */

        Set ddVals = desc.getConfigProperties();
        Properties mergedVals = null;
        String className = desc.getResourceAdapterClass();
        Properties introspectedVals = null;
        if(className != null && className.length() != 0) {
            introspectedVals=configParserUtil.introspectJavaBean(
                                       className,ddVals);
            mergedVals = configParserUtil.mergeProps(ddVals,introspectedVals);
        }
        return mergedVals;
    }

}
