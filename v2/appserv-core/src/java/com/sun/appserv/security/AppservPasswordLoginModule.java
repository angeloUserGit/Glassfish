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

package com.sun.appserv.security;

import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import com.sun.logging.LogDomains;
import com.sun.enterprise.util.i18n.StringManager;
import javax.security.auth.*;
import javax.security.auth.callback.*;
import javax.security.auth.login.*;
import javax.security.auth.spi.*;
import com.sun.enterprise.deployment.PrincipalImpl;
import com.sun.enterprise.deployment.Group;
import com.sun.enterprise.security.auth.realm.Realm;
import com.sun.enterprise.security.auth.login.PasswordCredential;
import com.sun.web.security.PrincipalGroupFactory;


/**
 * Abstract base class for password-based login modules.
 *
 * <P>Most login modules receive a username and password from the client
 * (possibly through HTTP BASIC auth, or FORM, or other mechanism) and
 * then make (or delegate) an authentication decision based on this data.
 * This class provides common methods for such password-based login modules.
 *
 * <P>Subclasses need to implement the authenticateUser() method and later
 * call commitUserAuthentication().
 *
 */
public abstract class AppservPasswordLoginModule implements LoginModule
{
    // The _subject, _sharedState and _options satisfy LoginModule and are
    // shared across sub-classes
    protected Subject _subject;
    protected Map _sharedState;
    protected Map _options;
    
    protected String _username;
    protected String _password;
    protected Realm _currentRealm;
    
    // the authentication status
    protected boolean _succeeded = false;
    protected boolean _commitSucceeded = false;
    protected PrincipalImpl _userPrincipal;
    protected String[] _groupsList = null;

    protected Logger _logger =
        LogDomains.getLogger(LogDomains.SECURITY_LOGGER);

    protected final static StringManager sm =
        StringManager.getManager("com.sun.enterprise.security.auth.login");

    
    /**
     * Initialize this login module.
     *
     * @param subject - the Subject to be authenticated.
     * @param callbackHandler - a CallbackHandler for obtaining the subject
     *    username and password.
     * @param sharedState - state shared with other configured LoginModules.
     * @param options - options specified in the login Configuration for
     *    this particular LoginModule.
     *
     */
    final public void initialize(Subject subject, CallbackHandler callbackHandler,
                           Map sharedState, Map options)
    {
        _subject = subject;
        _sharedState = sharedState;
        _options = options;
        if(_logger.isLoggable(Level.FINE)){
            _logger.log(Level.FINE, "Login module initialized: "+
                           this.getClass().toString());
        }
    }


