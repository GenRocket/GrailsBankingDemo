<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>GenRocket Bank ATM Menu</title>
  <meta name="layout" content="admin"/>
</head>

<body>
<div id="wrapper">
  <g:render template="/layouts/adminNavigation" model="[text: 'Cards']"/>

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
            <table id="cardTable" class="table table-bordered table-striped table-condensed">
              <thead>
              <tr>
                <th>User Name</th>
                <th>Account Number</th>
                <th>Name on Card</th>
                <th>Card Number</th>
                <th>Security Code</th>
                <th>Card Type</th>
                <th>Issued Date</th>
                <th>Expiry Date</th>
                <th>Activated Date</th>
                <th>DeActivated Date</th>
                <th>Enabled</th>
                <th>Actions</th>
              </tr>
              </thead>
              <tbody>
              <g:each in="${cards}" var="card">
                <tr>
                  <td><g:link controller="user" action="edit" id="${card.customer.user?.id}"
                              class="btn btn-default btn-mini">${card.customer.user.firstName} ${card.customer.user.lastName}</g:link></td>
                  <td><g:link controller="user" action="accounts" id="${card.customer.user?.id}"
                              class="btn btn-default btn-mini">${card.customer.account.accountNumber}</g:link></td>
                  <td>${card.nameOnCard}</td>
                  <td>${card.cardNumber}</td>
                  <td>${card.securityCode}</td>
                  <td>${card.cardType.name}</td>
                  <td>${card.dateIssued ? card.dateIssued.format("MMM dd, yyyy") : "Not Issued"}</td>
                  <td>${card.dateExpired ? card.dateExpired.format("MMM dd, yyyy") : ""}</td>
                  <td>${card.dateActivated ? card.dateActivated.format("MMM dd, yyyy") : ""}</td>
                  <td>${card.dateDeactivated ? card.dateDeactivated.format("MMM dd, yyyy") : ""}</td>
                  <td>${card.enabled ? "Yes" : "No"}&nbsp;&nbsp;</td>
                  <td><g:if test="${card.enabled}">
                    <g:link controller="card" action="disable" id="${card?.id}"
                            class="btn btn-success btn-mini">Disable</g:link></g:if>
                    <g:else>
                      <g:link controller="card" action="enable" id="${card?.id}"
                              class="btn btn-success btn-mini">Enable</g:link>
                    </g:else> <g:if test="${!card.dateDeactivated}">
                    <g:link controller="card" action="deactivate" id="${card?.id}"
                            class="btn btn-danger btn-mini">DeActivate</g:link>
                  </g:if></td>
                </tr>
              </g:each>
              </tbody>
            </table>

          </div>

          <div class="panel-footer text-center">
            %{-- <g:link controller="account" action="create" params="[userId: user.id]"
                     class="btn btn-primary">New Account</g:link>
             <g:link controller="account" action="join" params="[userId: user.id]"
                     class="btn btn-success">Join Account</g:link>--}%
          </div>
        </div>

      </div>
    </div>
  </div>
</div>
</body>
</html>