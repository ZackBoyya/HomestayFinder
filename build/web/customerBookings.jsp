<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="model.Booking" %>

<%
  String fullName = (String) session.getAttribute("full_name");
  List<Booking> bookings = (List<Booking>) request.getAttribute("bookings");
  String tab = request.getParameter("tab");
  if (tab == null) tab = "aktif";
  java.time.LocalDate hariIni = java.time.LocalDate.now();
  String msg = request.getParameter("msg");
%>

<!DOCTYPE html>
<html>
    <head>
      <title>HomestayFinder - My Reservation</title>
      <link rel="stylesheet" href="css/customerBookings.css">
    </head>
    <body>

        <!-- Sidebar -->
        <div class="sidebar">
          <h2 class="logo"><a href="homepage.jsp">HomestayFinder</a></h2>
          <ul class="nav">
            <li><a href="CustomerDashboardServlet">Dashboard</a></li>
            <li><a href="CustomerBookingsServlet" class="active">My Reservation</a></li>
            <li><a href="UpdateProfileServlet">Update Profile</a></li>
            <li><a href="LogoutServlet">Log Out</a></li>
          </ul>
        </div>

        <!-- Main -->
        <div class="main">
            <h1>Booking by <%= fullName %></h1>
            
            <% if ("success".equals(msg)) { %>
            <div class="popup-success">
                ✅ Your booking has been successfully sent!
            </div>
            <% } %>

            <!-- Tabs -->
            
            <div class="tabs">
              <a href="CustomerBookingsServlet?tab=aktif" class="tab <%= "aktif".equals(tab) ? "active" : "" %>">Active</a>
              <a href="CustomerBookingsServlet?tab=lalu" class="tab <%= "lalu".equals(tab) ? "active" : "" %>">Past</a>
              <a href="CustomerBookingsServlet?tab=tertunda" class="tab <%= "tertunda".equals(tab) ? "active" : "" %>">Pending</a>
            </div>

            <!-- Senarai Tempahan -->
            <% boolean adaData = false;
               // Logik untuk tentukan sama ada tempahan dipaparkan pada tab semasa
               if (bookings != null) {
                 for (Booking b : bookings) {
                   java.time.LocalDate checkOut = java.time.LocalDate.parse(b.getCheckOut());
                   boolean sesuai = ("aktif".equals(tab) && !checkOut.isBefore(hariIni) && !"pending".equals(b.getStatus())) ||
                                    ("lalu".equals(tab)  && checkOut.isBefore(hariIni)) ||
                                    ("tertunda".equals(tab) && "pending".equals(b.getStatus()));

                   if (sesuai) {
                     adaData = true;
            %>
              <div class="booking-item">
                <img src="<%= (b.getImagePath() != null) ? "FileServeServlet?name=" + new java.io.File(b.getImagePath()).getName() : "img/placeholder.png" %>" alt="Homestay" width="120">
                <div class="booking-info">
                  <strong><%= b.getHomestayName() %></strong>
                  <p>Check in/out Date: <%= b.getCheckIn() %> – <%= b.getCheckOut() %></p>
                  <p>Guest: <%= b.getTotalGuests() %></p>
                  <p>Total Payment: RM <%= String.format("%.2f", b.getTotalPrice()) %></p>
                  <p>Status: <span class="status <%= b.getStatus().toLowerCase() %>"><%= b.getStatus() %></span></p>
                </div>
              </div>
            <%   }
               }
             }

             if (!adaData) { %>
               <p>No bookings found in this category.</p>
            <% } %>
         </div>
    </body>
</html>
