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
package com.sun.web.security;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.security.auth.Subject;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.config.AuthConfigProvider;
import javax.security.auth.message.config.ServerAuthConfig;
import javax.security.auth.message.config.ServerAuthContext;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequestWrapper;

import sun.security.x509.X500Name;

import org.apache.catalina.Authenticator;
import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.Globals;
import org.apache.catalina.HttpRequest;
import org.apache.catalina.HttpResponse;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Realm;
import org.apache.catalina.authenticator.AuthenticatorBase;
import org.apache.catalina.deploy.LoginConfig;
import org.apache.catalina.deploy.SecurityConstraint;
import org.apache.catalina.realm.Constants;
import org.apache.catalina.realm.RealmBase;
import org.apache.catalina.util.StringManager;

import com.sun.enterprise.ComponentInvocation;
import com.sun.enterprise.Switch;
import com.sun.enterprise.admin.monitor.callflow.RequestInfo;
import com.sun.enterprise.admin.monitor.callflow.ContainerTypeOrApplicationType;
import com.sun.enterprise.deployment.Application;
import com.sun.enterprise.deployment.RunAsIdentityDescriptor;
import com.sun.enterprise.deployment.WebBundleDescriptor;
import com.sun.enterprise.deployment.WebComponentDescriptor;
import com.sun.enterprise.deployment.interfaces.SecurityRoleMapper;
import com.sun.enterprise.deployment.web.LoginConfiguration;
import com.sun.enterprise.security.SecurityContext;
import com.sun.enterprise.security.auth.LoginContextDriver;
import com.sun.enterprise.security.auth.realm.certificate.CertificateRealm;
import com.sun.enterprise.security.jmac.config.HttpServletConstants;
import com.sun.enterprise.security.jmac.config.HttpServletHelper;
import com.sun.enterprise.webservice.monitoring.WebServiceEngineImpl;
import com.sun.enterprise.webservice.monitoring.AuthenticationListener;
import com.sun.logging.LogDomains;

/**
 * This is the realm adapter used to authenticate users and authorize
 * access to web resources. The authenticate method is called by Tomcat
 * to authenticate users. The hasRole method is called by Tomcat during
 * the authorization process.
 * @author Harpreet Singh
 * @author JeanFrancois Arcand
 */

public class RealmAdapter extends RealmBase {
    private static final String UNCONSTRAINED = "unconstrained";
    
    private static final Logger _logger=LogDomains.getLogger(LogDomains.WEB_LOGGER);

    public static final String SECURITY_CONTEXT = "SecurityContext";

    public static final String BASIC = "BASIC";
    public static final String FORM = "FORM";
    private static final String SERVER_AUTH_CONTEXT = "__javax.security.auth.message.ServerAuthContext";
    private static final String MESSAGE_INFO = "__javax.security.auth.message.MessageInfo";

    // name of system property that can be used to define 
    // corresponding default provider for system apps.
    private static final String SYSTEM_HTTPSERVLET_SECURITY_PROVIDER =
        "system_httpservlet_security_provider";

    //private String realm = "default";
    private SecurityRoleMapper mapper = null;
    private WebBundleDescriptor webDesc = null;

    // BEGIN IASRI 4747594
    private HashMap runAsPrincipals = null;
    // END IASRI 4747594
    
    // required for realm-per-app login
    private String _realmName = null;
    /**
     * Descriptive information about this Realm implementation.
     */
    protected static final String name = "J2EE-RI-RealmAdapter";
    /**
     * The context Id value needed by the jacc architecture.
     */
    private String CONTEXT_ID = null;

    private Container virtualServer;

    /**
     * The string manager for this package.
     */
    protected static final StringManager sm =
        StringManager.getManager(Constants.Package);

    protected static final StringManager smRA =
        StringManager.getManager("com.sun.web.security");

    /**
     * A <code>WebSecurityManager</code> object associated with a CONTEXT_ID
     */
    protected volatile WebSecurityManager webSecurityManager = null;
    
    /**
     * The factory used for creating <code>WebSecurityManager</code> object.
     */
    protected WebSecurityManagerFactory webSecurityManagerFactory = 
        WebSecurityManagerFactory.getInstance();
    protected boolean isCurrentURIincluded = false;
    
    private ArrayList roles = null;
       
    /* the following fields are used to implement a bypass of
     * FBL related targets
     */
    protected final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private boolean contextEvaluated = false;
    private String loginPage = null;
    private String errorPage = null;
    
    private static SecurityConstraint[] emptyConstraints = 
            new SecurityConstraint[] {};

    /**
     * the default provider id for system apps if one has been established.
     * the default provider for system apps is established by defining
     * a system property.
     */
    private static String defaultSystemProviderID = 
	    getDefaultSystemProviderID();

    private String appID;
    private boolean isSystemApp;
    private String jmacProviderRegisID = null;
    private HttpServletHelper helper = null;

    public RealmAdapter() {}

    /**
     * Create for WS Ejb endpoint authentication.
     * Roles related data is not available here.
     */
    public RealmAdapter(String realmName) {
        _realmName = realmName;
    }

    /**
     * Create the realm adapter. Extracts the role to user/group mapping
     * from the runtime deployment descriptor.
     * @param the web bundle deployment descriptor.
     * @param isSystemApp if the app is a system app.
     */
    public RealmAdapter(WebBundleDescriptor descriptor, boolean isSystemApp) {
        this(descriptor, isSystemApp, null);
    }


