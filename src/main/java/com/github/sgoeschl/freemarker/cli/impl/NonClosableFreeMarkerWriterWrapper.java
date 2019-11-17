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
package com.github.sgoeschl.freemarker.cli.impl;

import java.io.IOException;
import java.io.Writer;

import static java.util.Objects.requireNonNull;

/**
 * Wraps a writer (usually the internal FreeMarker's writer instance
 * and avoids closing it since this would crash FreeMarker. E.g. the
 * Commons CSV integration uses the FreeMarker writer directly but
 * some implementation could call "CSVPrinter#close"
 */
public class NonClosableFreeMarkerWriterWrapper extends Writer {

    private final Writer delegate;

    public NonClosableFreeMarkerWriterWrapper(Writer delegate) {
        this.delegate = requireNonNull(delegate);
    }

    @Override
    public void write(int c) throws IOException {
        delegate.write(c);
    }

    @Override
    public void write(char[] cbuf) throws IOException {
        delegate.write(cbuf);
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        delegate.write(cbuf, off, len);
    }

    @Override
    public void write(String str) throws IOException {
        delegate.write(str);
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        delegate.write(str, off, len);
    }

    @Override
    public Writer append(CharSequence csq) throws IOException {
        return delegate.append(csq);
    }

    @Override
    public Writer append(CharSequence csq, int start, int end) throws IOException {
        return delegate.append(csq, start, end);
    }

    @Override
    public Writer append(char c) throws IOException {
        return delegate.append(c);
    }

    @Override
    public void flush() throws IOException {
        delegate.flush();
    }

    @Override
    public void close() {
        // ignore
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
