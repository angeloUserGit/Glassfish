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
package com.sun.enterprise.tools.verifier.tests.app;

import com.sun.enterprise.tools.verifier.tests.app.ApplicationTest;
import java.util.*;
import java.io.*;
import com.sun.enterprise.deployment.*;
import com.sun.enterprise.security.acl.*;
import com.sun.enterprise.tools.verifier.*;
import com.sun.enterprise.util.FileClassLoader;


/** 
 * The Application role-name element contains the name of a security role.
 */
public class AppSecurityRole extends ApplicationTest implements AppCheck { 

    

    /** 
     * The Application role-name element contains the name of a security role.
     *
     * @param descriptor the Application deployment descriptor
     *   
     * @return <code>Result</code> the results for this assertion
     */
    public Result check(Application descriptor) {

	Result result = getInitializedResult();


	if (!descriptor.getRoles().isEmpty()) {
	    boolean oneFailed = false;
	    boolean foundIt = false;
	    // get the security role name's in this app
	    Set sc = descriptor.getRoles();
	    Iterator itr = sc.iterator();
	    while (itr.hasNext()) {
		foundIt = false;
		Role role = (Role) itr.next();
		String roleName = role.getName(); 
		if (roleName.length() > 0) {
		    foundIt = true;
		} else {
		    foundIt = false;
		}

      
		if (foundIt) {
		    result.addGoodDetails(smh.getLocalString
					  (getClass().getName() + ".passed",
					   "The security role name [ {0} ] found within application [ {1} ]",
					   new Object[] {roleName, descriptor.getName()}));
		} else {
		    if (!oneFailed) {
			oneFailed = true;
		    }
		    result.addErrorDetails(smh.getLocalString
					   (getClass().getName() + ".failed",
					    "Error: The security role name [ {0} ] not found within application [ {1} ]",
					    new Object[] {roleName, descriptor.getName()}));
		}
	    }
	    if (oneFailed) {
		result.setStatus(Result.FAILED);
	    } else {
		result.setStatus(Result.PASSED);
	    }
	} else {
	    result.notApplicable(smh.getLocalString
				 (getClass().getName() + ".notApplicable",
				  "There are no role-name elements within the application [ {0} ]",
				  new Object[] {descriptor.getName()}));
	}

	return result;
    }
}
