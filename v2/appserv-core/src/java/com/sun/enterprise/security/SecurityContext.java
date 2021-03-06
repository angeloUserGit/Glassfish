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
package com.sun.enterprise.security;

import java.lang.System;
import java.lang.SecurityManager;
import java.util.logging.Logger;
import java.util.logging.Level;

import java.util.Iterator;
import java.util.Set;

import java.security.Principal;
import java.security.AccessControlContext;
import java.security.PrivilegedExceptionAction;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import javax.security.auth.Subject;
import com.sun.enterprise.deployment.PrincipalImpl;
import com.sun.enterprise.config.serverbeans.*;
import com.sun.enterprise.config.*;
import com.sun.enterprise.security.auth.login.DistinguishedPrincipalCredential;
import com.sun.enterprise.server.ApplicationServer;
import com.sun.web.security.PrincipalGroupFactory;
import com.sun.logging.*;


/**
* This  class that extends AbstractSecurityContext that gets 
 * stored in Thread Local Storage. If the current thread creates
 * child threads, the SecurityContext stored in the current 
 * thread is automatically propogated to the child threads.
 * 
 * This class is used on the server side to represent the 
 * security context.
 *
 * Thread Local Storage is a concept introduced in JDK1.2.
 * @see java.lang.ThreadLocal
 * @see java.lang.InheritableThreadLocal
 * 
 * @author Harish Prabandham
 * @author Harpreet Singh
 */
public class SecurityContext extends AbstractSecurityContext {

    private static Logger _logger=null;
    static {
        _logger=LogDomains.getLogger(LogDomains.SECURITY_LOGGER);
    }

    private static InheritableThreadLocal currentSecCtx =
        new InheritableThreadLocal();
    private static SecurityContext defaultSecurityContext = 
	generateDefaultSecurityContext();

    private static javax.security.auth.AuthPermission doAsPrivilegedPerm =
 	new javax.security.auth.AuthPermission("doAsPrivileged");
 
    // Did the client log in as or did the server generate the context 
    private boolean SERVER_GENERATED_SECURITY_CONTEXT = false;


    /* This creates a new SecurityContext object.
     * Note: that the docs for Subject state that the internal sets
     * (eg. the principal set) cannot be modified unless the caller 
     * has the modifyPrincipals AuthPermission. That said, there may
     * be some value to setting the Subject read only.
     * Note: changing the principals in the embedded subject (after
     * construction will likely cause problem in the principal set 
     * keyed HashMaps of EJBSecurityManager.
     * @param username The name of the user/caller principal.
     * @param subject contains the authenticated principals and credential.
     */
    public SecurityContext(String userName, Subject subject) {
	Subject s = subject;
	if (s == null) {
	    s = new Subject();
            if (_logger.isLoggable(Level.WARNING)) {
	        _logger.warning("java_security.null_subject");
            }
	} 
	this.initiator = new PrincipalImpl(userName);
        final Subject sub = s;
        this.subject = (Subject)
            AppservAccessController.doPrivileged(new PrivilegedAction(){
                public java.lang.Object run() {
                    sub.getPrincipals().add(initiator);
                    return sub;
                }
            });
    }

    /**
     * Create a SecurityContext with given subject having
     * DistinguishedPrincipalCredential.
     * This is used for JMAC environment.
     * @param subject
     */
    public SecurityContext(Subject subject) {
        if (subject == null) {
            subject = new Subject();
            if (_logger.isLoggable(Level.WARNING)) {
                _logger.warning("java_security.null_subject");
            }
	} 

        final Subject fsub = subject;
        this.subject = subject;
        this.initiator = (Principal)
            AppservAccessController.doPrivileged(new PrivilegedAction(){
                public java.lang.Object run() {
                    Principal prin = null;
                    Iterator pcIter = fsub.getPublicCredentials().iterator();
                    while (pcIter.hasNext()) {
                        Object obj = pcIter.next();
                        if (obj instanceof DistinguishedPrincipalCredential) {
                            DistinguishedPrincipalCredential dpc =
                                (DistinguishedPrincipalCredential)obj;
                            prin = dpc.getPrincipal();
                            break;
                        }
                    }
                    // for old auth module
                    if (prin == null) {
                        Iterator<Principal> prinIter = fsub.getPrincipals().iterator();
                        if (prinIter.hasNext()) {
                            prin = prinIter.next();
                        }
                    }
                    return prin;
                }
            });

        if (this.initiator == null) {
            this.initiator = getDefaultCallerPrincipal();
        }
    }

    public SecurityContext(String userName, Subject subject, String realm) {
	Subject s = subject;
	if (s == null) {
	    s = new Subject();
            if (_logger.isLoggable(Level.WARNING)) {
	        _logger.warning("java_security.null_subject");
            }
	} 
	this.initiator = PrincipalGroupFactory.getPrincipalInstance(userName, realm);
        final Subject sub = s;
        this.subject = (Subject)
            AppservAccessController.doPrivileged(new PrivilegedAction(){
                public java.lang.Object run() {
                    sub.getPrincipals().add(initiator);
                    return sub;
                }
            });
    }
    
    /* private constructor for constructing default security context
     */
    private SecurityContext() {
 	this.subject = new Subject();
	// delay assignment of caller principal until it is requested
 	this.initiator = null;
 	this.setServerGeneratedCredentials();
        // read only is only done for guest logins.
 	this.subject.setReadOnly();
    }
    
