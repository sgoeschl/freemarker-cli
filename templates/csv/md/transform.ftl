<#ftl output_format="plainText">
<#assign cvsFormat = CSVFormat.DEFAULT.withHeader()>
<#assign csvParser = CSVTool.parse(documents[0], cvsFormat)>
<#assign csvHeaders = csvParser.getHeaderMap()?keys>
<#assign csvRecords = csvParser.records>
<#--------------------------------------------------------------------------->
<#compress>
<@writeHeaders headers=csvHeaders/>
<@writeColums columns=csvRecords/>
</#compress>
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
