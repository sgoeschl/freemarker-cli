<#ftl output_format="plainText" strip_whitespace=true>

FreeMarker Information
---------------------------------------------------------------------------
FreeMarker version     : ${.version}
Template name          : ${.current_template_name}
Language               : ${.lang}
Locale                 : ${.locale}
Timestamp              : ${.now}
Output encoding        : ${.output_encoding!"not set"}
Output format          : ${.output_format}

<#if documents?has_content>
Documents
---------------------------------------------------------------------------
<#list documents as document>
- [${document?counter?left_pad(2)}] ${document.name}, ${document.location}, ${document.length} Bytes
</#list>
</#if>

<#if SystemTool.getSettings().getProperties()?has_content>
User Supplied Properties
---------------------------------------------------------------------------
<#list SystemTool.getSettings().getProperties() as name,value>
- ${name} ==> ${value}
</#list>
</#if>

SystemTool
---------------------------------------------------------------------------
Host name       : ${SystemTool.getHostName()}
User name       : ${SystemTool.getProperty("user.name", "N.A.")}
Command line    : ${SystemTool.getSettings().getArgs()?join(", ")}

FreeMarker Document Model
---------------------------------------------------------------------------
<#list .data_model?keys as key>
- ${key}
</#list>
${'\n'}
