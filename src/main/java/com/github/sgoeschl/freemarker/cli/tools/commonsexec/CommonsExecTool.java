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

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class CommonsExecTool {

    public CommonsExecTool(Map<String, Object> settings) {
        requireNonNull(settings);
    }

    public String execute(String line) {
        return execute(CommandLine.parse(line));
    }

    public String execute(String executable, List<String> args) {
        return execute(commandLine(executable, args));
    }

    private String execute(CommandLine commandLine) {
        try (ByteArrayOutputStream boas = new ByteArrayOutputStream()) {
            final Executor executor = new DefaultExecutor();
            executor.setStreamHandler(new PumpStreamHandler(boas));
            executor.execute(commandLine);
            return boas.toString();
        } catch (IOException e) {
            throw new RuntimeException("Executing command failed: " + commandLine, e);
        }
    }

    private static CommandLine commandLine(String executable, List<String> args) {
        final CommandLine cmdLine = new CommandLine(executable);
        cmdLine.addArguments(args.toArray(new String[0]));
        return cmdLine;
    }
}
