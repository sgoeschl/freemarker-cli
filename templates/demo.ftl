1) Language-specific Date Format
---------------------------------------------------------------------------
Report generated at ${.now}

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
user.name    : ${SystemProperties["user.name"]!""}
user.dir     : ${SystemProperties["user.dir"]!""}
user.home    : ${SystemProperties["user.home"]!""}
java.version : ${SystemProperties["java.version"]!""}

8) Report Data
---------------------------------------------------------------------------
description  : ${ReportData["description"]}
host         : ${ReportData["host"]}
user         : ${ReportData["user"]}
date         : ${ReportData["date"]}
time         : ${ReportData["time"]}

9) Environment
---------------------------------------------------------------------------
<#list Environment as name,value>
* ${name} ==> ${value}
</#list>

10) Documents
---------------------------------------------------------------------------
Get the number of documents:
    - ${Documents.size()}
Get the first document
    - ${Documents.get(0)}
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

11) Data Model
---------------------------------------------------------------------------

List a entries in the current data model

<#list .data_model?keys as key>
- ${key}
</#list>
