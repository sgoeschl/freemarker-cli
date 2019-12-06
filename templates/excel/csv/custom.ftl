<#ftl output_format="plainText" strip_text="true">
<#--
  Licensed to the Apache Software Foundation (ASF) under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The ASF licenses this file
  to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
-->
<#assign format = SystemTool.properties["csv.format"]!"DEFAULT">
<#assign salt = SystemTool.properties["salt"]!"salt">
<#-- Parse the first document & sheet of the Excel document -->
<#assign workbook = ExcelTool.parse(documents[0])>
<#assign sheet = ExcelTool.getSheets(workbook)[0]>
<#assign records = ExcelTool.parseSheet(sheet)>
<#-- Setup CSVPrinter  -->
<#assign cvsFormat = CSVTool.formats[format]>
<#assign csvPrinter = CSVTool.printer(cvsFormat)>
<#--
    Transform an arbitrary Excel file and add addition columns using Commons CSV
    We are using an instance of CSVPrinter directly have proper quoting of the output
    which in turn uses the underlying java.io.Writer.
-->
<#compress>
    <#list records as record>
        <#if record?is_first>
        <#-- Updated header line -->
            <#assign line = "Line">
            <#assign uuid = "UUID">
            <#assign quoted = "Quoted Text">
        <#else>
        <#-- Updated rows -->
            <#assign line = record?counter-1>
            <#assign uuid = uuidFromValueAndSalt(text, salt)>
            <#assign quoted = ",;">
        </#if>
        <#assign text = record[1]>
        <#assign date = record[2]>
        ${csvPrinter.printRecord(line, text, date, uuid, quoted)}
    </#list>
</#compress>
<#--------------------------------------------------------------------------->
<#function uuidFromValueAndSalt value salt>
    <#assign uuidSource = value + salt>
    <#assign buffer = FreeMarkerTool.statics["java.nio.charset.Charset"].forName("UTF-8").encode(uuidSource).rewind()>
    <#assign bytes = buffer.array()[0..<buffer.limit()]>
    <#return FreeMarkerTool.statics["java.util.UUID"].nameUUIDFromBytes(bytes)>
</#function>