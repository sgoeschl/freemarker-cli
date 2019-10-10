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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelTool {

    public Workbook parse(Document document) {
        try (InputStream is = document.getInputStream()) {
            return WorkbookFactory.create(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Ecxel document: " + document, e);
        }
    }

    public List<Sheet> getSheets(Workbook workbook) {
        final List<Sheet> result = new ArrayList<>();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            result.add(workbook.getSheetAt(i));
        }
        return result;
    }

    public List<List<String>> parseSheet(Sheet sheet) {
        final DataFormatter formatter = new DataFormatter();
        final Iterator<Row> iterator = sheet.iterator();
        final List<List<String>> result = new ArrayList<>();

        while (iterator.hasNext()) {
            final Row nextRow = iterator.next();
            final List<String> currRowValues = new ArrayList<>();
            final Iterator<Cell> cellIterator = nextRow.cellIterator();
            result.add(currRowValues);
            while (cellIterator.hasNext()) {
                final Cell cell = cellIterator.next();
                currRowValues.add(formatter.formatCellValue(cell));
            }
        }

        return result;
    }
}
