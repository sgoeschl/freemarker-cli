<#ftl output_format="HTML" >
<#assign sourceDocumentName = documents[0].name>
<#assign json = JsonPath.parse(documents[0].content)>
<#assign users = json.read("$[*]")>
<#assign userDetails = json.read("$[*].user")>
<#assign description = ReportData["description"]>
<#assign date =  ReportData["date"]>
<#--------------------------------------------------------------------------->
<!DOCTYPE html>
<html>
<head>
    <title>${sourceDocumentName}</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
</head>
<body>
<nav class="navbar navbar-expand-sm bg-dark navbar-dark">
    <a class="navbar-brand" href="/">
        <img src="/logo.jpg" alt="Logo" style="width:30px;">
    </a>
    <ul class="navbar-nav">
        <li class="nav-item">
            <a class="nav-link" href="/ui/tenants">Tenants</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="/actuator/logfile">Logfile</a>
        </li>
    </ul>
</nav>
<br>
<div class="container-fluid">
    <h1>WebAPI Test User Report <small>${description}, ${date}</small></h1>
    <hr/>

    <h2>1. Users</h2>
    <hr/>
    <@writeTestUserOverview users=userDetails/>

    <h2>2. User Products Overview</h2>
    <hr/>
    <@writeTestUserDetails users=users/>

    <h2>3. User Products</h2>
    <#list users as user>
    <hr/>
    <h3><a id="${user.user.disposerId}">3.${user?counter} ${user.user.name}</a></h3>
    <@writeProducts title="Accounts" products=user.accounts/>
    <@writeProducts title="Cards" products=user.cards/>
    <@writeProducts title="Building Saving Plans" products=user.buildings/>
    <@writeProducts title="Pension Saving Plans" products=user.pensions/>
    <@writeProducts title="Insurances" products=user.insurances/>
    <@writeProducts title="Securities" products=user.securities/>
    </#list>
</div>
</body>
</html>

<#--------------------------------------------------------------------------->
<#-- writeTestUserOverview                                                 -->
<#--------------------------------------------------------------------------->
<#macro writeTestUserOverview users>
    <table class="table table-striped">
        <tr>
            <th>#</th>
            <th>Name</th>
            <th>Login</th>
            <th>Password</th>
            <th>CustomerId</th>
            <th>Description</th>
        </tr>
        <#list users as user>
        <tr>
            <td>${user?counter}</td>
            <td><a href="#${user.disposerId}">${user.name}</a></td>
            <td>${user.disposerId}</td>
            <td>${user.password}</td>
            <td>${user.customerId}</td>
            <td>${user.description}</td>
        </tr>
        </#list>
    </table>
</#macro>

<#--------------------------------------------------------------------------->
<#-- writeTestUserOverview                                                 -->
<#--------------------------------------------------------------------------->
<#macro writeTestUserDetails users>
    <table class="table table-striped">
        <tr>
            <th>#</th>
            <th>Name</th>
            <th>Accounts</th>
            <th>Cards</th>
            <th>Buildings</th>
            <th>Pensions</th>
            <th>Insurances</th>
            <th>Securities</th>
        </tr>
        <#list users as user>
        <#assign nrOfAccounts = user.accounts?size>
        <#assign nrOfCards = user.cards?size>
        <#assign nrOfBuildingSavings = user.buildings?size>
        <#assign nrOfPensionSavings = user.pensions?size>
        <#assign nrOfInsurances = user.insurances?size>
        <#assign nrOfSecurities = user.securities?size>
        <tr>
            <td>${user?counter}</td>
            <td><a href="#${user.user.disposerId}">${user.user.name}</a></td>
            <td>${nrOfAccounts}</td>
            <td>${nrOfCards}</td>
            <td>${nrOfBuildingSavings}</td>
            <td>${nrOfPensionSavings}</td>
            <td>${nrOfInsurances}</td>
            <td>${nrOfSecurities}</td>
        </tr>
        </#list>
    </table>
</#macro>

<#--------------------------------------------------------------------------->
<#-- writeProducts                                                         -->
<#--------------------------------------------------------------------------->
<#macro writeProducts title products>
    <#if products?size != 0>
    <h4>${title}</h4>
    <table class="table table-striped">
        <tr>
            <th>#</th>
            <th>Id</th>
            <th>Type</th>
            <th>Product Code</th>
            <th>Product Name</th>
            <th>IBAN</th>
            <th>Number</th>
            <th>Description</th>
            <th>State</th>
            <th>Transactions</th>
        </tr>
        <#list products as product>
        <tr>
            <td>${product?counter}</td>
            <td>${product.id}</td>
            <td>${product.type!}</td>
            <td>${product.product!}</td>
            <td>${product.productI18N!}</td>
            <td>${product.accountnoIban!}</td>
            <td>${product.number!}</td>
            <td>${product.description!}</td>
            <td>${product.state!}</td>
            <td>${product.transactions!}</td>
        </tr>
        </#list>
    </table>
    </#if>
</#macro>
