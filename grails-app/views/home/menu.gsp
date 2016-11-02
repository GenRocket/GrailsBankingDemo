<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Welcome to GenRocket Bank ATM</title>
  <meta name="layout" content="main"/>
  <asset:javascript src="custom/menu.js"/>
</head>

<body>
<div class="content animate-panel m-t-xxxl" id="bank-menu">
  <div class="col-lg-12">
    <g:if test="${flash.error}">
      <div class="alert alert-block alert-danger">
        ${flash.error}
      </div>
    </g:if>
    <g:if test="${flash.message}">
      <div class="alert alert-block alert-info">
        ${flash.message}
      </div>
    </g:if>
    <div class="text-center m-b-md p-m">
      <h2>Account Type: ${accountType?.name}</h2>
    </div>

    <div class="row">
      <div class="col-sm-6">
        <div class="hpanel plan-box hyellow" id="balance-menu">
          <div class="panel-heading hbuilt text-center">
            <h4 class="font-bold h-bg-navy-blue">Balance</h4>
          </div>
        </div>
      </div>

      <div class="col-sm-6">
        <div class="hpanel plan-box hgreen" id="deposit-menu">
          <div class="panel-heading hbuilt text-center">
            <h4 class="font-bold h-bg-navy-blue">Deposit</h4>
          </div>
        </div>
      </div>

      <div class="col-sm-6">
        <div class="hpanel plan-box hblue" id="withdrawal-menu">
          <div class="panel-heading hbuilt text-center">
            <h4 class="font-bold h-bg-navy-blue">Withdrawal</h4>
          </div>
        </div>
      </div>

      <div class="col-sm-6">
        <div class="hpanel plan-box hred" id="transfer-menu">
          <div class="panel-heading hbuilt text-center">
            <h4 class="font-bold h-bg-navy-blue">Transfer</h4>
          </div>
        </div>
      </div>

      <div class="col-sm-6">
        <div class="hpanel plan-box hred" id="change-pin">
          <div class="panel-heading hbuilt text-center">
            <h4 class="font-bold h-bg-navy-blue">Change Pin</h4>
          </div>
        </div>
      </div>

      <div class="col-sm-6">
        <div class="hpanel plan-box hred" id="history-menu">
          <div class="panel-heading hbuilt text-center">
            <h4 class="font-bold h-bg-navy-blue">Transaction History</h4>
          </div>
        </div>
      </div>

      <div class="col-sm-6">
        <div class="hpanel plan-box hred" id="exit-menu">
          <div class="panel-heading hbuilt text-center">
            <h4 class="font-bold h-bg-navy-blue">Exit</h4>
          </div>
        </div>
      </div>

    </div>
  </div>

</div>
</body>
</html>