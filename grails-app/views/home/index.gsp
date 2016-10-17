<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Welcome to GenRocket Bank ATM</title>
    <meta name="layout" content="main"/>
</head>

<body>
<div class="login-container">
    <div class="row">
        <div class="col-md-12">
            <div class="text-center m-b-md">
                <h2>Welcome to GenRocket Bank ATM</h2>
            </div>

            <div class="hpanel">
                <g:if test="${loginCO?.hasErrors()}">
                    <div class="alert alert-block alert-danger">
                        <g:renderErrors bean="${loginCO}" as="list"/>
                    </div>
                </g:if>

                <form action='${createLink(controller: 'home', action: 'login')}' method='POST' id='loginForm'>
                    <div class="form-group"><label class="col-sm-5 control-label">Card Number:</label>
                        <input type="text" placeholder="**** **** **** ****" title="Please enter you card number"
                               name="cardNumber" id="cardNumber" class="form-control" value="${loginCO?.cardNumber}">
                    </div>
                    <div class="form-group"><label class="col-sm-5 control-label">Pin Number:</label>
                        <input type="password" placeholder="****" title="Please enter you pin number"
                               name="pinNumber" id="pinNumber" class="form-control">
                    </div>


                    <input type='submit' id="submit" class="btn btn-primary btn-block"
                           value='Login'/>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>