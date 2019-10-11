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

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExamplesTest extends AbstractMainTest {

    private static final int MIN_OUTPUT_SIZE = 128;

    @Test
    public void shouldRunDemoExamples() throws IOException {
        assertValid(execute("-t templates/demo.ftl README.md"));
    }

    @Test
    public void shouldRunCsvExamples() throws IOException {
        assertValid(execute("-t templates/csv/html/transform.ftl site/sample/csv/contract.csv"));
        assertValid(execute("-t templates/csv/md/transform.ftl site/sample/csv/contract.csv"));
        assertValid(execute("-t templates/csv/shell/curl.ftl site/sample/csv/user.csv"));
        assertValid(execute("-t templates/csv/fo/transform.ftl site/sample/csv/locker-test-users.csv"));
        assertValid(execute("-t templates/csv/fo/transactions.ftl site/sample/csv/transactions.csv"));
        assertValid(execute("-t templates/csv/html/transactions.ftl site/sample/csv/transactions.csv"));
    }

    @Test
    public void shouldRunExcelExamples() throws IOException {
        assertValid(execute("-t templates/excel/html/transform.ftl site/sample/excel/test.xls"));
        assertValid(execute("-t templates/excel/html/transform.ftl site/sample/excel/test.xlsx"));
        assertValid(execute("-t templates/excel/html/transform.ftl site/sample/excel/test-multiple-sheets.xlsx"));
        assertValid(execute("-t templates/excel/md/transform.ftl site/sample/excel/test-multiple-sheets.xlsx"));
        assertValid(execute("-t templates/excel/csv/transform.ftl site/sample/excel/test-multiple-sheets.xlsx"));
    }

    @Test
    public void shouldRunHtmlExamples() throws IOException {
        assertValid(execute("-t templates/html/csv/dependencies.ftl site/sample/html/dependencies.html"));
    }

    @Test
    public void shouldRunJsonExamples() throws IOException {
        assertValid(execute("-t templates/json/csv/swagger-endpoints.ftl site/sample/json/swagger-spec.json"));
        assertValid(execute("-t templates/json/html/customer-user-products.ftl site/sample/json/customer-user-products.json"));
        assertValid(execute("-t templates/json/md/customer-user-products.ftl site/sample/json/customer-user-products.json"));
        assertValid(execute("-t templates/json/md/github-users.ftl site/sample/json/github-users.json"));
    }

    @Test
    public void shouldRunPropertiesExamples() throws IOException {
        assertValid(execute("-t templates/properties/csv/locker-test-users.ftl site/sample/properties"));
    }

    @Test
    public void shouldRunYamlExamples() throws IOException {
        assertValid(execute("-t ./templates/yaml/txt/transform.ftl ./site/sample/yaml/customer.yaml"));
    }

    @Test
    public void shouldRunXmlExamples() throws IOException {
        assertValid(execute("-t ./templates/xml/txt/recipients.ftl site/sample/xml/recipients.xml"));
    }

    private static void assertValid(String output) {
        assertTrue(output.length() > MIN_OUTPUT_SIZE);
        assertFalse(output.contains("Exception"));
    }
}
