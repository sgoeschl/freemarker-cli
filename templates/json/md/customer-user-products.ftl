<#ftl output_format="plainText" >
<#assign json = JsonPath.parse(documents[0].content)>
<#assign users = json.read("$[*]")>
<#assign userDetails = json.read("$[*].user")>
<#--------------------------------------------------------------------------->
# 1. Overview

Report generated at ${.now?iso_utc}

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

    | Id                    | Type               | Product Code          | Product Name            |  IBAN                     |  Number                   | Description               | State                     |
    |:----------------------|:-------------------|:----------------------|:------------------------|:--------------------------|:--------------------------|:--------------------------|:--------------------------|
        <#list products as product>
        | ${product.id}         | ${product.type!}   | ${product.product!}   | ${product.productI18N!} | ${product.accountnoIban!} | ${product.number!}        | ${product.description!}   | ${product.state!}         |
        </#list>
    </#if>
</#macro>
