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

package com.sun.appserv.connectors.internal.api;

import org.jvnet.hk2.annotations.Contract;
import org.glassfish.api.invocation.InvocationManager;

import javax.naming.NamingException;
import javax.resource.ResourceException;
import javax.resource.spi.ManagedConnectionFactory;
import javax.security.auth.callback.CallbackHandler;
import java.util.*;

import com.sun.enterprise.config.serverbeans.ResourcePool;
import com.sun.enterprise.config.serverbeans.WorkSecurityMap;
import com.sun.enterprise.transaction.api.JavaEETransactionManager;
import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;
import com.sun.corba.se.spi.orbutil.threadpool.NoSuchThreadPoolException;


/**
 * This class is the entry point to connector backend module.
 * It exposes different API's called by external entities like JPA, admin
 * to perform various connector backend related  operations.
 * It delegates calls to various connetcor admin services and other
 * connector services which actually implement the functionality.
 * This is a delegating class.
 *
 * @author Binod P.G, Srikanth P, Aditya Gore, Jagadish Ramu
 */

@Contract
public interface ConnectorRuntime extends ConnectorConstants{

    /**
     * Creates Active resource Adapter which abstracts the rar module.
     * During the creation of ActiveResourceAdapter, default pools and
     * resources also are created.
     *
     * @param sourcePath  Directory where rar module is exploded.
     * @param moduleName Name of the module
     * @param loader Classloader used to load the .rar
     * @throws ConnectorRuntimeException if creation fails.
     */
    public void createActiveResourceAdapter(String sourcePath, String moduleName, ClassLoader loader)
            throws ConnectorRuntimeException;

    /**
     * Creates Active resource Adapter which abstracts the rar module.
     * During the creation of ActiveResourceAdapter, default pools and
     * resources also are created.
     *
     * @param moduleName Name of the module
     * @throws ConnectorRuntimeException if creation fails.
     */
    public void createActiveResourceAdapterForEmbeddedRar(String moduleName) throws ConnectorRuntimeException;

    /**
     * Destroys/deletes the Active resource adapter object from the
     * connector container. Active resource adapter abstracts the rar
     * deployed.
     *
     * @param moduleName Name of the rarModule to destroy/delete
     * @throws ConnectorRuntimeException if the deletion fails
     */
    public void destroyActiveResourceAdapter(String moduleName) throws ConnectorRuntimeException;

    /**
     * Will be used by resource proxy objects to load the resource lazily.
     *
     * @param resource     Resource's config
     * @param pool         Pool's config
     * @param resourceType type of resource (Connector/Jdbc etc.,)
     * @param resourceName name of the resource
     * @param raName       Resource-adapter name
     * @return status indicating whether the resource is successfully loaded or not
     * @throws ConnectorRuntimeException when unable to load the resource
     */
    public boolean checkAndLoadResource(Object resource, Object pool, String resourceType, String resourceName,
                                        String raName) throws ConnectorRuntimeException;

    /**
     * Shut down all active resource adapters
     */
    public void shutdownAllActiveResourceAdapters();

    /**
     * Given the module directory, creates a connector-class-finder (class-loader) for the module
     * @param moduleDirectory rar module directory for which classloader is needed
     * @param parent parent classloader<br>
     * For standalone rars, pass null, as the parent should be common-class-loader
     * that will be automatically taken care by ConnectorClassLoaderService.<br>
     * For embedded rars, parent is necessary<br>
     * @return classloader created for the module
     */
    public ClassLoader createConnectorClassLoader(String moduleDirectory, ClassLoader parent);

    /**
     * Does lookup of non-tx-datasource. If found, it will be returned.<br><br>
     * <p/>
     * If not found and <b>force</b> is true,  this api will try to get a wrapper datasource specified
     * by the jdbcjndi name. The motivation for having this
     * API is to provide the CMP backend/ JPA-Java2DB a means of acquiring a connection during
     * the codegen phase. If a user is trying to deploy an JPA-Java2DB app on a remote server,
     * without this API, a resource reference has to be present both in the DAS
     * and the server instance. This makes the deployment more complex for the
     * user since a resource needs to be forcibly created in the DAS Too.
     * This API will mitigate this need.
     *
     * @param jndiName jndi name of the resource
     * @param force    provide the resource (in DAS)  even if it is not enabled in DAS
     * @return DataSource representing the resource.
     * @throws javax.naming.NamingException when not able to get the datasource.
     */
    public Object lookupNonTxResource(String jndiName, boolean force) throws NamingException;

