$(document).ready(function () {
  // Dropdown toggle
  $('#dropdownToggle').click(function (e) {
    e.stopPropagation();
    $('#dropdownMenu').toggle();
  });

  $(document).click(function () {
    $('#dropdownMenu').hide();
  });

  // Fungsi AJAX paparkan random homstay
  function loadRandomHomestays() {
    $("#homestayContainer").load("RandomHomestaysServlet");
  }

  // Mula-mula load & ulang setiap 10 saat
  loadRandomHomestays();
  setInterval(loadRandomHomestays, 10000);
  
});
