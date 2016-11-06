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
                <h2>Deposit in Checking</h2>
            </div>

          <div class="hpanel">
                <div id="withdrawalErrorContainer">
                  <g:if test="${errorMessage}">
                    <div class="alert alert-block alert-danger">
                      <ul><li>${errorMessage}</li></ul>
                    </div>
                  </g:if>
                </div>

                <form action='${createLink(controller: 'account', action: 'doDeposit')}' method='POST' id='withdrawalDeposit'>
                    <div class="form-group">
                        <input type="text" placeholder="Amount" title="Amount" name="amount" maxlength="10" id="amount" class="form-control">
                    </div>

                    <input type='submit' id="submit" class="btn btn-primary btn-block"
                           value='Deposit'/>
                </form>
            </div>
        </div>
    </div>
</div>
</div>
</body>
</html>