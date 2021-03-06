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
/*
 *  Copyright 1999-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.sun.enterprise.web.connector.grizzly;

import com.sun.enterprise.web.connector.grizzly.blocking.SelectorBlockingThread;
import com.sun.enterprise.web.connector.grizzly.ssl.SSLSelectorThread;
import com.sun.enterprise.web.portunif.PortUnificationPipeline;
import com.sun.enterprise.web.portunif.ProtocolFinder;
import com.sun.enterprise.web.portunif.ProtocolHandler;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.ObjectName;

import com.sun.org.apache.commons.modeler.Registry;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.coyote.http11.Http11Protocol;
import org.apache.tomcat.util.net.SSLImplementation;
import org.apache.tomcat.util.net.ServerSocketFactory;

/**
 * Abstract the protocol implementation, including threading, etc.
 * Processor is single threaded and specific to stream-based protocols.
 * This is an adaptation of org.apache.coyote.http11.Http11Processor except here
 * NIO is used.
 * 
 * @author Jean-Francois Arcand
 */
public class GrizzlyHttpProtocol extends Http11Protocol {    
    
    private int socketBuffer = 9000;
    
    /**
     * The <code>SelectorThread</code> used by this object.
     */
    private SelectorThread selectorThread;
    
    
    /**
     * The JMX management class.
     */
    private Management jmxManagement = null;

    // ------------------------------------------------------ Compression ---/
    
    
    /**
     * Compression value.
     */
    private String compression = "off";
    private String noCompressionUserAgents = null;
    private String restrictedUserAgents = null;
    private String compressableMimeTypes = "text/html,text/xml,text/plain";
    private int compressionMinSize    = 2048;
    
    
    /**
     * List of proxied protocol supported by the listener.
     */
    private String proxiedProtocols;
    
    
    /**
     * The default proxied protocol.
     */
    private final static ConcurrentLinkedQueue<String> supportedHandlers 
            = new ConcurrentLinkedQueue<String>();
    
    
    /**
     * The default proxied protocol.
     */
    private final static ConcurrentHashMap<String,String> supportedProtocols 
            = new ConcurrentHashMap<String,String>();  
    
    
    private final static String TLS = "tls";    
    private final static String HTTP = "http";
    
    static{
        supportedProtocols.put(
                HTTP,com.sun.enterprise.web.portunif.
                HttpProtocolFinder.class.getName());
        supportedProtocols.put(
                TLS,com.sun.enterprise.web.portunif.
                TlsProtocolFinder.class.getName());
        supportedProtocols.put(
                "ws/tcp","com.sun.xml.ws.transport.tcp.grizzly.WSTCPProtocolFinder");      
        supportedHandlers.add(com.sun.enterprise.web.portunif.
                HttpProtocolHandler.class.getName());
        supportedHandlers.add("com.sun.xml.ws.transport.tcp.grizzly.WSTCPProtocolHandler");        
    }


    // ------------------------------------------------------- Constructor --//
    
