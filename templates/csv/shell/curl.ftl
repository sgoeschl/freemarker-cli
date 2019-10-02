<#ftl output_format="plainText">
<#assign cvsFormat = CSVFormat.DEFAULT.withHeader()>
<#assign csvParser = CSVParser.parse(documents[0], cvsFormat)>
<#assign records = csvParser.records>
<#assign csvMap = CSVParser.toMap(csvParser, records, "disposer")>
<#--------------------------------------------------------------------------->
#!/bin/sh

<#noparse>
MY_BASE_URL=${MY_BASE_URL:=https://postman-echo.com}
</#noparse>
 
echo "time,user,status,duration,size"
<#list records as record>
date "+%FT%H:%M:%S" | tr -d '\n'; curl --write-out ',${record.disposer},%{http_code},%{time_total},%{size_download}\n' --silent --show-error --output /dev/null "${r"${MY_BASE_URL}"}/get"
<#--    ${csvMap["0401234"].user}, ${csvMap["0401234"].disposer} -->
    <#list records?sort_by("user") as i>
        - ${i.user}: ${i.token}
    </#list>
</#list>
