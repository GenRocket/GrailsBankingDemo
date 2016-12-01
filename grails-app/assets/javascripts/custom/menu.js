$(function () {
  $("#exit-menu").click(function () {
    window.location.href = contextPath + "/home/exit"
  });

  $("#manage-menu").click(function () {
    window.location.href = contextPath + "/branch/list"
  });

  $("#withdrawal-menu").click(function () {
    window.location.href = contextPath + "/account/withdrawal"
  });

  $("#deposit-menu").click(function () {
    window.location.href = contextPath + "/account/deposit"
  });

  $("#transfer-menu").click(function () {
    window.location.href = contextPath + "/account/transfer"
  });

  $("#withdrawalDeposit").submit(function () {
    var errorContainer = $("#withdrawalErrorContainer");
    errorContainer.html("");
    var amount = $("#amount").val();
    var valid = isValid(amount);
    valid = valid ? !isNaN(amount) : false;
    if (!valid) {
      errorContainer.html('<div class="alert alert-block alert-danger"><ul><li>Please enter valid amount.</li></ul></div>');
    }
    return valid;
  });

  $("#balance-menu").click(function() {
    window.location.href = contextPath + "/account/balance"
  });

  $("#change-pin").click(function() {
    window.location.href = contextPath + "/account/changePin"
  });

  $("#history-menu").click(function() {
    window.location.href = contextPath + "/account/history"
  });

});

function isValid(val) {
  return val != "" && val != null && val != undefined;
}