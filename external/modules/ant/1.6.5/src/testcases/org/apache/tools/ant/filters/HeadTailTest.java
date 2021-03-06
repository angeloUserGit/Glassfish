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

/** JUnit Testcases for TailFilter and HeadFilter
 */
/* I wrote the testcases in one java file because I want also to test the
 * combined behaviour (see end of the class).
*/
public class HeadTailTest extends BuildFileTest {

    public HeadTailTest(String name) {
        super(name);
    }

    public void setUp() {
        configureProject("src/etc/testcases/filters/head-tail.xml");
    }

    public void tearDown() {
        executeTarget("cleanup");
    }

    public void testHead() throws IOException {
        executeTarget("testHead");
        File expected = getProject().resolveFile("expected/head-tail.head.test");
        File result = getProject().resolveFile("result/head-tail.head.test");
        FileUtils fu = FileUtils.newFileUtils();
        assertTrue("testHead: Result not like expected", fu.contentEquals(expected, result));
    }

    public void testHeadLines() throws IOException {
        executeTarget("testHeadLines");
        File expected = getProject().resolveFile("expected/head-tail.headLines.test");
        File result = getProject().resolveFile("result/head-tail.headLines.test");
        FileUtils fu = FileUtils.newFileUtils();
        assertTrue("testHeadLines: Result not like expected", fu.contentEquals(expected, result));
    }

    public void testHeadSkip() throws IOException {
        executeTarget("testHeadSkip");
        File expected = getProject().resolveFile("expected/head-tail.headSkip.test");
        File result = getProject().resolveFile("result/head-tail.headSkip.test");
        FileUtils fu = FileUtils.newFileUtils();
        assertTrue("testHeadSkip: Result not like expected", fu.contentEquals(expected, result));
    }

    public void testHeadLinesSkip() throws IOException {
        executeTarget("testHeadLinesSkip");
        File expected = getProject().resolveFile("expected/head-tail.headLinesSkip.test");
        File result = getProject().resolveFile("result/head-tail.headLinesSkip.test");
        FileUtils fu = FileUtils.newFileUtils();
        assertTrue("testHeadLinesSkip: Result not like expected", fu.contentEquals(expected, result));
    }

    public void testFilterReaderHeadLinesSkip() throws IOException {
        executeTarget("testFilterReaderHeadLinesSkip");
        File expected = getProject().resolveFile(
            "expected/head-tail.headLinesSkip.test");
        File result = getProject().resolveFile(
            "result/head-tail.filterReaderHeadLinesSkip.test");
        FileUtils fu = FileUtils.newFileUtils();
        assertTrue("testFilterReaderHeadLinesSkip: Result not like expected",
                   fu.contentEquals(expected, result));
    }

    public void testTail() throws IOException {
        executeTarget("testTail");
        File expected = getProject().resolveFile("expected/head-tail.tail.test");
        File result = getProject().resolveFile("result/head-tail.tail.test");
        FileUtils fu = FileUtils.newFileUtils();
        assertTrue("testTail: Result not like expected", fu.contentEquals(expected, result));
    }

    public void testTailLines() throws IOException {
        executeTarget("testTailLines");
        File expected = getProject().resolveFile("expected/head-tail.tailLines.test");
        File result = getProject().resolveFile("result/head-tail.tailLines.test");
        FileUtils fu = FileUtils.newFileUtils();
        assertTrue("testTailLines: Result not like expected", fu.contentEquals(expected, result));
    }

    public void testTailSkip() throws IOException {
        executeTarget("testTailSkip");
        File expected = getProject().resolveFile("expected/head-tail.tailSkip.test");
        File result = getProject().resolveFile("result/head-tail.tailSkip.test");
        FileUtils fu = FileUtils.newFileUtils();
        assertTrue("testTailSkip: Result not like expected", fu.contentEquals(expected, result));
    }

    public void testTailLinesSkip() throws IOException {
        executeTarget("testTailLinesSkip");
        File expected = getProject().resolveFile("expected/head-tail.tailLinesSkip.test");
        File result = getProject().resolveFile("result/head-tail.tailLinesSkip.test");
        FileUtils fu = FileUtils.newFileUtils();
        assertTrue("testTailLinesSkip: Result not like expected", fu.contentEquals(expected, result));
    }

    public void testFilterReaderTailLinesSkip() throws IOException {
        executeTarget("testFilterReaderTailLinesSkip");
        File expected = getProject().resolveFile(
            "expected/head-tail.tailLinesSkip.test");
        File result = getProject().resolveFile(
            "result/head-tail.filterReaderTailLinesSkip.test");
        FileUtils fu = FileUtils.newFileUtils();
        assertTrue("testFilterReaderTailLinesSkip: Result not like expected",
                   fu.contentEquals(expected, result));
    }

    public void testHeadTail() throws IOException {
        executeTarget("testHeadTail");
        File expected = getProject().resolveFile("expected/head-tail.headtail.test");
        File result = getProject().resolveFile("result/head-tail.headtail.test");
        FileUtils fu = FileUtils.newFileUtils();
        assertTrue("testHeadTail: Result not like expected", fu.contentEquals(expected, result));
    }

}
