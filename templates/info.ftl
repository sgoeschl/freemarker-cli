<#ftl output_format="plainText" strip_whitespace=true>
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
FreeMarker Information
---------------------------------------------------------------------------
FreeMarker version     : ${.version}
Template name          : ${.current_template_name}
Language               : ${.lang}
Locale                 : ${.locale}
Timestamp              : ${.now}
Output encoding        : ${.output_encoding!"not set"}
Output format          : ${.output_format}

<#if documents?has_content>
Documents
---------------------------------------------------------------------------
<#list documents as document>
[${document?counter}] ${document.name}, ${document.location}, ${document.length} Bytes
</#list>
</#if>

User Supplied Properties
---------------------------------------------------------------------------
<#list SystemTool.getSettings().getProperties() as name,value>
- ${name} ==> ${value}
</#list>

Template Directories
---------------------------------------------------------------------------
<#list SystemTool.getSettings().getTemplateDirectories() as directory>
[${directory?counter}] ${directory}
</#list>

SystemTool
---------------------------------------------------------------------------
Host name       : ${SystemTool.getHostName()}
User name       : ${SystemTool.getProperty("user.name", "N.A.")}
Command line    : ${SystemTool.getSettings().getArgs()?join(", ")}

FreeMarker Document Model
---------------------------------------------------------------------------
<#list .data_model?keys?sort as key>
- ${key}
</#list>