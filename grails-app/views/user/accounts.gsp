<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>GenRocket Bank ATM Menu</title>
  <meta name="layout" content="admin"/>
</head>

<body>
<div id="wrapper">
  <g:render template="/layouts/adminNavigation" model="[text: 'User\'s Accounts']"/>

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
            <table id="customerTable" class="table table-bordered table-striped table-condensed">
              <thead>
              <tr>
                <th>User Name</th>
                <th>Account Number</th>
                <th>Account Type</th>
                <th>Branch Name</th>
                <th>Balance</th>
                <th>Customer Level</th>
                <th>Daily Withdrawal Limit</th>
                <th>Monthly Max Transfers Allowed</th>
                <th>Overdraft Allowed</th>
                <th>Enabled</th>
                <th>Cards</th>
              </tr>
              </thead>
              <tbody>
              <g:each in="${customers}" var="customer">
                <tr>
                  <td><g:link controller="user" action="edit" id="${customer.user?.id}"
                              class="btn btn-default btn-mini">${customer.user.firstName} ${customer.user.lastName}</g:link></td>
                  <td>${customer.account.accountNumber}</td>
                  <td>${customer.account.accountType.name}</td>
                  <td>${customer.account.branch.name}</td>
                  <td>${customer.account.balance}</td>
                  <td>${customer.customerLevel.name}</td>
                  <td>${customer.customerLevel.dailyWithdrawalLimit}</td>
                  <td>${customer.customerLevel.monthlyMaxTransfersAllowed}</td>
                  <td>${customer.customerLevel.overdraftAllowed ? "Yes" : "No"}</td>
                  <td>${customer.enabled ? "Yes" : "No"}&nbsp;&nbsp;<g:if test="${customer.enabled}">
                    <g:link controller="user" action="disable" id="${customer?.id}"
                            class="btn btn-success btn-mini">Disable</g:link></g:if>
                    <g:else>
                      <g:link controller="user" action="enable" id="${customer?.id}"
                              class="btn btn-success btn-mini">Enable</g:link>
                    </g:else></td>
                  <td><g:link controller="card" action="list" params="[customerId: customer?.id]"
                              class="btn btn-primary2 btn-mini">Cards</g:link></td>
                </tr>
              </g:each>
              </tbody>
            </table>

          </div>

          <div class="panel-footer text-center">
            <g:link controller="account" action="create" params="[userId: user.id]"
                    class="btn btn-primary">New Account</g:link>
            <g:link controller="account" action="join" params="[userId: user.id]"
                    class="btn btn-success">Join Account</g:link>
          </div>
        </div>

      </div>
    </div>
  </div>
</div>
</body>
</html>