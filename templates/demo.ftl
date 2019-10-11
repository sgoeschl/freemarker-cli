<#ftl output_format="plainText" >
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
1) FreeMarker Special Variables
---------------------------------------------------------------------------

FreeMarker version     : ${.version}
Template name          : ${.current_template_name}
Language               : ${.lang}
Locale                 : ${.locale}
Timestamp              : ${.now}
Output encoding        : ${.output_encoding!"not set"}
Output format          : ${.output_format}

2) Invoke a constructor of a Java class
---------------------------------------------------------------------------
<#assign date = ObjectConstructor("java.util.Date", 1000 * 3600 * 24)>
new java.utilDate(1000 * 3600 * 24): ${date?datetime}

3) Invoke a static method of an non-constructor class
---------------------------------------------------------------------------
System.currentTimeMillis: ${Statics["java.lang.System"].currentTimeMillis()}

4) Access an Enumeration
---------------------------------------------------------------------------
java.math.RoundingMode#UP: ${Enums["java.math.RoundingMode"].UP}

5) Loop Over The Values Of An Enumeration
---------------------------------------------------------------------------
<#list Enums["java.math.RoundingMode"]?values as roundingMode>
* java.math.RoundingMode.${roundingMode}
</#list>

6) Display list of input files
---------------------------------------------------------------------------
List all files:
<#list documents as document>
- Document: name=${document.name} location=${document.location} length=${document.length} encoding=${document.encoding!""}
</#list>

7) SystemTool
---------------------------------------------------------------------------
Host name       : ${SystemTool.getHostName()}
Command line    : ${SystemTool.getSettings().getArgs()?join(", ")}
User name       : ${SystemTool.getProperty("user.name", "N.A.")}
Timestamp       : ${SystemTool.currentTimeMillis()?c}
Environment     : ${SystemTool.getEnvironment("foo", SystemTool.getProperty("foo", "N.A."))}

8) Access System Properties
---------------------------------------------------------------------------
app.dir      : ${SystemProperties["app.dir"]!""}
app.home     : ${SystemProperties["app.home"]!""}
app.pid      : ${SystemProperties["app.pid"]!""}
basedir      : ${SystemProperties["basedir"]!""}
java.version : ${SystemProperties["java.version"]!""}
user.name    : ${SystemProperties["user.name"]!""}
user.dir     : ${SystemProperties["user.dir"]!""}
user.home    : ${SystemProperties["user.home"]!""}

9) Environment Variables
---------------------------------------------------------------------------
<#list Environment as name,value>
* ${name} ==> ${value}
</#list>

10) Accessing Documents
---------------------------------------------------------------------------
Get the number of documents:
    - ${Documents.size()}
<#if !Documents.isEmpty()>
Get the first document
    - ${Documents.get(0)!"NA"}
</#if>
List all files containing "README" in the name
<#list Documents.find("*README*") as document>
    - ${document.name}
</#list>
List all files having "md" extension
<#list Documents.find("*.md") as document>
    - ${document.name}
</#list>
Get all documents
<#list Documents.getAll() as document>
    - ${document.name} => ${document.location}
</#list>

11) Document Data Model
---------------------------------------------------------------------------

Top-level entries in the current data model

<#list .data_model?keys as key>
- ${key}
</#list>

12) Create a UUID
---------------------------------------------------------------------------

See https://stackoverflow.com/questions/43501297/i-have-a-simplescalar-i-need-its-strings-getbytes-return-value-what-can-i-d

<#assign uuidSource = "value and salt">
<#assign buffer = Statics["java.nio.charset.Charset"].forName("UTF-8").encode(uuidSource).rewind()>
<#assign bytes = buffer.array()[0..<buffer.limit()]>
<#assign uuid = Statics["java.util.UUID"].nameUUIDFromBytes(bytes)>
Random UUID           : ${Statics["java.util.UUID"].randomUUID()}
Name UUID from bytes  : ${uuid}
Name UUID as function : ${uuidFromValueAndSalt("value and ", "salt")}

13) Printing Special Characters
---------------------------------------------------------------------------

German Special Characters: äöüßÄÖÜ

14) Locale-specific output
---------------------------------------------------------------------------
<#setting number_format=",##0.00">
<#assign smallNumber = 1.234>
<#assign largeNumber = 12345678.9>

Small Number :  ${smallNumber}
Large Number :  ${largeNumber}
Currency     :  ${largeNumber} EUR
Date         :  ${.now?date}
Time         :  ${.now?time}

<#--------------------------------------------------------------------------->
<#function uuidFromValueAndSalt value salt>
    <#assign uuidSource = value + salt>
    <#assign buffer = Statics["java.nio.charset.Charset"].forName("UTF-8").encode(uuidSource).rewind()>
    <#assign bytes = buffer.array()[0..<buffer.limit()]>
    <#return Statics["java.util.UUID"].nameUUIDFromBytes(bytes)>
</#function>