    public GrizzlyHttpProtocol(boolean secure, boolean blocking, 
                               String selectorThreadImpl) {
        super(secure,blocking,selectorThreadImpl);     
    }   
    
    
    /**
     * This method is called by the constructor of the Http11Protocol
     * superclass.
     */
    protected void create() {         
        if (blocking){
            selectorThread = new SelectorBlockingThread();   
            ((SelectorBlockingThread)selectorThread).setSecure(secure);
        } else if ( !secure ){
            if (selectorThreadImpl != null){
                try{               
                    Class clazz = Class.forName(selectorThreadImpl);
                    selectorThread = (SelectorThread)clazz.newInstance();
                } catch (Throwable t){
                    selectorThread.logger().log(Level.WARNING,
                      "Unable to load SelectorThread: " + selectorThreadImpl,t);
                }
            } 
            
            if ( selectorThread == null ){
                selectorThread = new SelectorThread(); 
            }            
        } else { 
            selectorThread = new SSLSelectorThread();
        }
        setSoLinger(-1);
        setSoTimeout(Constants.DEFAULT_TIMEOUT * 1000);
        setServerSoTimeout(Constants.DEFAULT_SERVER_SOCKET_TIMEOUT);
        setTcpNoDelay(Constants.DEFAULT_TCP_NO_DELAY);
    }

    
    // ---------------------------------------------------- Public methods --//
    /** 
     * Start the protocol
     */
    public void init() throws Exception {
        try {
            checkSocketFactory();
        } catch( Exception ex ) {
            SelectorThread.logger().log(Level.SEVERE,
                       "grizzlyHttpProtocol.socketfactory.initerror",ex);
            throw ex;
        }

        if( socketFactory!=null ) {
            Enumeration attE=attributes.keys();
            while( attE.hasMoreElements() ) {
                String key=(String)attE.nextElement();
                Object v=attributes.get( key );
                socketFactory.setAttribute( key, v );
            }
        }

        if ( secure && !blocking){
            socketFactory.init();
            ((SSLSelectorThread)selectorThread)
                    .setSSLContext(socketFactory.getSSLContext());
        }
        
        try {
             selectorThread.setAdapter(adapter);
             if (proxiedProtocols != null|| 
                  System.getProperty(
                     PortUnificationPipeline.PROTOCOL_HANDLERS) != null){
                 
                 selectorThread.pipelineClassName = com.sun.enterprise.web.
                         portunif.PortUnificationPipeline.class.getName();
             }
             selectorThread.initEndpoint();
             if (proxiedProtocols != null && !blocking){
                configureProxiedProtocols();
             }
        } catch (Exception ex) {
            SelectorThread.logger().log(Level.SEVERE, 
                       "grizzlyHttpProtocol.endpoint.initerror", ex);
            throw ex;
        }
    }
    
    
    public void start() throws Exception {
        try {            
            if ( this.oname != null ) {
                jmxManagement = new ModelerManagement();
                selectorThread.setManagement(jmxManagement);
                
                try {
                    ObjectName sname = new ObjectName(domain
                                   +  ":type=Selector,name=http"
                                   + selectorThread.getPort());                   
                    jmxManagement.registerComponent(selectorThread, sname, null);
                } catch (Exception ex) {
                    SelectorThread.logger().log(Level.SEVERE,
                      "grizzlyHttpProtocol.selectorRegistrationFailed", ex);
                }
            } else {
                SelectorThread.logger().log(Level.INFO,
                           "grizzlyHttpProtocol.selectorRegisterProtocol");
            }

            selectorThread.start();
        } catch (Exception ex) {
            SelectorThread.logger().log(Level.SEVERE, 
                       "grizzlyHttpProtocol.endpoint.starterror", 
                       ex);
            throw ex;
        }
        
        SelectorThread.logger().log(Level.INFO, 
                   "grizzlyHttpProtocol.start", String.valueOf(getPort()));
    }


    public void destroy() throws Exception {
        SelectorThread.logger().log(Level.INFO, 
                   "grizzlyHttpProtocol.stop", 
                                String.valueOf(getPort()));
        
        if ( domain != null ){
           jmxManagement.
                    unregisterComponent(new ObjectName(domain,"type", "Selector"));
        }
        selectorThread.stopEndpoint();
    }
    
    
    // -------------------- Pool setup --------------------


    public int getMaxThreads() {
        return selectorThread.getMaxThreads();
    }
    
    public void setMaxThreads( int maxThreads ) {
        selectorThread.setMaxThreads(maxThreads);
        setAttribute("maxThreads", "" + maxThreads);
    }
    
    
    public void setMaxPostSize(int maxPostSize) {
        selectorThread.setMaxPostSize(maxPostSize);
        setAttribute("maxPostSize", maxPostSize);
    }    

    
    public int getProcessorThreadsIncrement() {
        return selectorThread.getThreadsIncrement();
    }

    
    public void setProcessorThreadsIncrement( int threadsIncrement ) {
        selectorThread.setThreadsIncrement(threadsIncrement);
        setAttribute("threadsIncrement", "" + threadsIncrement);  
    }  
    
    
    public void setProcessorWorkerThreadsTimeout(int timeout){
        selectorThread.setThreadsTimeout(timeout);
        setAttribute("threadsTimeout", "" + timeout);     
    }
    
    
    public int getProcessorWorkerThreadsTimeout(){
        return selectorThread.getThreadsTimeout();
    }
    // -------------------- Tcp setup --------------------

