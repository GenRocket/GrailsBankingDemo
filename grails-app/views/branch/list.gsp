<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>GenRocket Bank ATM Menu</title>
  <meta name="layout" content="admin"/>
</head>

<body>
<div id="wrapper">
  <g:render template="/layouts/adminNavigation" model="[text: 'Branches']"/>

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
            <table id="branchTable" class="table table-bordered table-striped table-condensed">
              <thead>
              <tr>
                <th>Branch Name</th>
                <th>Branch Code</th>
                <th>Address</th>
                <th>City</th>
                <th>State</th>
                <th>Zip Code</th>
                <th>Country</th>
                <th>Phone Number</th>
              </tr>
              </thead>
              <tbody>
              <g:each in="${branches}" var="branch">
                <tr>
                  <td>${branch.name}<g:link controller="branch" action="edit" id="${branch.id}" class="fl-rt"><i
                      class="fa fa-edit"></i></g:link></td>
                  <td>${branch.branchCode}</td>
                  <td>${branch.address}</td>
                  <td>${branch.city}</td>
                  <td>${branch.state}</td>
                  <td>${branch.zipCode}</td>
                  <td>${branch.country}</td>
                  <td>${branch.phoneNumber}</td>
                </tr>
              </g:each>
              </tbody>
            </table>
            <div class="pagination pull-right">
              <g:paginate total="${count ?: 0}"/>
            </div>
          </div>

          <div class="panel-footer text-center">
            <g:link controller="branch" action="edit" class="btn btn-info">Add New Branch</g:link>
          </div>
        </div>

      </div>
    </div>
  </div>
</div>
</body>
</html>