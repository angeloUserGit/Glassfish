
<!--
 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 
 Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 
 The contents of this file are subject to the terms of either the GNU
 General Public License Version 2 only ("GPL") or the Common Development
 and Distribution License("CDDL") (collectively, the "License").  You
 may not use this file except in compliance with the License. You can obtain
 a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 language governing permissions and limitations under the License.
 
 When distributing the software, include this License Header Notice in each
 file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 Sun designates this particular file as subject to the "Classpath" exception
 as provided by Sun in the GPL Version 2 section of the License file that
 accompanied this code.  If applicable, add the following below the License
 Header, with the fields enclosed by brackets [] replaced by your own
 identifying information: "Portions Copyrighted [year]
 [name of copyright owner]"
 
 Contributor(s):
 
 If you wish your version of this file to be governed by only the CDDL or
 only the GPL Version 2, indicate your decision by adding "[Contributor]
 elects to include this software in this distribution under the [CDDL or GPL
 Version 2] license."  If you don't indicate a single choice of license, a
 recipient has the option to distribute your version of this file under
 either the CDDL, the GPL Version 2 or to extend the choice of license to
 its licensees as provided above.  However, if you add GPL Version 2 code
 and therefore, elected the GPL Version 2 license, then the option applies
 only if the new code is made subject to such option by the copyright
 holder.
-->

<sun:tabSet id="webServiceTabs" immediate="$boolean{true}" selected="#{sessionScope.webServiceTabs}">
    <!beforeCreate
        setPageSessionAttribute(key="webServiceKey" value="$requestParameter{webServiceKey}" )
        urlencode(value="#{webServiceKey}", value=>$attribute{wsKeyEncoded});
        setPageSessionAttribute(key="wsKeyEncoded" value="$attribute{wsKeyEncoded}");
    />
    <sun:tab id="general" immediate="$boolean{true}" text="$resource{i18n.common.General}" >
        <!command
            setSessionAttribute(key="webServiceTabs" value="general");
            redirect(page="webServiceGeneral.jsf?webServiceKey=#{wsKeyEncoded}");
	/>       
    </sun:tab>
    <sun:tab id="publish" immediate="$boolean{true}" text="$resource{i18n.webServicePublish.publish}">
	<!command
            setSessionAttribute(key="webServiceTabs" value="publish");
	    redirect(page="webServicePublish.jsf?webServiceKey=#{wsKeyEncoded}");
	/>
    </sun:tab>
    <sun:tab id="monitor" immediate="$boolean{true}" text="$resource{i18n.monitor.Monitor}">
            <!command
                setSessionAttribute(key="webServiceTabs" value="statistics");
                redirect(page="webServiceMonitorStatistics.jsf?webServiceKey=#{wsKeyEncoded}&type=#{type}&name=#{name}&isStandalone=#{isStandalone}&appName=#{appName}&bundleName=#{bundleName}");
	    />
	<sun:tab id="statistics" immediate="$boolean{true}" text="$resource{i18n.webServiceMonitor.Statistics}" >
	    <!command
                setSessionAttribute(key="webServiceTabs" value="statistics");
                redirect(page="webServiceMonitorStatistics.jsf?webServiceKey=#{wsKeyEncoded}&type=#{type}&name=#{name}&isStandalone=#{isStandalone}&appName=#{appName}&bundleName=#{bundleName}");
	    />       
	</sun:tab>
	<sun:tab id="messages" immediate="$boolean{true}" text="$resource{i18n.webServiceMonitor.Messages}" >
	    <!command
                setSessionAttribute(key="webServiceTabs" value="messages");
                redirect(page="webServiceMessages.jsf?webServiceKey=#{wsKeyEncoded}&type=#{type}&name=#{name}&isStandalone=#{isStandalone}&appName=#{appName}&bundleName=#{bundleName}");
	    />             
	</sun:tab>
        <sun:tab id="config" immediate="$boolean{true}" text="$resource{i18n.common.Configuration}" >
	    <!command
                setSessionAttribute(key="webServiceTabs" value="config");
		redirect(page="webServiceMonitorConfig.jsf?webServiceKey=#{wsKeyEncoded}&type=#{type}&name=#{name}&isStandalone=#{isStandalone}&appName=#{appName}&bundleName=#{bundleName}");
	    />             
	</sun:tab>
    </sun:tab>
    <sun:tab id="transformation" immediate="$boolean{true}" text="$resource{i18n.webServiceTransformation.TabTitle}">
	    <!command
                setSessionAttribute(key="webServiceTabs" value="transformation");
		redirect(page="webServiceTransformation.jsf?webServiceKey=#{wsKeyEncoded}");
	    /> 
    </sun:tab>
</sun:tabSet>
