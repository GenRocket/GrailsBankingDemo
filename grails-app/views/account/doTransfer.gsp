<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Welcome to GenRocket Bank ATM</title>
  <meta name="layout" content="main"/>
  <asset:javascript src="custom/menu.js"/>
</head>

<body>
<g:render template="/layouts/header"/>
<div class="col-md-9">
  <div class="login-container">
  <div class="row">
    <div class="col-md-12">
      <div class="text-center m-b-md">
        <h3>${fromAccount.accountType.name} to ${toAccount.accountType.name} Transfer Completed</h3>

        <h3>${amount} transferred to ${toAccount.accountType.name} account.</h3>

        <h3>Balance Amount: $${fromAccount.balance}</h3>

        <a href="${createLink(controller: 'home', action: 'menu')}" class="btn btn-primary btn-block">Ok</a>
      </div>
    </div>
  </div>
</div>
</div>
</body>
</html>