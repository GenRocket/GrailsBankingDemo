<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>GenRocket Bank ATM Menu</title>
  <meta name="layout" content="admin"/>
</head>

<body>
<div id="wrapper">
  <g:render template="/layouts/adminNavigation" model="[text: 'Join an Account']"/>

  <div class="content animate-panel">
    <div class="row">
      <div class="col-lg-12">
        <div class="hpanel">
          <div class="panel-body">

            <g:hasErrors bean="${associateAccountCO}">
              <div class="alert alert-block alert-danger">
                <a class="close" data-dismiss="alert">×</a>
                <g:renderErrors bean="${associateAccountCO}" as="list"/>
              </div>

              <p></p>
            </g:hasErrors>

            <g:form action="associate" class="form-horizontal">
              <div class="form-group"><label class="col-sm-3 control-label">User</label>
                <g:hiddenField name="userId" value="${user.id}"/>
                <div class="col-lg-5">
                  <label class="control-label">
                    <g:link controller="user" action="edit" style="text-decoration: underline;"
                            id="${user.id}">${user.firstName} ${user.middleInitial} ${user.lastName}</g:link>
                  </label>
                </div>
              </div>

              <div class="form-group"><label class="col-sm-3 control-label">Customer Level:</label>

                <div class="col-lg-5">
                  <g:select from="${customerLevels}" name="customerLevelId" optionKey="id" optionValue="name"
                            class="form-control"/>
                </div>
              </div>

              <div class="form-group"><label class="col-sm-3 control-label">Card Type</label>

                <div class="col-lg-5">
                  <g:select from="${cardTypes}" name="cardTypeId" optionKey="id" optionValue="name"
                            class="form-control"/>
                </div>
              </div>

              <div class="form-group"><label class="col-sm-3 control-label">Enter the account number to join:</label>

                <div class="col-lg-5">
                  <g:textField name="accountNumber" class="form-control"/>
                </div>
              </div>

              <div class="col-lg-5 control-label">
                <g:submitButton name="save" class="btn btn-info" value="Associate"/>
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