    public int getBacklog() {
        return selectorThread.getSsBackLog();
    }
    
    public void setBacklog( int i ) {
        ;
    }
    
    public int getPort() {
        return selectorThread.getPort();
    }
    
    public void setPort( int port ) {
        selectorThread.setPort(port);
        setAttribute("port", "" + port);
        //this.port=port;
    }

    public InetAddress getAddress() {
        return selectorThread.getAddress();
    }
    
    public void setAddress(InetAddress ia) {
        selectorThread.setAddress( ia );
        setAttribute("address", "" + ia);
    }
    
    public String getName() {
        String encodedAddr = "";
        if (getAddress() != null) {
            encodedAddr = "" + getAddress();
            if (encodedAddr.startsWith("/"))
                encodedAddr = encodedAddr.substring(1);
            encodedAddr = URLEncoder.encode(encodedAddr) + "-";
        }
        return ("http-" + encodedAddr + selectorThread.getPort());
    }
    
    public boolean getTcpNoDelay() {
        return selectorThread.getTcpNoDelay();
    }
    
    public void setTcpNoDelay( boolean b ) {
        selectorThread.setTcpNoDelay( b );
        setAttribute("tcpNoDelay", "" + b);
    }

    public boolean getDisableUploadTimeout() {
        return disableUploadTimeout;
    }
    
    public void setDisableUploadTimeout(boolean isDisabled) {
        disableUploadTimeout = isDisabled;
        selectorThread.setDisableUploadTimeout(disableUploadTimeout);
    }

    public int getSocketBuffer() {
        return socketBuffer;
    }
    
    public void setSocketBuffer(int valueI) {
        socketBuffer = valueI;
    }

    public void setMaxHttpHeaderSize(int maxHttpHeaderSize) {
        super.setMaxHttpHeaderSize(maxHttpHeaderSize);
        selectorThread.setMaxHttpHeaderSize(maxHttpHeaderSize);
    }
   
    public String getCompression() {
        return compression;
    }

    public void setCompression(String valueS) {
        compression = valueS;
        selectorThread.setCompression(compression);
    }
    
    public String getRestrictedUserAgents() {
        return restrictedUserAgents;
    }
    
    public void setRestrictedUserAgents(String valueS) {
        restrictedUserAgents = valueS;
        selectorThread.setRestrictedUserAgents(valueS);
    }

    public String getNoCompressionUserAgents() {
        return noCompressionUserAgents;
    }
    
    public void setNoCompressionUserAgents(String valueS) {
        noCompressionUserAgents = valueS;
        selectorThread.setNoCompressionUserAgents(valueS);
    }

    public String getCompressableMimeType() {
        return compressableMimeTypes;
    }
    
    public void setCompressableMimeType(String valueS) {
        compressableMimeTypes = valueS;
        selectorThread.setCompressableMimeTypes(valueS);
    }

    public int getCompressionMinSize() {
        return compressionMinSize;
    }
    
    public void setCompressionMinSize(int valueI) {
        compressionMinSize = valueI;
        selectorThread.setCompressionMinSize(valueI);
    }

    public int getSoLinger() {
        return selectorThread.getSoLinger();
    }
    
    public void setSoLinger( int i ) {
        selectorThread.setSoLinger( i );
        setAttribute("soLinger", "" + i);
    }

    public int getSoTimeout() {
        return selectorThread.getSoTimeout();
    }
    
    public void setSoTimeout( int i ) {
        selectorThread.setSoTimeout(i);
        setAttribute("soTimeout", "" + i);
    }
    
