<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Welcome to GenRocket Bank ATM</title>
  <meta name="layout" content="main"/>
  <asset:javascript src="custom/menu.js"/>
</head>

<body>
<g:render template="/layouts/header"/>
<div class="login-container">
  <div class="row">
    <div class="col-md-12">
      <div class="text-center m-b-md">
        <h3>Transaction is Complete</h3>

        <h3>Please take money.</h3>

        <h3>You drew $${withdrawalAmount}.</h3>

        <h3>Adjusted balance $${balance}.</h3>

        <button class="btn btn-primary btn-block" id="moveToMenu">Ok</button>
      </div>
    </div>
  </div>
</div>
</body>
</html>