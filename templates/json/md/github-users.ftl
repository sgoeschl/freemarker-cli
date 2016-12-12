<#ftl output_format="plainText" >
<#assign json = JsonPath.parse(documents[0].content)>
<#assign users = json.read("$[*]")>
<#--------------------------------------------------------------------------->
# GitHub Users

Report generated at ${.now?iso_utc}

<#list users as user>
    <#assign userAvatarUrl = user.avatar_url>
    <#assign userHomeUrl = user.html_url>
# ${user.login}

| User                                                    | Homepage                                      |
|:--------------------------------------------------------|:----------------------------------------------|
| <img src="${user.avatar_url}" width="48" height="48"/> | [${userHomeUrl}](${userHomeUrl})              |
</#list>