    /**
     * Does lookup of "__pm" datasource. If found, it will be returned.<br><br>
     * <p/>
     * If not found and <b>force</b> is true, this api will try to get a wrapper datasource specified
     * by the jdbcjndi name. The motivation for having this
     * API is to provide the CMP backend/ JPA-Java2DB a means of acquiring a connection during
     * the codegen phase. If a user is trying to deploy an JPA-Java2DB app on a remote server,
     * without this API, a resource reference has to be present both in the DAS
     * and the server instance. This makes the deployment more complex for the
     * user since a resource needs to be forcibly created in the DAS Too.
     * This API will mitigate this need.
     * When the resource is not enabled, datasource wrapper provided will not be of
     * type "__pm"
     *
     * @param jndiName jndi name of the resource
     * @param force    provide the resource (in DAS)  even if it is not enabled in DAS
     * @return DataSource representing the resource.
     * @throws javax.naming.NamingException when not able to get the datasource.
     */
    public Object lookupPMResource(String jndiName, boolean force) throws NamingException;

    /**
     * Provide the configuration of the pool
     * @param poolName connection pool name
     * @return ResourcePool connection pool configuration
     */
    public ResourcePool getConnectionPoolConfig(String poolName);

    /**
     * Tests whether the configuration for the pool is valid by making a connection.
     * @param poolName connection pool name
     * @return boolean indicating ping status
     * @throws ResourceException when unable to ping
     */
    public boolean pingConnectionPool(String poolName) throws ResourceException;

    /**
      * Gets the properties of the Java bean connection definition class that
      * have setter methods defined and the default values as provided by the
      * Connection Definition java bean developer.
      * This method is used to get properties of jdbc-data-source<br>
      * To get Connection definition properties for Connector Connection Pool,
      * use ConnectorRuntime.getMCFConfigProperties()<br>
      * When the connection definition class is not found, standard JDBC
      * properties (of JDBC 3.0 Specification) will be returned.<br>
      *
      * @param connectionDefinitionClassName
      *                     The Connection Definition Java bean class for which
      *                     overrideable properties are required.
      * @return Map<String, Object> String represents property name
      * and Object is the defaultValue that is a primitive type or String
      */
    public Map<String, Object> getConnectionDefinitionPropertiesAndDefaults(String connectionDefinitionClassName);

    /**
     * Provides specified ThreadPool or default ThreadPool from server
     * @param threadPoolId Thread-pool-id
     * @return ThreadPool
     * @throws NoSuchThreadPoolException when unable to get a ThreadPool
     */
    public ThreadPool getThreadPool(String threadPoolId) throws NoSuchThreadPoolException, ConnectorRuntimeException;


    /**
     * provides the transactionManager
     *
     * @return TransactionManager
     */
    public JavaEETransactionManager getTransactionManager();

    /**
     * provides the invocationManager
     *
     * @return InvocationManager
     */
    public InvocationManager getInvocationManager();

    /**
     * get resource reference descriptors from current component's jndi environment
     * @return set of resource-refs
     */
    public Set getResourceReferenceDescriptor();


    /**
     * provide the MCF of the pool (either retrieve or create)
     * @param poolName connection pool name
     * @return ManagedConnectionFactory mcf of the pool
     * @throws ConnectorRuntimeException when unable to provide the MCF
     */
    public ManagedConnectionFactory obtainManagedConnectionFactory(String poolName)
            throws ConnectorRuntimeException;

    /**
     * Indicates whether the execution environment is server or client
     * @return ConnectorConstants.SERVER or ConnectorConstants.CLIENT
     */
    public int getEnvironment();

    /**
     * provides container's (application server's) callback handler
     * @return container callback handler
     */
    public CallbackHandler getCallbackHandler();

