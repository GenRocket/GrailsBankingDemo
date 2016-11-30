<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>GenRocket Bank ATM Menu</title>
  <meta name="layout" content="admin"/>
</head>

<body>
<div id="wrapper">
  <g:render template="/layouts/adminNavigation" model="[text: 'Users']"/>

  <div class="content animate-panel">
    <div class="row">
      <div class="col-lg-12">
        <div class="hpanel">
          <div class="panel-body">
            <g:if test="${flash.message}">
              <div class="alert alert-success">
                <a class="close" data-dismiss="alert">×</a>
                ${flash.message}
              </div>

              <p></p>
            </g:if>
            <table id="userTable" class="table table-bordered table-striped table-condensed">
              <thead>
              <tr>
                <th>Title</th>
                <th>First Name</th>
                <th>Middle Initial</th>
                <th>Last Name</th>
                <th>Suffix</th>
                <th>User Name</th>
                <th>Email Address</th>
                <th>Phone Number</th>
                <th>Accounts</th>
              </tr>
              </thead>
              <tbody>
              <g:each in="${users}" var="user">
                <tr>
                  <td>${user.title}</td>
                  <td>${user.firstName}<g:link controller="user" action="edit" id="${user.id}" class="fl-rt"><i
                      class="fa fa-edit"></i></g:link></td>
                  <td>${user.middleInitial}</td>
                  <td>${user.lastName}</td>
                  <td>${user.suffix}</td>
                  <td>${user.username}</td>
                  <td>${user.emailAddress}</td>
                  <td>${user.phoneNumber}</td>
                  <td><g:link controller="user" action="accounts" id="${user?.id}"
                              class="btn btn-primary2 btn-mini">Accounts</g:link>
                    <g:link controller="account" action="create" params="[userId: user.id]"
                            class="btn btn-info btn-mini">New Account</g:link>
                    <g:link controller="account" action="join" params="[userId: user.id]"
                            class="btn btn-success btn-mini">Join Account</g:link></td>
                </tr>
              </g:each>
              </tbody>
            </table>

            <div class="grailsPagination">
              <div class="pagination pull-right">
                <g:paginate total="${count ?: 0}"/>
              </div>
            </div>
          </div>

          <div class="panel-footer text-center">
            <g:link controller="user" action="edit" class="btn btn-primary">Add New User</g:link>
          </div>
        </div>

      </div>
    </div>
  </div>
</div>
</body>
</html>