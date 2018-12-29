[![Build Status](https://travis-ci.org/sgoeschl/freemarker-cli.svg?branch=master)](https://travis-ci.org/sgoeschl/freemarker-cli)

# 1. Why 'freemarker-cli' Is For You

You somehow found this GitHub project and wonder if it solves a problem you might have?!

* You need to transform some structured text document (CSV, JSON, XML, Java Property file) into CSV, HTML, Markdown or Confluence markup 
* You need to convert an Excel document into CSV, HTML or Markdown
* You need to create a nice-looking PDF from some boring JSON content 

The goal of `freemarker-cli` is to automate repeated transformation tasks 

* Which are too boring to be done manually 
* Which happen not often enough to write a script or program

# 2. How It Started

For a customer I needed a little bit of test data management - to make a long story short (after writing a few scripts) it boiled down to transforming one or more JSON files to something human readable.

What are the options

* The cool kids say 'Node.js' - but they always say 'Node.js' 
* Some fancy Groovy scripts using markup builder - but the Groovy syntax looks a bit odd
* Using 'JsonPath' and 'Velocity' to reuse good & old stuff

So I went with 'Apache Groovy', 'JsonPath' and 'Apache Velocity'

* Playing with Groovy over the public holidays
* Groovy has a built-in package manager which makes distribution a breeze
* Provding samples to transform JSON to Markdown

Using Velocity actually created some minor issues so I migrated to Apache FreeMarker during Christmas 2016

* Velocity 1.7 was released 2010
* I was painful to get Velocity Tools working 
* Velocity XML processing support is also painful
* Spring 4.3 deprecated velocity support which could affect me in the long run
* Freemarker has no additional dependencies and things are just working

While I love Apache Velocity (Apache Turbine anyone?) I decided to give FreeMarker a chance and migrated my [velocity-cli](https://github.com/sgoeschl/velocity-cli) to FreeMarker.

# 3. Design Goals

* Support multiple files/directories for a single transformation
* Support transformation of Property files using plain-vanilla JDK
* Support transformation of CSV files using [Apache Commons CSV](https://commons.apache.org/proper/commons-csv/)
* Support transformation of JSON using [Jayway's JSONPath](https://github.com/jayway/JsonPath)
* Support transformation of Excel using [Apache POI](https://poi.apache.org)
* XML is supported by FreeMarker anyway - see http://freemarker.org/docs/xgui.html
* Support for reading document content from STDIN to integrate with command line tools
* Add some commonly useful information such as `System Properties`, `Enviroment Variables`

# 4. Usage

```text
> groovy freemarker-cli.groovy
usage: groovy freemarker-cli.groovy [options] file[s]
 -b,--basedir <arg>       Base directory to resolve FreeMarker templates
 -d,--description <arg>   Custom report description
 -h,--help                Usage information
 -i,--include             Ant file pattern for directory search
 -l,--locale <arg>        Locale value
 -o,--output <arg>        Generated output file
 -t,--template <arg>      Template name
 -v,--verbose             Verbose mode
```

# 5. Examples

The examples were tested with Groovy 2.5.4 on Mac OS X so please upgrade your Groovy version if you have problems.

```text
> groovy -v
Groovy Version: 2.5.4 JVM: 1.8.0_192 Vendor: Oracle Corporation OS: Mac OS X
```

## 5.1 Transforming GitHub JSON To Markdown

A simple example with real JSON data

### Invocation

You can either use the existing JSON sample

> groovy freemarker-cli.groovy -t templates/json/md/github-users.ftl site/sample/json/github-users.json

or pipe a cURL response

> curl -s https://api.github.com/users | groovy freemarker-cli.groovy -t templates/json/md/github-users.ftl

### FreeMarker Template

```text
<#ftl output_format="plainText" >
<#assign json = JsonPath.parse(documents[0].content)>
<#assign users = json.read("$[*]")>
<#--------------------------------------------------------------------------->
# GitHub Users

Report generated at ${.now?iso_utc}

<#compress>
<#list users as user>
<#assign userAvatarUrl = user.avatar_url>
<#assign userHomeUrl = user.html_url>
# ${user.login}

| User                                                    | Homepage                                      |
|:--------------------------------------------------------|:----------------------------------------------|
| <img src="${user.avatar_url}" width="48" height="48"/> | [${userHomeUrl}](${userHomeUrl})               |
</#list>
</#compress>
```

creates the following output

![](./site/image/github.png)

## 5.2 Markdown Test User Documentation

For a customer I created a Groovy script to fetch all products for a list of users - the script generates a JSON file which can be easily transformed to Markdown

```text
> groovy freemarker-cli.groovy -t templates/json/md/customer-user-products.ftl  site/sample/json/customer-user-products.json
```

The resulting file can be viewed with any decent Markdown viewer

![Customer User Products Markdown](./site/image/customer-user-products-md.png "Customer User Products Markdown")

Since many of our QA people have no Markdown viewer installed I also created a very similar HTML representaton

> groovy freemarker-cli.groovy -t templates/json/html/customer-user-products.ftl  site/sample/json/customer-user-products.json

![Customer User Products HTML](./site/image/customer-user-products-html.png "Customer User Products HTML")

As added bonus we can easily transform the CSV to PDF using [wkhtmltopdf](https://wkhtmltopdf.org) `wkhtmltopdf`

```text
freemarker-cli> wkhtmltopdf ./site/sample/customer-user-products.html customer-user-products.pdf
Loading pages (1/6)
Counting pages (2/6)                                               
Resolving links (4/6)                                                       
Loading headers and footers (5/6)                                           
Printing pages (6/6)
Done  
```

which creates the following PDF document

![Customer User Products PDF](./site/image/customer-user-products-pdf.png "Customer User Products PDF")

## 5.3 CSV to HTML/Markdown Transformation

Sometimes you have a CSV file which needs to be translated in Markdown or HTML - there are on-line solutions available such as [CSV To Markdown Table Generator](https://donatstudios.com/CsvToMarkdownTable) but having a local solution gives you more flexibility.

```text
> groovy freemarker-cli.groovy -t templates/csv/md/transform.ftl site/sample/csv/contract.csv
> groovy freemarker-cli.groovy -t templates/csv/html/transform.ftl site/sample/csv/contract.csv
```

The FreeMarker template is shown below

```text
<#ftl output_format="HTML" >
<#assign name = documents[0].name>
<#assign content = documents[0].content>
<#assign cvsFormat = CSVFormat.DEFAULT.withHeader()>
<#assign csvParser = CSVParser.parse(content, cvsFormat)>
<#assign csvHeaders = csvParser.getHeaderMap()?keys>
<#assign csvRecords = csvParser.records>
<#--------------------------------------------------------------------------->
<!DOCTYPE html>
<html>
<head>
    <title>${name}</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
</head>
<body>
<table class="table table-striped">
    <@writeHeaders headers=csvHeaders/>
    <@writeColums columns=csvRecords/>
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
<#macro writeColums columns>
    <#list columns as column>
    <tr>
    <#list column.iterator() as field>
        <td>${field}</td>
    </#list>
    </tr>
    </#list>
</#macro>

```

The resulting file actually looks pleasant when compared to raw CSV

![Contract CSV to HTML](./site/image/contract.png "Contract CSV to HTML")

## 5.4 Transform XML To Plain Text

Of course you can also transform a XML document

```text
> groovy freemarker-cli.groovy -t ./templates/xml/txt/recipients.ftl site/sample/xml/recipients.xml
```

using the following template

```text
<#ftl output_format="plainText" >
<#assign xml = XmlParser.parse(documents[0].content)>
<#list xml.recipients.person as recipient>
To: ${recipient.name}
${recipient.address}

Dear ${recipient.name},

Thank you for your interest in our products. We will be sending you a catalog shortly.
To take advantage of our free gift offer, please fill in the survey attached to this
letter and return it to the address on the reverse. Only one participant is allowed for
each household.

Sincere salutations,


D. H.

---------------------------------------------------------------------------------------
</#list>

```

which generates the following output

```text
To: John Smith
3033 Long Drive, Houston, TX

Dear John Smith,

Thank you for your interest in our products. We will be sending you a catalog shortly.
To take advantage of our free gift offer, please fill in the survey attached to this
letter and return it to the address on the reverse. Only one participant is allowed for
each household.

Sincere salutations,


D. H.
```

## 5.5 Transform JSON To CSV

One day I was asked a to prepare a CSV files containind REST endpoints described by Swagger - technically this is a JSON to CSV transformation. Of course I could create that CSV manually but writing a FTL template doing that was simply more fun and might save some time in the future

```text
<#ftl output_format="plainText">
<#assign json = JsonPath.parse(documents[0].content)>
<#assign paths = json.read("$.paths")>
ENDPOINT,METHOD
<#list paths as url,entry>
<#assign http_method = entry?keys[0]>
${url},${http_method?upper_case}
</#list>
```

Invoking the FTL template

> groovy freemarker-cli.groovy -t templates/json/csv/swagger-endpoints.ftl site/sample/json/swagger-spec.json 

gives you

```text
ENDPOINT,METHOD
/pets,GET
/pets/{id},GET
```

## 5.6 Transforming Excel Documents

Apache POI supports XLS and XLSX documents. 

```text
> groovy freemarker-cli.groovy -t templates/excel/html/test.ftl site/sample/excel/test.xls
> groovy freemarker-cli.groovy -t templates/excel/html/test.ftl site/sample/excel/test.xlsx
```

The provided FTL transforms a known Excel document structure into a HTML document and is not sophisticated

```text
<#ftl output_format="HTML" >
<#assign sourceDocumentName = documents[0].name>
<#assign workbook = ExcelParser.parseFile(documents[0].file)>
<#assign date =  ReportData["date"]>
<#--------------------------------------------------------------------------->
<!DOCTYPE html>
<html>
<head>
    <title>${sourceDocumentName}</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
</head>
<body>
<div class="container-fluid">
    <h1>Excel Test <small>${sourceDocumentName}, ${date}</small></h1>
    <@writeSheet sheet=workbook.getSheetAt(0)/>
</div>
</body>
</html>
<#--------------------------------------------------------------------------->
<#-- writeSheet                                                            -->
<#--------------------------------------------------------------------------->
<#macro writeSheet sheet>
    <#assign rows = ExcelParser.parseSheet(sheet)>
    <h2>${sheet.getSheetName()}</h2>
    <@writeRows rows=rows/>
</#macro>
<#--------------------------------------------------------------------------->
<#-- writeRow                                                              -->
<#--------------------------------------------------------------------------->
<#macro writeRows rows>
<table class="table table-striped">
    <#list rows as row>
        <#if row?is_first>
            <tr>
                <th>#</th>
                <th>${row[0]}</th>
                <th>${row[1]}</th>
                <th>${row[2]}</th>
                <th>${row[3]}</th>
                <th>${row[4]}</th>
                <th>${row[5]}</th>
                <th>${row[6]}</th>
            </tr>
        <#else>
            <tr>
                <td>${row?index}</td>
                <td>${row[0]}</td>
                <td>${row[1]}</td>
                <td>${row[2]}</td>
                <td>${row[3]}</td>
                <td>${row[4]}</td>
                <td>${row[5]}</td>
                <td>${row[6]}</td>
            </tr>
        </#if>
    </#list>
</table>
</#macro>
```

but the result looks reasonable

![](./site/image/excel-to-html.png)

## 5.7 Transform Property Files To CSV

In this sample we transform all property files found in a directory (recursive search using include pattern) to a CSV file

```text
> groovy freemarker-cli.groovy -i **/*.properties -t templates/properties/csv/locker-test-users.ftl site/sample/properties
TENANT,SITE,USER_ID,DISPOSER_ID,PASSWORD,SMS_OTP,NAME,DESCRIPTION
ro,fat,01303494,01303494,01303494,,,
at,fat,205089760,205089760,205089760,,,
sk,uat,9200021464,9200021464,9200021464,,,
cz,fat,9422350309,9422350309,9422350309,000000,,
```

The FTL uses a couple of interesting features

* We process a list of property files
* The `strip_text` and `compress` strips any whitespaces and linebreaks from the output so we can create a proper CSV file
* We use FTL functions to extract the `tenant` and `site`, e.g. `extractTenant`
* We add a manual line break using ```${'\n'}``

```text
<#ftl output_format="plainText" strip_text="true">
<#compress>
    TENANT,SITE,USER_ID,DISPOSER_ID,PASSWORD,SMS_OTP,NAME,DESCRIPTION
    <#list documents as document>
        <#assign properties = PropertiesParser.parse(document.content)>
        <#assign environments = properties["ENVIRONMENTS"]!"">
        <#assign tenant = extractTenant(environments)>
        <#assign site = extractSite(environments)>
        <#assign userId = properties["USER_ID"]!"">
        <#assign disposerId = properties["USER_ID"]!"">
        <#assign password = properties["PASSWORD"]!"">
        <#assign smsOtp = properties["SMS_OTP"]!"">
        <#assign name = properties["NAME"]!"">
        <#assign description = properties["NAME"]!"">
        ${tenant},${site},${userId},${disposerId},${password},${smsOtp},${name},${description}
    </#list>
</#compress>
${'\n'}

<#function extractSite environments>
    <#if (environments)?contains("_DEV")>
        <#return "dev">
    <#elseif (environments)?contains("_FAT")>
        <#return "fat">
    <#elseif (environments)?contains("_ST")>
        <#return "st">
    <#elseif (environments)?contains("_PROD")>
        <#return "prod">
    <#elseif (environments)?contains("_UAT")>
        <#return "uat">
    <#else>
        <#return "???">
    </#if>
</#function>

<#function extractTenant environments>
    <#if (environments)?contains("AT_")>
        <#return "at">
    <#elseif (environments)?contains("BCR_")>
        <#return "ro">
    <#elseif (environments)?contains("CSAS_")>
        <#return "cz">
    <#elseif (environments)?contains("SK_")>
        <#return "sk">
    <#else>
        <#return "???">
    </#if>
</#function>

${'\n'}
```

## 5.8 Using Advanced FreeMarker Features

There is a `demo.ftl` which shows some advanced FreeMarker functionality

* Invoking a Java constructor
* Invoke a static method of non-instantiable class
* Work with Java enums
* Access System properties
* Access Environment variables

```text
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
Document: name=${document.name} file=${document.file.getAbsolutePath()} length=${document.length} isFile=${document.isFile()?c}
</#list>

7) Access System Properties
---------------------------------------------------------------------------
user.name    : ${SystemProperties["user.name"]}
user.dir     : ${SystemProperties["user.dir"]}
user.home    : ${SystemProperties["user.home"]}
java.version : ${SystemProperties["java.version"]}
|
7) Report Data
---------------------------------------------------------------------------
description  : ${ReportData["description"]}
host         : ${ReportData["host"]}
user         : ${ReportData["user"]}

9) Environment
---------------------------------------------------------------------------
<#list Environment as name,value>
* ${name} ==> ${value}
</#list>
```

Running the command

> groovy freemarker-cli.groovy -d "This is a description" -t ./templates/demo.ftl README.md

generated the following output

```text
1) Language-specific Date Format
---------------------------------------------------------------------------
Report generated at Dec 29, 2018 8:35:10 PM

2) Invoke a constructor of a Java class
---------------------------------------------------------------------------
new java.utilDate(1000 * 3600 * 24): Jan 2, 1970 1:00:00 AM

3) Invoke a static method of an non-constructor class
---------------------------------------------------------------------------
System.currentTimeMillis: 1,546,112,110,295

4) Access an Enumeration
---------------------------------------------------------------------------
java.math.RoundingMode#UP: UP

5) Loop Over The Values Of An Enumeration
---------------------------------------------------------------------------
* java.math.RoundingMode.UP
* java.math.RoundingMode.DOWN
* java.math.RoundingMode.CEILING
* java.math.RoundingMode.FLOOR
* java.math.RoundingMode.HALF_UP
* java.math.RoundingMode.HALF_DOWN
* java.math.RoundingMode.HALF_EVEN
* java.math.RoundingMode.UNNECESSARY

