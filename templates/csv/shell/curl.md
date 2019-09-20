<#ftl output_format="plainText">
<#assign cvsFormat = CSVFormat.DEFAULT.withHeader()>
<#assign csvParser = CSVParser.parse(documents[0], cvsFormat)>
<#assign records = csvParser.records>
<#--------------------------------------------------------------------------->
#!/bin/sh

<#noparse>
MY_BASE_URL=${MY_BASE_URL:=https://postman-echo.com}
echo "MY_BASE_URL = ${MY_BASE_URL}" 
</#noparse>
 
echo "Executing ${records?size} requests - starting at `date`"
echo "status,time,user"
<#list records as record>
curl --write-out '%{http_code},%{time_total},${record.disposer}' --insecure --silent --show-error --output /dev/null -H "Authorization: Bearer ${record.token}" "${r"${MY_BASE_URL}"}/get?__=${record.disposer}"; echo
</#list>
echo "Finished at `date`"
