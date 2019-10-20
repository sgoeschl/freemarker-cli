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
package com.github.sgoeschl.freemarker.cli.tools.excel;

import com.github.sgoeschl.freemarker.cli.model.Document;
import com.github.sgoeschl.freemarker.cli.resolver.DocumentFactory;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static junit.framework.TestCase.assertEquals;

public class ExcelToolTest {

    private final File TEST_XLS = new File("./site/sample/excel/test.xls");
    private final File TEST_XLSX = new File("./site/sample/excel/test.xlsx");
    private final File MULTIPLE_SHEETS_XSLX_FILE = new File("./site/sample/excel/test-multiple-sheets.xlsx");

    @Test
    public void shallParseXlsFile() {
        final Workbook workbook = workbook(TEST_XLS);

        final List<Sheet> sheets = excelTool().getSheets(workbook);
        final List<List<String>> records = excelTool().parseSheet(sheets.get(0));

        assertEquals(1, sheets.size());
        assertEquals(3, records.size());
    }

    @Test
    public void shallParseXlsxFile() {
        final Workbook workbook = workbook(TEST_XLSX);

        final List<Sheet> sheets = excelTool().getSheets(workbook);
        final List<List<String>> records = excelTool().parseSheet(sheets.get(0));

        assertEquals(1, sheets.size());
        assertEquals(3, records.size());
    }

    @Test
    public void shallParseMultiSheetExcel() {
        final Workbook workbook = workbook(MULTIPLE_SHEETS_XSLX_FILE);

        final List<Sheet> sheets = excelTool().getSheets(workbook);

        assertEquals(2, sheets.size());
        assertEquals(3, excelTool().parseSheet(sheets.get(0)).size());
        assertEquals(2, excelTool().parseSheet(sheets.get(1)).size());
    }

    @Test
    public void shouldParseCellContent() {
        final Workbook workbook = workbook(TEST_XLSX);
        final List<Sheet> sheets = excelTool().getSheets(workbook);
        final List<List<String>> records = excelTool().parseSheet(sheets.get(0));

        final List<String> record = records.get(1);

        assertEquals("Row 1", record.get(0));
        assertEquals("01/01/17", record.get(1));
        assertEquals("100.00", record.get(2));
        assertEquals("€100.00", record.get(3));
        assertEquals("11:00", record.get(4));
        assertEquals("10.00%", record.get(5));
        assertEquals("C2*F2", record.get(6));
    }

    private Workbook workbook(File file) {
        return excelTool().parse(document(file));
    }

    private ExcelTool excelTool() {
        return new ExcelTool();
    }

    private Document document(File file) {
        return DocumentFactory.create(file, UTF_8);
    }
}
