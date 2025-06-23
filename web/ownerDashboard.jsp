<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Booking" %>
<%@ page import="java.time.LocalDate" %>

<%
  String fullName      = (String) session.getAttribute("full_name");
  int    totalHomestay = (request.getAttribute("totalHomestay") != null)
                         ? Integer.parseInt(request.getAttribute("totalHomestay").toString())
                         : 0;

  List<Booking> tempahan = (List<Booking>) request.getAttribute("tempahanTerkini");
  LocalDate today = LocalDate.now();
%>

<!DOCTYPE html>
<html>
    <head>
      <title>HomestayFinder - Owner Dashboard</title>
      <link rel="stylesheet" href="css/ownerDashboard.css">
    </head>
    <body>

    <!--Sidebar-->
    <div class="sidebar">
      <h2 class="logo"><a href="homepage.jsp">HomestayFinder</a></h2>
      <ul class="nav">
        <li><a href="OwnerDashboardServlet" class="active">Dashboard</a></li>
        <li><a href="MyHomestayServlet">My Homestay List</a></li>
        <li><a href="BookingServlet">Manage Booking</a></li>
        <li><a href="UpdateProfileServlet">Update Profile</a></li>
        <li><a href="LogoutServlet">Log Out</a></li>
      </ul>
    </div>

    <div class="main">

      <!-- Header -->
      <div class="topbar">
        <h1>Welcome <%= (fullName != null) ? fullName : "" %></h1>
      </div>

      <!-- Kad Ringkas -->
      <div class="cards">
        <div class="card">
          <h3>Total Homestay</h3>
          <p><%= totalHomestay %></p>
        </div>
      </div>

      <!-- Bahagian Bawah -->
      <div class="dashboard-bottom">

        <!-- Tempahan Terkini -->
        <div class="bookings">
            <h2>Latest Booking</h2>

            <%
              boolean adaTempahan = false;
              if (tempahan != null) {
                for (Booking b : tempahan) {

                  // Skip tempahan yang sudah tamat
                  LocalDate checkOut = LocalDate.parse(b.getCheckOut());
                  if (checkOut.isBefore(today)) continue;

                  adaTempahan = true;
            %>

              <div class="booking-item">
                <img src="<%= (b.getImagePath() != null) ? "FileServeServlet?name=" + new java.io.File(b.getImagePath()).getName() : "img/placeholder.png" %>" alt="Homestay image">
                <div>
                  <p class="homestay-name"><%= b.getHomestayName() %></p>
                  <p><%= b.getCheckIn() %> – <%= b.getCheckOut() %></p>
                  <p>RM <%= String.format("%.2f", b.getTotalPrice()) %></p>
                  <p>
                    <span class="status <%= "pending".equalsIgnoreCase(b.getStatus()) ? "aktif" : "selesai" %>">
                      <%= b.getStatus() %>
                    </span>
                  </p>
                </div>
              </div>

            <%
                }
              }

              if (!adaTempahan) {
            %>
                <p>No reservations found.</p>
            <%
              }
            %>
         </div>

        <!-- Kalendar -->
        <div class="chart">
          <h2>Calendar</h2>
          <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:2%;">
            <button id="prevMonth">←</button>
            <div id="calendar-title-display" style="font-weight:bold;"></div>
            <button id="nextMonth">→</button>
          </div>
          <div class="calendar-widget"><!-- calendar.js akan isi --></div>
        </div>

      </div>
    </div>

    <script src="js/calendar.js"></script>
    </body>
</html>
