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
package com.github.sgoeschl.freemarker.cli.picocli;

import com.github.sgoeschl.freemarker.cli.Main;
import picocli.CommandLine.IVersionProvider;

import java.io.InputStream;
import java.util.Properties;

import static java.lang.String.format;

public class GitVersionProvider implements IVersionProvider {

    private final String gitBuildVersion;
    private final String gitCommitId;
    private final String gitCommitTime;

    public GitVersionProvider() {
        final Properties gitProperties = getGitProperties();
        this.gitBuildVersion = gitProperties.getProperty("git.build.version", "unknown");
        this.gitCommitId = gitProperties.getProperty("git.commit.id", "unknown");
        this.gitCommitTime = gitProperties.getProperty("git.commit.time", "unknown");
    }

    @Override
    public String[] getVersion() {
        return new String[] { format("version=%s, time=%s, commit=%s", gitBuildVersion, gitCommitTime, gitCommitId) };
    }

    private static Properties getGitProperties() {
        final Properties properties = new Properties();
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream("git.properties")) {
            if (is != null) {
                properties.load(is);
            }
        } catch (Exception e) {
            return properties;
        }
        return properties;
    }
}
