<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Homestay" %>

<%
  String fullName = (String) session.getAttribute("full_name");
  List<Homestay> homestays = (List<Homestay>) request.getAttribute("homestays");
%>

<!DOCTYPE html>
<html>
    <head>
      <title>HomestayFinder - MyHomestay</title>
      <link rel="stylesheet" href="css/myHomestays.css">
    </head>
    <body>
        <!-- Sidebar -->
        <div class="sidebar">
            <h2 class="logo"><a href="homepage.jsp">HomestayFinder</a></h2>
            <ul class="nav">
              <li><a href="OwnerDashboardServlet">Dashboard</a></li>
              <li><a href="MyHomestayServlet" class="active">My Homestay List</a></li>
              <li><a href="BookingServlet">Manage Booking</a></li>
              <li><a href="UpdateProfileServlet">Update Profile</a></li>
              <li><a href="LogoutServlet">Log Out</a></li>
            </ul>
        </div>

        <!-- Main -->
        <div class="main">
          <div class="topbar">
              
            <!-- Butang tambah homestay -->
            <div style="margin-bottom: 20px;">
              <a href="addHomestay.jsp" class="btn-add">+ Add New Homestay</a>
            </div>
              
            <h1>Homestay List for <%= fullName %></h1>
          </div>

          <!--Check kewujudan homestay  untuk owner-->
          <% if (homestays != null && !homestays.isEmpty()) { 
               for (Homestay h : homestays) { %>

            <div class="homestay-card">
              <img src="<%= (h.getImagePath() != null) ? "FileServeServlet?name=" + new java.io.File(h.getImagePath()).getName() : "img/placeholder.png" %>" alt="Gambar Homestay">
              <div class="details">
                <h2><%= h.getName() %></h2>
                <p><%= h.getAddress() %>, <%= h.getCity() %>, <%= h.getState() %></p>
                <p><strong>Price / Night:</strong> RM <%= String.format("%.2f", h.getPricePerNight()) %></p>
                <p><strong>Max Guest:</strong> <%= h.getMaxGuests() %></p>

                <!-- Kemudahan -->
                <div class="amenities">
                  <p><img src="img/wifi.png" alt="WiFi"> WiFi: <%= h.isHasWifi() ? "✓ Available" : "✗ Not Available" %></p>
                  <p><img src="img/parking.png" alt="Parking"> Parking: <%= h.isHasParking() ? "✓ Available" : "✗ Not Available" %></p>
                  <p><img src="img/aircond.png" alt="Aircond"> Aircond: <%= h.isHasAircond() ? "✓ Available" : "✗ Not Available" %></p>
                  <p><img src="img/tv.png" alt="TV"> Television: <%= h.isHasTv() ? "✓ Available" : "✗ Not Available" %></p>
                  <p><img src="img/kitchen.png" alt="Kitchen"> Kitchen: <%= h.isHasKitchen() ? "✓ Available" : "✗ Not Available" %></p>
                  <p><img src="img/washing.png" alt="Mesin Basuh"> Washing Machine: <%= h.isHasWashingMachine() ? "✓ Available" : "✗ Not Available" %></p>
                  <p><img src="img/bedroom.png" alt="Bilik Tidur"> Badroom: <%= h.getNumBedrooms() %></p>
                  <p><img src="img/bathroom.png" alt="Bilik Air"> BathRoom: <%= h.getNumBathrooms() %></p>
                </div>

                <!-- Butang delete dan edit -->
                <div class="actions">
                  <a href="EditHomestayServlet?id=<%= h.getHomestayId() %>" class="btn-edit">Edit</a>
                  <form action="DeleteHomestayServlet" method="post" onsubmit="return confirm('Delete this homestay?');" style="display:inline;">
                    <input type="hidden" name="homestay_id" value="<%= h.getHomestayId() %>">
                    <button type="submit" class="btn-delete">Delete</button>
                  </form>
                </div>
              </div>

            </div>

          <% } 
             } else { %>
            <p>No homestays are registered.</p>
          <% } %>

        </div>
    </body>
</html>
