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
                <h2>Withdrawal From Checking</h2>
            </div>

            <div class="hpanel">
                <g:if test="${flash.error}">
                    <div class="alert alert-block alert-danger">
                        ${flash.error}
                    </div>
                </g:if>

                <form action='${createLink(controller: 'account', action: 'doWithdrawal')}' method='POST' id='withdrawal'>
                    <div class="form-group">
                        <input type="text" placeholder="Amount" title="Amount" name="amount" id="amount" class="form-control">
                    </div>

                    <input type='submit' id="submit" class="btn btn-primary btn-block"
                           value='Withdraw'/>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>