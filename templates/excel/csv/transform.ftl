<#ftl output_format="plainText" strip_text="true">
<#assign workbook = ExcelParser.parse(documents[0])>
<#assign sheet = ExcelParser.getAllSheets(workbook)[0]>
<#assign records = ExcelParser.parseSheet(sheet)>
<#assign recordDelimiter = SystemProperties["csv.delimiter"]!";">
<#--------------------------------------------------------------------------->
<#compress>
    <#list records as record>
        ${record?join(recordDelimiter, "", "")}
    </#list>
</#compress>
${'\n'}