    public int getServerSoTimeout() {
        return selectorThread.getServerSoTimeout();
    }
    
    public void setServerSoTimeout( int i ) {
        selectorThread.setServerSoTimeout(i);
        setAttribute("serverSoTimeout", "" + i);
    }
    
    public void setSecure( boolean b ) {
        super.setSecure(b);
    }

    /** 
     * Set the maximum number of Keep-Alive requests that we will honor.
     */
    public void setMaxKeepAliveRequests(int mkar) {
        selectorThread.setMaxKeepAliveRequests(mkar);
    }   
   

    /** 
     * Sets the number of seconds before a keep-alive connection that has
     * been idle times out and is closed.
     *
     * @param timeout Keep-alive timeout in number of seconds
     */    
    public void setKeepAliveTimeoutInSeconds(int timeout) {
        selectorThread.setKeepAliveTimeoutInSeconds(timeout);
    }


    /** 
     * Sets the number of keep-alive threads.
     *
     * @param threadCount Number of keep-alive threads
     */    
    public void setKeepAliveThreadCount(int threadCount) {
        selectorThread.setKeepAliveThreadCount(threadCount);
    }


    /**
     * The minimun threads created at startup.
     */ 
    public void setMinThreads(int minThreads){
        selectorThread.setMinThreads(minThreads);
    }
    

    /**
     * Set the request input buffer size
     */
    public void setBufferSize(int requestBufferSize){
        super.setBufferSize(requestBufferSize);
        selectorThread.setBufferSize(requestBufferSize);
    }
    
    
    /**
     * Set the <code>Selector</code> times out value.
     */ 
    public void setSelectorTimeout(int selectorTimeout){
        selectorThread.setSelectorTimeout(selectorTimeout);
    }
    
    
    /**
     * Return the <code>Selector</code> times out value.
     */
    public int getSelectorTimeout(){
        return selectorThread.getSelectorTimeout();
    }
    
    
    /**
     * Set the upload timeout.
     */
    public void setTimeout(int timeout) {
        selectorThread.setUploadTimeout(timeout);
    }

    
    /**
     * Get the upload timeout.
     */
    public int getTimeout() {
        return selectorThread.getTimeout();
    }
    
    
    /**
     * Set the <code>reader-thread</code> from domain.xml.
     */
    public void setMaxReadWorkerThreads(int maxReadWorkerThreads){
        selectorThread.setMaxReadWorkerThreads(maxReadWorkerThreads);
    }
    
    
    /**
     * Return the <code>read-thread</code> used by this <code>Selector</code>
     */   
    public int getMaxReadWorkerThreads(){
        return selectorThread.getMaxReadWorkerThreads();
    }  

    
    public void setDisplayConfiguration(boolean displayConfiguration){
        selectorThread.setDisplayConfiguration(displayConfiguration);
    }
        
    
    public boolean getDisplayConfiguration(){
        return selectorThread.isDisplayConfiguration();
    }

    
    /**
     * Set the <code>recycle-tasks</code> by this <code>Selector</code>
     */
    public void setRecycleTasks(boolean recycleTasks){
        selectorThread.setRecycleTasks(recycleTasks);
    }
    
    
    /**
     * Return the <code>recycle-tasks</code> used by this 
     * <code>Selector</code>
     */     
    public boolean getRecycleTasks(){
        return selectorThread.isRecycleTasks();
    }    
    
     
    public void setUseByteBufferView(boolean useByteBufferView){
        selectorThread.setUseByteBufferView(useByteBufferView);
    }
    
            
    public boolean getUseByteBufferView(){
        return selectorThread.isUseByteBufferView() ;
    }

    
    /**
     * Set the <code>processor-thread</code> from domain.xml
     */   
    public void setMaxProcessorWorkerThreads(int maxProcessorWorkerThreads){
        selectorThread.setMaxProcessorWorkerThreads(maxProcessorWorkerThreads);
    }
    
    
    /**
     * Return the <code>processor-thread</code> used by this <code>Selector</code>
     */   
    public int getMaxProcessorWorkerThreads(){
        return selectorThread.getMaxProcessorWorkerThreads();
    }
 
   
    /**
     * Set the <code>reader-queue-length</code> value 
     * on this <code>Selector</code>
     */
    public void setMinReadQueueLength(int minReadQueueLength){
        selectorThread.setMinReadQueueLength(minReadQueueLength);
    }

    
    /**
     * Return the <code>reader-queue-length</code> value
     * on this <code>Selector</code>
     */
    public int getMinReadQueueLength(){
        return selectorThread.getMinReadQueueLength();
    }
 
  
    /**
     * Set the <code>processor-queue-length</code> value 
     * on this <code>Selector</code>
     */
    public void setMinProcessorQueueLength(int minProcessorQueueLength){
        selectorThread.setMinProcessorQueueLength(minProcessorQueueLength);
    }
 
     
    /**
     * Return the <code>processor-queue-length</code> value
     * on this <code>Selector</code>
     */   
    public int getMinProcessorQueueLength(){
        return selectorThread.getMinProcessorQueueLength();
    }
    
    
    /**
     * Set the <code>use-nio-non-blocking</code> by this <code>Selector</code>
     */    
    public void setUseDirectByteBuffer(boolean useDirectByteBuffer){
        selectorThread.setUseDirectByteBuffer(useDirectByteBuffer);
    }
    
    
    /**
     * Return the <code>use-nio-non-blocking</code> used by this 
     * <code>Selector</code>
     */     
    public boolean getUseDirectByteBuffer(){
        return selectorThread.isUseDirectByteBuffer();
    }
    
    
    /**
     * Set the maximum pending connection this <code>Pipeline</code>
     * can handle.
     */
    public void setQueueSizeInBytes(int maxQueueSizeInBytes){
        selectorThread.setMaxQueueSizeInBytes(maxQueueSizeInBytes);
    }
    
    
    /**
     * Set the <code>SocketServer</code> backlog.
     */
    public void setSocketServerBacklog(int ssBackLog){
        selectorThread.setSsBackLog(ssBackLog);
    }
    
