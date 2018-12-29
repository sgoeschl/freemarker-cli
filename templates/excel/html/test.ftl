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
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
</head>
<body>
<div class="container-fluid">
    <h1>Excel Test
        <small>${sourceDocumentName}, ${date}</small>
    </h1>
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
