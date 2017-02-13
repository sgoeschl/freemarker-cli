# 1. Introduction

For a customer I needed a little bit of test data management - to make a long story short (after writing a few scripts) it boiled down to transforming one or more JSON files to something human readable.

What are the options

* The cool kids say 'Node.js' - but they always say 'Node.js' 
* Some fancy Groovy scripts using markup builder - but the Groovy syntax looks a bit odd
* Using 'JsonPath' and 'Velocity' to reuse good & old stuff

So I went with 'Apache Groovy', 'JsonPath' and 'Apache Velocity'

* Playing with Groovy over the public holidays
* Groovy has a built-in package manager which makes distribution a breeze
* Provding samples to transform JSON to Markdown

Using Velocity actually created some minor issues so I migrated to FreeMarker

* Velocity 1.7 was released 2010
* I was painful to get Velocity Tools working 
* Velocity XML processing support is also painful
* Spring 4.3 deprecated velocity support which could affect me in the long run

While I love Apache Velocity I decided to give FreeMarker a chance and migrated my [velocity-cli](https://github.com/sgoeschl/velocity-cli) to FreeMarker

# 2. Design Goals

* Support multiple documents for a single transformation
* Support transformation of CSV files using [Apache Commons CSV](http://commons.apache.org/proper/commons-csv/)
* Support for reading document content from STDIN to integrate with command line tools
* Add some commonly useful information such as `System Properties`, `Enviroment Variables`

# 3. Usage

```text
> groovy freemarker-cli.groovy
usage: groovy freemarker-cli.groovy [options] file[s]
 -b,--basedir <arg>       Base directory to resolve FreeMarker templates
 -d,--description <arg>   Custom report description
 -h,--help                Usage information
 -l,--locale <arg>        Locale value
 -o,--output <arg>        Generated output file
 -t,--template <arg>      Template name
 -v,--verbose             Verbose mode
```

# 4. Examples

## 4.1 Transforming GitHub JSON To Markdown

A simple example with real JSON data

### Invocation

You can either use the existing JSON sample

> groovy freemarker-cli.groovy -t temlates/json/md/github-users.ftl site/sample/json/github-users.json

or pipe a cURL response

> curl -s https://api.github.com/users | groovy freemarker-cli.groovy -t templates/json/md/github-users.ftl

### FreeMarker Template

```
<#ftl output_format="plainText" >
<#assign json = JsonPath.parse(documents[0].content)>
<#assign users = json.read("$[*]")>
<#--------------------------------------------------------------------------->
# GitHub Users

Report generated at ${.now?iso_utc}

<#list users as user>
<#assign userAvatarUrl = user.avatar_url>
<#assign userHomeUrl = user.html_url>
# ${user.login}

| User                                                    | Homepage                                      |
|:--------------------------------------------------------|:----------------------------------------------|
| <img src="${user.avatar_url}" width="48" height="48" /> | [${userHomeUrl}](${userHomeUrl})              |
</#list>
```

creates the following output

![Github Users](./site/image/github.png "Github Users")


## 4.2 Markdown Test User Documentation

For a customer I created a Groovy script to fetch all products for a list of users - the script generates a JSON file which can be easily transformed to Markdown

```
> groovy freemarker-cli.groovy -t templates/json/md/customer-user-products.ftl  site/sample/json/customer-user-products.json
```

The resulting file can be viewed with any decent Markdown viewer

![Customer User Products Markdown](./site/image/customer-user-products-md.png "Customer User Products Markdown")

Since many of our QA people have no Markdown viewer installed I also created a very similar HTML representaton

> groovy freemarker-cli.groovy -t templates/json/html/customer-user-products.ftl  site/sample/json/customer-user-products.json

![Customer User Products HTML](./site/image/customer-user-products-html.png "Customer User Products HTML")

## 4.3 CSV to Markdown Transformation

Sometimes you have a CSV file which needs to be translated in Markdown or HTML - there are on-line solutions available such as [CSV To Markdown Table Generator](https://donatstudios.com/CsvToMarkdownTable) but having a local solution gives you more flexibility.

```
> groovy freemarker-cli.groovy -t templates/csv/md/transform.ftl site/sample/csv/contract.csv
> groovy freemarker-cli.groovy -t templates/csv/html/transform.ftl site/sample/csv/contract.csv
```

The FreeMarker template is shown below

```
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

## 4.4 Transform XML To Plain Text

Of course you can also transform a XML document

```
> groovy freemarker-cli.groovy -t ./templates/xml/txt/recipients.ftl site/sample/xml/recipients.xml
```

using the following template

```
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

## 4.5 Transform JSON To CSV

One day I was asked a to prepare a CSV files containind REST endpoints described by Swagger - technically this is a JSON to CSV transformation. Of course I could create that CSV manually but writing a FTL template doing that was simply more fun and might save some time in the future

```
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

## 4.6 Using Advanced FreeMarker Features

There is a `demo.ftl` which shows some advanced FreeMarker functionality

* Invoking a Java constructor
* Invoke a static method of non-instantiable class
* Work with Java enums
* Access System properties
* Access Environment variables

> groovy freemarker-cli.groovy -t ./templates/demo.ftl README.md 

```
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
user.name    : ${SystemProperties["user.name"]}
user.dir     : ${SystemProperties["user.dir"]}
user.home    : ${SystemProperties["user.home"]}
java.version : ${SystemProperties["java.version"]}

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

# 5. Tips & Tricks

## 5.1 Template Base Directory

When doing some ad-hoc scripting it is useful to rely on a base directory to resolve the FTL templates

* As a default the FTL templates are resolved relative to the script directory
* The caller can provide a `-b` or `--basedir` command line parameter

> groovy freemarker-cli/freemarker-cli.groovy -t templates/json/html/customer-user-products.ftl freemarker-cli/site/sample/json/customer-user-products.jso

## 5.2 Using Pipes

When doing ad-hoc scripting it useful to pipe the output of one command directly into the Groovy script

> cat site/sample/json/customer-user-products.json | groovy freemarker-cli.groovy -t ./templates/json/html/customer-user-products.ftl

## 5.3 Executable Groovy Scripts

When you run on Unix and are tired of always typing `groovy` there is light on the end of the tunnel - assuming that the `freemarker-cli.groovy` is executable you can use

> ./freemarker-cli.groovy -t templates/json/html/customer-user-products.ftl site/sample/json/customer-user-products.json 



