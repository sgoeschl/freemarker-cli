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

6) Display input files
---------------------------------------------------------------------------
<#list documents as document>
Document: name=${document.name} file=${document.file.getAbsolutePath()} length=${document.length} hasFile=${document.hasFile()?c}
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

9) Environment
---------------------------------------------------------------------------
<#list Environment as name,value>
* ${name} ==> ${value}
</#list>