    /**
     * Checks whether the executing environment is application server
     * @return true if execution environment is server
     *         false if it is client
     */
    boolean isServer();

    /**
     * Initializes the execution environment. If the execution environment
     * is appserv runtime it is set to ConnectorConstants.SERVER else
     * it is set ConnectorConstants.CLIENT
     *
     * @param environment set to ConnectorConstants.SERVER if execution
     *                    environment is appserv runtime else set to
     *                    ConnectorConstants.CLIENT
     */
/*
    void initialize(int environment);
*/

    /**
     * provides connector class loader
     * @return ClassLoader
     */
    ClassLoader getConnectorClassLoader();

    /** Obtains all the Connection definition names of a rar
     *  @param rarName rar moduleName
     *  @return Array of connection definition names.
     *  @throws ConnectorRuntimeException when unable to obtain connection definition from descriptor.
     */
    public String[] getConnectionDefinitionNames(String rarName)
               throws ConnectorRuntimeException ;

    /**
     *  Obtains the Permission string that needs to be added to the
     *  to the security policy files. These are the security permissions needed
     *  by the resource adapter implementation classes.
     *  These strings are obtained by parsing the ra.xml and by processing annotations if any
     *  @param moduleName rar module Name
     *  @throws ConnectorRuntimeException If rar.xml parsing or annotation processing fails.
     *  @return security permission spec
     */
    public String getSecurityPermissionSpec(String moduleName)
                         throws ConnectorRuntimeException ;

    /**
     * Obtains all the Admin object interface names of a rar
     * @param rarName rar moduleName
     * @return Array of admin object interface names.
     * @throws ConnectorRuntimeException when unable to obtain admin object interface names
     */
    public String[] getAdminObjectInterfaceNames(String rarName)
               throws ConnectorRuntimeException ;

    /**
     *  Retrieves the Resource adapter javabean properties with default values.
     *  The default values will the values present in the ra.xml. If the
     *  value is not present in ra.xxml, javabean is introspected to obtain
     *  the default value present, if any. If intrspection fails or null is the
     *  default value, empty string is returned.
     *  If ra.xml has only the property and no value, empty string is the value
     *  returned.
     *  If the Resource Adapter Java bean is annotated, properties will be the result of merging
     *  annotated config property and config-property of Resource Adapter bean in ra.xml 
     *  @param rarName rar module name
     *  @return Resource adapter javabean properties with default values.
     *  @throws ConnectorRuntimeException if property retrieval fails.
     */
    public Properties getResourceAdapterConfigProps(String rarName)
                throws ConnectorRuntimeException ;

    /**
     *  Retrieves the MCF javabean properties with default values.
     *  The default values will the values present in the ra.xml. If the
     *  value is not present in ra.xxml, javabean is introspected to obtain
     *  the default value present, if any. If intrspection fails or null is the
     *  default value, empty string is returned.
     *  If ra.xml has only the property and no value, empty string is the value
     *  returned.
     *  If the ManagedConnectionFactory Java bean is annotated, properties will be the result of merging
     *  annotated config property and config-property of MCF in ra.xml

     *  @param rarName rar module name
     *  @param connectionDefName connection-definition-name
     *  @return managed connection factory javabean properties with
     *          default values.
     *  @throws ConnectorRuntimeException if property retrieval fails.
     */
    public Properties getMCFConfigProps(
     String rarName,String connectionDefName) throws ConnectorRuntimeException ;

    /**
     *  Retrieves the admin object javabean properties with default values.
     *  The default values will the values present in the ra.xml. If the
     *  value is not present in ra.xxml, javabean is introspected to obtain
     *  the default value present, if any. If intrspection fails or null is the
     *  default value, empty string is returned.
     *  If ra.xml has only the property and no value, empty string is the value
     *  returned.
     *  If the AdministeredObject Java bean is annotated, properties will be the result of merging
     *  annotated config property and config-property of AdministeredObject in ra.xml

     *  @param rarName rar module name
     *  @param adminObjectIntf admin-object-interface name
     * @return admin object javabean properties with
     *          default values.
     *  @throws ConnectorRuntimeException if property retrieval fails.
     */
    public Properties getAdminObjectConfigProps(
      String rarName,String adminObjectIntf) throws ConnectorRuntimeException ;


