<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>GenRocket Bank ATM Menu</title>
  <meta name="layout" content="admin"/>
</head>

<body>
<div id="wrapper">
  <g:render template="/layouts/adminNavigation" model="[text: 'Edit Branch']"/>

  <div class="content animate-panel">
    <div class="row">
      <div class="col-lg-12">
        <div class="hpanel">
          <div class="panel-body">

            <g:hasErrors bean="${branch}">
              <div class="alert alert-block alert-danger">
                <a class="close" data-dismiss="alert">×</a>
                <g:renderErrors bean="${branch}" as="list"/>
              </div><p></p>
            </g:hasErrors>

            <g:form action="save" class="form-horizontal">
              <g:hiddenField name="id" value="${branch?.id}"/>

              <div class="form-group"><label class="col-sm-3 control-label">Branch Code</label>

                <div class="col-lg-5">
                  <g:textField name="branchCode" value="${branch?.branchCode}" class="form-control"/>
                </div>
              </div>

              <div class="form-group"><label class="col-sm-3 control-label">Name:</label>

                <div class="col-lg-5">
                  <g:textField name="name" value="${branch?.name}" class="form-control" maxlength="40"/>
                </div>
              </div>

              <div class="form-group"><label class="col-sm-3 control-label">Address:</label>

                <div class="col-lg-5">
                  <g:textField name="address" value="${branch?.address}" class="form-control" maxlength="50"/>
                </div>
              </div>

              <div class="form-group"><label class="col-sm-3 control-label">City:</label>

                <div class="col-lg-5">
                  <g:textField name="city" value="${branch?.city}" class="form-control" maxlength="25"/>
                </div>
              </div>

              <div class="form-group"><label class="col-sm-3 control-label">State:</label>

                <div class="col-lg-5">
                  <g:textField name="state" value="${branch?.state}" class="form-control" maxlength="25"/>
                </div>
              </div>

              <div class="form-group"><label class="col-sm-3 control-label">Zip Code:</label>

                <div class="col-lg-5">
                  <g:textField name="zipCode" value="${branch?.zipCode}" class="form-control" maxlength="10"/>
                </div>
              </div>

              <div class="form-group"><label class="col-sm-3 control-label">Country:</label>

                <div class="col-lg-5">
                  <g:textField name="country" value="${branch?.country}" class="form-control" maxlength="25"/>
                </div>
              </div>

              <div class="form-group"><label class="col-sm-3 control-label">Phone Number:</label>

                <div class="col-lg-5">
                  <g:textField name="phoneNumber" value="${branch?.phoneNumber}" class="form-control" maxlength="25"/>
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