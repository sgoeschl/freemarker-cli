<#ftl output_format="plainText">
<#assign json = JsonPath.parse(documents[0])>
<#assign paths = json.read("$.paths")>
ENDPOINT,METHOD
<#list paths as url,entry>
<#assign http_method = entry?keys[0]>
${url},${http_method?upper_case}
</#list>
