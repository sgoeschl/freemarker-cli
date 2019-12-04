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
package com.github.sgoeschl.freemarker.cli.tools.grok;

import io.krakens.grok.api.Grok;
import io.krakens.grok.api.GrokCompiler;
import org.apache.commons.io.LineIterator;

import static com.github.sgoeschl.freemarker.cli.util.ClosableUtils.closeQuietly;

public class GrokTool {

    private static final String DEFAULT_PATTERN = "/patterns/patterns";

    private final GrokCompiler grokCompiler;

    public GrokTool() {
        this.grokCompiler = GrokCompiler.newInstance();
    }

    public GrokWrapper compile(String pattern) {
        return compile(DEFAULT_PATTERN, pattern);
    }

    public GrokWrapper compile(String path, String pattern) {
        grokCompiler.registerPatternFromClasspath(path);
        final Grok grok = grokCompiler.compile(pattern);
        return new GrokWrapper(grok);
    }

    public void close(LineIterator lineIterator) {
        closeQuietly(lineIterator);
    }
}
