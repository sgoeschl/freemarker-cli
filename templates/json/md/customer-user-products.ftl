<#ftl output_format="plainText" >
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
<#assign document = Documents.get(0)>
<#assign sourceDocumentName = document.name>
<#assign json = JsonPathTool.parse(document)>
<#assign users = json.read("$[*]")>
<#assign userDetails = json.read("$[*].user")>
<#compress>
<#--------------------------------------------------------------------------->
# 1. Overview

| Parameter                     | Value                                     |
|:------------------------------|:------------------------------------------|
| Source Document Name          | ${sourceDocumentName}                     |
| Report Generation Date        | ${.now?iso_utc}                           |
| Report User Name              | ${SystemTool.properties["user.name"]}     |

# 2. Users

<@writeTestUserOverview users=userDetails/>

# 3. User Products Overview

<@writeTestUserDetails users=users/>

# 4. User Products

<#list users as user>
## 4.${user?counter} ${user.user.name} [${user.user.disposerId}]

<@writeProducts title="Accounts" products=user.accounts/>
<@writeProducts title="Cards" products=user.cards/>
<@writeProducts title="Building Saving Plans" products=user.buildings/>
<@writeProducts title="Pension Saving Plans" products=user.pensions/>
<@writeProducts title="Insurances" products=user.insurances/>
<@writeProducts title="Securities" products=user.securities/>
</#list>
</#compress>

<#--------------------------------------------------------------------------->
<#-- writeTestUsers                                                        -->
<#--------------------------------------------------------------------------->
<#macro writeTestUserOverview users>
| #                     | Name              | Login                 | Password                    | CustomerId                  | Description                 |
|:----------------------|:------------------|:----------------------|:----------------------------|:----------------------------|:----------------------------|
<#list users as user>
<#assign anchor = "[${user.name}](#${user.disposerId})">
| ${user?counter}       | ${anchor}         | ${user.disposerId}    | ${user.password}            | ${user.customerId}          | ${user.description}         |
</#list>
</#macro>
<#--------------------------------------------------------------------------->
<#-- writeTestUsersOverview                                                -->
<#--------------------------------------------------------------------------->
<#macro writeTestUserDetails users>
| #                     | Name              | Accounts              | Cards                       | Buildings                   | Pensions                    | Insurances                  | Securities                  |
|:----------------------|:------------------|:----------------------|:----------------------------|:----------------------------|:----------------------------|:----------------------------|:----------------------------|
<#list users as user>
<#assign anchor = "[${user.user.name}](#${user.user.disposerId})">
<#assign nrOfAccounts = user.accounts?size>
<#assign nrOfCards = user.cards?size>
<#assign nrOfBuildingSavings = user.buildings?size>
<#assign nrOfPensionSavings = user.pensions?size>
<#assign nrOfInsurances = user.insurances?size>
<#assign nrOfSecurities = user.securities?size>
| ${user?counter}       | ${anchor}         | ${nrOfAccounts}       | ${nrOfCards}                | ${nrOfBuildingSavings}      | ${nrOfPensionSavings}       | ${nrOfInsurances}           | ${nrOfSecurities}           |
</#list>
</#macro>
<#--------------------------------------------------------------------------->
<#-- writeProducts                                                         -->
<#--------------------------------------------------------------------------->
<#macro writeProducts title products>
<#if products?size != 0>
### ${title}

| Id                    | Type               | Product Code          | Product Name            |  IBAN                     |  Number                   | Description               | State                     | Transactions 				|
|:----------------------|:-------------------|:----------------------|:------------------------|:--------------------------|:--------------------------|:--------------------------|:--------------------------|:---------------------------|
<#list products as product>
| ${product.id}         | ${product.type!}   | ${product.product!}   | ${product.productI18N!} | ${product.accountnoIban!} | ${product.number!}        | ${product.description!}   | ${product.state!}         | ${product.transactions!}   |
</#list>
</#if>
</#macro>
