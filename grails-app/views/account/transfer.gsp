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
        <h2>Transfer Funds</h2>
      </div>

      <div class="hpanel">
        <g:if test="${transferCO?.hasErrors()}">
          <div class="alert alert-block alert-danger">
            <g:renderErrors bean="${transferCO}" as="list"/>
          </div>
        </g:if>

        <form action='${createLink(controller: 'account', action: 'transferAmount')}' method='POST' id='transfer'>
          <div class="form-group"><label class="col-sm-9 control-label">Enter the account number to transfer into:</label>
            <input type="text" placeholder="Account Number" title="Account Number" name="accountNumber" id="accountNumber" class="form-control">
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