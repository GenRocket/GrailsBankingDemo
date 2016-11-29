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
                  <td>${branch.name}</td>
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
          </div>
        </div>

        <div class="pagination">
          <g:paginate total="${count ?: 0}"/>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>