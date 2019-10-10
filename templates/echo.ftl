<#ftl output_format="plainText" >
<#list documents as document>
${document.name}, ${document.location}
=============================================================================
${document.text}
</#list>