    /**
     * Create the realm adapter. Extracts the role to user/group mapping
     * from the runtime deployment descriptor.
     * @param the web bundle deployment descriptor.
     * @param isSystemApp if the app is a system app.
     * @param realmName The realm name to use if the app does not specify its
     * own
     */
    public RealmAdapter(WebBundleDescriptor descriptor,
                        boolean isSystemApp,
                        String realmName) {

        this.isSystemApp = isSystemApp;
        webDesc = descriptor;
        Application app = descriptor.getApplication();
        mapper = app.getRoleMapper();
        LoginConfiguration loginConfig = descriptor.getLoginConfiguration();
        _realmName = app.getRealm();
        if (_realmName == null && loginConfig != null) {
            _realmName = loginConfig.getRealmName();
        }
        if (realmName != null
                && (_realmName == null || _realmName.equals(""))) {
            _realmName = realmName;
        }

        // BEGIN IASRI 4747594
   	CONTEXT_ID = WebSecurityManager.getContextID(descriptor);
        runAsPrincipals = new HashMap();
        Iterator bundle = webDesc.getWebComponentDescriptors().iterator();
	
        while(bundle.hasNext()) {
            
            WebComponentDescriptor wcd = (WebComponentDescriptor)bundle.next();
            RunAsIdentityDescriptor runAsDescriptor = wcd.getRunAsIdentity();
	    
            if (runAsDescriptor != null) {
                String principal = runAsDescriptor.getPrincipal();
                String servlet = wcd.getCanonicalName();
		
                if (principal == null || servlet == null) {
                    _logger.warning("web.realmadapter.norunas");
                } else {
                    runAsPrincipals.put(servlet, principal);
                    _logger.fine("Servlet "+servlet+
                     " will run-as: "+principal);
		}
	    }
	}	
	// END IASRI 4747594

	this.appID = app.getRegistrationName();
        // helper are set until setVirtualServer is invoked
    }

    public void destroy() {
        super.destroy();
        if (helper != null) {
            helper.disable();
        }
    }

    /**
     * Sets the virtual server on which the web module (with which this
     * RealmAdapter is associated with) has been deployed.
     *
     * @param container The virtual server
     */
    public void setVirtualServer(Container container) {
        this.virtualServer = container;
        this.helper = getConfigHelper();
    }


    public WebBundleDescriptor getWebDescriptor() {
        return webDesc;
    }

    // utility method to get web security anager.
    // will log warning if the manager is not found in the factory, and
    // logNull is true.
    WebSecurityManager getWebSecurityManager(boolean logNull) {
	if (webSecurityManager == null) {
	    synchronized(this) {
		webSecurityManager = webSecurityManagerFactory.
		    getWebSecurityManager(CONTEXT_ID);
	    }
	    if (webSecurityManager == null && logNull) {
		String msg = smRA.getString("realmAdapter.noWebSecMgr", CONTEXT_ID);
		_logger.warning(msg);
	    }
	}

	return webSecurityManager;
    }

    /**
     * Check if the given principal has the provided role. Returns
     * true if the principal has the specified role, false otherwise.
     * @return true if the principal has the specified role.
     * @param request Request we are processing
     * @param response Response we are creating
     * @param the principal 
     * @param the role
     */
    //START OF SJSAS 6232464 
    //public boolean hasRole(Principal principal, String role) {
    public boolean hasRole(HttpRequest request, 
                           HttpResponse response, 
                           Principal principal, 
                           String role) {
        WebSecurityManager secMgr = getWebSecurityManager(true);
	if (secMgr == null) {
	    return false;
	}

        //add HttpResponse and HttpResponse to the parameters, and remove
        //instance variable currentRequest from this class. References to
        //this.currentRequest are also removed from other methods.
        //String servletName = getResourceName( currentRequest.getRequestURI(),
        //                                      currentRequest.getContextPath());
        HttpServletRequest hrequest = (HttpServletRequest) request;
        String servletName = getResourceName( hrequest.getRequestURI(), 
                                              hrequest.getContextPath());
        //END OF SJSAS 6232464 
        
        // First try with the request.
        boolean isGranted = 
            secMgr.hasRoleRefPermission(servletName, role, principal);
        
        if (!isGranted){
            // START S1AS8PE 4966609
            // This case occurs when a direct call is made
            // to a jsp instead of using the server-name element defined in web.xml
            servletName = getCanonicalName(hrequest);

            // If we can't find any servletMapping for a resource
            // (usually a jsp), return false. 
            if (servletName.equalsIgnoreCase(UNCONSTRAINED)){
                if(_logger.isLoggable(Level.INFO)){
                    _logger.log(Level.INFO,
                        "Unable to find a <servlet-name> element which map: " 
                            + hrequest.getRequestURI());
                }

                /*
                 * For every security role in the web application add a
                 * WebRoleRefPermission to the corresponding role. The name of all such
                 * permissions shall be the empty string, and the actions of each 
                 * permission shall be the corresponding role name. When checking a 
                 * WebRoleRefPermission from a JSP not mapped to a servlet, use a 
                 * permission with the empty string as its name
                 * and with the argument to isUserInRole as its actions
                 */            
                isGranted = secMgr.hasRoleRefPermission("", role, principal);

                // END S1AS8PE 4966609
            } else {
                isGranted = secMgr.hasRoleRefPermission(servletName, 
                                                               role, 
                                                               principal);
            }
        }
 
        if(_logger.isLoggable(Level.FINE)) {
            _logger.fine( "Checking if servlet " + servletName 
                        + " with principal " + principal 
                        + " has role " + role
                        + " isGranted: " + isGranted);
        }
        
        return isGranted;
        
    }

