<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Booking" %>

<%
  String ownerName = (String) session.getAttribute("full_name");
  List<Booking> bookings = (List<Booking>) request.getAttribute("bookings");
%>

<!DOCTYPE html>
<html>
    <head>
      <title>HomestayFinder - ManageBookings</title>
      <link rel="stylesheet" href="css/manageBookings.css">
    </head>
    <body>

      <!-- Sidebar -->
      <div class="sidebar">
      <h2 class="logo"><a href="homepage.jsp">HomestayFinder</a></h2>
      <ul class="nav">
        <li><a href="OwnerDashboardServlet">Dashboard</a></li>
        <li><a href="MyHomestayServlet">My Homestay List</a></li>
        <li><a href="BookingServlet" class="active">Manage Booking</a></li>
        <li><a href="UpdateProfileServlet">Update Profile</a></li>
        <li><a href="LogoutServlet">Log Out</a></li>
      </ul>
    </div>

      <!-- Main -->
      <div class="main">
        <h1>Manage Booking by <%= ownerName %></h1>

        <!--Check ada book atau tidak-->
        <% if (bookings != null && !bookings.isEmpty()) {
             for (Booking b : bookings) {
        %>
          <div class="booking-item">
            <div class="booking-details">
              <img src="<%= (b.getImagePath() != null) ? "FileServeServlet?name=" + new java.io.File(b.getImagePath()).getName() : "img/placeholder.png" %>" alt="Homestay" width="120">
              <div class="booking-info">
                <strong><%= b.getHomestayName() %></strong>
                <p>Check In: <%= b.getCheckIn() %></p>
                <p>Check Out: <%= b.getCheckOut() %> | Guest: <%= b.getTotalGuests() %></p>
                <p>By: <%= b.getCustomerName() %></p>
              </div>
            </div>
            <!--Butang approve reject-->  
            <div class="booking-actions">
                <% if ("pending".equalsIgnoreCase(b.getStatus())) { %>
                <form method="post" action="UpdateBookingStatusServlet" style="display:inline;">
                  <input type="hidden" name="booking_id" value="<%= b.getBookingId() %>">
                  <input type="hidden" name="action" value="approve">
                  <button type="submit" class="approve">Approve</button>
                </form>
                <form method="post" action="UpdateBookingStatusServlet" style="display:inline;">
                  <input type="hidden" name="booking_id" value="<%= b.getBookingId() %>">
                  <input type="hidden" name="action" value="reject">
                  <button type="submit" class="reject">Reject</button>
                </form>
                <% } else { %>
                  <span class="status <%= b.getStatus().toLowerCase() %>"><%= b.getStatus() %></span>
                <% } %>

            </div>
          </div>
        <% 
             }
           } else {
        %>
          <p>No booking found.</p>
        <% } %>
      </div>

    </body>
</html>