    /**
     * Set the number of <code>SelectorThread</code> Grizzly will uses.
     */
    public void setSelectorReadThreadsCount(int selectorReadThreadsCount){
        selectorThread.setSelectorReadThreadsCount(selectorReadThreadsCount);
    }

 
    /**
     * Set the default response type used. Specified as a semi-colon
     * delimited string consisting of content-type, encoding,
     * language, charset
     */
    public void setDefaultResponseType(String defaultResponseType){
         selectorThread.setDefaultResponseType(defaultResponseType);
    }


    /**
     * Return the default response type used
     */
    public String getDefaultResponseType(){
         return  selectorThread.getDefaultResponseType();
    }
    
    
    /**
     * Sets the forced request type, which is forced onto requests that
     * do not already specify any MIME type.
     */
    public void setForcedRequestType(String forcedResponseType){
        selectorThread.setForcedRequestType(forcedResponseType);
    }  
    
        
    /**
     * Return the default request type used
     */
    public String getForcedRequestType(){
        return  selectorThread.getForcedRequestType();
    }   
    
    
    //------------------------------------------------- FileCache config -----/

   
    /**
     * The timeout in seconds before remove a <code>FileCacheEntry</code>
     * from the <code>fileCache</code>
     */
    public void setSecondsMaxAge(int sMaxAges){
        selectorThread.secondsMaxAge = sMaxAges;
    }
    
    
    /**
     * Set the maximum entries this cache can contains.
     */
    public void setMaxCacheEntries(int mEntries){
        selectorThread.maxCacheEntries = mEntries;
    }

    
    /**
     * Return the maximum entries this cache can contains.
     */    
    public int getMaxCacheEntries(){
        return selectorThread.maxCacheEntries;
    }
    
    
    /**
     * Set the maximum size a <code>FileCacheEntry</code> can have.
     */
    public void setMinEntrySize(long mSize){
        selectorThread.minEntrySize = mSize;
    }
    
    
    /**
     * Get the maximum size a <code>FileCacheEntry</code> can have.
     */
    public long getMinEntrySize(){
        return selectorThread.minEntrySize;
    }
     
    
    /**
     * Set the maximum size a <code>FileCacheEntry</code> can have.
     */
    public void setMaxEntrySize(long mEntrySize){
        selectorThread.maxEntrySize = mEntrySize;
    }
    
    
    /**
     * Get the maximum size a <code>FileCacheEntry</code> can have.
     */
    public long getMaxEntrySize(){
        return selectorThread.maxEntrySize;
    }
    
    
    /**
     * Set the maximum cache size
     */ 
    public void setMaxLargeCacheSize(long mCacheSize){
        selectorThread.maxLargeFileCacheSize = mCacheSize;
    }

    
    /**
     * Get the maximum cache size
     */ 
    public long getMaxLargeCacheSize(){
        return selectorThread.maxLargeFileCacheSize;
    }
    
    
    /**
     * Set the maximum cache size
     */ 
    public void setMaxSmallCacheSize(long mCacheSize){
        selectorThread.maxSmallFileCacheSize = mCacheSize;
    }
    
    
    /**
     * Get the maximum cache size
     */ 
    public long getMaxSmallCacheSize(){
        return selectorThread.maxSmallFileCacheSize;
    }    

    
    /**
     * Is the fileCache enabled.
     */
    public boolean isFileCacheEnabled(){
        return selectorThread.isFileCacheEnabled;
    }

    
    /**
     * Is the file caching mechanism enabled.
     */
    public void setFileCacheEnabled(boolean isFileCacheEnabled){
        selectorThread.isFileCacheEnabled = isFileCacheEnabled;
    }
   
    
    /**
     * Is the large file cache support enabled.
     */
    public void setLargeFileCacheEnabled(boolean isLargeEnabled){
        selectorThread.isLargeFileCacheEnabled = isLargeEnabled;
    }
   
    
    /**
     * Is the large file cache support enabled.
     */
    public boolean getLargeFileCacheEnabled(){
        return selectorThread.isLargeFileCacheEnabled;
    }    
   
    
    /**
     * Set the documenr root folder
     */
    public void setWebAppRootPath(String rootFolder){
        selectorThread.setRootFolder(rootFolder);
    }
    
    
    /**
     * Return the folder's root where application are deployed.
     */
    public String getWebAppRootPath(){
        return selectorThread.getRootFolder();
    }     
    
    
    /**
     * Set the logger
     */
    public static void setLogger(Logger logger){
        if ( logger != null )
            SelectorThread.setLogger(logger);
    }
    
    
    /**
     * Return the logger used by the Grizzly classes.
     */
    public static Logger getLogger(){
        return SelectorThread.getLogger();
    }  
    
    
    /**
     * Return the instance of SelectorThread used by this class.
     */
    public SelectorThread selectorThread(){
        return selectorThread;
    }


