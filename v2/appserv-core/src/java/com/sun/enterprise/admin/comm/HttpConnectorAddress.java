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

package com.sun.enterprise.admin.comm;

import com.sun.enterprise.admin.util.HostAndPort;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import sun.misc.BASE64Encoder;
/**
   This class abstracts the details of URLS from a client. allowing
   the client to set the host, port, security property and
   authorization informaiton. This information is then used to create
   an URLConnection which will only connect to the admin servlet using
   basic authorization (if authorization information is given).
*/
public final class HttpConnectorAddress implements GenericHttpConnectorAddress
{
  private static final String HTTP_CONNECTOR = "http";
  private static final String HTTPS_CONNECTOR = "https";
  private static final String  AUTHORIZATION_KEY     = "Authorization";
  private static final String AUTHORIZATION_TYPE = "Basic ";

  private String              host;
  private int                 port;
  private AuthenticationInfo  authInfo;

  public HttpConnectorAddress() {
  }


  public HttpConnectorAddress(HostAndPort h){
	this(h.getHost(), h.getPort(), h.isSecure());
  }
  
  public HttpConnectorAddress(String host, int port){
	this(host, port, false);
  }
	  /**
	   * construct an address which indicates the host, port and
	   * security attributes desired.
	   * @param host a host address
	   * @param port a port number
	   * @secure an indication of whether the connection should be
       *  secure (i.e. confidential) or not
	   */
  public HttpConnectorAddress(String host, int port, boolean secure){
	this.host = host;
	this.port = port;
	this.secure = secure;
  }

  	  /**
		 Open a connection using the reciever and the given path
		 @param path the path to the required resource (path here is
		 the portion after the <code>hostname:port</code> portion of a URL)
		 @returns a connection to the required resource. The
		 connection returned may be a sub-class of
		 <code>URLConnection</code> including
		 <code>HttpsURLConnection</code>. If the sub-class is a
		 <code>HttpsURLConnection</code> then this connection will
		 accept any certificate from any server where the server's
		 name matches the host name of this object. Specifically we
		 allows the certificate <em>not</em> to contain the name of
		 the server. This is a potential security hole, but is also a
		 usability enhancement.
		 @throws IOException if there's a problem in connecting to the
		 resource
	  */
  public URLConnection openConnection(String path) throws IOException {
	return this.openConnection(this.toURL(path));
  }


	  /**
	   * get the protocol prefix to be used for a connection for the
	   * receiver
	   * @return the protocol prefix - one of <code>http</code> or
	   *<code>https</code> depending upon the security setting.
	   */
  public String getConnectorType() {
	return this.isSecure() ? HTTPS_CONNECTOR : HTTP_CONNECTOR;
  }

  public String getHost() {
	return host;
  }

  public void setHost(String host) {
	this.host = host;
  }

  public int getPort() {
	return port;
  }

  public void setPort(int port) {
	this.port = port;
  }

  public AuthenticationInfo getAuthenticationInfo() {
	return authInfo;
  }

  public void setAuthenticationInfo(AuthenticationInfo authInfo) {
	this.authInfo = authInfo;
  }

	  /**
	   * Set the security attibute
	   */
  public void setSecure(boolean secure){
	this.secure = secure;
  }
  

	  /**
	   * Indicate if the reciever represents a secure address
	   */
  public boolean isSecure(){
	return secure;
  }

  private boolean secure;

  private final String getUser(){
	return authInfo != null ? authInfo.getUser() : "";
  }

  private final String getPassword(){
	return authInfo != null ? authInfo.getPassword() : "";
  }

  	  /**
	   * Return a string which can be used as the specification to
	   * form an URL.
	   * @return a string which can be used as the specification to
	   *form an URL. This string is in the form of
	   *<code>&gt;protocol>://&gt;host>:&gtport>/</code> with the
	   *appropriate substitutions
	   */
  private final String asURLSpec(String path){
	return this.getConnectorType()
	+"://"+this.getAuthority()
	+(path != null? path : "");
  
  }

	  /**
		 Return the authority portion of the URL spec
	  */
  private final String getAuthority(){
	return this.getHost() + ":" + this.getPort();
  }
  

  private final URL toURL(String path) throws MalformedURLException{
	return new URL(this.asURLSpec(path));
  }
  

  private final URLConnection openConnection(URL url) throws IOException {
	return this.setOptions(this.makeConnection(url));
  }

  private final URLConnection makeConnection(URL url) throws IOException {
	URLConnection uc = url.openConnection();
	if (uc instanceof HttpsURLConnection){
	  setHostnameVerifier((HttpsURLConnection) uc);
	}
	return uc;
  }

	  /**
	   * Set the hostname verifier on the given connection so that a
	   peer which appears to be the one we want to connect to is accepted.
	   * @param uc the non-null URL connection whose hostname verifier may be
	   * set. 
	   * @returns the URLConnection argument
	   */
  private final URLConnection setHostnameVerifier(HttpsURLConnection uc) {
	uc.setHostnameVerifier(
	  new HostnameVerifier() {
		private final String expected = host;
		public boolean verify(String h, SSLSession s){
		  return expected.equals(h);
		}
	  }
	  );
	return uc;
  }  
  
  private final URLConnection setOptions(URLConnection uc){
	uc.setDoOutput(true);
	uc.setUseCaches(false);
	uc.setRequestProperty("Content-type", "application/octet-stream");
	return this.setAuthentication(uc);
  }

  private final URLConnection setAuthentication(URLConnection uc){
	if (authInfo != null) {
	  uc.setRequestProperty(AUTHORIZATION_KEY, this.getBasicAuthString());
	}
	return uc;
  }

  private final String getBasicAuthString(){
	return AUTHORIZATION_TYPE+ this.getBase64Encoded(this.getUser() + ":" + this.getPassword());
  }
  

  private static final String getBase64Encoded(String clearString) {
	return new BASE64Encoder().encode(clearString.getBytes());
  }
}

	  

  
