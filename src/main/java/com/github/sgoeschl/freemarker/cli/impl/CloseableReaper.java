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

import com.github.sgoeschl.freemarker.cli.util.ClosableUtils;

import java.io.Closeable;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Keep track of closables created on behalf of the client to close
 * them all later on.
 */
public class CloseableReaper implements Closeable {

    private final List<WeakReference<Closeable>> closeables = new ArrayList<>();

    public <T extends Closeable> T add(T closeable) {
        synchronized (this) {
            if (closeable != null) {
                closeables.add(new WeakReference<>(closeable));
            }
            return closeable;
        }
    }

    @Override
    public void close() throws IOException {
        synchronized (this) {
            closeables.forEach(ClosableUtils::closeQuietly);
            closeables.clear();
        }
    }
}

