<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Homestay" %>

<%
    String keyword = request.getParameter("search") != null ? request.getParameter("search") : "";
    List<Homestay> results = (List<Homestay>) request.getAttribute("homestays");
    String fullName = (String) session.getAttribute("full_name");
    String userType = (String) session.getAttribute("user_type");
    Integer userId = (Integer) session.getAttribute("user_id");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>HomestayFinder - Search Homestay</title>
        <link rel="stylesheet" href="css/searchHomestay.css">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="js/homepage.js"></script>
    </head>
    <body>

    <!-- Navigation -->
    <div class="topnav">
        <div class="logo">HomestayFinder</div>

        <nav class="center-links">
            <a href="homepage.jsp">Homepage</a>
            <a href="SearchHomestayServlet" class="active">Search Homestay</a>
        </nav>

        <div class="right-links">
            <% if (userId == null) { %>
                <a href="register.jsp">Register</a>
                <a href="login.jsp">Log In</a>
            <% } else { %>
                <div class="user-dropdown">
                    <span class="username" id="dropdownToggle"><%= fullName %> ▼</span>
                    <div class="dropdown-menu" id="dropdownMenu">
                        <% if ("customer".equals(userType)) { %>
                            <a href="CustomerDashboardServlet">Dashboard</a>
                        <% } else if ("homestay_owner".equals(userType)) { %>
                            <a href="OwnerDashboardServlet">Dashboard</a>
                        <% } %>
                        <a href="UpdateProfileServlet">Profile Setting</a>
                        <a href="LogoutServlet">Log Out</a>
                    </div>
                </div>
            <% } %>
        </div>
    </div>

    <!-- Search Bar & Filter -->
    <div class="search-section">
        <div class="search-section">
            <form action="SearchHomestayServlet" method="get">
              <input type="text" name="search" placeholder="Example: Kuala Terengganu" value="<%= keyword %>" />

              <label><input type="checkbox" name="wifi" <%= request.getParameter("wifi") != null ? "checked" : "" %> > WiFi</label>
              <label><input type="checkbox" name="parking" <%= request.getParameter("parking") != null ? "checked" : "" %> > Parking</label>
              <label><input type="checkbox" name="aircond" <%= request.getParameter("aircond") != null ? "checked" : "" %> > Aircond</label>
              <label><input type="checkbox" name="kitchen" <%= request.getParameter("kitchen") != null ? "checked" : "" %> > Kitchen</label>

              <button type="submit">Search / Sort</button>
            </form>
        </div>

    </div>

    <!-- Homestay Results -->
    <div class="homestay-container">
        <% if (results != null && !results.isEmpty()) {
            for (Homestay h : results) { %>
            <div class="homestay-card" onclick="location.href='HomestayDetailServlet?id=<%= h.getHomestayId() %>';">
                <img src="<%= (h.getImagePath() != null) ? "FileServeServlet?name=" + new java.io.File(h.getImagePath()).getName() : "img/placeholder.png" %>" alt="Gambar Homestay">
                <h3><%= h.getName() %></h3>
                <p><%= h.getCity() %>, <%= h.getState() %></p>
                <p>RM <%= String.format("%.2f", h.getPricePerNight()) %> / night</p>
            </div>
        <% }} else { %>
            <p style="text-align:center;">No Homestay Found "<%= keyword %>"</p>
        <% } %>
    </div>

    <!-- Footer -->
    <jsp:include page="components/footer.jsp" />

    </body>
</html>
