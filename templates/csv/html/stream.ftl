<#ftl output_format="HTML" >
<#assign name = documents[0].name>
<#assign cvsFormat = CSVFormat.DEFAULT.withHeader()>
<#assign csvReader = CSVParser.getReader(documents[0], cvsFormat)>
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
    <#list 0..1000000 as i>
        <#if csvReader.hasNext()>
            <#assign record = csvReader.getNext()>
            <#if i == 0>
                <@writeHeaders record/>
            <#else>
                <@writeColumns record/>
            </#if>
        <#else>
            <#break>
        </#if>
    </#list>
</table>
</body>
</html>
<#--------------------------------------------------------------------------->
<#macro writeHeaders record>
    <tr>
        <#list record.iterator() as field>
            <th>${field}</th>
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

