$(document).ready(function () {
  const urlParams = new URLSearchParams(window.location.search);
  const error = (urlParams.get("error") || "").trim().toLowerCase();

  console.log("Error param:", error);

  if (error === "duplicate") {
    showPopup("E-mel telah digunakan. Sila guna e-mel lain.");
  } else if (error === "fail") {
    showPopup("Pendaftaran gagal. Sila cuba semula.");
  }

  $("#popupClose").click(function () {
    $("#popupBox").fadeOut();
  });

  function showPopup(message) {
    $("#popupMessage").text(message);
    $("#popupBox").fadeIn();
  }
});
