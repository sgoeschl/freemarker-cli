<#ftl output_format="plainText" strip_text="true">
<#assign name = documents[0].name>
<#assign cvsFormat = CSVFormat.DEFAULT.withDelimiter(';')>
<#assign csvParser = CSVTool.parse(documents[0], cvsFormat)>
<#assign csvRecords = csvParser.records>

<#compress>
disposer,user,password,token,device,name,description
<#list csvRecords as record>
    <#assign user = record.get(0)>
    <#assign token = record.get(1)>
    ${user},${user},,${token},,${user},HU performance test user ${user}
</#list>
</#compress>
