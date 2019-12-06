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
<#assign cvsFormat = CSVTool.formats["DEFAULT"].withDelimiter(';')>
<#assign csvParser = CSVTool.parse(document, cvsFormat)>
<#assign csvRecords = csvParser.records>

<#compress>
disposer,user,password,token,device,name,description
<#list csvRecords as record>
    <#assign user = record.get(0)>
    <#assign token = record.get(1)>
    ${user},${user},,${token},,${user},HU performance test user ${user}
</#list>
</#compress>