    public void setCometSupport(boolean cometSupport){
        selectorThread.enableCometSupport(cometSupport);
    }
    
    
    public void setRcmSupport(boolean rcmSupport){
        selectorThread.enableRcmSupport(rcmSupport);
    }
    
    
    // --------------------------------------------------------- Private method

    /** Sanity check and socketFactory setup.
     *  IMHO it is better to stop the show on a broken connector,
     *  then leave Tomcat running and broken.
     *  @exception TomcatException Unable to resolve classes
     */
    private void checkSocketFactory() throws Exception {
        if ( !blocking && !secure) return;
        
        SecureSelector secureSel = (SecureSelector)selectorThread;
        if (secure) {
            // The SSL setup code has been moved into
            // SSLImplementation since SocketFactory doesn't
            // provide a wide enough interface
            sslImplementation =
                SSLImplementation.getInstance(sslImplementationName);
            
            socketFactory = sslImplementation.getServerSocketFactory();
            
            secureSel.setSSLImplementation(sslImplementation);
            secureSel.setEnabledCipherSuites(toStringArray(getCiphers()));
            secureSel.setEnabledProtocols(toStringArray(getProtocols()));
            String clientAuthStr = (String) getAttribute("clientauth");
            if (clientAuthStr != null){
                secureSel.setNeedClientAuth(
                        Boolean.valueOf(clientAuthStr).booleanValue());
            }            
        } else if (socketFactoryName != null) {
            socketFactory = string2SocketFactory(socketFactoryName);
        }
        
        if(socketFactory==null)
            socketFactory=ServerSocketFactory.getDefault();

        secureSel.setServerSocketFactory(socketFactory);
    }
    
    
    private static final String[] toStringArray(String list){
        
        if ( list == null ) return null;
        
        StringTokenizer st = new StringTokenizer(list,",");
        String[] array = new String[st.countTokens()];
        int i = 0;
        while(st.hasMoreTokens()){
            array[i++] = st.nextToken();
        }
        return array;
    }
       
    
    /**
     * JMX Wrapper around a JMX implementation.
     */
    class ModelerManagement implements Management{

