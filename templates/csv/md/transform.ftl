<#ftl output_format="plainText" >
<#assign content = documents[0].content>
<#assign cvsFormat = CSVFormat.DEFAULT.withHeader()>
<#assign csvParser = CSVParser.parse(content, cvsFormat)>
<#assign csvHeaders = csvParser.getHeaderMap()?keys>
<#assign csvRecords = csvParser.records>
<#--------------------------------------------------------------------------->
<@writeHeaders headers=csvHeaders/>
<@writeColums columns=csvRecords/>
<#--------------------------------------------------------------------------->
<#macro writeHeaders headers>
| ${csvHeaders?join(" | ", "")} |
    <#list csvHeaders as csvHeader>| --------</#list>|
</#macro>
<#--------------------------------------------------------------------------->
<#macro writeColums columns>
    <#list columns as column>
    | ${column.iterator()?join(" | ", "")} |
    </#list>
</#macro>
