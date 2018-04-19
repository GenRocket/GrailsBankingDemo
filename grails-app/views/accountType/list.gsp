<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>GenRocket Bank ATM Menu</title>
  <meta name="layout" content="main"/>
</head>

<body>
<div class="content animate-panel">
  <div class="row">
    <div class="col-lg-12">
      <div class="hpanel">
        <div class="panel-body">
          <p>Account Types</p>
          <table id="cardTable" class="table table-bordered table-striped table-condensed">
            <thead>
            <tr>
              <th>Id</th>
              <th>Name</th>
            </tr>
            </thead>
            <tbody>
            <g:each in="${accountTypes}" var="accountType">
              <tr>
                <td>${accountType.id}</td>
                <td>${accountType.name}</td>
              </tr>
            </g:each>
            </tbody>
          </table>

        </div>

      </div>

    </div>
  </div>
</div>
</body>
</html>