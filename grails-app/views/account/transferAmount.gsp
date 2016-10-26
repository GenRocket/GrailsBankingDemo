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
        <h2>Transfer from ${currentAccountType?.name} to ${accountToTransfer?.accountType?.name ?: transferAmountCO?.account?.accountType?.name}</h2>
      </div>

      <div class="hpanel">
        <g:if test="${transferAmountCO?.hasErrors()}">
          <div class="alert alert-block alert-danger">
            <g:renderErrors bean="${transferAmountCO}" as="list"/>
          </div>
        </g:if><br/>

        <form action='${createLink(controller: 'account', action: 'doTransfer')}' method='POST' id='transfer'>
          <input type="hidden" name="accountIdTo" value="${accountToTransfer?.id ?: transferAmountCO?.accountIdTo}"/>

          <div class="form-group">
            <input type="text" placeholder="Amount" title="Amount" name="amount" id="amount" class="form-control">
          </div>

          <input type='submit' id="submit" class="btn btn-primary btn-block"
                 value='Transfer'/>
        </form>
      </div>
    </div>
  </div>
</div>
</div>
</body>
</html>