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
          <h2>Your ${accountType?.name} Balance</h2>

          <h3 id="balance">$${balance ? balance.format() : "0.00"}</h3>
        </div>
        <a href="${createLink(controller: 'home', action: 'menu')}" class="btn btn-primary btn-block">Ok</a>
      </div>
    </div>
  </div>
</div>
</body>
</html>