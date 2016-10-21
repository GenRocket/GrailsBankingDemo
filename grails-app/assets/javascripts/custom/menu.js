$(function () {
  $("#exit-menu").click(function () {
    window.location.href = contextPath + "/home/exit"
  });

  $("#withdrawal-menu").click(function () {
    window.location.href = contextPath + "/account/withdrawal"
  });

  $("#withdrawal").submit(function () {
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

  $("#moveToMenu").click(function() {
    window.location.href = contextPath + "/home/menu"
  });
});

function isValid(val) {
  return val != "" && val != null && val != undefined;
}