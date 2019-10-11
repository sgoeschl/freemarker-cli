<#ftl output_format="plainText" strip_text="true">
<#--
  Licensed to the Apache Software Foundation (ASF) under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The ASF licenses this file
  to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
-->
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