    /**
     * Initialize the SecurityContext and handle the unauthenticated 
     * principal case
     */
    public static SecurityContext init(){
        SecurityContext sc = (SecurityContext) currentSecCtx.get();
        if(sc == null) { // there is no current security context...
            sc = defaultSecurityContext;
	}
        return sc;
    }
      
    public static SecurityContext getDefaultSecurityContext(){
        //unauthen. Security Context.
        return defaultSecurityContext; 
    }

    public static Subject getDefaultSubject(){
        //Subject of unauthen. Security Context.
        return defaultSecurityContext.subject; 
    }
     
    // ger caller principal of unauthenticated Security Context
    public static Principal getDefaultCallerPrincipal(){
        synchronized(SecurityContext.class) {
	    if (defaultSecurityContext.initiator == null) { 
		String guestUser = null;
		try {
		    guestUser = (String)
			AppservAccessController.doPrivileged(new PrivilegedExceptionAction() {
				public java.lang.Object run() throws Exception {
				    ConfigContext configContext =
					ApplicationServer.getServerContext().
					getConfigContext();
				    assert(configContext != null);
				    SecurityService securityBean = 
					ServerBeansFactory.
					getSecurityServiceBean(configContext);
				    assert(securityBean != null);
				    return securityBean.getDefaultPrincipal();
				}
			    });
		} catch (Exception e) {
		    _logger.log(Level.SEVERE,
				"java_security.default_user_login_Exception", e);
		} finally {
		    if (guestUser == null) {
			guestUser = "ANONYMOUS";
		    }
		}
		defaultSecurityContext.initiator = new PrincipalImpl(guestUser);
	    }
	}
	return defaultSecurityContext.initiator;
    }
      
    private static SecurityContext generateDefaultSecurityContext() {
        synchronized (SecurityContext.class) {
	    try{
		return (SecurityContext) 
		    AppservAccessController.doPrivileged(new PrivilegedExceptionAction() {
			    public java.lang.Object run() throws Exception{
				return new SecurityContext();
			    }
			});
	    } catch(Exception e){
		_logger.log(Level.SEVERE,
			    "java_security.security_context_exception",e);
	    }
	}
        return null;
    }
    
    /**
     * No need to unmarshall the unauthenticated principal....
     */
    public static void reset(SecurityContext sc){
	setCurrent(sc);
    }


    /**
     * This method gets the SecurityContext stored in the 
     * Thread Local Store (TLS) of the current thread. 
     * @return The current Security Context stored in TLS. It returns
     * null if SecurityContext could not be found in the current thread.
     */
    public static SecurityContext getCurrent() {
	SecurityContext sc = (SecurityContext) currentSecCtx.get();
 	if (sc == null) {
	    sc = defaultSecurityContext;
	}
 	return sc;
    }

    
    /**
     * This method sets the SecurityContext stored in the TLS. 
     * @param The Security Context that should be stored in TLS.
     * This public static method needs to be protected
     * such that it can only be called by container code. Otherwise
     * it can be called by application code to set its subject (which the
     * EJB security manager will use to create a domain combiner,
     * and then everything the ejb does will be run as the
     * corresponding subject.
     */
    public static void setCurrent(SecurityContext sc) {

 	if (sc != null && sc != defaultSecurityContext) {
 
 	    SecurityContext current = (SecurityContext)currentSecCtx.get();
 
 	    if (sc != current) {
 
 		boolean permitted = false;
 
 		try {
		    SecurityManager sm = System.getSecurityManager();
		    if (sm != null) {
			if(_logger.isLoggable(Level.FINE)){
			    _logger.fine("permission check done to set SecurityContext");
			}
			sm.checkPermission(doAsPrivilegedPerm);
		    }
 		    permitted = true;
 		} catch (java.lang.SecurityException se) {
 		    _logger.log(Level.SEVERE, "java_security.security_context_permission_exception", se);
 		} catch (Throwable t) {
 		    _logger.log(Level.SEVERE, "java_security.security_context_unexpected_exception", t);
 		}
 	    
 		if (permitted) {
		    currentSecCtx.set(sc);
		} else {
 		    _logger.severe("java_security.security_context_nochange");
 		}
 	    }
 	} else {
	    currentSecCtx.set(sc);
	}
    }
   
    public static void setUnauthenticatedContext() {
 	currentSecCtx.set(defaultSecurityContext);
    }

    public boolean didServerGenerateCredentials (){
	return SERVER_GENERATED_SECURITY_CONTEXT;
    }
    
    private void setServerGeneratedCredentials(){
	SERVER_GENERATED_SECURITY_CONTEXT = true;
    }


    /**
     * This method returns the caller principal. 
     * This information may be redundant since the same information 
     * can be inferred by inspecting the Credentials of the caller. 
     * @return The caller Principal. 
     */
    public Principal getCallerPrincipal() {
	return this == defaultSecurityContext ? getDefaultCallerPrincipal() : initiator;
    }

    
    public Subject getSubject() {
	return subject;
    }


    public String toString() {
	return "SecurityContext[ " + "Initiator: " + 
	    initiator + "Subject " + subject + " ]";
    }

    public Set getPrincipalSet() {
        return subject.getPrincipals();       
    }
}
