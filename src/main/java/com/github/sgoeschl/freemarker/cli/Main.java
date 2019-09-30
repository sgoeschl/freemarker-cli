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
package com.github.sgoeschl.freemarker.cli;

import com.github.sgoeschl.freemarker.cli.model.Provider;
import com.github.sgoeschl.freemarker.cli.model.Settings;
import com.github.sgoeschl.freemarker.cli.util.IOUtils;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

@Command(description = "Apache FreeMarker CLI", name = "freemarker-cli", mixinStandardHelpOptions = true, version = "2.0.0")
public class Main implements Callable<Integer> {

    @Option(names = { "-b", "--basedir" }, description = "Base directory to resolve FreeMarker templates", defaultValue = ".")
    private String baseDir;

    @Option(names = { "-t", "--template" }, description = "FreeMarker template used for rendering", required = true)
    private String template;

    @Option(names = { "-e", "--source-encoding" }, description = "Encoding of source file", defaultValue = "UTF-8")
    String sourceEncoding;

    @Option(names = { "--output-encoding" }, description = "Encoding of output file, e.g. UTF-8", defaultValue = "UTF-8")
    String outputEncoding;

    @Option(names = { "-d", "--description" }, description = "Optional report description")
    private String description;

    @Option(names = { "-v", "--verbose" }, description = "Verbose mode")
    private boolean verbose;

    @Option(names = { "-o", "--output" }, description = "Output file")
    private String outputFile;

    @Option(names = { "--include" }, description = "File pattern for directory search")
    private String include;

    @Option(names = { "-l", "--locale" }, description = "Locale being used for output file, e.g. 'en_US")
    private String locale;

    @Option(names = { "--stdin" }, description = "Read from stdin")
    private boolean readFromStdin;

    @Parameters(description = "Any number of input source files and/or directories")
    private List<String> sources;

    private String stdin;

    public static void main(String[] args) throws Exception {
        try {
            System.exit(new CommandLine(new Main()).execute(args));
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public Integer call() {

        // Read from stdin if we have no positional command line arguments or requested by the caller
        if (sources == null || sources.isEmpty() || readFromStdin) {
            stdin = IOUtils.readStdin();
        }

        final Settings settings = Settings.builder()
                .setBaseDir(baseDir)
                .setTemplate(template)
                .setSourceEncoding(sourceEncoding)
                .setOutputEncoding(outputEncoding)
                .setDescription(description)
                .setVerbose(verbose)
                .setOutputFile(outputFile)
                .setInclude(include)
                .setLocale(locale)
                .setStdin(stdin)
                .setSources(sources != null ? sources : new ArrayList<>())
                .build();

        // Set default locale for the whole JVM
        Locale.setDefault(settings.getLocale());

        new Provider(settings).run();

        return 0;
    }
}
