/* 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.felix.shell.impl;

import java.io.PrintStream;
import java.util.StringTokenizer;

import org.apache.felix.shell.Command;
import org.osgi.framework.*;

public class StartCommandImpl extends InstallCommandImpl implements Command
{
    private BundleContext m_context = null;

    public StartCommandImpl(BundleContext context)
    {
        super(context);
        m_context = context;
    }

    public String getName()
    {
        return "start";
    }

    public String getUsage()
    {
        return "start <id> [<id> <URL> ...]";
    }

    public String getShortDescription()
    {
        return "start bundle(s).";
    }

    public void execute(String s, PrintStream out, PrintStream err)
    {
        StringTokenizer st = new StringTokenizer(s, " ");

        // Ignore the command name.
        st.nextToken();

        // There should be at least one bundle id.
        if (st.countTokens() >= 1)
        {
            while (st.hasMoreTokens())
            {
                String id = st.nextToken().trim();

                try {
                    Bundle bundle = null;

                    // The id may be a number or a URL, so check.
                    if (Character.isDigit(id.charAt(0)))
                    {
                        long l = Long.parseLong(id);
                        bundle = m_context.getBundle(l);
                    }
                    else
                    {
                        bundle = install(id, out, err);
                    }

                    if (bundle != null)
                    {
                        bundle.start();
                    }
                    else
                    {
                        err.println("Bundle ID " + id + " is invalid.");
                    }
                } catch (NumberFormatException ex) {
                    err.println("Unable to parse id '" + id + "'.");
                } catch (BundleException ex) {
                    if (ex.getNestedException() != null)
                    {
                        ex.printStackTrace();
                        err.println(ex.getNestedException().toString());
                    }
                    else
                    {
                        err.println(ex.toString());
                    }
                } catch (Exception ex) {
                    err.println(ex.toString());
                }
            }
        }
        else
        {
            err.println("Incorrect number of arguments");
        }
    }
}