        public void registerComponent(Object bean, ObjectName oname, String type) 
                throws Exception{
            Registry.getRegistry().registerComponent(bean,oname,type);
        }

        public void unregisterComponent(ObjectName oname) throws Exception{
            Registry.getRegistry().
                    unregisterComponent(oname);
        }     
    }
 

    public void setReuseAddress(boolean reuseAddress){
        selectorThread.setReuseAddress(reuseAddress);
    }

    public boolean getReuseAddress(){
        return selectorThread.getReuseAddress();
    }

    public String getProxiedProtocols() {
        return proxiedProtocols;
    }

    
    public void setProxiedProtocols(String proxiedProtocols) {
        this.proxiedProtocols = proxiedProtocols;
    }

    
    /**
     * Configure the domain.xml proxied protocols.
     */
    private void configureProxiedProtocols() {
        PortUnificationPipeline pipeline = (PortUnificationPipeline)
            selectorThread.getProcessorPipeline();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        StringTokenizer st = new StringTokenizer(proxiedProtocols,",");
          
        String className;
        ProtocolFinder finder;
        // By default, we must add an https finder if secure.
        if (secure){
            addProtocol(TLS,pipeline,classLoader);  
        } 

        String protocol;
        while (st.hasMoreTokens()){
            protocol = st.nextToken().toLowerCase();
            if (protocol.equals(TLS) && secure){
                continue;
            } else if (protocol.equals(HTTP)){
                continue;
            }
            addProtocol(protocol,pipeline,classLoader);
        }
                
        // Always add http finder at the end.
        addProtocol(HTTP,pipeline,classLoader);   

        Iterator<String> iterator = supportedHandlers.iterator();
        while(iterator.hasNext()){
            ProtocolHandler handler = 
                    (ProtocolHandler)loadInstance(iterator.next(),classLoader);
            if (handler != null){
                pipeline.addProtocolHandler(handler);
            }
        }               
    }
    
    
    private void addProtocol(String protocol,PortUnificationPipeline pipeline,
            ClassLoader classLoader){
        String className = supportedProtocols.get(protocol);
        if (className == null) {
            SelectorThread.logger().log(Level.WARNING, 
                       "Invalid proxied protocol value: " + protocol);            
            return;
        }
        
        ProtocolFinder finder = 
                (ProtocolFinder)loadInstance(className,classLoader);
        if (finder != null){
            pipeline.addProtocolFinder(finder);
        }          
    }
    
    
    /**
     * Util to load classes using reflection.
     */
    private Object loadInstance(String name,ClassLoader classLoader){     
        if (name == null) return null;
        Class className = null;                               
        try{                              
            className = Class.forName(name,true,classLoader);
            return className.newInstance();
        } catch (Throwable t) {
            SelectorThread.logger().log(Level.WARNING, 
                       "Error loading proxied protocol " + name, t);            
        }   
        return null;
    }
}

