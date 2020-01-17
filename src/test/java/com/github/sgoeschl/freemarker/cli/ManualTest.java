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

/**
 * Invoke freemarker-cli and dump the output for ad-hoc manual testing.
 */
public class ManualTest {

    private static final String SPACE = " ";
    // private static final String CMD = "-b ./src/test -t templates/csv/html/transactions.ftl site/sample/csv/transactions.csv";
    // private static final String CMD = "-b ./src/test -l de_AT -Dfoo=bar -t templates/info.ftl site/sample/csv/transactions.csv";
    // private static final String CMD = "-b ./src/test -DFOO=foo -DBAR=bar -l de -t templates/demo.ftl site/sample/csv/transactions.csv";
    // private static final String CMD = "-b ./src/test -DFOO=foo -DBAR=bar -t templates/demo.ftl site/sample/csv/transactions.csv";
    // private static final String CMD = "-b ./src/test -Dcsv.out.format=TDF -t templates/csv/transform.ftl site/sample/csv/contract.csv";
    // private static final String CMD = "-t templates/excel/csv/transform.ftl -l de_AT site/sample/excel/test.xlsx";
    // private static final String CMD = "-i ${JsonPathTool.parse(Documents.first).read('$.info.title')} site/sample/json/swagger-spec.json";
    // private static final String CMD = "-i ${XmlTool.parse(Documents.first)['recipients/person[1]/name']} site/sample/xml/recipients.xml";
    private static final String CMD = "-i ${JsoupTool.parse(Documents.first).select('a')[0]} site/sample/html/dependencies.html";

    public static void main(String[] args) {
        Main.execute(CMD.split(SPACE));
    }
}
