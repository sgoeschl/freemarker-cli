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
package com.github.sgoeschl.freemarker.cli.util;

import java.util.Scanner;

public class IOUtils {

    public static String getFileExtension(String name) {
        final int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return null;
        }
        return name.substring(lastIndexOf + 1);
    }

    public static String readStdin() {
        final StringBuilder buffer = new StringBuilder();
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNext()) {
                buffer.append(scanner.nextLine());
            }
        }
        return buffer.toString();
    }
}
