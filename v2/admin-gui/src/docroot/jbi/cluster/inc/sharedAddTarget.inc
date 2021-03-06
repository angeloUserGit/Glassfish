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

<!-- jbi/cluster/inc/sharedAddTarget.inc -->

<!beforeCreate
   setPageSessionAttribute(key="isValid", value="$boolean{true}")
   isEE(isEE=>$attribute{isEE}); 
   
   checkSupport(supportCluster => $attribute{supportCluster});
   getStandaloneServerList(serverList => $attribute{standaloneServerList});
   
   addListToOptions(
       inputList    = "$attribute{standaloneServerList}"
       inputOptions = "$attribute{optionList}"
       outputOptions => $attribute{optionList});

   getClusterList(clusterList => $attribute{clusterList});
   jbiGetTargetsLists(
           inputList    = "$attribute{clusterList}"
           inputOptions = "$attribute{optionList}"
           isJBIArchiveAvailable = "#{isJBIArchiveAvailable}"
           availableTargetList => $attribute{availableTargetList}
           installedTargetList => $attribute{installedTargetList});
 />

<!-- if single target, show property -->
<sun:propertySheetSection id="singleTargetpropertySheetSection" 
    label="#{sessionScope.jbiStep2targetPss}"
    rendered="#{!sessionScope.jbiSharedAddTargetTableRendered}"
    >

    <sun:property id="showSingleTargetProperty"  
        overlapLabel="#{false}" 
        rendered="#{!sessionScope.jbiSharedAddTargetTableRendered}"
        label="#{sessionScope.jbiStep2namePropLbl}"
        labelAlign="left" 
        noWrap="#{true}" 
        >

        <sun:staticText id="showTargetName" 
            text="#{sessionScope.jbiSharedAddTargetSingleTargetName}" 
        /> 

    </sun:property>
</sun:propertySheetSection>

<!-- else if variable number of targets, show targets table -->
<sun:table id="AddToTargets" title="$resource{i18n.jbi.install.AvailableTargets}" 
    deselectMultipleButton="$boolean{true}"
    rendered="#{sessionScope.jbiSharedAddTargetTableRendered}"
    selectMultipleButton="$boolean{true}"
    >

    <!afterCreate
        getClientId(component="$this{component}" clientId=>$page{tableId});
    />

   <sun:tableRowGroup id="addTableRowGroup" 
       selected="#{td.value.selected}" 
       data={"$attribute{availableTargetList}"} 
       sourceVar="td">
   
    <!afterCreate
         getClientId(component="$this{component}" clientId=>$page{tableRowGroupId});
    />
    
    <sun:tableColumn selectId="select" rowHeader="$boolean{false}" id="col0">
        <sun:checkbox id="select" 
            selected="#{td.value.selected}" 
            selectedValue="$boolean{true}" 
            onClick="setTimeout('initAllRows();', 0)" 
            />
    </sun:tableColumn>
      
    <sun:tableColumn headerText="$resource{i18n.jbi.install.PropertyName}" 
                     sort="name" rowHeader="$boolean{false}" id="addtocol2">
        <sun:staticText id="addtocol1St"   text="#{td.value.name}" />
    </sun:tableColumn>
  
  </sun:tableRowGroup>
        
</sun:table >
