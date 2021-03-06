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

package com.sun.enterprise.tools.upgrade.logging;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import java.text.*;
import com.sun.enterprise.tools.upgrade.common.*;
import com.sun.enterprise.server.logging.UniformLogFormatter;

/**
 *
 * author : Servesh Singh
 *
 */

public class LogFormatter  extends UniformLogFormatter  {

	private static List listenerList = new ArrayList();

 public String format(LogRecord rec) {
 /*
	StringBuffer buf = new StringBuffer(1000);
	buf.append("[");
	buf.append(rec.getLevel());
	buf.append("|");
	buf.append(getForamtedDate(rec.getMillis()));
	buf.append("|");
	buf.append(rec.getLoggerName( ));
	buf.append("|");
	buf.append("ThreadID="+rec.getThreadID( ));
	buf.append("|");
	buf.append(formatMessage(rec));
    Throwable th = rec.getThrown(); 
    if(th !=null) {    
        buf.append("|");    
        buf.append("\n");
		buf.append("	message: "+th.getMessage());
        StackTraceElement[] st = th.getStackTrace();
        buf.append("\n");
        for(int i =0;i<st.length;i++){
            buf.append("        "+st[i]);
            buf.append("\n");
        }
    }
	buf.append("]");
    buf.append('\n');
	String msg = buf.toString();
 */

        notifyRegisteredListeners(rec.getMessage(),rec);
	return super.format(rec);
    }

    private void notifyRegisteredListeners(String msg, LogRecord record){
	int size = listenerList.size();
	for(int i =0; i<size;i++) {
	    LogMessageListener listener = (LogMessageListener)listenerList.get(i);
	    LogMessageEvent e = new LogMessageEvent("UpgradeTool", msg);
         e.setLogRecord(record);
	    listener.logMessageReceived(e);
	}
    }
/*
    public String getHead(Handler h) {
	return "\n";
    }

    public String getTail(Handler h) {
	return "\n";
    }
*/
    public static void addLogMessageListener(LogMessageListener listener){
        listenerList.add(listener);
    }

    public static void removeLogMessageListener(LogMessageListener listener){
	listenerList.remove(listener);
    }
/*
    public String getForamtedDate(long milisecond) {
    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy MMMMM dd hh:mm: ss aaa");
	StringBuffer bf = new StringBuffer();
    dateformat.format(new Date(milisecond),bf,new FieldPosition(0));
    return bf.toString();
    }
 */
 }