    /**
     * Perform login.
     *
     * <P>The callback handler is used to obtain authentication info
     * for the subject and a login is attempted. This PasswordLoginModule
     * expects to find a PasswordCredential in the private credentials
     * of the Subject. If not present the login fails. The callback
     * handler is ignored as it is not really relevant on the server side.
     * Finally, the authenticateUser() method is invoked.
     *
     * @returns true if login succeeds, otherwise an exception is thrown.
     * @throws LoginException Thrown if login failed, or on other problems.
     *
     */
    final public boolean login() throws LoginException
    {
        if (_subject==null) {
            String msg = sm.getString("pwdlm.noinfo");
            _logger.log(Level.SEVERE, msg);
            throw new LoginException(msg);
        }

        PasswordCredential pwdCred = null;

        try {
            Iterator i = _subject.getPrivateCredentials().iterator();
            while (i.hasNext() && pwdCred==null) {
                Object privCred = i.next();
                if (privCred instanceof PasswordCredential) {
                    pwdCred = (PasswordCredential)privCred;
                }
            }
        } catch (Exception e) {
            _logger.log(Level.WARNING, "passwordlm.nocreds", e.toString());
        }

        if (pwdCred==null) {
            _logger.log(Level.SEVERE, "passwordlm.nopwdcred");
            String msg = sm.getString("pwdlm.nocreds");            
            throw new LoginException(msg);
        }

        // Need to obtain the requested realm to get parameters.

        String realm = null;
        try {
            realm = pwdCred.getRealm();
            _currentRealm = Realm.getInstance(realm);

        } catch (Exception e) {
            String msg = sm.getString("pwdlm.norealm", realm);
            _logger.log(Level.SEVERE, msg);
            throw new LoginException(msg);
        }

        if (_currentRealm == null) {
            String msg = sm.getString("pwdlm.norealmavail", realm);
            _logger.log(Level.SEVERE, msg);
            throw new LoginException(msg);
        }

        // Get username and password data from credential (ignore callback)

        _username = pwdCred.getUser();
        _password = pwdCred.getPassword();

        // Delegate the actual authentication to subclass.

        authenticateUser();
        if(_logger.isLoggable(Level.FINE)){
            _logger.log(Level.FINE, "JAAS login complete.");
        }
        return true;
    }

    
    /**
     * Commit the authentication.
     *
     * <P>Commit is called after all necessary login modules have succeeded.
     * It adds (if not present) a PrincipalImpl principal and a
     * LocalCredentials public credential to the Subject.
     *
     * @throws LoginException If commit fails.
     *
     */
    public boolean commit() throws LoginException
    {
        if (_succeeded == false) {
            return false;
        }

        // Add a Principal (authenticated identity) to the Subject
        // Assume the user we authenticated is the PrincipalImpl [RI]
        String realm_name = _currentRealm.getName();
        _userPrincipal = 
            PrincipalGroupFactory.getPrincipalInstance(_username, realm_name);
        Set principalSet = _subject.getPrincipals();
        if (!principalSet.contains(_userPrincipal)){
            principalSet.add(_userPrincipal);
        }
        /* populate the group in the subject and clean out the slate at the same
         * time
         */
        for(int i = 0; i<_groupsList.length; i++){
            if(_groupsList[i] != null){
                Group g =
                    PrincipalGroupFactory.getGroupInstance(_groupsList[i], realm_name);
                if(!principalSet.contains(g)){
                    principalSet.add(g);
                }
                
                // cleaning the slate
                _groupsList[i] = null;
            }
        }
        
        // In any case, clean out state.
        _groupsList = null;
        _username = null;
        _password = null;
        _commitSucceeded = true;
        if(_logger.isLoggable(Level.FINE)){
            _logger.log(Level.FINE,"JAAS authentication committed.");
        }
        return true;
    }


    /**
     * Abort the authentication process.
     *
     */
    final public boolean abort() throws LoginException
    {
        if(_logger.isLoggable(Level.FINE)){
            _logger.log(Level.FINE,"JAAS authentication aborted.");
        }
        
        if (_succeeded == false) {
            return false;
        } else if (_succeeded == true && _commitSucceeded == false) {
            // login succeeded but overall authentication failed
            _succeeded = false;
            _username = null;
            _password = null;
            _userPrincipal = null;
            for(int i = 0; i < _groupsList.length; i++){
                _groupsList[i] = null;
            }
            _groupsList = null;
        } else {
            // overall authentication succeeded and commit succeeded,
            // but someone else's commit failed
            logout();
        }
        return true;
    }


    /**
     * Log out the subject.
     *
     */
    final public boolean logout() throws LoginException
    {
        if(_logger.isLoggable(Level.FINE)){
            _logger.log(Level.FINE, "JAAS logout for: " + _subject.toString());
        }

        _subject.getPrincipals().clear();
        _subject.getPublicCredentials().clear();
        _subject.getPrivateCredentials().clear();
        
        _succeeded = false;
        _commitSucceeded = false;
        _username = null;
        _password = null;
        _userPrincipal = null;
        if(_groupsList != null){
            for (int i = 0; i < _groupsList.length; i++){
                _groupsList[i] = null;
            }
            _groupsList = null;
        }
        return true;
    }

    
    /**
     *
     * <P>This is a convenience method which can be used by subclasses
     *
     * <P>Note that this method is called after the authentication
     * has succeeded. If authentication failed do not call this method.
     * 
     * Global instance field succeeded is set to true by this method.
     *
     * @param groups String array of group memberships for user (could be
     *     empty). 
     */
    public final void commitUserAuthentication (final String[] groups)
    {
        _groupsList = groups;
        _succeeded = true;
    }

    /**
     * @return the subject being authenticated.
     * use case:
     * A custom login module could overwrite commit() method, and call getSubject()
     * to get subject being authenticated inside its commit(). Custom principal
     * then can be added to subject. By doing this,custom principal will be stored
     * in calling thread's security context and participate in following Appserver's
     * authorization.
     *
     */
    public Subject getSubject()
    {
        return _subject;
    }
    
    /**
     * Perform authentication decision.
     *
     * Method returns silently on success and returns a LoginException
     * on failure.
     * @throws LoginException on authentication failure.
     *
     */
    abstract protected void authenticateUser() throws LoginException;
}
