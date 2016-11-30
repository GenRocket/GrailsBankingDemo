<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>GenRocket Bank ATM Menu</title>
  <meta name="layout" content="admin"/>
</head>

<body>
<div id="wrapper">
  <g:render template="/layouts/adminNavigation"
            model="[text: 'New Account for ' + user.firstName + ' ' + user.lastName]"/>

  <div class="content animate-panel">
    <div class="row">
      <div class="col-lg-12">
        <div class="hpanel">
          <div class="panel-body">

            <g:hasErrors bean="${accountCO}">
              <div class="alert alert-block alert-danger">
                <a class="close" data-dismiss="alert">×</a>
                <g:renderErrors bean="${accountCO}" as="list"/>
              </div>

              <p></p>
            </g:hasErrors>

            <g:form action="save" class="form-horizontal">
              <g:hiddenField name="userId" value="${user.id}"/>
              <div class="form-group"><label class="col-sm-3 control-label">Branch:</label>

                <div class="col-lg-5">
                  <g:select from="${branches}" value="${accountCO?.branchId}" name="branchId" optionKey="id"
                            optionValue="name" class="form-control"/>
                </div>
              </div>

              <div class="form-group"><label class="col-sm-3 control-label">Account Type:</label>

                <div class="col-lg-5">
                  <g:select from="${accountTypes}" name="accountTypeId" optionKey="id" optionValue="name"
                            value="${accountCO?.accountTypeId}"
                            class="form-control"/>
                </div>
              </div>


              <div class="form-group"><label class="col-sm-3 control-label">Customer Level:</label>

                <div class="col-lg-5">
                  <g:select from="${customerLevels}" name="customerLevelId" optionKey="id" optionValue="name"
                            value="${accountCO?.customerLevelId}"
                            class="form-control"/>
                </div>
              </div>

              <div class="form-group"><label class="col-sm-3 control-label">Card Type</label>

                <div class="col-lg-5">
                  <g:select from="${cardTypes}" name="cardTypeId" optionKey="id" optionValue="name"
                            value="${accountCO?.cardTypeId}"
                            class="form-control"/>
                </div>
              </div>


              <div class="col-lg-5 control-label">
                <g:submitButton name="save" class="btn btn-info" value="Save"/>
              </div>
            </g:form>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>