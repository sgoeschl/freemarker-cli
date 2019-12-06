<#ftl output_format="HTML" >
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
<#assign cvsFormat = CSVTool.formats["DEFAULT"].withHeader()>
<#assign csvParser = CSVTool.parse(documents[0], cvsFormat)>
<#assign csvHeaders = csvParser.getHeaderNames()>
<#--------------------------------------------------------------------------->
<!DOCTYPE html>
<html>
<head>
    <title>${name}</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
</head>
<body>
<table class="table table-striped">
    <@writeHeaders csvParser.getHeaderNames()/>
    <#list csvParser.iterator() as record>
        <@writeColumns record/>
    </#list>
</table>
</body>
</html>
<#--------------------------------------------------------------------------->
<#macro writeHeaders headers>
    <tr>
        <#list headers as header>
            <th>${header}</th>
        </#list>
    </tr>
</#macro>
<#--------------------------------------------------------------------------->
<#macro writeColumns record>
    <tr>
        <#list record.iterator() as field>
            <th>${field}</th>
        </#list>
    </tr>
</#macro>