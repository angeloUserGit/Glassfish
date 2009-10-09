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

package com.sun.enterprise.security.webservices;

import com.sun.enterprise.deployment.ServiceReferenceDescriptor;
import com.sun.enterprise.deployment.WebServiceEndpoint;
import com.sun.enterprise.deployment.runtime.common.MessageSecurityBindingDescriptor;
import com.sun.enterprise.security.jauth.AuthException;
import com.sun.logging.LogDomains;
import com.sun.web.security.RealmAdapter;
import com.sun.xml.rpc.spi.runtime.SOAPMessageContext;
import com.sun.xml.rpc.spi.runtime.SystemHandlerDelegate;
import com.sun.xml.ws.assembler.ClientPipelineHook;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import org.glassfish.webservices.Ejb2RuntimeEndpointInfo;
import org.glassfish.webservices.EjbRuntimeEndpointInfo;
import org.glassfish.webservices.SecurityService;
import org.glassfish.webservices.WebServiceContextImpl;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.component.Singleton;

import com.sun.enterprise.security.web.integration.WebPrincipal;
import com.sun.enterprise.security.SecurityContext;
import java.security.Principal;
import org.apache.catalina.Globals;
import org.apache.catalina.util.Base64;
import org.glassfish.webservices.monitoring.AuthenticationListener;
import org.glassfish.webservices.monitoring.Endpoint;
import org.glassfish.webservices.monitoring.WebServiceEngineImpl;
import com.sun.enterprise.security.audit.AuditManager;
import com.sun.enterprise.security.jauth.ServerAuthContext;
import com.sun.enterprise.security.jmac.provider.ClientAuthConfig;
import com.sun.enterprise.security.jmac.provider.ServerAuthConfig;
import com.sun.xml.rpc.spi.runtime.StreamingHandler;
import java.util.HashMap;
import java.util.Map;
import org.jvnet.hk2.annotations.Inject;

/**
 *
 * @author Kumar
 */
@Service
@Scoped(Singleton.class)
public class SecurityServiceImpl implements SecurityService {
    
    @Inject
    private AuditManager auditManager;

    protected static final Logger _logger = LogDomains.getLogger(SecurityServiceImpl.class,
        LogDomains.SECURITY_LOGGER);
    
    private static final Base64 base64Helper = new Base64();
    private static final String AUTHORIZATION_HEADER = "authorization";
    
    public Object mergeSOAPMessageSecurityPolicies(MessageSecurityBindingDescriptor desc) {
        try {
	    // merge message security policy from domain.xml and sun-specific
	    // deployment descriptor
	     ServerAuthConfig 
                     serverAuthConfig =
                     com.sun.enterprise.security.jmac.provider.ServerAuthConfig.getConfig
		(com.sun.enterprise.security.jauth.AuthConfig.SOAP,
		 desc,
		 null);
             return serverAuthConfig;
	} catch (com.sun.enterprise.security.jauth.AuthException ae) {
            _logger.log(Level.SEVERE, 
		       "EJB Webservice security configuration Failure", ae);
	}
        return null;
    }

