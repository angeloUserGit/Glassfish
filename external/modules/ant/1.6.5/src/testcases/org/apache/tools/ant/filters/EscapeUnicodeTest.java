/*
 * Copyright  2003-2004 The Apache Software Foundation
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
 *
 */

package org.apache.tools.ant.filters;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.BuildFileTest;
import org.apache.tools.ant.util.FileUtils;

/**
 */
public class EscapeUnicodeTest extends BuildFileTest {

    public EscapeUnicodeTest(String name) {
        super(name);
    }

    public void setUp() {
        configureProject("src/etc/testcases/filters/build.xml");
    }

    public void tearDown() {
        executeTarget("cleanup");
    }

    public void testEscapeUnicode() throws IOException {
        executeTarget("testEscapeUnicode");
        File expected = getProject().resolveFile("expected/escapeunicode.test");
        File result = getProject().resolveFile("result/escapeunicode.test");
        FileUtils fu = FileUtils.newFileUtils();
        assertTrue(fu.contentEquals(expected, result));
    }

}
