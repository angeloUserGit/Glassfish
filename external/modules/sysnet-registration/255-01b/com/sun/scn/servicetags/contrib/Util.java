
/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.scn.servicetags.contrib;

import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.TimeZone;
import java.util.UUID;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

// Utility class for com.sun.scn.servicetags.contrib package
class Util {
    final static String ST_NODE_REGISTRATION_DATA = "registration_data";
    final static String ST_ATTR_REGISTRATION_VERSION = "version";
    final static String ST_NODE_ENVIRONMENT = "environment";
    final static String ST_NODE_HOSTNAME = "hostname";
    final static String ST_NODE_HOST_ID = "hostId";
    final static String ST_NODE_OS_NAME = "osName";
    final static String ST_NODE_OS_VERSION = "osVersion";
    final static String ST_NODE_OS_ARCH = "osArchitecture";
    final static String ST_NODE_SYSTEM_MODEL = "systemModel";
    final static String ST_NODE_SYSTEM_MANUFACTURER = "systemManufacturer";
    final static String ST_NODE_CPU_MANUFACTURER = "cpuManufacturer";
    final static String ST_NODE_SERIAL_NUMBER = "serialNumber";
    final static String ST_NODE_REGISTRY = "registry";
    final static String ST_ATTR_REGISTRY_URN = "urn";
    final static String ST_ATTR_REGISTRY_VERSION = "version";
    final static String ST_NODE_SERVICE_TAG = "service_tag";
    final static String ST_NODE_INSTANCE_URN = "instance_urn";
    final static String ST_NODE_PRODUCT_NAME = "product_name";
    final static String ST_NODE_PRODUCT_VERSION = "product_version";
    final static String ST_NODE_PRODUCT_URN = "product_urn";
    final static String ST_NODE_PRODUCT_PARENT_URN = "product_parent_urn";
    final static String ST_NODE_PRODUCT_PARENT = "product_parent";
    final static String ST_NODE_PRODUCT_DEFINED_INST_ID = "product_defined_inst_id";
    final static String ST_NODE_PRODUCT_VENDOR = "product_vendor";
    final static String ST_NODE_PLATFORM_ARCH = "platform_arch";
    final static String ST_NODE_TIMESTAMP = "timestamp";
    final static String ST_NODE_CONTAINER = "container";
    final static String ST_NODE_SOURCE = "source";
    final static String ST_NODE_INSTALLER_UID = "installer_uid";

    private static boolean verbose = (System.getProperty("servicetag.verbose") != null);
    private static String jrepath = null;

    // for debugging and tracing
    static boolean isVerbose() {
        return verbose;
    }

    /**
     * Gets the pathname of JRE in the running platform
     * This can be a JDK or JRE.
     */
    static synchronized String getJrePath() {
        if (jrepath == null) {
            // Determine the JRE path by checking the existence of
            // <HOME>/jre/lib and <HOME>/lib.
            String javaHome = System.getProperty("java.home");
            jrepath = javaHome + File.separator + "jre";
            File f = new File(jrepath, "lib");
            if (!f.exists()) {
                // java.home usually points to the JRE path
                jrepath = javaHome;
            }
        }
        return jrepath;
    }

    /**
     * Tests if the running platform is a JDK.
     */
    static boolean isJdk() {
        // <HOME>/jre exists which implies it's a JDK
        return getJrePath().endsWith(File.separator + "jre");
    }

    /**
     * Generates the URN string of "urn:st" namespace
     */
    static String generateURN() {
        return "urn:st:" + UUID.randomUUID().toString();
    }