    public void logout() {
        setSecurityContext(null);
    }

    /**
     * Authenticates and sets the SecurityContext in the TLS.
     * @return the authenticated principal.
     * @param the user name.
     * @param the authenticated data.
     */
    public Principal authenticate(String username, byte[] authData) {
        return authenticate(username, new String(authData));
    }

    /**
     * Authenticates and sets the SecurityContext in the TLS.
     * @return the authenticated principal.
     * @param the user name.
     * @param the password.
     */
    public Principal authenticate(String username, String password) {
        if (_logger.isLoggable(Level.FINE)){
            _logger.fine( "Tomcat callback for authenticate user/password");
            _logger.fine( "usename = " + username);
        }
        if(authenticate(username, password, null)) {
            SecurityContext secCtx = SecurityContext.getCurrent();
            assert (secCtx != null); // or auth should've failed
            return new WebPrincipal(username, password, secCtx);
        } else {
            return null;
        }
    }

    public Principal authenticate(X509Certificate certs[]) {
        if(authenticate(null, null, certs)) {
            SecurityContext secCtx = SecurityContext.getCurrent();
            assert (secCtx != null); // or auth should've failed
            return new WebPrincipal(certs, secCtx);
        } else {
            return null;
        }
    }

    /* IASRI 4688449
       This method was only used by J2EEInstanceListener to set the security
       context prior to invocations by re-authenticating a previously set
       WebPrincipal. This is now cached so no need.
    */
    public boolean authenticate(WebPrincipal prin) {
        if (prin.isUsingCertificate()) {
            return authenticate(null, null, prin.getCertificates());
        } else {
            return authenticate(prin.getName(), prin.getPassword(), null);
        }
    }
    

    /**
     * Authenticates and sets the SecurityContext in the TLS.
     * @return true if authentication succeeded, false otherwise.
     * @param the username.
     * @param the authentication method.
     * @param the authentication data.
     */
    protected boolean authenticate(String username, String password,
                                   X509Certificate[] certs) {

        SecurityContext.setCurrent(null);
        String realm_name = null;
        boolean success = false;
        try {
            if (certs != null) {
                Subject subject = new Subject();
                X509Certificate certificate = certs[0];
                X500Name x500Name = (X500Name) certificate.getSubjectDN();
                Switch.getSwitch().getCallFlowAgent().addRequestInfo(
                        RequestInfo.REMOTE_USER, x500Name.getName());
                subject.getPublicCredentials().add(x500Name);
                LoginContextDriver.login(subject, X500Name.class);
                realm_name = CertificateRealm.AUTH_TYPE;
            } else {
                Switch.getSwitch().getCallFlowAgent().addRequestInfo(
                        RequestInfo.REMOTE_USER, username);
                realm_name = _realmName;
                LoginContextDriver.login(username, password, realm_name);
            }
            success = true;
        } catch (Exception le) {
            success = false;
            if (_logger.isLoggable(Level.WARNING)) {
                _logger.warning("Web login failed: " + le.getMessage());
            }
        }
        if(success){
            if (_logger.isLoggable(Level.FINE)) {
                _logger.log(Level.FINE,"Web login succeeded for: " + username);
            }
        }
        if (webDesc!=null && webDesc.hasWebServices()) {
            WebPrincipal principal = new WebPrincipal(username, password, null);
            for (AuthenticationListener listener : WebServiceEngineImpl.getInstance().getAuthListeners()) {
                if (success) {
                    listener.authSucess(webDesc, null, principal);                        
                } else {
                    listener.authFailure(webDesc, null, principal);
                }
            }
        }
        return success;
    }
    // BEGIN IASRI 4747594
    /**
     * Set the run-as principal into the SecurityContext when needed.
     *
     * <P>This method will attempt to obtain the name of the servlet from
     * the ComponentInvocation. Note that there may not be one since this
     * gets called also during internal processing (not clear..) not just
     * part of servlet requests. However, if it is not a servlet request
     * there is no need (or possibility) to have a run-as setting so no
     * further action is taken.
     *
     * <P>If the servlet name is present the runAsPrincipals cache is
     * checked to find the run-as principal to use (if any). If one is set,
     * the SecurityContext is switched to this principal.
     *
     * @param inv The invocation object to process.
     *
     */
    public void preSetRunAsIdentity (ComponentInvocation inv) {

        String name=this.getServletName(inv);
        if (name == null) {
            return;
        }

        String runAs = (String)runAsPrincipals.get(name);
        
        if (runAs != null) {
            // The existing SecurityContext is saved - however, this seems
            // meaningless - see bug 4757733. For now, keep it unchanged
            // in case there are some dependencies elsewhere in RI.
            SecurityContext old = getSecurityContext();
            inv.setOldSecurityContext(old);

            // Set the run-as principal into SecurityContext
            loginForRunAs(runAs);
            
            if (_logger.isLoggable(Level.FINE)) {
                _logger.fine("run-as principal for " + name +
                             " set to: "+ runAs);
            }
        }
    }


    /**
     * Obtain servlet name from invocation.
     *
     * <P>In order to obtain the servlet name the following must be true:
     * The ComponentInvocation contains a 'class' of type HttpServlet, which
     * contains a valid ServletConfig object. This method returns the
     * value returned by getServletName() on the ServletConfig. If the above
     * is not met, null is returned.
     *
     * @param inv The invocation object to process.
     * @return Servlet name or null.
     *
     */
    private String getServletName(ComponentInvocation inv)
    {
        Object invInstance = inv.getInstance();
        
        if (invInstance instanceof HttpServlet) {

            HttpServlet thisServlet = (HttpServlet)invInstance;
            ServletConfig svc = thisServlet.getServletConfig();

            if (svc != null) {
                return thisServlet.getServletName();
            }
        }
        return null;
    }


