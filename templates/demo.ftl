<#ftl output_format="plainText" >

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

7) Access System Properties
---------------------------------------------------------------------------
app.dir      : ${SystemProperties["app.dir"]!""}
app.home     : ${SystemProperties["app.home"]!""}
app.pid      : ${SystemProperties["app.pid"]!""}
basedir      : ${SystemProperties["basedir"]!""}
java.version : ${SystemProperties["java.version"]!""}
user.name    : ${SystemProperties["user.name"]!""}
user.dir     : ${SystemProperties["user.dir"]!""}
user.home    : ${SystemProperties["user.home"]!""}

8) Report Data
---------------------------------------------------------------------------
description  : ${ReportData["description"]}
host         : ${ReportData["host"]}
user         : ${ReportData["user"]}
date         : ${ReportData["date"]}
time         : ${ReportData["time"]}

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
<#list Documents.findByName("README") as document>
    - ${document.name}
</#list>
List all files having "md" extension
<#list Documents.findByExtension("md") as document>
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

<#--------------------------------------------------------------------------->
<#function uuidFromValueAndSalt value salt>
    <#assign uuidSource = value + salt>
    <#assign buffer = Statics["java.nio.charset.Charset"].forName("UTF-8").encode(uuidSource).rewind()>
    <#assign bytes = buffer.array()[0..<buffer.limit()]>
    <#return Statics["java.util.UUID"].nameUUIDFromBytes(bytes)>
</#function>