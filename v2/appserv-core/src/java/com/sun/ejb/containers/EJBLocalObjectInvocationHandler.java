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
package com.sun.ejb.containers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.lang.reflect.Method;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.ejb.EJBContext;
import javax.ejb.EJBException;
import javax.ejb.EJBLocalObject;
import com.sun.ejb.Invocation;
import com.sun.ejb.InvocationInfo;
import com.sun.ejb.Container;
import com.sun.ejb.codegen.WrapperGenerator;
import com.sun.ejb.containers.util.MethodMap;
import com.sun.enterprise.util.LocalStringManagerImpl;
import com.sun.logging.LogDomains;
import com.sun.ejb.spi.io.IndirectlySerializable;

/** 
 * Handler for EJBLocalObject invocations through EJBLocalObject proxy.
 * 
 *
 * @author Kenneth Saks
 */    

public final class EJBLocalObjectInvocationHandler 
    extends EJBLocalObjectImpl implements InvocationHandler {

    private static final Logger logger = LogDomains.getLogger(LogDomains.EJB_LOGGER);

    private static LocalStringManagerImpl localStrings =
        new LocalStringManagerImpl(EJBLocalObjectInvocationHandler.class);
    
    // Our associated proxy object.  Used when a caller needs EJBLocalObject
    // but only has InvocationHandler.
    private Object proxy_;

    // Cache reference to invocation info.  There is one of these per
    // container.  It's populated during container initialization and
    // passed in when the InvocationHandler is created.  This avoids the
    // overhead of building the method info each time a LocalObject proxy
    // is created.  
    private MethodMap invocationInfoMap_;

    private Class localIntf_;

    /**
     * Constructor used for Local Home view
     */
    public EJBLocalObjectInvocationHandler(MethodMap invocationInfoMap,
                                           Class localIntf)
        throws Exception {

        invocationInfoMap_ = invocationInfoMap;
        
        localIntf_ = localIntf;
        setIsLocalHomeView(true);

        // NOTE : Container is not set on super-class until after 
        // constructor is called.
    }

    /**
     * Constructor used for Local Business view.
     */
      
    public EJBLocalObjectInvocationHandler(MethodMap invocationInfoMap)
        throws Exception {

        invocationInfoMap_ = invocationInfoMap;
        
        setIsLocalHomeView(false);

        // NOTE : Container is not set on super-class until after 
        // constructor is called.
    }

    public void setProxy(Object proxy) {
        proxy_ = proxy;
    }

    public Object getClientObject() {
        return proxy_;
    }

    /**
     * This entry point is only used for Local Home view.
     */
    public Object invoke(Object proxy, Method method, Object[] args) 
        throws Throwable {

        return invoke(localIntf_, method, args);
    }

    Object invoke(Class clientInterface, Method method, Object[] args) 
        throws Throwable {

        // NOTE : be careful with "args" parameter.  It is null
        //        if method signature has 0 arguments.
        try {
        container.onEnteringContainer();
        Class methodClass = method.getDeclaringClass();
        if( methodClass == java.lang.Object.class ) {
            return InvocationHandlerUtil.invokeJavaObjectMethod(this, method, args);
        } else if( methodClass == IndirectlySerializable.class ) {
            return this.getSerializableObjectFactory();
        }

        // Use optimized version of get that takes param count as an argument.
        InvocationInfo invInfo = (InvocationInfo)
            invocationInfoMap_.get(method, ((args != null) ? args.length : 0));
            
        if( invInfo == null ) {
            throw new IllegalStateException("Unknown method :" + method);
        }

        if( (methodClass == javax.ejb.EJBLocalObject.class) ||
            invInfo.ejbIntfOverride ) {
            return invokeEJBLocalObjectMethod(method.getName(), args);
        } else if( invInfo.targetMethod1 == null ) {
            Object [] params = new Object[] 
                { invInfo.ejbName, "Local", invInfo.method.toString() };
            String errorMsg = localStrings.getLocalString
                ("ejb.bean_class_method_not_found", "", params);              
            logger.log(Level.SEVERE, "ejb.bean_class_method_not_found",
                       params);                                   
            throw new EJBException(errorMsg);
        }

        // Process application-specific method.

        Object returnValue = null;

        Invocation inv = new Invocation();
        
        inv.isLocal   = true;
        inv.isBusinessInterface = !isLocalHomeView();
        inv.isHome    = false;
        inv.ejbObject = this;
        inv.method    = method;
        inv.methodParams = args;

        inv.clientInterface = clientInterface;

        // Set cached invocation params.  This will save additional lookups
        // in BaseContainer.
        inv.transactionAttribute = invInfo.txAttr;
        inv.securityPermissions = invInfo.securityPermissions;
        inv.invocationInfo = invInfo;
        inv.beanMethod = invInfo.targetMethod1;

        try {
            container.preInvoke(inv);

            returnValue = container.intercept(inv);

        } catch(InvocationTargetException ite) {
            inv.exception = ite.getCause();
            inv.exceptionFromBeanMethod = inv.exception;
        } catch(Throwable t) {
            inv.exception = t;
        } finally {
            container.postInvoke(inv);
        }
        if (inv.exception != null) {
            InvocationHandlerUtil.throwLocalException
                (inv.exception, method.getExceptionTypes());
        }

        return returnValue;
        } finally {
            container.onLeavingContainer();
        }
    }


    private Object invokeEJBLocalObjectMethod(String methodName, Object[] args)
        throws Exception
    {
        // Return value is null if target method returns void.
        Object returnValue = null;

        // Can only be remove, isIdentical, getEJBLocalHome, or getPrimaryKey,
        // so optimize by comparing as few characters as possible.
        switch(methodName.charAt(0)) {
            case 'r' :

                // void remove();

                super.remove();
                break;

            case 'i' :

                // boolean isIdentical(EJBLocalObject)

                // Convert the param into an EJBLocalObjectImpl.  Can't 
                // assume it's an EJBLocalObject for an ejb that was deployed
                // using dynamic proxies.
                EJBLocalObject other = (EJBLocalObject) args[0];
                EJBLocalObjectImpl otherImpl = 
                    EJBLocalObjectImpl.toEJBLocalObjectImpl(other);
                    
                returnValue = new Boolean(super.isIdentical(otherImpl));
                break;

            case 'g' :

                if( methodName.charAt(3) == 'E' ) {
                    // EJBLocalHome getEJBLocalHome();
                    returnValue = super.getEJBLocalHome(); 
                } else {
                    // Object getPrimaryKey();
                    returnValue = super.getPrimaryKey();
                }
                break;

            default :

                throw new EJBException("unknown method = " + methodName);
        }

        return returnValue;
    }

}
