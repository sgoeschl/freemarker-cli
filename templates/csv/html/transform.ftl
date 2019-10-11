<#ftl output_format="HTML" >
<#assign name = documents[0].name>
<#assign cvsFormat = CSVFormat.DEFAULT.withHeader()>
<#assign csvParser = CSVTool.parse(documents[0], cvsFormat)>
<#assign csvHeaders = csvParser.getHeaderNames()>
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
    <@writeHeaders csvParser.getHeaderNames()/>
    <#list csvParser.iterator() as record>
        <@writeColumns record/>
    </#list>
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
<#macro writeColumns record>
    <tr>
        <#list record.iterator() as field>
            <th>${field}</th>
        </#list>
    </tr>
</#macro>