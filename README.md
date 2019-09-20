[![Build Status](https://travis-ci.org/sgoeschl/freemarker-cli.svg?branch=master)](https://travis-ci.org/sgoeschl/freemarker-cli)

# 1. Is This Project For You?

You somehow found this GitHub project and wonder if it solves a problem you might have?!

* You need to transform some structured text document (CSV, HTML, JSON, XML, Java Property file) into CSV, HTML, Markdown or Confluence markup 
* You need to convert an Excel document into CSV, HTML or Markdown
* You need to create a nice-looking PDF from some boring-looking CSV or JSON content 

The goal of `freemarker-cli` is to automate repeated transformation tasks 

* Which are too boring to be done manually 
* Which happen not often enough to write a dedicated script or program

Assuming that you are still interested - install [Apache Groovy](http://groovy-lang.org/install.html) and run `./run-samples.sh`

```text
templates/demo.ftl
templates/csv/html/transform.ftl
templates/csv/md/transform.ftl
templates/csv/fo/transform.ftl
fop -fo target/out/locker-test-users.fo target/out/locker-test-users.pdf
templates/csv/fo/transactions.ftl
fop -fo target/out/transactions.fo target/out/transactions-fo.pdf
templates/csv/html/transform.ftl
wkhtmltopdf -O landscape target/out/transactions.html target/out/transactions-html.pdf
templates/excel/html/transform.ftl
templates/excel/md/transform.ftl
templates/html/csv/dependencies.ftl
templates/json/csv/swagger-endpoints.ftl
templates/json/html/customer-user-products.ftl
wkhtmltopdf -O landscape target/out/customer-user-products.html target/out/customer-user-products.pdf
templates/json/md/customer-user-products.ftl
templates/json/md/github-users.ftl
templates/properties/csv/locker-test-users.ftl
templates/xml/txt/recipients.ftl
Created the following sample files in ./target/out
total 1168
-rw-r--r--@ 1 sgoeschl  staff   22412 May 26 20:47 contract.html
-rw-r--r--@ 1 sgoeschl  staff    7933 May 26 20:47 contract.md
-rw-r--r--@ 1 sgoeschl  staff  103477 May 26 20:48 customer-user-products.html
-rw-r--r--@ 1 sgoeschl  staff   34990 May 26 20:48 customer-user-products.md
-rw-r--r--  1 sgoeschl  staff  114668 May 26 20:48 customer-user-products.pdf
-rw-r--r--  1 sgoeschl  staff    4028 May 26 20:47 demo.txt
-rw-r--r--  1 sgoeschl  staff    1310 May 26 20:48 dependencies.csv
-rw-r--r--@ 1 sgoeschl  staff    2029 May 26 20:48 github-users-curl.md
-rw-r--r--@ 1 sgoeschl  staff     235 May 26 20:48 locker-test-users.csv
-rw-r--r--  1 sgoeschl  staff    6291 May 26 20:47 locker-test-users.fo
-rw-r--r--  1 sgoeschl  staff    5503 May 26 20:47 locker-test-users.pdf
-rw-r--r--  1 sgoeschl  staff     921 May 26 20:48 recipients.txt
-rw-r--r--  1 sgoeschl  staff     341 May 26 20:48 swagger-spec.csv
-rw-r--r--  1 sgoeschl  staff    1907 May 26 20:48 test-multiple-sheets.xlsx.html
-rw-r--r--  1 sgoeschl  staff     389 May 26 20:48 test-multiple-sheets.xlsx.md
-rw-r--r--  1 sgoeschl  staff    1546 May 26 20:48 test.xls.html
-rw-r--r--  1 sgoeschl  staff    1548 May 26 20:48 test.xslx.html
-rw-r--r--@ 1 sgoeschl  staff   11352 May 26 20:47 transactions-fo.pdf
-rw-r--r--@ 1 sgoeschl  staff   33294 May 26 20:48 transactions-html.pdf
-rw-r--r--  1 sgoeschl  staff  106462 May 26 20:47 transactions.fo
-rw-r--r--@ 1 sgoeschl  staff   18148 May 26 20:48 transactions.html
```

# 2. Once Upon A Time

I needed a little bit of test data management for a customer project - to make a long story short (after writing a few more Groovy scripts) it boiled down to transforming one or more JSON files to something human readable.

What are the options?

* The cool kids say 'Node.js' - but they always say 'Node.js' 
* Some fancy Groovy scripts using Groovy's markup builder - but the syntax looks a bit odd
* Using 'JsonPath' and 'Velocity' to reuse good & old stuff

So I went with 'Apache Groovy', 'JsonPath' and 'Apache Velocity'

* Playing with Groovy over the public holidays
* Groovy has a built-in package manager which makes distribution a breeze
* Provding samples to transform JSON to Markdown

Using Velocity actually created some minor issues so I migrated to [Apache FreeMarker](https://freemarker.apache.org) during Christmas 2016

* Velocity 1.7 was released 2010 and only recently there was a new release
* I was painful to get Velocity Tools working
* Velocity XML processing support is also painful
* Spring 4.3 deprecated Velocity support which could affect me in the long run
* Freemarker has no additional dependencies and things are just working :-)

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
 -b,--basedir <arg>           Base directory to resolve templates
 -d,--description <arg>       Custom report description
 -e,--encoding <arg>          Encoding of output file, e.g. UTF-8
 -h,--help                    Usage information
 -i,--include <arg>           Ant file pattern for directory search
 -l,--locale <arg>            Locale being used for output file
 -o,--output <arg>            Generated output file
 -s,--source-encoding <arg>   Encoding of source file
 -t,--template <arg>          Template name
 -v,--verbose                 Verbose mode
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
<#assign json = JsonPath.parse(documents[0])>
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

As added bonus we can easily transform the HTML to PDF using [wkhtmltopdf](https://wkhtmltopdf.org)

```text
freemarker-cli> wkhtmltopdf ./site/sample/customer-user-products.html customer-user-products.pdf
Loading pages (1/6)
Counting pages (2/6)                                               
Resolving links (4/6)                                                       
Loading headers and footers (5/6)                                           
Printing pages (6/6)
Done  
```

which creates the following PDF document (please note that even the links within the document are working)

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
<#assign cvsFormat = CSVFormat.DEFAULT.withHeader()>
<#assign csvParser = CSVParser.parse(documents[0], cvsFormat)>
<#assign csvHeaders = csvParser.getHeaderMap()?keys>
<#assign csvRecords = csvParser.records>
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
<#assign xml = XmlParser.parse(documents[0])>
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

One day I was asked a to prepare a CSV files containind REST endpoints described by Swagger - technically this is a JSON to CSV transformation. Of course I could create that CSV manually but writing a FTL template doing that was simply more fun and saves time in the future.

```text
<#ftl output_format="plainText" strip_text="true">
<#assign json = JsonPath.parse(documents[0])>
<#assign basePath = json.read("$.basePath")>
<#assign paths = json.read("$.paths")>

<#compress>
    ENDPOINT;METHOD;DESCRIPTION
    <#list paths as endpoint,metadata>
        <#assign relative_url = basePath + endpoint>
        <#assign methods = metadata?keys>
        <#list methods as method>
            <#assign description = paths[endpoint][method]["description"]?replace(";", ",")>
            ${relative_url};${method?upper_case};${description}
        </#list>
    </#list>
</#compress>
${'\n'}
```

Invoking the FTL template

> groovy freemarker-cli.groovy -t templates/json/csv/swagger-endpoints.ftl site/sample/json/swagger-spec.json 

gives you

```text
ENDPOINT;METHOD;DESCRIPTION
/api/pets;GET;Returns all pets from the system that the user has access to
/api/pets;POST;Creates a new pet in the store. Duplicates are allowed
/api/pets/{id};GET;Returns a user based on a single ID, if the user does not have access to the pet
/api/pets/{id};DELETE;Deletes a single pet based on the ID supplied
```

## 5.6 Transforming Excel Documents

Another day my project management asked me to create a CSV configuration file based on an Excel documents - as usual manual copying was not an option due to required data cleanup and data transformation. So I thought about Apache POI which support XLS and XLSX documents - integration of Apache POI was a breeze but the resulting code was not particulary useful example. So a more generic transformation was provided to show the transformation of Excel documents ...

```text
> groovy freemarker-cli.groovy -t templates/excel/html/transform.ftl site/sample/excel/test.xls
> groovy freemarker-cli.groovy -t templates/excel/html/transform.ftl site/sample/excel/test.xlsx
> groovy freemarker-cli.groovy -t templates/excel/html/transform.ftl site/sample/excel/test-multiple-sheets.xlsx
> groovy freemarker-cli.groovy -t templates/excel/md/transform.ftl site/sample/excel/test-multiple-sheets.xlsx
```

The provided FTL transforms an Excel into a HTML document supporting multiple Excel sheets

```text
<#ftl output_format="HTML" >
<#assign documentName = documents[0].name>
<#assign workbook = ExcelParser.parse(documents[0])>
<#assign date =  ReportData["date"]>
<#--------------------------------------------------------------------------->
<!DOCTYPE html>
<html>
<head>
    <title>${documentName}</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
</head>
<body>
<div class="container-fluid">
    <h1>Excel Test
        <small>${documentName}, ${date}</small>
    </h1>
    <@writeSheets workbook/>
</div>
</body>
</html>

<#--------------------------------------------------------------------------->
<#-- writeSheets                                                           -->
<#--------------------------------------------------------------------------->
<#macro writeSheets workbook>
    <#assign sheets = ExcelParser.getAllSheets(workbook)>
    <#list sheets as sheet>
        <@writeSheet sheet/>
    </#list>
</#macro>

<#--------------------------------------------------------------------------->
<#-- writeSheet                                                            -->
<#--------------------------------------------------------------------------->
<#macro writeSheet sheet>
    <#assign rows = ExcelParser.parseSheet(sheet)>
    <h2>${sheet.getSheetName()}</h2>
    <@writeRows rows/>
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
                    <#list row as column>
                        <th>${column}</th>
                    </#list>
                </tr>
            <#else>
                <tr>
                    <td>${row?index}</td>
                    <#list row as column>
                        <td>${column}</td>
                    </#list>
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
        <#assign properties = PropertiesParser.parse(document)>
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
```

## 5.8 Transform CSV To XML-FO

For a POC (proof of concept) I created a sample transformation from CSV to XML-FO in order to create a PDF document using [Apache FOP](https://xmlgraphics.apache.org/fop) using the following template file

```text
<#ftl output_format="XML" >
<#assign name = documents[0].name>
<#assign cvsFormat = CSVFormat.DEFAULT.withHeader()>
<#assign csvParser = CSVParser.parse(documents[0], cvsFormat)>
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
```

In order to create the PDF you need to execute the following commands (assuming that you have Apache FOP installed)

```text
> groovy freemarker-cli.groovy -t templates/csv/fo/transform.ftl site/sample/csv/locker-test-users.csv > sample.fo
> fop -fo sample.fo sample.pdf
Dec 29, 2018 10:24:30 PM org.apache.fop.events.LoggingEventListener processEvent
WARNING: Font "Symbol,normal,700" not found. Substituting with "Symbol,normal,400".
Dec 29, 2018 10:24:30 PM org.apache.fop.events.LoggingEventListener processEvent
WARNING: Font "ZapfDingbats,normal,700" not found. Substituting with "ZapfDingbats,normal,400".
Dec 29, 2018 10:24:30 PM org.apache.fop.events.LoggingEventListener processEvent
INFO: Rendered page #1.
```

The result does not look very impressive but it is a PDF :-)

![](./site/image/locker-test-users-pdf.png)

Further along the line of the POC we converted a transaction export from CSV to PDF using Apache FOP

```text
> freemarker-cli.groovy -t templates/csv/fo/transactions.ftl site/sample/csv/transactions.csv > transactions.fo
> fop -fo transactions.fo transactions.pdf
Jan 16, 2019 11:15:21 PM org.apache.fop.events.LoggingEventListener processEvent
WARNING: Font "Symbol,normal,700" not found. Substituting with "Symbol,normal,400".
Jan 16, 2019 11:15:21 PM org.apache.fop.events.LoggingEventListener processEvent
WARNING: Font "ZapfDingbats,normal,700" not found. Substituting with "ZapfDingbats,normal,400".
Jan 16, 2019 11:15:21 PM org.apache.fop.events.LoggingEventListener processEvent
WARNING: The contents of fo:block line 1 exceed the available area in the inline-progression direction by 11027 millipoints. (See position 1519:51)
Jan 16, 2019 11:15:22 PM org.apache.fop.events.LoggingEventListener processEvent
INFO: Rendered page #1.
Jan 16, 2019 11:15:22 PM org.apache.fop.events.LoggingEventListener processEvent
INFO: Rendered page #2.
```

![](./site/image/transactions.png)

## 5.9 Transforming HTML To CSV

Recently I got the rather unusual question how to determine the list of dependecied of an application - one easy way is the Maven "dependencies.html" but this is unstructured data. Having said that the Jsoup library is perfectly able to parse most real-life HTML and provides a DOM model

```text
<#ftl output_format="plainText" strip_text="true">
<#assign documentName = documents[0].name>
<#assign html = JsoupBean.parse(documents[0])>

<#compress>
    <@writeHeader/>
    <@writeDependencies "Project_Dependencies_compile"/>
    <@writeDependencies "Project_Transitive_Dependencies_compile"/>
    <@writeDependencies "Project_Transitive_Dependencies_runtime"/>
    <@writeDependencies "Project_Transitive_Dependencies_provided"/>
</#compress>

<#macro writeHeader>
    GroupId,ArtifactId,Version,Type,Licenses
</#macro>

<#macro writeDependencies section>
    <#assign selection = html.select("a[name=${section}]")>
    <#if selection?has_content>
        <#assign table = selection[0].nextElementSibling().child(2).child(0)>
        <#assign rows = table.children()>
        <#list rows as row>
            <#if !row?is_first>
                <#assign groupId = row.child(0).text()>
                <#assign artificatId = row.child(1).text()>
                <#assign version = row.child(2).text()>
                <#assign type = row.child(3).text()>
                <#assign licences = row.child(4).text()?replace(",", "")>
                ${groupId},${artificatId},${version},${type},${licences}
            </#if>
        </#list>
    </#if>
</#macro>
```

Your dependencies as CSV can be generated as shown below

```text
> groovy freemarker-cli.groovy -t templates/html/csv/dependencies.ftl site/sample/html/dependencies.html 
GroupId,ArtifactId,Version,Type,Licenses
com.jayway.jsonpath,json-path,2.4.0,jar,The Apache Software License Version 2.0
commons-cli,commons-cli,1.4,jar,Apache License Version 2.0
org.apache.commons,commons-csv,1.5,jar,Apache License Version 2.0
org.apache.poi,poi,4.0.1,jar,The Apache Software License Version 2.0
org.apache.poi,poi-ooxml,3.17,jar,The Apache Software License Version 2.0
org.apache.poi,poi-ooxml-schemas,3.17,jar,The Apache Software License Version 2.0
org.freemarker,freemarker,2.3.28,jar,Apache License Version 2.0
org.jsoup,jsoup,1.11.3,jar,The MIT License
org.slf4j,slf4j-api,1.7.21,jar,MIT License
org.slf4j,slf4j-log4j12,1.7.21,jar,MIT License
com.github.virtuald,curvesapi,1.04,jar,BSD License
commons-codec,commons-codec,1.11,jar,Apache License Version 2.0
log4j,log4j,1.2.17,jar,The Apache Software License Version 2.0
net.minidev,accessors-smart,1.2,jar,The Apache Software License Version 2.0
net.minidev,json-smart,2.3,jar,The Apache Software License Version 2.0
org.apache.commons,commons-collections4,4.2,jar,Apache License Version 2.0
org.apache.commons,commons-math3,3.6.1,jar,Apache License Version 2.0
org.apache.xmlbeans,xmlbeans,2.6.0,jar,The Apache Software License Version 2.0
org.ow2.asm,asm,5.0.4,jar,BSD
stax,stax-api,1.0.1,jar,The Apache Software License Version 2.0
```

## 5.10 Transform CSV To Shell Script

For a customer project we wanted to record REST request / responses using WireMock - really quick and dirty. So we decided to avoid any sophistacted test tool but generate a ready-tu-use shell script executing cURL commands. It turned out that handling of dollar signs is a bit tricky

* Using ```noparse``` directive to disable parsing of dollar signs
* Using ```${r"${MY_BASE_URL}"``` to generate output with dollar signs

```
<#ftl output_format="plainText">
<#assign cvsFormat = CSVFormat.DEFAULT.withHeader()>
<#assign csvParser = CSVParser.parse(documents[0], cvsFormat)>
<#assign records = csvParser.records>
<#--------------------------------------------------------------------------->
#!/bin/sh

<#noparse>
MY_BASE_URL=${MY_BASE_URL:=https://postman-echo.com}
echo "MY_BASE_URL = ${MY_BASE_URL}" 
</#noparse>
 
echo "Executing ${records?size} requests - starting at `date`"
echo "status,time,user"
<#list records as record>
curl --write-out '%{http_code},%{time_total},${record.disposer}' --insecure --silent --show-error --output /dev/null -H "Authorization: Bearer ${record.token}" "${r"${MY_BASE_URL}"}/get?__=${record.disposer}"; echo
</#list>
echo "Finished at `date`"
```

Rendering the FreeMarker template generates the following shell script

```
#!/bin/sh

MY_BASE_URL=${MY_BASE_URL:=https://postman-echo.com}
echo "MY_BASE_URL = ${MY_BASE_URL}" 
 
echo "Executing 4 requests - starting at `date`"
echo "status,time,user"
curl --write-out '%{http_code},%{time_total},0401126' --insecure --silent --show-error --output /dev/null -H "Authorization: Bearer 0401126-0000-0000-0000-0123456789012" "${MY_BASE_URL}/get?__=0401126"; echo
curl --write-out '%{http_code},%{time_total},0401133' --insecure --silent --show-error --output /dev/null -H "Authorization: Bearer 0401133-0000-0000-0000-0123456789012" "${MY_BASE_URL}/get?__=0401133"; echo
curl --write-out '%{http_code},%{time_total},0401173' --insecure --silent --show-error --output /dev/null -H "Authorization: Bearer 0401173-0000-0000-0000-0123456789012" "${MY_BASE_URL}/get?__=0401173"; echo
curl --write-out '%{http_code},%{time_total},0401234' --insecure --silent --show-error --output /dev/null -H "Authorization: Bearer 0401234-0000-0000-0000-0123456789012" "${MY_BASE_URL}/get?__=0401234"; echo
echo "Finished at `date`"
```


## 5.11 Using Advanced FreeMarker Features

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
Document: name=${document.name} location=${document.location} length=${document.length} encoding=${document.encoding}
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
Document: name=README.md location=/Users/sgoeschl/work/github/sgoeschl/freemarker-cli/README.md length=19,502

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
| documents             | List of documents passed on the command line                        |
| Documents             | Helper to find documents, e.g. by name or extension                 |
| Enums                 | Helper to work with Java enumerations                               |
| Environment           | Environment variables                                               |
| ExcelParser           | Excel parser exposing a `parse` method                              |
| JsonPath              | JSON Parser                                                         |
| JsoupParser           | Jsoup HTML parser                                                   |
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
