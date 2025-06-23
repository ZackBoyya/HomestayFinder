<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="javax.servlet.http.HttpSession" %>

<%
  String fullName = (String) session.getAttribute("full_name");
%>

<!DOCTYPE html>
<html>
    <head>
      <title>HomestayFinder - AddHomestay</title>
      <link rel="stylesheet" href="css/addHomestay.css">
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
            <h1>Add New Homestay for <%= fullName %></h1>
          </div>

          <!-- Borang Tambah -->
          <div class="form-container">
            <form action="AddHomestayServlet" method="post" enctype="multipart/form-data">

              <label>Homestay Name</label>
              <input type="text" name="name" required>

              <label>Description</label>
              <textarea name="description" rows="4" required></textarea>

              <label>Address</label>
              <input type="text" name="address" required>

              <label>City</label>
              <input type="text" name="city" required>

              <label>State</label>
              <input type="text" name="state" required>

              <label>Price per Night (RM)</label>
              <input type="number" name="price_per_night" min="0" step="0.01" required>

              <label>Max Guest</label>
              <input type="number" name="max_guests" min="1" required>

              <label><input type="checkbox" name="has_wifi"> WiFi</label>
              <label><input type="checkbox" name="has_parking"> Parkir</label>
              <label><input type="checkbox" name="has_aircond"> Aircond</label>
              <label><input type="checkbox" name="has_tv"> Televison</label>
              <label><input type="checkbox" name="has_kitchen"> Kitchen</label>
              <label><input type="checkbox" name="has_washing_machine">Washing Machine</label>

              <label>Number of Bedroom</label>
              <input type="number" name="num_bedrooms" min="1" value="1">

              <label>Number of Bathroom</label>
              <input type="number" name="num_bathrooms" min="1" value="1">

              <label>Picture (jpeg/png)</label>
              <input type="file" name="image" accept="image/*" required>

              <button type="submit">Add New Homestay</button>
            </form>
          </div>
        </div>

    </body>
</html>
