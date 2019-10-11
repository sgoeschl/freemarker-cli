<#ftl output_format="plainText">
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
<#assign map = YamlTool.parse(documents[0])>
<#--------------------------------------------------------------------------->
<#compress>
<@print map 1/>
</#compress>

<#macro print object level>
    <#if object?is_sequence>
        <#list object as value>
            <@print value level/>
        </#list>
    <#elseif object?is_hash>
        <#list object as key, value>
            ${level} ${key} : <@printValue value level/>
        </#list>
    </#if>
</#macro>

<#macro printValue value level>
        <#if value?is_sequence>
            ${'\n'}<@print value level+1/>
        <#elseif value?is_hash>
            ${'\n'}<@print value level+1/>
        <#elseif value?is_number>
            ${value?c}
        <#else>
            ${value}
        </#if>
</#macro>
