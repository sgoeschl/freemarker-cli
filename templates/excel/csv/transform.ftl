<#ftl output_format="plainText" strip_text="true">
<#assign format = SystemProperties["csv.format"]!"DEFAULT">
<#-- Parse the first document & sheet of the Excel document -->
<#assign workbook = ExcelTool.parse(documents[0])>
<#assign sheet = ExcelTool.getSheets(workbook)[0]>
<#assign records = ExcelTool.parseSheet(sheet)>
<#-- Setup CSVPrinter  -->
<#assign cvsFormat = CSVFormat[format]>
<#assign csvPrinter = CSVTool.printer(cvsFormat)>
<#-- Print each line of the Excel as CSV record -->
<#compress>
    <#list records as record>
        ${csvPrinter.printRecord(record)}
    </#list>
</#compress>
