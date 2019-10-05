<#ftl output_format="HTML" >
<#assign name = documents[0].name>
<#assign cvsFormat = CSVFormat.DEFAULT.withHeader()>
<#assign csvParser = CSVParser.parse(documents[0], cvsFormat)>
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
    <#list csvParser as row>
        <#if row?is_first>
            <@writeHeaders row/>
        <#else>
            <@writeColums row/>
        </#if>
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
<#macro writeColums column>
    <tr>
        <#list column as field>
            <td>${field}</td>
        </#list>
    </tr>
</#macro>
