<#ftl output_format="plainText" strip_text="true">
<#assign json = JsonPathTool.parse(documents[0])>
<#assign basePath = json.read("$.basePath")>
<#assign paths = json.read("$.paths")>

<#compress>
    ENDPOINT;METHOD;DESCRIPTION
    <#list paths as endpoint,metadata>
        <#assign relative_url = basePath + endpoint>
        <#assign methods = metadata?keys>
        <#list methods as method>
            <#assign description = paths[endpoint][method]["description"]?replace(";", ",")>
            ${relative_url};${method?upper_case};${description}
        </#list>
    </#list>
</#compress>
${'\n'}
