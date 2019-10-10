<#ftl output_format="plainText" strip_text="true">
<#assign workbook = ExcelTool.parse(documents[0])>
<#assign sheet = ExcelTool.getSheets(workbook)[0]>
<#assign records = ExcelTool.parseSheet(sheet)>
<#assign recordDelimiter = SystemProperties["csv.delimiter"]!";">
<#--------------------------------------------------------------------------->
<#compress>
    <#list records as record>
        ${record?join(recordDelimiter, "", "")}
    </#list>
</#compress>
${'\n'}
