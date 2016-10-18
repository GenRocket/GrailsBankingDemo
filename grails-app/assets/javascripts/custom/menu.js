$(function() {
  $("#exit-menu").click(function() {
     window.location.href = contextPath + "/home/exit"
  });

  $("#withdrawal-menu").click(function() {
      window.location.href = contextPath + "/account/withdrawal"
  });
});