    public boolean doSecurity(HttpServletRequest hreq, EjbRuntimeEndpointInfo epInfo, String realmName, WebServiceContextImpl context) {
        //BUG2263 - Clear the value of UserPrincipal from previous request
        //If authentication succeeds, the proper value will be set later in
        //this method.
        boolean authenticated = false;
        try {
        if (context != null) {
            context.setUserPrincipal(null);        
        }
        
        WebServiceEndpoint endpoint = epInfo.getEndpoint();
        
        String method = hreq.getMethod();
        if( method.equals("GET") || !endpoint.hasAuthMethod() ) {
            return true;
        }
        
        WebPrincipal webPrincipal = null;
        String endpointName = endpoint.getEndpointName();
        
        if( endpoint.hasBasicAuth() ) {
            String rawAuthInfo = hreq.getHeader(AUTHORIZATION_HEADER);
            if (rawAuthInfo==null) {
                sendAuthenticationEvents(false, hreq.getRequestURI(), null);
                return false;
            }
            
            String[] usernamePassword =
                    parseUsernameAndPassword(rawAuthInfo);
            if( usernamePassword != null ) {
                webPrincipal = new WebPrincipal
                        (usernamePassword[0], usernamePassword[1], SecurityContext.init());
            } else {
                _logger.log(Level.WARNING, "BASIC AUTH username/password " +
                           "http header parsing error for " + endpointName);
            }
        } else {

            X509Certificate certs[] =  (X509Certificate[]) hreq.getAttribute(Globals.CERTIFICATES_ATTR);
            if ((certs == null) || (certs.length < 1)) {
                certs = (X509Certificate[])
                    hreq.getAttribute(Globals.SSL_CERTIFICATE_ATTR);
            }

            if( certs != null ) {
                webPrincipal = new WebPrincipal(certs, SecurityContext.init());
            } else {
                _logger.log(Level.WARNING, "CLIENT CERT authentication error for " + endpointName);
            }

        }

        if (webPrincipal==null) {
            sendAuthenticationEvents(false, hreq.getRequestURI(), null);           
            return authenticated;
        }
        
        RealmAdapter ra = new RealmAdapter(realmName);
        authenticated = ra.authenticate(webPrincipal);
        if( authenticated == false ) {
            sendAuthenticationEvents(false, hreq.getRequestURI(), webPrincipal);
            _logger.fine("authentication failed for " +  endpointName);
        } else {
            
        }
        sendAuthenticationEvents(true, hreq.getRequestURI(), webPrincipal);
        if(epInfo instanceof Ejb2RuntimeEndpointInfo) {
            // For JAXRPC based EJb endpoints the rest of the steps are not needed
            return authenticated;
        }
        //Setting if userPrincipal in WSCtxt applies for JAXWS endpoints only
        epInfo.prepareInvocation(false);
        WebServiceContextImpl ctxt = (WebServiceContextImpl)
                ((EjbRuntimeEndpointInfo)epInfo).getWebServiceContext();
        ctxt.setUserPrincipal(webPrincipal);
        
        if (org.glassfish.internal.api.Globals.getDefaultHabitat() != null) {
            if (auditManager.isAuditOn()) {
                auditManager.ejbAsWebServiceInvocation(
                        epInfo.getEndpoint().getEndpointName(), authenticated);
            }
        }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return authenticated;
    } 

    private String[] parseUsernameAndPassword(String rawAuthInfo) {

        String[] usernamePassword = null;
        if ( (rawAuthInfo != null) && 
             (rawAuthInfo.startsWith("Basic ")) ) {
            String authString = rawAuthInfo.substring(6).trim();
            // Decode and parse the authorization credentials
            String unencoded =
                new String(base64Helper.decode(authString.getBytes()));
            int colon = unencoded.indexOf(':');
            if (colon > 0) {
                usernamePassword = new String[2];
                usernamePassword[0] = unencoded.substring(0, colon).trim();
                usernamePassword[1] = unencoded.substring(colon + 1).trim();
            }
        }
        return usernamePassword;
    }

    
     private void sendAuthenticationEvents(boolean success,
            String url, Principal principal) {
        
        Endpoint endpoint = WebServiceEngineImpl.getInstance().getEndpoint(url);
        if (endpoint==null) {
            return;
        }
        for (AuthenticationListener listener : WebServiceEngineImpl.getInstance().getAuthListeners()) {
            if (success) {
                listener.authSucess(endpoint.getDescriptor().getBundleDescriptor(),
                        endpoint, principal);
            } else {
                listener.authFailure(endpoint.getDescriptor().getBundleDescriptor(),
                        endpoint, principal);
            }
        }
    }    
        
    public void resetSecurityContext() {
        SecurityContext.setCurrent(null);
    }

    public SystemHandlerDelegate getSecurityHandler(WebServiceEndpoint endpoint) {

        if (!endpoint.hasAuthMethod()) {
            try {
                ServerAuthConfig config = ServerAuthConfig.getConfig(com.sun.enterprise.security.jauth.AuthConfig.SOAP,
                        endpoint.getMessageSecurityBinding(),
                        null);
                if (config != null) {
                    return new ServletSystemHandlerDelegate(config, endpoint);
                }
            } catch (Exception e) {
                _logger.log(Level.SEVERE,
                        "Servlet Webservice security configuration Failure", e);
            }
        }
        return null;
    }

    public boolean validateRequest(Object serverAuthConfig, StreamingHandler implementor, SOAPMessageContext context) {
        ServerAuthConfig authConfig = (ServerAuthConfig) serverAuthConfig;
        if (authConfig != null) {
            ServerAuthContext sAC = authConfig.getAuthContext((StreamingHandler) implementor, context.getMessage());
            if (sAC != null) {
                try {
                    return WebServiceSecurity.validateRequest(context, sAC);
                } catch (AuthException ex) {
                    _logger.log(Level.SEVERE, ex.getMessage(), ex);
                    throw new RuntimeException(ex);
                }
            }
        }
        return true;
    }

    public void secureResponse(Object serverAuthConfig, StreamingHandler implementor,SOAPMessageContext msgContext) {
        if (serverAuthConfig != null) {
            ServerAuthConfig config = (ServerAuthConfig)serverAuthConfig;
            ServerAuthContext sAC = config.getAuthContext(implementor, msgContext.getMessage());
            if (sAC != null) {
                try {
                    WebServiceSecurity.secureResponse(msgContext, sAC);
                } catch (AuthException ex) {
                    _logger.log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex);
                }
            }

        }
    }

    public HandlerInfo getMessageSecurityHandler(MessageSecurityBindingDescriptor binding, QName serviceName) {
        HandlerInfo rvalue = null;
        try {
            ClientAuthConfig config = ClientAuthConfig.getConfig(com.sun.enterprise.security.jauth.AuthConfig.SOAP, binding, null);
            if (config != null) {
                // get understood headers from auth module.
                QName[] headers = config.getMechanisms();
                Map properties = new HashMap();
                properties.put(MessageLayerClientHandler.CLIENT_AUTH_CONFIG, config);
                properties.put(javax.xml.ws.handler.MessageContext.WSDL_SERVICE, serviceName);
                rvalue = new HandlerInfo(MessageLayerClientHandler.class, properties, headers);
            }

        } catch (AuthException ex) {
            _logger.log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
        return rvalue;
    }

    @Override
    public ClientPipelineHook getClientPipelineHook(ServiceReferenceDescriptor ref) {
        return new ClientPipeCreator(ref);
    }
}