    static int getIntValue(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("\"" + value + "\"" +
                " expected to be an integer");
        }
    }

    /**
     * Formats the Date into a timestamp string in YYYY-MM-dd HH:mm:ss GMT.
     * @param timestamp Date
     * @return a string representation of the timestamp
     *         in the YYYY-MM-dd HH:mm:ss GMT format.
     */
    static String formatTimestamp(Date timestamp) {
        if (timestamp == null) {
            return "[No timestamp]";
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(timestamp);
    }

    /**
     * Parses a timestamp string in YYYY-MM-dd HH:mm:ss GMT format.
     * @param timestamp Timestamp in the YYYY-MM-dd HH:mm:ss GMT format.
     * @return Date
     */
    static Date parseTimestamp(String timestamp) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            return df.parse(timestamp);
        } catch (ParseException e) {
            // should not reach here
            e.printStackTrace();
            return new Date();
        }
    }

    static String commandOutput(Process p) throws IOException {
        Reader r = null;
        Reader err = null;
        try {
            r = new InputStreamReader(p.getInputStream());
            err = new InputStreamReader(p.getErrorStream());
            String output = commandOutput(r);
            String errorMsg = commandOutput(err);
            p.waitFor();
            return output + errorMsg.trim();
        } catch (InterruptedException e) {
            if (isVerbose()) {
                e.printStackTrace();
            }
            return e.getMessage();
        } finally {
            if (r != null) {
                r.close();
            }
            if (err != null) {
                err.close();
            }
        }
    }

    static String commandOutput(Reader r) throws IOException {
        StringBuilder sb = new StringBuilder();
        int c;
        while ((c = r.read()) > 0) {
            if (c != '\r') {
                sb.append((char) c);
            }
        }
        return sb.toString();
    }

    static int getJdkVersion() {
        parseVersion();
        return jdkVersion;
    }

    static int getUpdateVersion() {
        parseVersion();
        return jdkUpdate;
    }

    private static int jdkVersion = 0;
    private static int jdkUpdate = 0;
    private static synchronized void parseVersion() {
        if (jdkVersion > 0) {
            return;
        }

        // parse java.runtime.version
        // valid format of the version string is:
        // n.n.n[_uu[c]][-<identifer>]-bxx
        String cs = System.getProperty("java.runtime.version");
        if (cs.length() >= 5 &&
            Character.isDigit(cs.charAt(0)) && cs.charAt(1) == '.' &&
            Character.isDigit(cs.charAt(2)) && cs.charAt(3) == '.' &&
            Character.isDigit(cs.charAt(4))) {
            jdkVersion = Character.digit(cs.charAt(2), 10);
            cs = cs.substring(5, cs.length());
            if (cs.charAt(0) == '_' && cs.length() >= 3 &&
                Character.isDigit(cs.charAt(1)) &&
                Character.isDigit(cs.charAt(2))) {
                int nextChar = 3;
                try {
                    String uu = cs.substring(1, 3);
                    jdkUpdate = Integer.valueOf(uu).intValue();
                } catch (NumberFormatException e) {
                    // not conforming to the naming convention
                    return;
                }
            }
        } else {
            throw new InternalError("Invalid java.runtime.version" + cs);
        }
    }

    /**
     * Returns this java string as a null-terminated byte array
     */
    private static byte[] stringToByteArray(String str) {
        return (str + "\u0000").getBytes();
    }
    
    /** 
     * Converts a null-terminated byte array to java string
     */
    private static String byteArrayToString(byte[] array) {
	return new String(array, 0, array.length -1);
    }

    /**
     * Gets the stclient path using a well known location from 
     * the Windows platform Registry, otherwise it will return null.
     */
    static File getWindowsStClientFile() {
        File out = null;
        String regKey = "software\\microsoft\\windows\\currentversion\\app paths\\stclient.exe";
        String keyName = "" ; // use the default  key
        String path = getRegistryKey(regKey, keyName);

        if (path != null && (new File(path)).exists()) {
            out = new File(path);
        }
        if (isVerbose()) {
            System.out.println("stclient=" + out);
        }
        return out;
    }

    /**
     * This uses reflection to access a private java windows registry 
     * interface, any changes to that Class must be appropriately adjusted.
     * Returns a null if unsuccessful.
     */
    private static String getRegistryKey(String regKey, String keyName) {
        String out = null;
        try {
            Class<?> clazz = Class.forName("java.util.prefs.WindowsPreferences");

	    // Get the registry methods
            Method winRegOpenKeyM = clazz.getDeclaredMethod("WindowsRegOpenKey", 
                    int.class, byte[].class, int.class);
            winRegOpenKeyM.setAccessible(true);

            Method winRegCloseKeyM = clazz.getDeclaredMethod("WindowsRegCloseKey", 
                    int.class);
            winRegCloseKeyM.setAccessible(true);

            Method winRegQueryValueM = clazz.getDeclaredMethod("WindowsRegQueryValueEx", 
                    int.class, byte[].class);
            winRegQueryValueM.setAccessible(true);

            // Get all the constants we need
            int HKLM = getValueFromStaticField("HKEY_LOCAL_MACHINE", clazz);
            int KEY_READ = getValueFromStaticField("KEY_READ", clazz);
            int ERROR_CODE = getValueFromStaticField("ERROR_CODE", clazz);
            int NATIVE_HANDLE = getValueFromStaticField("NATIVE_HANDLE", clazz);
            int ERROR_SUCCESS = getValueFromStaticField("ERROR_SUCCESS", clazz);

            // Convert keys
            byte[] reg = stringToByteArray(regKey);
            byte[] key = stringToByteArray(keyName);

            // Open the registry
            int[] result = (int[]) winRegOpenKeyM.invoke(null, HKLM, reg, KEY_READ);

            if (result[ERROR_CODE] == ERROR_SUCCESS) {
                byte[] stvalue = (byte[]) winRegQueryValueM.invoke(null, 
                    result[NATIVE_HANDLE], key);
                out = byteArrayToString(stvalue);
                winRegCloseKeyM.invoke(null, result[NATIVE_HANDLE]);
            }
        } catch (Exception ex) {
            if (isVerbose()) {
                ex.printStackTrace();
            }
        }
        return out;
    }

    private static int getValueFromStaticField(String fldName, Class<?> klass) throws Exception {
        Field f = klass.getDeclaredField(fldName);
        f.setAccessible(true);
        return f.getInt(null);
    }
}
