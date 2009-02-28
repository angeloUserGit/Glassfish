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

package org.glassfish.javaee.core.deployment;

import org.glassfish.api.deployment.archive.ArchiveHandler;
import org.glassfish.api.deployment.archive.ReadableArchive;
import org.glassfish.api.deployment.archive.WritableArchive;
import org.glassfish.api.deployment.archive.CompositeHandler;
import org.glassfish.api.deployment.DeploymentContext;
import org.glassfish.api.deployment.DeployCommandParameters;
import org.glassfish.internal.deployment.ExtendedDeploymentContext;
import org.glassfish.api.admin.ServerEnvironment;
import org.glassfish.deployment.common.DeploymentUtils;
import org.glassfish.deployment.common.DeploymentContextImpl;
import org.glassfish.loader.util.ASClassLoaderUtil;
import org.glassfish.internal.deployment.Deployment;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.component.Habitat;
import org.xml.sax.SAXParseException;
import com.sun.enterprise.deploy.shared.AbstractArchiveHandler;
import com.sun.enterprise.deploy.shared.ArchiveFactory;
import com.sun.enterprise.deployment.deploy.shared.Util;
import com.sun.enterprise.util.io.FileUtils;
import com.sun.enterprise.deployment.archivist.ApplicationArchivist;
import com.sun.enterprise.deployment.Application;
import com.sun.enterprise.deployment.util.ModuleDescriptor;
import com.sun.enterprise.config.serverbeans.DasConfig;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Manifest;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: dochez
 * Date: Jan 16, 2009
 * Time: 3:33:40 PM
 * To change this template use File | Settings | File Templates.
 */
@Service(name="ear")
public class EarHandler extends AbstractArchiveHandler implements CompositeHandler {

    @Inject
    Deployment deployment;

    @Inject
    Habitat habitat;

    @Inject
    ArchiveFactory archiveFactory;

    @Inject
    ServerEnvironment env;

    @Inject
    DasConfig dasConfig;
    
    public String getArchiveType() {
        return "ear";
    }

    public boolean handles(ReadableArchive archive) throws IOException {
        return DeploymentUtils.isEAR(archive);
    }

    @Override
    public void expand(ReadableArchive source, WritableArchive target, DeploymentContext context) throws IOException {
        // expand the top level first so we could read application.xml
        super.expand(source, target, context);

        ReadableArchive source2 = 
            archiveFactory.openArchive(target.getURI());

        ApplicationHolder holder = getApplicationHolder(source2, context);

        // now start to expand the sub modules 
        for (ModuleDescriptor md : holder.app.getModules()) {
            String moduleUri = md.getArchiveUri();
            try {
                ReadableArchive subArchive = source.getSubArchive(moduleUri);
                ArchiveHandler subHandler = deployment.getArchiveHandler(subArchive);
                if (subHandler!=null) {
                    WritableArchive subTarget = target.createSubArchive(
                        FileUtils.makeFriendlyFilename(moduleUri));
                    subHandler.expand(subArchive, subTarget, context);
                    target.closeEntry(subTarget);
                    // delete the original module file
                    File origSubArchiveFile = new File(
                        target.getURI().getSchemeSpecificPart(), moduleUri);
                    origSubArchiveFile.delete();
                    continue;
                }
            } catch(IOException ioe) {
                _logger.log(Level.FINE, "Exception while processing " + 
                    moduleUri, ioe);
            }
        }
    }

    public ClassLoader getClassLoader(ClassLoader parent, DeploymentContext context) {
        final ReadableArchive archive  = context.getSource();

        ApplicationHolder holder = getApplicationHolder(archive, context);

        EarClassLoader cl;
        // add the libraries packaged in the application library directory
        try {
            cl = new EarClassLoader(getAppLibDirLibraries(context, holder.app), parent);
        } catch (IOException e) {
            _logger.log(Level.SEVERE, "error in adding libraries in library directory" ,e);
            throw new RuntimeException(e);
        }



        for (ModuleDescriptor md : holder.app.getModules()) {
            ReadableArchive sub = null;
            try {
                sub = archive.getSubArchive(md.getArchiveUri());
            } catch (IOException e) {
                _logger.log(Level.FINE, "Sub archive " + md.getArchiveUri() + " seems unreadable" ,e);
            }
            if (sub!=null) {
                try {
                    ArchiveHandler handler = deployment.getArchiveHandler(sub);
                    if (handler!=null) {
                        // todo : this is a hack, once again, 
                        // the handler is assuming a file:// url
                        ExtendedDeploymentContext subContext = 
                            new DeploymentContextImpl(context.getLogger(), 
                            sub, 
                            context.getCommandParameters(
                                DeployCommandParameters.class), env) {

                            @Override
                            public File getScratchDir(String subDirName) {
                                String modulePortion = Util.getURIName(
                                    getSource().getURI());
                                return (new File(super.getScratchDir(
                                    subDirName), modulePortion));
                            }
                        };

                        // sub context will store the root archive handler also 
                        // so we can figure out the enclosing archive type
                        subContext.setArchiveHandler
                            (context.getArchiveHandler());

                        ClassLoader subCl = handler.getClassLoader(cl, subContext);
                        cl.addModuleClassLoader(md.getArchiveUri(), subCl);
                    }
                } catch (IOException e) {
                    _logger.log(Level.SEVERE, "Cannot find a class loader for submodule", e);
                }

            }

        }
        return cl;
    }

    /**
     * add all the libraries packaged in the application library directory
     *
     * @param context the deployment context
     * @param app the Application object
     * @return an array of URL
     */
    private URL[] getAppLibDirLibraries(DeploymentContext context,
        Application app) throws IOException {
        if (app.getLibraryDirectory() != null) {
            String libPath =
                app.getLibraryDirectory().replace('/', File.separatorChar);
            return ASClassLoaderUtil.getURLs(null,
                new File[] {new File(context.getSourceDir(), libPath)}, true);
        }
        return new URL[0];
    }


    public boolean accept(ReadableArchive source, String entryName) {
        // I am hiding everything but the metadata.
        return entryName.startsWith("META-INF");

    }

    public final class ApplicationHolder {
        final Application app;

        private ApplicationHolder(Application app) {
            this.app = app;
        }
    }

    private ApplicationHolder getApplicationHolder(ReadableArchive source, 
        DeploymentContext context) {
        ApplicationHolder holder = context.getModuleMetaData(ApplicationHolder.class);
        if (holder==null || holder.app==null) {
            try {
                long start = System.currentTimeMillis();
                ApplicationArchivist archivist = habitat.getComponent(ApplicationArchivist.class);
                archivist.setAnnotationProcessingRequested(true);

                String xmlValidationLevel = dasConfig.getDeployXmlValidation();
                archivist.setXMLValidationLevel(xmlValidationLevel);
                if (xmlValidationLevel.equals("none")) {
                    archivist.setXMLValidation(false);
                }

                holder = new ApplicationHolder(archivist.createApplication(
                    source, false));
                System.out.println("time to read application.xml " + (System.currentTimeMillis() - start));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SAXParseException e) {
                throw new RuntimeException(e);
            }
            context.addModuleMetaData(holder);
        }

        if (holder.app==null) {
            throw new RuntimeException("Cannot read application metadata");
        }
        return holder;
    }
} 
