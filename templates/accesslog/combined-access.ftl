<#ftl output_format="plainText" strip_whitespace=true>
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
<#assign grok = GrokTool.compile("%{COMBINEDAPACHELOG}")>
<#assign lines = documents[0].getLineIterator()>

<#compress>
    TIMESTAMP;VERB;REQUEST;HTTPVERSION
    <#list lines as line>
        <#assign parts = grok.match(line)>
        <#assign timestamp = parts["timestamp"]>
        <#assign verb = parts["verb"]>
        <#assign request = parts["request"]>
        <#assign httpversion = parts["httpversion"]>
        ${timestamp};${verb};${request};${httpversion}
    </#list>
    <#-- We should close the iterator -->
    ${GrokTool.close(lines)}
</#compress>