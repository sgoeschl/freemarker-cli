<#ftl output_format="plainText" strip_text="true">
<#assign format = SystemProperties["csv.format"]!"DEFAULT">
<#assign salt = SystemProperties["salt"]!"salt">
<#-- Parse the first document & sheet of the Excel document -->
<#assign workbook = ExcelTool.parse(documents[0])>
<#assign sheet = ExcelTool.getSheets(workbook)[0]>
<#assign records = ExcelTool.parseSheet(sheet)>
<#-- Setup CSVPrinter  -->
<#assign cvsFormat = CSVFormat[format]>
<#assign csvPrinter = CSVTool.printer(cvsFormat)>
<#--
    Transform an arbitrary Excel file and add addition columns using Commons CSV
    We are using an instance of CSVPrinter directly have proper quoting of the output
    which in turn uses the underlying java.io.Writer.
-->
<#compress>
    <#list records as record>
        <#if record?is_first>
        <#-- Updated header line -->
            <#assign line = "Line">
            <#assign uuid = "UUID">
            <#assign quoted = "Quoted Text">
        <#else>
        <#-- Updated rows -->
            <#assign line = record?counter-1>
            <#assign uuid = uuidFromValueAndSalt(text, salt)>
            <#assign quoted = ",;">
        </#if>
        <#assign text = record[1]>
        <#assign date = record[2]>
        ${csvPrinter.printRecord(line, text, date, uuid, quoted)}
    </#list>
</#compress>
<#--------------------------------------------------------------------------->
<#function uuidFromValueAndSalt value salt>
    <#assign uuidSource = value + salt>
    <#assign buffer = Statics["java.nio.charset.Charset"].forName("UTF-8").encode(uuidSource).rewind()>
    <#assign bytes = buffer.array()[0..<buffer.limit()]>
    <#return Statics["java.util.UUID"].nameUUIDFromBytes(bytes)>
</#function>