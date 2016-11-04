<%@ page import="java.text.DecimalFormat" contentType="text/html;charset=UTF-8" %>
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
        <h3>Transaction is Complete</h3>

        <h3>Please take money.</h3>

        <h3>You drew $${new DecimalFormat("#.00").format(withdrawalAmount)}</h3>

        <h3>Adjusted balance $${new DecimalFormat("#.00").format(balance)}</h3>

        <a href="${createLink(controller: 'home', action: 'menu')}" class="btn btn-primary btn-block">Ok</a>
      </div>
    </div>
  </div>
</div>
</div>
</body>
</html>