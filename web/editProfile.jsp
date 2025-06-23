<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String fullName = (String) session.getAttribute("full_name");
    String email = (String) session.getAttribute("email");
    String userType = (String) session.getAttribute("user_type");
    String phone = (String) session.getAttribute("phone"); // pastikan diset semasa login
    String message = request.getParameter("msg");
%>

<!DOCTYPE html>
<html>
    <head>
      <meta charset="UTF-8">
      <title>HomestayFinder - UpdateProfile</title>
      <link rel="stylesheet" href="css/editProfile.css">
    </head>
    <body>

        <!-- ===== Sidebar ===== -->
        <div class="sidebar">
          <h2 class="logo"><a href="homepage.jsp">HomestayFinder</a></h2>
          <ul class="nav">
            <%if ("customer".equals(userType)){%>  
                <li><a href="CustomerDashboardServlet">Dashboard</a></li>
                <li><a href="CustomerBookingsServlet">My Reservation</a></li>
                <li><a href="UpdateProfileServlet" class="active">Update Profile</a></li>
                <li><a href="LogoutServlet">Log Out</a></li>
            <% }else {%>
                <li><a href="OwnerDashboardServlet">Dashboard</a></li>
                <li><a href="MyHomestayServlet">My Homestay List</a></li>
                <li><a href="BookingServlet">Booking Receive</a></li>
                <li><a href="UpdateProfileServlet" class="active">Update Profile</a></li>
                <li><a href="LogoutServlet">Log Out</a></li>
            <% } %>
          </ul>
        </div>
        
        <div class="main">
          <h2>Update Profile</h2>

          <% if ("success".equals(message)) { %>
            <p class="success">Profile successfully updated!</p>
          <% } else if ("fail".equals(message)) { %>
            <p class="error">Update failed. Make sure the passwords match!</p>
          <% } %>

          <form method="post" action="UpdateProfileServlet">
            <label>Full Name</label>
            <input type="text" value="<%= fullName %>" disabled>

            <label>Email</label>
            <input type="text" value="<%= email %>" disabled>

            <label>Type of User</label>
            <input type="text" value="<%= userType %>" disabled>

            <label>Phone Number</label>
            <input type="text" name="phone" value="<%= (phone != null) ? phone : "" %>" required>

            <label>New Password</label>
            <input type="password" name="new_password" required>

            <label>Re-enter Password</label>
            <input type="password" name="confirm_password" required>

            <button type="submit">Save</button>
          </form>

          <p><a href="<%= "homestay_owner".equals(userType) ? "OwnerDashboardServlet" : "customerDashboard.jsp" %>">← Return to Dashboard</a></p>
        </div>

    </body>
</html>
