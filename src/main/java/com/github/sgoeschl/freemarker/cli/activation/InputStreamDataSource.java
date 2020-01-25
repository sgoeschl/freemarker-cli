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
import java.io.InputStream;
import java.io.OutputStream;

import static java.util.Objects.requireNonNull;

public class InputStreamDataSource implements DataSource {

    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

    private final String name;
    private final InputStream is;
    private final String contentType;

    public InputStreamDataSource(String name, InputStream is) {
        this(name, is, DEFAULT_CONTENT_TYPE);
    }

    public InputStreamDataSource(String name, InputStream is, String contentType) {
        this.name = requireNonNull(name);
        this.is = requireNonNull(is);
        this.contentType = requireNonNull(contentType);
    }

    @Override
    public InputStream getInputStream() {
        return is;
    }

    @Override
    public OutputStream getOutputStream() {
        return null;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "InputStreamDataSource{" +
                "name='" + name + '\'' +
                ", contentType='" + contentType + '\'' +
                ", inputStream=" + is.getClass().getName() +
                '}';
    }
}