6) Display input files
---------------------------------------------------------------------------
Document: name=README.md file=/Users/sgoeschl/work/github/sgoeschl/freemarker-cli/README.md length=19,502 hasFile=true

7) Access System Properties
---------------------------------------------------------------------------
user.name    : sgoeschl
user.dir     : /Users/sgoeschl/work/github/sgoeschl/freemarker-cli
user.home    : /Users/sgoeschl
java.version : 1.8.0_192

8) Report Data
---------------------------------------------------------------------------
description  : 
host         : murderbot.local
user         : sgoeschl
date         : 2018-12-29

9) Environment
---------------------------------------------------------------------------
* PATH ==> /Users/sgoeschl/bin:/Library/Java/JavaVirtualMachines/jdk1.8.0_192.jdk/Contents/Home/bin:/usr/local/Cellar/ruby/2.5.3//bin:/usr/local/Cellar/git/2.19.1/bin:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin
* GIT_HOME ==> /usr/local/Cellar/git/2.19.1
* JAVA_HOME ==> /Library/Java/JavaVirtualMachines/jdk1.8.0_192.jdk/Contents/Home
* TERM ==> xterm-256color
* LANG ==> en_US
* DISPLAY ==> :0.0
* JAVA_MAIN_CLASS_32553 ==> org.codehaus.groovy.tools.GroovyStarter
* LOGNAME ==> sgoeschl
* XPC_SERVICE_NAME ==> 0
* PWD ==> /Users/sgoeschl/work/github/sgoeschl/freemarker-cli
* TERM_PROGRAM_VERSION ==> 421.1
* JAVA_1_8_HOME ==> /Library/Java/JavaVirtualMachines/jdk1.8.0_192.jdk/Contents/Home
* RUBY_HOME ==> /usr/local/Cellar/ruby/2.5.3/
* SHELL ==> /bin/bash
* PROFILE_TYPE ==> development
* TERM_PROGRAM ==> Apple_Terminal
* LSCOLORS ==> ExFxCxDxBxegedabagacad
* APP_ICON_32553 ==> /usr/local/Cellar/groovy/2.5.4/libexec/lib/groovy.icns
* PROFILE_ENV ==> default
* SECURITYSESSIONID ==> 186a8
* OLDPWD ==> /usr/local/Cellar/groovy/2.5.4/libexec
* USER ==> sgoeschl
* CLICOLOR ==> 1
* TMPDIR ==> /var/folders/cd/jbgc9cg14ld7dlsqk44tpmrw0000gn/T/
* APP_NAME_32553 ==> Groovy
* SSH_AUTH_SOCK ==> /private/tmp/com.apple.launchd.ymn0c6f7kR/Listeners
* EDITOR ==> vi
* XPC_FLAGS ==> 0x0
* TERM_SESSION_ID ==> CB91CF57-17A0-4623-96DD-6A2A3EB6D9CA
* LC_ALL ==> en_US.utf-8
* __CF_USER_TEXT_ENCODING ==> 0x1F5:0x0:0x0
* Apple_PubSub_Socket_Render ==> /private/tmp/com.apple.launchd.EWTvdMJuxl/Render
* LC_CTYPE ==> UTF-8
* HOME ==> /Users/sgoeschl
* SHLVL ==> 1
```

# 6. Design Considerations

## 6.1 How It Works

* The user-supplied files are loaded into memory or if there are no file the script reads the from `stdin`
* The FreeMarker data model containing the documents and helper object is created and passed tp the template
* The generated output is written to the user-supplied file or to `stdout`

## 6.2 FreeMarker Data Model

Within the script a FreeMarker data model is set up and passed to the template - it contains the documents to be processed and helper objects

| Helper                | Description                                                         |
|-----------------------|---------------------------------------------------------------------|
| CSVFormat             | Available CSV formats, e.g. "DEFAULT", "EXCEL"                      |
| CSVParser             | CSV parser exposing a `parse` method                                |
| Enums                 | Helper to work with Java enumerations                               |
| Environment           | Environment variables                                               |
| ExcelParser           | Excel parser exposing a `parse` method                              |
| JsonPath              | JSON Parser                                                         |
| ObjectConstructor     | Creata Java instances using reflection                              |
| PropertiesParser      | Properties parser exposing a `parse` method                         |
| ReportData            | Bean containing some convinience data, e.g. `user` and `host`       |
| Statics               | Invoke static Java methods using reflection                         |
| SystemProperties      | JVM System properties                                               |
| XmlParser             | XML parser exposing a `parse` method                                |

# 7. Tips & Tricks

## 7.1 Template Base Directory

When doing some ad-hoc scripting it is useful to rely on a base directory to resolve the FTL templates

* As a default the FTL templates are resolved relative to the script directory
* The caller can provide a `-b` or `--basedir` command line parameter

> groovy freemarker-cli/freemarker-cli.groovy -t templates/json/html/customer-user-products.ftl freemarker-cli/site/sample/json/customer-user-products.jso

## 7.2 Using Pipes

When doing ad-hoc scripting it useful to pipe the output of one command directly into the Groovy script

> cat site/sample/json/customer-user-products.json | groovy freemarker-cli.groovy -t ./templates/json/html/customer-user-products.ftl

## 7.3 Executable Groovy Scripts

When you run on Unix and are tired of always typing `groovy` there is light on the end of the tunnel - assuming that the `freemarker-cli.groovy` is executable you can use

> ./freemarker-cli.groovy -t templates/json/html/customer-user-products.ftl site/sample/json/customer-user-products.json 
