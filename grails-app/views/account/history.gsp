<%@ page import="java.text.DecimalFormat" contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Welcome to GenRocket Bank ATM</title>
  <meta name="layout" content="main"/>
  <asset:javascript src="custom/menu.js"/>
  <asset:javascript src="vendor/datatables/media/js/jquery.dataTables.min.js"/>
  <asset:javascript src="vendor/datatables.net-bs/js/dataTables.bootstrap.min.js"/>
</head>

<body>
<g:render template="/layouts/header"/>
<div class="col-md-9">
    <div class="row">
      <div class="col-md-12">
        <div class="text-center m-b-md m-t-xl">
          <h2>Transaction History</h2>
        </div>

        <div class="hpanel">
          <table id="transactionHistory" class="table table-striped table-bordered">
            <thead>
            <tr>
              <th>Account Number</th>
              <th>Account Type</th>
              <th>Transaction Type</th>
              <th>Amount</th>
              <th>Transaction Date</th>
            </tr>
            </thead>
            <tbody>
            <g:each in="${transactions}" var="transaction">
              <tr>
                <td>${transaction.account.accountNumber}</td>
                <td>${transaction.account.accountType.name}</td>
                <td>${transaction.transactionType.name}</td>
                <td>$${transaction.amount.format()}</td>
                <td>${transaction.dateCreated.format("MMM dd, yyyy")}</td>
              </tr>
            </g:each>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
<script>

  $(function () {
    $('#transactionHistory').dataTable({
      "sDom": "<''<'col-sm-5'l><'pull-right'f>r>t<''<'col-sm-5'i><'pull-right'p>>",
      "bPaginate": true,
      "bLengthChange": false,
      "iDisplayLength": 10,
      "bFilter": false,
      "bSort": false,
      "bInfo": true,
      "bAutoWidth": true,
      oLanguage: {
        sSearch: ""
      },
      "bDestroy": true
    });
  });

</script>
</body>
</html>