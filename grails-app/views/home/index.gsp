<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Welcome to Acme ATM</title>
    <meta name="layout" content="main"/>
</head>

<body>
<div class="login-container">
    <div class="row">
        <div class="col-md-12">
            <div class="text-center m-b-md">
                <h2>Welcome to Acme ATM</h2>
            </div>

            <div class="hpanel">
                <g:if test='${flash.message && flash.success}'>
                    <div class="alert alert-block alert-success" style="margin: 5px;">
                        ${flash.message}
                    </div>
                </g:if>

                <g:if test='${flash.message && !flash.success}'>
                    <div class="alert alert-block alert-danger" style="margin: 5px;">
                        <h4 class="alert-heading">Login Error</h4>
                        ${flash.message}
                    </div>
                </g:if>

                <form action='${postUrl}' method='POST' id='loginForm'>
                    <div class="form-group"><label class="col-sm-5 control-label">Card Number:</label>
                        <input type="text" placeholder="**** **** **** ****" title="Please enter you card number" required="" value=""
                               name="cardNumber" id="cardNumber" class="form-control">
                    </div>
                    <div class="form-group"><label class="col-sm-5 control-label">Pin Number:</label>
                        <input type="text" placeholder="****" title="Please enter you pin number" required="" value=""
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