    /**
     * Attempts to restore old SecurityContext (but fails).
     *
     * <P>In theory this method seems to attempt to check if a run-as
     * principal was set by preSetRunAsIdentity() (based on the indirect
     * assumption that if the servlet in the given invocation has a run-as
     * this must've been the case). If so, it retrieves the oldSecurityContext
     * from the invocation object and set it in the SecurityContext.
     *
     * <P>The problem is that the invocation object is not the same object
     * as was passed in to preSetRunAsIdentity() so it will never contain
     * the right info - see bug 4757733.
     *
     * <P>In practice it means this method only ever sets the
     * SecurityContext to null (if run-as matched) or does nothing. In
     * particular note the implication that it <i>will</i> be set to
     * null after a run-as invocation completes. This behavior will be
     * retained for the time being for consistency with RI. It must be fixed
     * later.
     *
     * @param inv The invocation object to process.
     *
     */
    public void postSetRunAsIdentity (ComponentInvocation inv){

        String name=this.getServletName(inv);
        if (name == null) {
            return;
        }

        String runAs = (String)runAsPrincipals.get(name);
        if (runAs != null) {
            setSecurityContext (inv.getOldSecurityContext ()); // always null

        }
    }
    // END IASRI 4747594
    
    private void loginForRunAs (String principal) {
	LoginContextDriver.loginPrincipal (principal, _realmName);    
    }
    private SecurityContext getSecurityContext (){
        return SecurityContext.getCurrent ();
    }
    private void setSecurityContext (SecurityContext sc){
        SecurityContext.setCurrent (sc);
    }

    protected String getPassword(String username) {
        throw new IllegalStateException("Should not reach here");
    }

    protected Principal getPrincipal(String username) {
        throw new IllegalStateException("Should not reach here");
    }
    
    //START OF IASRI 4809144
    /** 
     * This method is added to create a Principal based on the username only. 
     * Hercules stores the username as part of authentication failover and 
     * needs to create a Principal based on username only <sridhar.satuloori@sun.com>
     * @param username  
     * @return Principal for the user username
     * HERCULES:add
     */
    public Principal createFailOveredPrincipal(String username){
        _logger.log(Level.FINEST,"IN createFailOveredPrincipal ("+username+")");
        //set the appropriate security context
        loginForRunAs(username);
        SecurityContext secCtx = SecurityContext.getCurrent();
         _logger.log(Level.FINE,"Security context is "+secCtx);
        assert (secCtx != null);
        Principal principal = new WebPrincipal(username, null, secCtx);
        _logger.log(Level.INFO,"Principal created for FailOvered user "+principal);
        return principal;
    }
    //END OF IASRI 4809144     
    
