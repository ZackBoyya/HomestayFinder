<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Booking" %>

<%
    /*  nilai diberi oleh CustomerDashboardServlet  */
    String fullName            = (String) session.getAttribute("full_name");
    int tempahanAktif          = (request.getAttribute("aktif")     != null) ? (Integer) request.getAttribute("aktif")     : 0;
    int tempahanLalu           = (request.getAttribute("lalu")      != null) ? (Integer) request.getAttribute("lalu")      : 0;
    int tempahanTertunda       = (request.getAttribute("tertunda")  != null) ? (Integer) request.getAttribute("tertunda")  : 0;
    List<Booking> terkini      = (List<Booking>) request.getAttribute("tempahanTerkini");
%>

<!DOCTYPE html>
<html>
    <head>
      <title>HomestayFinder - CustomerDashboard</title>
      <link rel="stylesheet" href="css/customerDashboard.css">
      <script src="js/calendar.js"></script>
    </head>
    <body>

        <!-- ===== Sidebar ===== -->
        <div class="sidebar">
          <h2 class="logo"><a href="homepage.jsp">HomestayFinder</a></h2>
          <ul class="nav">
            <li><a href="CustomerDashboardServlet" class="active">Dashboard</a></li>
            <li><a href="CustomerBookingsServlet">My Reservation</a></li>
            <li><a href="UpdateProfileServlet">Update Profile</a></li>
            <li><a href="LogoutServlet">Log Out</a></li>
          </ul>
        </div>

        <!-- ===== Main ===== -->
        <div class="main">

          <!-- Banner -->
          <h1 class="welcome">Welcome, <%= (fullName != null) ? fullName : "" %></h1>

          <!-- Kad Ringkasan -->
          <div class="cards">
            <div class="card">
              <h3>Active Reservation</h3>
              <p><%= tempahanAktif %></p>
            </div>
            <div class="card">
              <h3>History Reservation</h3>
              <p><%= tempahanLalu %></p>
            </div>
            <div class="card">
              <h3>Pending Reservation</h3>
              <p><%= tempahanTertunda %></p>
            </div>
          </div>

          <!-- Bahagian Bawah -->
          <div class="bottom">
            <!-- Tempahan Terkini -->
            <div class="recent">
              <h2>Latest Booking</h2>

              <%
                if (terkini != null && !terkini.isEmpty()) {
                  for (Booking b : terkini) {
              %>
                <div class="booking-item">
                  <img src="<%= (b.getImagePath() != null) ? "FileServeServlet?name=" + new java.io.File(b.getImagePath()).getName() : "img/placeholder.png" %>" alt="Gambar Homestay">
                  <div class="info">
                    <p class="name"><%= b.getHomestayName() %></p>
                    <p><%= b.getCheckIn() %> – <%= b.getCheckOut() %></p>
                    <p>RM <%= b.getTotalPrice() %></p>
                    <span class="status <%= b.getStatus() %>"><%= b.getStatus() %></span>
                  </div>
                </div>
              <%
                  }
                } else {
              %>
                <p>No recent bookings.</p>
              <%
                }
              %>
            </div>

            <!-- Kalendar -->
            <div class="calendar-box">
              <h2>Calendar</h2>
              <div class="calendar-nav">
                <button id="prevMonth">←</button>
                <div id="calendar-title-display" class="title"></div>
                <button id="nextMonth">→</button>
              </div>
              <div class="calendar-widget"><!-- calendar.js akan isi --></div>
            </div>
          </div>

        </div>

    </body>
</html>
