<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>GenRocket Bank ATM Menu</title>
  <meta name="layout" content="admin"/>
</head>

<body>
<div id="wrapper">
  <g:render template="/layouts/adminNavigation" model="[text: user.id ? 'Edit User' : 'Add User']"/>

  <div class="content animate-panel">
    <div class="row">
      <div class="col-lg-12">
        <div class="hpanel">
          <div class="panel-body">

            <g:hasErrors bean="${user}">
              <div class="alert alert-block alert-danger">
                <a class="close" data-dismiss="alert">×</a>
                <g:renderErrors bean="${user}" as="list"/>
              </div><p></p>
            </g:hasErrors>
            <g:form action="save" class="form-horizontal">
              <g:hiddenField name="id" value="${user?.id}"/>

              <div class="form-group"><label class="col-sm-3 control-label">Title</label>

                <div class="col-lg-5">
                  <g:textField name="title" value="${user?.title}" class="form-control" maxlength="5"/>
                </div>
              </div>

              <div class="form-group"><label class="col-sm-3 control-label">First Name:</label>

                <div class="col-lg-5">
                  <g:textField name="firstName" value="${user?.firstName}" class="form-control" maxlength="25"/>
                </div>
              </div>

              <div class="form-group"><label class="col-sm-3 control-label">Middle Initial:</label>

                <div class="col-lg-5">
                  <g:textField name="middleInitial" value="${user?.middleInitial}" class="form-control" maxlength="1"/>
                </div>
              </div>

              <div class="form-group"><label class="col-sm-3 control-label">Last Name:</label>

                <div class="col-lg-5">
                  <g:textField name="lastName" value="${user?.lastName}" class="form-control" maxlength="25"/>
                </div>
              </div>

              <div class="form-group"><label class="col-sm-3 control-label">Suffix:</label>

                <div class="col-lg-5">
                  <g:textField name="suffix" value="${user?.suffix}" class="form-control" maxlength="10"/>
                </div>
              </div>

              <div class="form-group"><label class="col-sm-3 control-label">User Name:</label>

                <div class="col-lg-5">
                  <g:textField name="username" value="${user?.username}" class="form-control" maxlength="25"/>
                </div>
              </div>

              <div class="form-group"><label class="col-sm-3 control-label">Email Address:</label>

                <div class="col-lg-5">
                  <g:textField name="emailAddress" value="${user?.emailAddress}" class="form-control" maxlength="50"/>
                </div>
              </div>

              <div class="form-group"><label class="col-sm-3 control-label">Phone Number:</label>

                <div class="col-lg-5">
                  <g:textField name="phoneNumber" value="${user?.phoneNumber}" class="form-control" maxlength="25"/>
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