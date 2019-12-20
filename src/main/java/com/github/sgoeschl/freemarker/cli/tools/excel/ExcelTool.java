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
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK;

/**
 * Parse Excel documents (XLS &amp; XLSX) using Apache POI.
 */
public class ExcelTool {

    private static final boolean EMULATE_CSV = true;
    private static final SimpleDateFormat TIME_DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat YEAR_TIME_FORMAT = new SimpleDateFormat("yyyy");

    public Workbook parse(Document document) {
        try (InputStream is = document.getUnsafeInputStream()) {
            final Workbook workbook = WorkbookFactory.create(is);
            // make sure that the workbook is closed together with the document
            return document.addClosable(workbook);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Ecxel document: " + document, e);
        }
    }

    /**
     * Get all sheets of a workbook.
     *
     * @param workbook The workbook
     * @return Sheets of the workbook
     */
    public List<Sheet> getSheets(Workbook workbook) {
        final List<Sheet> result = new ArrayList<>();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            result.add(workbook.getSheetAt(i));
        }
        return result;
    }

    /**
     * Transform the sheet to table. Please not that locales are mostly
     * ignored by Apache POI (see https://poi.apache.org/apidocs/dev/org/apache/poi/ss/usermodel/DataFormatter.html)
     *
     * @param sheet Excel sheet
     * @return Table containing formatted cell values as strings
     */
    public List<List<Object>> toTable(Sheet sheet) {
        final DataFormatter dataFormatter = dataFormatter();
        final Iterator<Row> iterator = sheet.iterator();
        final List<List<Object>> result = new ArrayList<>();

        while (iterator.hasNext()) {
            final Row row = iterator.next();
            result.add(toColumns(row, dataFormatter));
        }

        return result;
    }

    /**
     * Transform the sheet to table contaning raw Java objects, e.g. Date, Double, ...
     *
     * @param sheet Excel sheet
     * @return Table containing cells as raw Java objects
     */
    public List<List<Object>> toRawTable(Sheet sheet) {
        final DataFormatter dataFormatter = dataFormatter();
        final Iterator<Row> iterator = sheet.iterator();
        final List<List<Object>> result = new ArrayList<>();

        while (iterator.hasNext()) {
            final Row row = iterator.next();
            result.add(toRawColumns(row, dataFormatter));
        }

        return result;
    }

    private static List<Object> toColumns(Row row, DataFormatter dataFormatter) {
        final List<Object> columnValues = new ArrayList<>();
        for (int columnIndex = 0; columnIndex < row.getLastCellNum(); columnIndex++) {
            final Cell cell = row.getCell(columnIndex, CREATE_NULL_AS_BLANK);
            final String formatedCellValue = dataFormatter.formatCellValue(cell).trim();
            columnValues.add(formatedCellValue);
        }
        return columnValues;
    }

    private static List<Object> toRawColumns(Row row, DataFormatter dataFormatter) {
        final List<Object> columnValues = new ArrayList<>();
        for (int columnIndex = 0; columnIndex < row.getLastCellNum(); columnIndex++) {
            final Cell cell = row.getCell(columnIndex, CREATE_NULL_AS_BLANK);
            final Object cellValue = toCellValue(cell, dataFormatter);
            columnValues.add(cellValue);
        }
        return columnValues;
    }

    private static Object toCellValue(Cell cell, DataFormatter formatter) {
        final CellType cellType = cell.getCellType();
        switch (cellType) {
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case NUMERIC:
                return DateUtil.isCellDateFormatted(cell) ? toDateCellValue(cell) : cell.getNumericCellValue();
            default:
                return formatter.formatCellValue(cell);
        }
    }

    /**
     * Try desperately to make sense out of Excel and its handling of dates.
     * See https://stackoverflow.com/questions/15710888/reading-time-values-from-spreadsheet-using-poi-api
     *
     * @param cell Cell containing some sort of date or time
     * @return The corresponding Java istance
     */
    private static Object toDateCellValue(Cell cell) {
        final Date date = cell.getDateCellValue();

        // Get a date year
        // "Time-only" values have date set to 31-Dec-1899 so if year is "1899"
        // you can assume it is a "time-only" value
        final String dateStamp = YEAR_TIME_FORMAT.format(date);

        if (dateStamp.equals("1899")) {
            // Return "Time-only" value as String HH:mm:ss
            return TIME_DATE_FORMAT.format(date);
        } else {
            //here you may have a date-only or date-time value

            //get time as String HH:mm:ss
            final String timeStamp = TIME_DATE_FORMAT.format(date);

            if (timeStamp.equals("00:00:00")) {
                // if time is 00:00:00 you can assume it is a date only value (but it could be midnight)
                // In this case I'm fine with the default Cell.toString method (returning dd-MMM-yyyy in case of a date value)
                return cell.getDateCellValue();
            } else {
                // return date-time value as "dd-MMM-yyyy HH:mm:ss"
                return cell.toString() + " " + timeStamp;
            }
        }
    }

    private static DataFormatter dataFormatter() {
        return new DataFormatter(EMULATE_CSV);
    }
}
