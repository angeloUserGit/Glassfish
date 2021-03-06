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
package com.sun.enterprise.ee.admin.event;

import com.sun.enterprise.config.ConfigContext;
import com.sun.enterprise.config.serverbeans.Server;

/**
 * Represents a event notification end point.
 *
 * @author Nazrul Islam
 * @since  JDK1.4
 */
class EndPoint {

    /**
     * Constructor.
     *
     * @param  s  server instance representing this end point
     */
    EndPoint(Server s, ConfigContext ctx) {
        _configBean = s;
        _ctx        = ctx;
    }

    /**
     * Returns the server instance name.
     *
     * @return  the server instance name
     */
    public String getHost() {
        return _configBean.getName();
    }

    /** 
     * Returns the server instance port for event communication.
     *
     * @return  the server instance port for event communication
     */
    public int getPort() {
        // FIXME
        return 0;
    }

    /**
     * Returns the config bean associated with this end point.
     *
     * @return  the config bean associated with this end point
     */
    public Server getConfigBean() {
        return _configBean;
    }

    /**
     * Returns the config context for this end point. 
     *
     * @return  the config context
     */
    public ConfigContext getConfigContext() {
        return _ctx;
    }

    // ---- VARIABLE(S) - PRIVATE --------------------------
    private Server _configBean  = null;
    private ConfigContext _ctx  = null;
}
