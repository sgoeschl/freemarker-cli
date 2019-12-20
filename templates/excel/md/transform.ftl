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
<#assign document = Documents.get(0)>
<#assign documentName = document.name>
<#assign workbook = ExcelTool.parse(document)>
<#assign date = .now?iso_utc>

<#compress>
    # ${documentName}
    <@writeSheets workbook/>
</#compress>
${'\n'}

<#--------------------------------------------------------------------------->
<#-- writeSheets                                                           -->
<#--------------------------------------------------------------------------->
<#macro writeSheets workbook>
    <#assign sheets = ExcelTool.getSheets(workbook)>
    <#list sheets as sheet>
        <@writeSheet sheet/>
    </#list>
</#macro>

<#--------------------------------------------------------------------------->
<#-- writeSheet                                                            -->
<#--------------------------------------------------------------------------->
<#macro writeSheet sheet>
    <#assign rows = ExcelTool.toTable(sheet)>
    ## ${sheet.getSheetName()}
    ${'\n'}
    <@writeRows rows/>
</#macro>

<#--------------------------------------------------------------------------->
<#-- writeRows                                                             -->
<#--------------------------------------------------------------------------->
<#macro writeRows rows>
    <#list rows as row>
        <#if row?is_first>
            <@writeHeaderRow row/>
        <#else>
            <@writeDataRow row/>
        </#if>
    </#list>
</#macro>

<#--------------------------------------------------------------------------->
<#-- writeHeaderRow                                                        -->
<#--------------------------------------------------------------------------->
<#macro writeHeaderRow headers>
    | ${headers?join(" | ", "", " |")}
    <#list headers as header>| -------- </#list>|
</#macro>

<#--------------------------------------------------------------------------->
<#-- writeDataRow                                                          -->
<#--------------------------------------------------------------------------->
<#macro writeDataRow columns>
    | ${columns?join(" | ", "", " |")}
</#macro>
