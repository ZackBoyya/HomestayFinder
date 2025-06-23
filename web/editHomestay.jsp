<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Homestay" %>

<%
    Homestay h = (Homestay) request.getAttribute("homestay");
    if (h == null) {
        response.sendRedirect("MyHomestayServlet");
        return;
    }
%>

<!DOCTYPE html>
<html>
    <head>
      <title>HomestayFinder - EditHomestay</title>
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

        <div class="main">

            <div class="topbar">
            <h1>Update Homestay Detail</h1>
          </div>


            <div class="form-container">

              <form action="EditHomestayServlet" method="post">
                <input type="hidden" name="homestay_id" value="<%= h.getHomestayId() %>">

                <label>Homestay Name</label>
                <input type="text" name="name" value="<%= h.getName() %>" required>

                <label>Description</label>
                <textarea name="description" rows="4" required><%= h.getDescription() %></textarea>

                <label>Address</label>
                <input type="text" name="address" value="<%= h.getAddress() %>" required>

                <label>City</label>
                <input type="text" name="city" value="<%= h.getCity() %>" required>

                <label>State</label>
                <input type="text" name="state" value="<%= h.getState() %>" required>

                <label>Price per Night (RM)</label>
                <input type="number" name="price_per_night" step="0.01" value="<%= h.getPricePerNight() %>" required>

                <label>Max Guest</label>
                <input type="number" name="max_guests" value="<%= h.getMaxGuests() %>" required>

                <label><input type="checkbox" name="has_wifi"> WiFi</label>
                <label><input type="checkbox" name="has_parking"> Parkir</label>
                <label><input type="checkbox" name="has_aircond"> Aircond</label>
                <label><input type="checkbox" name="has_tv"> Television</label>
                <label><input type="checkbox" name="has_kitchen"> Kitchen</label>
                <label><input type="checkbox" name="has_washing_machine"> Washing Machine</label>

                <label>Number of Bedroom</label>
                <input type="number" name="num_bedrooms" min="1" value="<%= h.getNumBedrooms() %>">

                <label>Number of Bathroom</label>
                <input type="number" name="num_bathrooms" min="1" value="<%= h.getNumBathrooms() %>">

                <button type="submit">Update Homestay</button>
              </form>
            </div>

        </div>
    </body>
</html>
