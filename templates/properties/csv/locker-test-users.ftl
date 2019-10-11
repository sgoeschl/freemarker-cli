<#ftl output_format="plainText" strip_text="true">
<#compress>
    TENANT,SITE,USER_ID,DISPOSER_ID,PASSWORD,SMS_OTP,NAME,DESCRIPTION
    <#list documents as document>
        <#assign properties = PropertiesTool.parse(document)>
        <#assign environments = properties["ENVIRONMENTS"]!"">
        <#assign tenant = extractTenant(environments)>
        <#assign site = extractSite(environments)>
        <#assign userId = properties["USER_ID"]!"">
        <#assign disposerId = properties["USER_ID"]!"">
        <#assign password = properties["PASSWORD"]!"">
        <#assign smsOtp = properties["SMS_OTP"]!"">
        <#assign name = properties["NAME"]!"">
        <#assign description = properties["NAME"]!"">
        ${tenant},${site},${userId},${disposerId},${password},${smsOtp},${name},${description}
    </#list>
</#compress>
${'\n'}

<#function extractSite environments>
    <#if (environments)?contains("_DEV")>
        <#return "dev">
    <#elseif (environments)?contains("_FAT")>
        <#return "fat">
    <#elseif (environments)?contains("_ST")>
        <#return "st">
    <#elseif (environments)?contains("_PROD")>
        <#return "prod">
    <#elseif (environments)?contains("_UAT")>
        <#return "uat">
    <#else>
        <#return "???">
    </#if>
</#function>

<#function extractTenant environments>
    <#if (environments)?contains("AT_")>
        <#return "at">
    <#elseif (environments)?contains("BCR_")>
        <#return "ro">
    <#elseif (environments)?contains("CSAS_")>
        <#return "cz">
    <#elseif (environments)?contains("SK_")>
        <#return "sk">
    <#else>
        <#return "???">
    </#if>
</#function>
