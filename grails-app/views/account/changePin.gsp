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
        <h2>Change Pin</h2>
      </div>

      <div class="hpanel">
        <g:if test="${changePinCO?.hasErrors()}">
          <div class="alert alert-block alert-danger">
            <g:renderErrors bean="${changePinCO}" as="list"/>
          </div>
        </g:if>

        <form action='${createLink(controller: 'account', action: 'savePin')}' method='POST' id='changePinForm'>
          <div class="form-group"><label class="col-sm-5 control-label">Old Pin:</label>
            <input type="password" name="oldPinNumber" id="oldPinNumber" class="form-control">
          </div>

          <div class="form-group"><label class="col-sm-5 control-label">New Pin:</label>
            <input type="password" name="newPinNumber" id="newPinNumber" class="form-control">
          </div>

          <div class="form-group"><label class="col-sm-7 control-label">Confirm New Pin:</label>
            <input type="password" name="confirmPinNumber" id="confirmPinNumber" class="form-control">
          </div>
          <br/>

          <input type='submit' id="submit" class="btn btn-primary btn-block"
                 value='Change'/>
        </form>
      </div>
    </div>
  </div>
</div>
</div>
</body>
</html>