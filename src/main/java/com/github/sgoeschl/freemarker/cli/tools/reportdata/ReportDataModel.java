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
package com.github.sgoeschl.freemarker.cli.tools.reportdata;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ReportDataModel {

    public Map<String, Object> create(String description) {
        final Properties properties = new Properties();
        properties.put("host", getHostName());
        properties.put("description", description != null ? description : "");
        properties.put("user", System.getProperty("user.name", "unknown"));
        properties.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        properties.put("time", new SimpleDateFormat("hh:mm:ss").format(new Date()));
        final Map<String, Object> dataModel = new HashMap<String, Object>();
        dataModel.put("ReportData", properties);
        return dataModel;
    }

    private static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ignored) {
            return "localhost";
        }
    }
}