    /**
     * Perform access control based on the specified authorization constraint.
     * Return <code>true</code> if this constraint is satisfied and processing
     * should continue, or <code>false</code> otherwise.
     *
     * @param request Request we are processing
     * @param response Response we are creating
     * @param constraint Security constraint we are enforcing
     * @param The Context to which client of this class is attached.
     *
     * @exception IOException if an input/output error occurs
     */
    public boolean hasResourcePermission(HttpRequest request,
                                         HttpResponse response,
                                         SecurityConstraint[] constraints,
                                         Context context)
        throws IOException {
        boolean isGranted = false;
        try {
            isGranted = invokeWebSecurityManager(
                request, response, constraints);
        } catch(IOException iex) {
            throw iex;
        } catch(Throwable ex) {
            ((HttpServletResponse) response.getResponse()).sendError
                    (HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.setDetailMessage(sm.getString("realmBase.forbidden"));
            return isGranted;
        }
          
        if ( isGranted ){
            return isGranted;
        } else {
            ((HttpServletResponse) response.getResponse()).sendError
                    (HttpServletResponse.SC_FORBIDDEN);
            response.setDetailMessage(sm.getString("realmBase.forbidden"));
            // invoking secureResponse
            invokePostAuthenticateDelegate(request, response, context);
            return isGranted;
        }
    }
    
    /**
     * Invokes WebSecurityManager to perform access control check.
     * Return <code>true</code> if permission is granted, or <code>false</code> 
     * otherwise.
     *
     * @param request Request we are processing
     * @param response Response we are creating
     * @param constraint Security constraint we are enforcing
     *
     * @exception IOException if an input/output error occurs
     */
    private boolean invokeWebSecurityManager(HttpRequest request,
                                         HttpResponse response,
                                         SecurityConstraint[] constraints)
	throws IOException {

        // allow access to form login related pages and targets
        // and the "j_security_check" action
        boolean evaluated = false;
        try {
            rwLock.readLock().lock();
            evaluated = contextEvaluated;
        } finally {
            rwLock.readLock().unlock();
        }

        if (!evaluated) {
            try {
                rwLock.writeLock().lock();
                if (!contextEvaluated) {
                    // get Context here as preAuthenticateCheck does not have it
                    // and our Container is always a Context
                    Context context = (Context)getContainer();
                    LoginConfig config = context.getLoginConfig();
                    if ((config != null) &&
                            (Constants.FORM_METHOD.equals(config.getAuthMethod()))) {
                        loginPage = config.getLoginPage();
                        errorPage = config.getErrorPage();
                    }
                    contextEvaluated = true;
                }
            } finally {
                rwLock.writeLock().unlock();
            }
        }

        if (loginPage != null || errorPage != null) {
            String requestURI = request.getRequestPathMB().toString();
            if(_logger.isLoggable(Level.FINE)) {
                _logger.fine("[Web-Security]  requestURI: " + requestURI + 
                        " loginPage: " + loginPage);
            }
            if (loginPage != null && loginPage.equals(requestURI)) {
                if(_logger.isLoggable(Level.FINE)) {
                    _logger.fine(" Allow access to login page " + loginPage);
                }
                return true;
            }
            else if (errorPage != null && errorPage.equals(requestURI)) {
                if(_logger.isLoggable(Level.FINE)) {
                    _logger.fine(" Allow access to error page " + errorPage);
                }
                return true;
            }

            else if (requestURI.endsWith(Constants.FORM_ACTION)) {
                if(_logger.isLoggable(Level.FINE)) {
		    _logger.fine(" Allow access to username/password submission");
                }
                return true;
            }
        }

        HttpServletRequest hrequest = (HttpServletRequest)request;
                
        if ( hrequest.getServletPath() == null){
            request.setServletPath( getResourceName( hrequest.getRequestURI(), 
                    hrequest.getContextPath()));
        }
                
        if(_logger.isLoggable(Level.FINE))
            _logger.fine("[Web-Security] [ hasResourcePermission ] Principal: " 
                + hrequest.getUserPrincipal() + " ContextPath: " + hrequest.getContextPath());

        WebSecurityManager secMgr = getWebSecurityManager(true);
	if (secMgr == null) {
	    return false;
	}
        return secMgr.hasResourcePermission(hrequest);
    }
    
    /**
     * Enforce any user data constraint required by the security constraint
     * guarding this request URI.  Return <code>true</code> if this constraint
     * was not violated and processing should continue, or <code>false</code>
     * if we have created a response already.
     *
     * @param request Request we are processing
     * @param response Response we are creating
     * @param constraint Security constraint being checked
     *
     * @exception IOException if an input/output error occurs
     */
    public boolean hasUserDataPermission(HttpRequest request,
                                         HttpResponse response,
                                         SecurityConstraint[] constraints) throws IOException {
        HttpServletRequest hrequest = (HttpServletRequest)request;
        if ( hrequest.getServletPath() == null){
            request.setServletPath( 
                getResourceName( hrequest.getRequestURI(), 
                                hrequest.getContextPath()));
        }
       
        if(_logger.isLoggable(Level.FINE)){
            _logger.fine("[Web-Security][ hasUserDataPermission ] Principal: " 
                        + hrequest.getUserPrincipal() 
                        + " ContextPath: " 
                        + hrequest.getContextPath());    
        }
        
        if (request.getRequest().isSecure()) {            
            if(_logger.isLoggable(Level.FINE))
                _logger.fine("[Web-Security] request.getRequest().isSecure(): " + request.getRequest().isSecure());   
            return true;
        }
        
        WebSecurityManager secMgr = getWebSecurityManager(true);
	if (secMgr == null) {
	    return false;
	}

        int isGranted = 0;
        try {
            isGranted = secMgr.hasUserDataPermission(hrequest);
        } catch (IllegalArgumentException e) {
            //end the request after getting IllegalArgumentException while checking
            //user data permission
            String msg = smRA.getString("realmAdapter.badRequest", e.getMessage());
            _logger.warning(msg);
            if(_logger.isLoggable(Level.FINE)) {
                _logger.log(Level.FINE, msg, e);
            }
            ((HttpServletResponse) response.getResponse()).sendError
                (HttpServletResponse.SC_BAD_REQUEST, msg);
            return false;
        }
        
        // Only redirect if we are sure the user will be granted.
        // See bug 4947698 

        // This method will return:
        // 1  - if granted
        // 0  - if not granted
        // -1 - if the current transport is not granted, but a redirection can occur
        //      so the grand will succeed.
        if ( isGranted == -1 ){
            if(_logger.isLoggable(Level.FINE))
                _logger.fine( "[Web-Security] redirecting using SSL");
            return redirect(request, response);
        } 
        
        if (isGranted == 0){
            ((HttpServletResponse) response.getResponse()).sendError
                (HttpServletResponse.SC_FORBIDDEN,
                    sm.getString("realmBase.forbidden")); 
            return false;
        }

        return true;
     }
    
    private boolean redirect(HttpRequest request, HttpResponse response) throws IOException{
        // Initialize variables we need to determine the appropriate action
        HttpServletRequest hrequest =
            (HttpServletRequest) request.getRequest();
        HttpServletResponse hresponse =
            (HttpServletResponse) response.getResponse();
        int redirectPort = request.getConnector().getRedirectPort();

        // Is redirecting disabled?
        if (redirectPort <= 0) {
            if(_logger.isLoggable(Level.INFO))
                _logger.fine("[Web-Security]  SSL redirect is disabled");

            hresponse.sendError
                (HttpServletResponse.SC_FORBIDDEN, hrequest.getRequestURI());
            return (false);
        }
       
        String protocol = "https";
        String host = hrequest.getServerName();
        StringBuffer file = new StringBuffer(hrequest.getRequestURI());
        String requestedSessionId = hrequest.getRequestedSessionId();
        if ((requestedSessionId != null) &&
            hrequest.isRequestedSessionIdFromURL()) {
            file.append(";jsessionid=");
            file.append(requestedSessionId);
        }
        String queryString = hrequest.getQueryString();
        if (queryString != null) {
            file.append('?');
            file.append(queryString);
        }
        URL url = null;
        try {
            url = new URL(protocol, host, redirectPort, file.toString());
            hresponse.sendRedirect(url.toString());
            return (false);
        } catch (MalformedURLException e) {
            hresponse.sendError
                (HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                 hrequest.getRequestURI());
            return (false);
        }
    }

    //START SJSAS 6232464
    //pass in HttpServletResponse instead of saving it as instance variable
    //private String getCanonicalName(){
    private String getCanonicalName(HttpServletRequest currentRequest){
    //END SJSAS 6232464
        String servletUri = "";
        String currentUri = "";
        String aliasUri = "";
        String currentUriExtension = "";
        String aliasUriExtension = "";        
        boolean isAliasExists = false;
        for (Iterator itr = webDesc.getWebComponentDescriptors().iterator(); itr.hasNext();) {
            WebComponentDescriptor webComponentDescriptor = (WebComponentDescriptor)itr.next();
            servletUri = webComponentDescriptor.getWebComponentImplementation();

            currentUri = getResourceName( currentRequest.getRequestURI(), currentRequest.getContextPath());
            currentUriExtension = getExtension(currentUri);
            
            // First check the servlet mapping
            for (Iterator i = webComponentDescriptor.getUrlPatternsSet().iterator(); i.hasNext();) {
                aliasUri = i.next().toString();
                aliasUriExtension = getExtension(aliasUri);
                
                if (aliasUri.equalsIgnoreCase(currentUri)){
                    isAliasExists = true;
                    break;
                }     
                
                if (aliasUriExtension.equalsIgnoreCase(currentUriExtension) 
                        && aliasUri.equalsIgnoreCase("*" + aliasUriExtension)){
                    isAliasExists = true;
                    break;
                }  
            }

            if (currentUri.equalsIgnoreCase(servletUri) 
                    || isAliasExists){
                return webComponentDescriptor.getCanonicalName();
            }            
        }
        return UNCONSTRAINED;
    }
    
    private String getResourceName(String uri, String contextPath){
        try{
            return uri.substring(contextPath.length());           
        } catch (java.lang.Exception ex){
            return "";
        }   
    }
    
    private String getExtension(String uri){
        try{
            return uri.substring(uri.lastIndexOf("."));           
        } catch (java.lang.Exception ex){
            // don't use the cache and let jacc create the permission.
            return "";
        }   
    }
    

    /**
     * Return a short name for this Realm Adapter implementation.
     */
    protected String getName() {
        return (this.name);
    }

     /**
      * Return the name of the realm this RealmAdapter uses.
      *
      * @return realm name
      *
      */
     public String getRealmName() {
         return _realmName;
     }

    public void setRealmName(String realmName){
        // do nothing since this is done when initializing the Realm.
    }

    //START SJSAS 6232464 6202703
    /**
     * Returns null 
     * 1. if there are no security constrainst defined on any of the web
     * resources within the context, or
     * 2. if the target is a form login related page or target.
     * 
     * otherwise return an empty array of SecurityConstraint.
     */
    public SecurityConstraint[] findSecurityConstraints(HttpRequest request, 
							Context context) {
	WebSecurityManager secMgr = getWebSecurityManager(false);
 
	if (secMgr != null && secMgr.hasNoConstrainedResources()) {
	    return null;
	}
 
	SecurityConstraint[] constraints = RealmAdapter.emptyConstraints;
	return constraints;
    }
    //END SJSAS 6232464 6202703
    
    //START SJSAS 6202703
    /**
     * Checks whether or not authentication is needed.
     * Returns an int, one of AUTHENTICATE_NOT_NEEDED, AUTHENTICATE_NEEDED,
     * or AUTHENTICATED_NOT_AUTHORIZED
     *
     * @param request Request we are processing
     * @param response Response we are creating
     * @param constraints Security constraint we are enforcing
     * @param disableProxyCaching whether or not to disable proxy caching for
     *        protected resources.
     * @param securePagesWithPragma true if we add headers which 
     
     * are incompatible with downloading office documents in IE under SSL but
     * which fix a caching problem in Mozilla.
     * @param ssoEnabled true if sso is enabled
     *
     * @exception IOException if an input/output error occurs
     */
    public int preAuthenticateCheck(HttpRequest request,
                                    HttpResponse response,
                                    SecurityConstraint[] constraints,
                                    boolean disableProxyCaching,
                                    boolean securePagesWithPragma,
                                    boolean ssoEnabled)
        throws IOException {
        boolean isGranted = false;

        try {
            if (helper != null && helper.getServerAuthConfig() != null) {
                return Realm.AUTHENTICATE_NEEDED;
            }

            isGranted = invokeWebSecurityManager(
                request, response, constraints);
        } catch(IOException iex) {
            throw iex;
        } catch(Throwable ex) {
            ((HttpServletResponse) response.getResponse()).sendError
                    (HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.setDetailMessage(sm.getString("realmBase.forbidden"));
            return Realm.AUTHENTICATED_NOT_AUTHORIZED;
        }
        
        if(isGranted) {
            HashMap sharedState = null;
            boolean delegateSessionMgmt = false;
            //XXX Keep it for reference
            /*
  	    if (this.sAC != null) {
                sharedState = new HashMap();
                try {
                    delegateSessionMgmt = this.sAC.managesSessions(sharedState);
                } catch (AuthException ae) {
                    delegateSessionMgmt = false;
                }
            }

            if (delegateSessionMgmt) {      
                if (validate(request, response, null, null)) {
                    disableProxyCaching(request, response, disableProxyCaching,
                        securePagesWithPragma);
                }
            } else if( ((HttpServletRequest) request).getUserPrincipal() != null) {
            */
            if( ((HttpServletRequest) request).getUserPrincipal() != null) {
                disableProxyCaching(request, response, disableProxyCaching,
                    securePagesWithPragma);
                if (ssoEnabled) {
                    HttpServletRequest hreq =
                        (HttpServletRequest)request.getRequest();
                    WebSecurityManager webSecMgr = getWebSecurityManager(true);
                    if (!webSecMgr.permitAll(hreq)) {
                        //create a session for protected sso association
                        hreq.getSession(true);
                    }
                }
            }
            return Realm.AUTHENTICATE_NOT_NEEDED;
        } else if(((HttpServletRequest) request).getUserPrincipal() != null){
            ((HttpServletResponse) response.getResponse()).sendError
                    (HttpServletResponse.SC_FORBIDDEN);
            response.setDetailMessage(sm.getString("realmBase.forbidden"));
            return Realm.AUTHENTICATED_NOT_AUTHORIZED;
        } else {
            disableProxyCaching(request, response, disableProxyCaching,securePagesWithPragma);
            return Realm.AUTHENTICATE_NEEDED;
        }
    }
    
    
    /**
     * Authenticates the user making this request, based on the specified
     * login configuration.  Return <code>true</code> if any specified
     * requirements have been satisfied, or <code>false</code> if we have
     * created a response challenge already.
     *
     * @param request Request we are processing
     * @param response Response we are creating
     * @param context The Context to which client of this class is attached.
     * @param authenticantion the current authenticator.
     * @exception IOException if an input/output error occurs
     */
    public boolean invokeAuthenticateDelegate(HttpRequest request,
                                              HttpResponse response,
                                              Context context,
                                              Authenticator authenticator)
	throws IOException {

        boolean result = false;
	LoginConfig config = context.getLoginConfig();
        ServerAuthConfig serverAuthConfig = null;
        try {
            if (helper != null) {
                serverAuthConfig = helper.getServerAuthConfig();
            }
        } catch(Exception ex) {
            IOException iex = new IOException();
            iex.initCause(ex);
            throw iex;
        }   
        if (serverAuthConfig != null) {
            //JSR 196 is enabled for this application
            result = validate(request,response, config, authenticator);
        } else {
            //jsr196 is not enabled.  Use the current authenticator.
            result = ((AuthenticatorBase) authenticator).authenticate(
                request, response, config);
        }
        return result;
    }

    /**
     * Post authentication for given request and response.
     *
     * @param request Request we are processing
     * @param response Response we are creating
     * @param context The Context to which client of this class is attached.
     * @exception IOException if an input/output error occurs
     */
    public boolean invokePostAuthenticateDelegate(HttpRequest request,
                                              HttpResponse response,
                                              Context context)
	throws IOException {

        boolean result = false;
        ServerAuthContext sAC = null;
        try {
            if (helper != null) {
                HttpServletRequest req = (HttpServletRequest)request.getRequest();
                MessageInfo messageInfo =
                        (MessageInfo)req.getAttribute(MESSAGE_INFO);
                if (messageInfo != null) {
                    //JSR 196 is enabled for this application
                    sAC = (ServerAuthContext)
                            messageInfo.getMap().get(SERVER_AUTH_CONTEXT);
                    if (sAC != null) {
                        AuthStatus authStatus = 
                                sAC.secureResponse(messageInfo,
                                    null); //null serviceSubject
                        result = AuthStatus.SUCCESS.equals(authStatus);
                    }
                } 
            }
        } catch(AuthException ex) {
            IOException iex = new IOException();
            iex.initCause(ex);
            throw iex;
        } finally {
            if (helper != null && sAC != null) {
                if (request instanceof HttpRequestWrapper) {
                    request.removeNote(Globals.WRAPPED_REQUEST);
                }
                if (response instanceof HttpResponseWrapper) {
                    request.removeNote(Globals.WRAPPED_RESPONSE);
                }
            }
        }   
        return result;
    }

    /**
     * Return <tt>true</tt> if a Security Extension is available.
     * @return <tt>true</tt> if a Security Extension is available. 
     */    
    public boolean isSecurityExtensionEnabled(){
        if (helper == null) {
            return false;
        } else {
            try {
                return (helper.getServerAuthConfig() != null);
            } catch(Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * This must be invoked after virtualServer is set.
     */
    private HttpServletHelper getConfigHelper() {
        Map map = new HashMap();
        map.put(HttpServletConstants.WEB_BUNDLE, webDesc);
        return new HttpServletHelper(getAppContextID(),
            map, null, // null handler
            _realmName, isSystemApp, defaultSystemProviderID);
    }

    /**
     * This must be invoked after virtualServer is set.
     */
    private String getAppContextID() {
        return this.virtualServer.getName() + " " + webDesc.getContextRoot();
    }

    private boolean validate(HttpRequest request,
			     HttpResponse response,
			     LoginConfig config,
			     Authenticator authenticator)
                             throws IOException {

        HttpServletRequest req = (HttpServletRequest)request.getRequest();
        HttpServletResponse res = (HttpServletResponse)response.getResponse();

        Subject subject = new Subject();

	MessageInfo messageInfo = new HttpMessageInfo(req, res);

        boolean rvalue = false;
        boolean isMandatory = true;
        try {
            WebSecurityManager webSecMgr = getWebSecurityManager(true);
            isMandatory = !webSecMgr.permitAll(req);
            if (isMandatory) {
                messageInfo.getMap().put(HttpServletConstants.IS_MANDATORY,
                    Boolean.TRUE.toString());
            }
            ServerAuthContext sAC =
                    helper.getServerAuthContext(messageInfo,
                        null); // null serviceSubject
            if (sAC != null) {
                AuthStatus authStatus = 
                        sAC.validateRequest(messageInfo, subject,
                            null); // null serviceSubject
                rvalue = AuthStatus.SUCCESS.equals(authStatus);

                if (rvalue) { // cache it only if validateRequest = true
                    messageInfo.getMap().put(SERVER_AUTH_CONTEXT, sAC);
                    req.setAttribute(MESSAGE_INFO, messageInfo);
                }
            } else {
		throw new AuthException("null ServerAuthContext");
	    }
        } catch(AuthException ae) {
            if (_logger.isLoggable(Level.FINE)) {
                _logger.log(Level.FINE,
                "JAMC: http msg authentication fail",ae);
            }
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        if (rvalue) {
            Set principalSet = subject.getPrincipals();
            // must be at least one new principal to establish 
	    // non-default security context
            if (principalSet != null && !principalSet.isEmpty()) {
                SecurityContext ctx = new SecurityContext(subject);
                //XXX assuming no null principal here
		Principal p = ctx.getCallerPrincipal();
		WebPrincipal wp = new WebPrincipal(p,ctx);
		try {
                    //XXX Keep it for reference
                    /*
		    if (this.sAC.managesSessions(sharedState)) {
			// registration (via proxy) does not occur
			// if context manages sessions
			// record authentication information in the request
			request.setAuthType(PROXY_AUTH_TYPE);
			request.setUserPrincipal(wp);
		    } else {
			AuthenticatorProxy proxy = 
			    new AuthenticatorProxy(authenticator,wp);
			proxy.authenticate(request,response,config);
		    }
                    */
                    String authType = (String)messageInfo.getMap().get(
                            HttpServletHelper.AUTH_TYPE);
                    boolean register = messageInfo.getMap().containsKey(
                            HttpServletConstants.REGISTER_WITH_AUTHENTICATOR);

                    if (authType == null && config != null &&
                            config.getAuthMethod() != null) {
                        authType = config.getAuthMethod();
                    }

                    if (register) {
                        AuthenticatorProxy proxy = new AuthenticatorProxy
                               (authenticator, wp, authType);
                        proxy.authenticate(request,response,config);
                    } else {
                        request.setAuthType((authType == null) ?
                                PROXY_AUTH_TYPE: authType);
                        request.setUserPrincipal(wp);
                    }
		} catch (LifecycleException le) {
		    _logger.log(Level.SEVERE,"[Web-Security] unable to register session",le);
		
                                }

                HttpServletRequest newRequest = (HttpServletRequest) 
                    messageInfo.getRequestMessage();
                if (newRequest != req) {
                    request.setNote(Globals.WRAPPED_REQUEST, 
                            new HttpRequestWrapper(request, newRequest));
                }

                HttpServletResponse newResponse = (HttpServletResponse) 
                    messageInfo.getResponseMessage();
                if (newResponse != res) {
                    request.setNote(Globals.WRAPPED_RESPONSE, 
                            new HttpResponseWrapper(response,newResponse));
                }

	    } else if (isMandatory) {
		rvalue = false;
	    }
	}
        return rvalue;
    }
    
    /**
     * get the default provider id for system apps if one has been established.
     * the default provider for system apps is established by defining
     * a system property.
     * @return the provider id or null. 
     */
    private static String getDefaultSystemProviderID() {
	String p = System.getProperty(SYSTEM_HTTPSERVLET_SECURITY_PROVIDER);
	if (p != null) {
	    p = p.trim();
	    if (p.length() == 0) {
		p = null;
	    }
	}
	return p;
    }

    private static String PROXY_AUTH_TYPE = "PLUGGABLE_PROVIDER";

    // inner class extends AuthenticatorBase such that session registration
    // of webtier can be invoked by RealmAdapter after authentication
    // by authentication module.

    class AuthenticatorProxy extends AuthenticatorBase {

	private AuthenticatorBase authBase;
	private Principal principal;
	private String authType;

	public boolean getCache() {
	    return authBase.getCache();
	}

	public Container getContainer() {
	    return authBase.getContainer();
	}

	AuthenticatorProxy(Authenticator authenticator, Principal p, String authType) 
	    throws LifecycleException 
	{

	    this.authBase = (AuthenticatorBase) authenticator;
	    this.principal = p;
	    this.authType = 
		authType == null ? RealmAdapter.PROXY_AUTH_TYPE : authType;

	    setCache(authBase.getCache());
	    setContainer(authBase.getContainer());
	    start(); //finds sso valve and sets its value in proxy
	}

	public boolean 
	authenticate(HttpRequest request,
		     HttpResponse response, 
		     LoginConfig config) throws IOException

	{
	    register(request,response,this.principal,this.authType,
		     this.principal.getName(),null);
	    return true;
	}
    }

    private class HttpMessageInfo implements MessageInfo {
        private Object request = null;
        private Object response = null;
        private Map map = new HashMap();

        HttpMessageInfo() {
        }

        HttpMessageInfo(HttpServletRequest request,
                HttpServletResponse response) {
            this.request = request;
            this.response = response;
        } 

        public Object getRequestMessage() {
            return request;
        }

        public Object getResponseMessage() {
            return response;
        }

        public void setRequestMessage(Object request) {
            this.request = request;
        }

        public void setResponseMessage(Object response) {
            this.response = response;
        }

        public Map getMap() {
            return map;
        }
    }
}
