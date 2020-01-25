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
package com.github.sgoeschl.freemarker.cli.activation;

import javax.activation.DataSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.codec.Charsets.UTF_8;

public class StringDataSource implements DataSource {

    private final String name;
    private final String content;
    private final Charset charset;

    public StringDataSource(String name, String content) {
        this(name, content, UTF_8);
    }

    public StringDataSource(String name, String content, Charset charset) {
        this.name = requireNonNull(name);
        this.content = requireNonNull(content);
        this.charset = requireNonNull(charset);
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(content.getBytes(charset));
    }

    @Override
    public OutputStream getOutputStream() {
        return null;
    }

    @Override
    public String getContentType() {
        return "plain/text";
    }

    @Override
    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "StringDataSource{" +
                "name='" + name + '\'' +
                ", charset=" + charset +
                ", length=" + content.length() +
                '}';
    }
}