    /**
     *  Retrieves the XXX javabean properties with default values.
     *  The javabean to introspect/retrieve is specified by the type.
     *  The default values will be the values present in the ra.xml. If the
     *  value is not present in ra.xxml, javabean is introspected to obtain
     *  the default value present, if any. If intrspection fails or null is the
     *  default value, empty string is returned.
     *  If ra.xml has only the property and no value, empty string is the value
     *  returned.
     *  @param rarName rar module name
     *  @param connectionDefName connection definition name
     *  @param type JavaBean type to introspect
     * @return admin object javabean properties with
     *          default values.
     *  @throws ConnectorRuntimeException if property retrieval fails.
     */
    public Properties getConnectorConfigJavaBeans(String rarName,
        String connectionDefName,String type) throws ConnectorRuntimeException ;

    /**
     * Return the ActivationSpecClass name for given rar and messageListenerType
     * @param rarName name of the rar module
     * @param messageListenerType MessageListener type
     * @throws  ConnectorRuntimeException If moduleDir is null.
     *          If corresponding rar is not deployed.
     * @return activation-spec class
     */
    public String getActivationSpecClass( String rarName,
             String messageListenerType) throws ConnectorRuntimeException ;


    /**
     *  Parses the ra.xml, processes the annotated rar artificats if any
     *  and returns all the Message listener types.
     *
     * @param  rarName name of the rar module.
     * @return Array of message listener types as strings.
     * @throws  ConnectorRuntimeException If moduleDir is null.
     *          If corresponding rar is not deployed.
     *
     */
    public String[] getMessageListenerTypes(String rarName)
               throws ConnectorRuntimeException  ;

    /** Parses the ra.xml for the ActivationSpec javabean
     *  properties and processes annotations if any. The ActivationSpec to be parsed is
     *  identified by the moduleDir where ra.xml is present and the
     *  message listener type.
     *
     *  message listener type will be unique in a given ra.xml.
     *
     *  It throws ConnectorRuntimeException if either or both the
     *  parameters are null, if corresponding rar is not deployed,
     *  if message listener type mentioned as parameter is not found in ra.xml.
     *  If rar is deployed and message listener (type mentioned) is present
     *  but no properties are present for the corresponding message listener,
     *  null is returned.
     *
     *  @param  rarName name of the rar module.
     *  @param  messageListenerType message listener type.It is uniqie
     *          across all <messagelistener> sub-elements in <messageadapter>
     *          element in a given rar.
     *  @return Javabean properties with the property names and values
     *          of properties. The property values will be the values
     *          mentioned in ra.xml if present. Otherwise it will be the
     *          default values obtained by introspecting the javabean.
     *          In both the case if no value is present, empty String is
     *          returned as the value.
     *  @throws  ConnectorRuntimeException if either of the parameters are null.
     *           If corresponding rar is not deployed i.e moduleDir is invalid.
     *           If messagelistener type is not found in ra.xml or could not be
     *           found in annotations if any
     */
    public Properties getMessageListenerConfigProps(String rarName,
         String messageListenerType)throws ConnectorRuntimeException ;

    /** Returns the Properties object consisting of propertyname as the
     *  key and datatype as the value.
     *  @param  rarName name of the rar module.
     *  @param  messageListenerType message listener type.It is uniqie
     *          across all <messagelistener> sub-elements in <messageadapter>
     *          element in a given rar.
     *  @return Properties object with the property names(key) and datatype
     *          of property(as value).
     *  @throws  ConnectorRuntimeException if either of the parameters are null.
     *           If corresponding rar is not deployed i.e moduleDir is invalid.
     *           If messagelistener type is not found in ra.xmlor could not be found
     *           in annotations if any
     */
    public Properties getMessageListenerConfigPropTypes(String rarName,
               String messageListenerType) throws ConnectorRuntimeException ;

    /**
     * get work security maps for a resource-adapter-name
     * @param raName resource-adapter name
     * @return all work security maps of a resource-adapter
     */
    public List<WorkSecurityMap> getWorkSecurityMap(String raName);

}
