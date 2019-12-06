<#ftl output_format="XML" >
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
<#assign name = documents[0].name>
<#assign csvFormatName = SystemProperties["csv.format"]!"DEFAULT">
<#assign cvsFormat = CSVTool.formats[csvFormatName].withHeader()>
<#assign csvParser = CSVTool.parse(documents[0], cvsFormat)>
<#assign csvHeaders = csvParser.getHeaderMap()?keys>
<#assign csvRecords = csvParser.records>
<#--------------------------------------------------------------------------->
<?xml version="1.0" encoding="UTF-8"?>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <fo:layout-master-set>
        <fo:simple-page-master master-name="first"
                               page-height="21cm"
                               page-width="29.7cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="1cm"
                               margin-right="1cm">
            <fo:region-body margin-top="1cm"/>
            <fo:region-before extent="1cm"/>
            <fo:region-after extent="1cm"/>
        </fo:simple-page-master>
    </fo:layout-master-set>
    <fo:page-sequence master-reference="first">
        <fo:flow flow-name="xsl-region-body">
            <fo:table table-layout="fixed" width="100%" border-collapse="separate">
                <@writeTableHeader headers=csvHeaders/>
                <@writeTableBody columns=csvRecords/>
            </fo:table>
        </fo:flow>
    </fo:page-sequence>
</fo:root>

<#--------------------------------------------------------------------------->
<#macro writeTableHeader headers>
    <fo:table-header>
        <fo:table-row>
            <#list headers as header>
                <fo:table-cell>
                    <fo:block font-weight="bold">${header}</fo:block>
                </fo:table-cell>
            </#list>
        </fo:table-row>
    </fo:table-header>
</#macro>

<#--------------------------------------------------------------------------->
<#macro writeTableBody columns>
    <fo:table-body>
        <#list columns as column>
            <fo:table-row>
                <#list column.iterator() as field>
                    <fo:table-cell>
                        <fo:block>${field}</fo:block>
                    </fo:table-cell>
                </#list>
            </fo:table-row>
        </#list>
    </fo:table-body>
</#macro>
