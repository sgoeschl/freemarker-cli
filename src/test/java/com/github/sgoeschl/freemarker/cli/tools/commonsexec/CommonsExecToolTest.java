/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.sgoeschl.freemarker.cli.tools.commonsexec;

import org.junit.Test;

import java.util.Collections;

import static junit.framework.TestCase.assertFalse;

public class CommonsExecToolTest {

    // The "date" command should work on all platforms
    private static final String ANY_EXECUTABLE = "date";

    @Test
    public void shallExecuteCommand() {
        final String result = commonsExecTool().execute(ANY_EXECUTABLE, Collections.emptyList());

        assertFalse(result.isEmpty());
    }

    @Test
    public void shallExecuteCommandLine() {
        final String result = commonsExecTool().execute(ANY_EXECUTABLE);

        assertFalse(result.isEmpty());
    }

    @Test(expected = RuntimeException.class)
    public void shallThrowExecptionForUnknownCommand() {
        commonsExecTool().execute("does-not-exist");
    }

    private CommonsExecTool commonsExecTool() {
        return new CommonsExecTool();
    }
}
