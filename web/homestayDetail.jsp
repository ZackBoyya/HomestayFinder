<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Homestay" %>

<%
    Homestay h = (Homestay) request.getAttribute("homestay");
    if (h == null) {
        response.sendRedirect("SearchHomestayServlet");
        return;
    }

    String fullName = (String) session.getAttribute("full_name");
    String userType = (String) session.getAttribute("user_type");
    Integer userId  = (Integer) session.getAttribute("user_id");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title><%= h.getName() %> - Homestay Detail</title>
        <link rel="stylesheet" href="css/homestayDetail.css">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script>
            const HOMESTAY_ID = <%= h.getHomestayId() %>;
        </script>
        <script src="js/homestayDetail.js"></script>
        <script src="js/homepage.js"></script>
    </head>
    <body>

    <!-- Navigation -->
    <div class="topnav">
        <div class="logo">HomestayFinder</div>

        <nav class="center-links">
            <a href="homepage.jsp">Homepage</a>
            <a href="SearchHomestayServlet">Search Homestay</a>
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

    <!-- Butiran Homestay -->
    <div class="detail-container">
        <div class="image-box">
            <img src="<%= (h.getImagePath() != null) ? "FileServeServlet?name=" + new java.io.File(h.getImagePath()).getName() : "img/placeholder.png" %>" alt="Gambar Homestay">
        </div>

        <div class="info-box" data-price="<%= h.getPricePerNight() %>">

            <h2><%= h.getName() %></h2>
            <p><strong>Location:</strong> <%= h.getAddress() %>, <%= h.getCity() %>, <%= h.getState() %></p>
            <p><strong>Price:</strong> RM <%= String.format("%.2f", h.getPricePerNight()) %> / night</p>
            <p><strong>Max Guest:</strong> <%= h.getMaxGuests() %></p>
            <p><strong>Bedroom:</strong> <%= h.getNumBedrooms() %></p>
            <p><strong>Bathroom:</strong> <%= h.getNumBathrooms() %></p>
            <p><strong>Description:</strong> <%= h.getDescription() %></p>

            <div class="kemudahan">
                <h3>Facility:</h3>
                <ul>
                    <li>WiFi: <%= h.isHasWifi() ? "✅ Available" : "❌ Unvailable" %></li>
                    <li>Parking: <%= h.isHasParking() ? "✅ Available" : "❌ Unvailable" %></li>
                    <li>Aircond: <%= h.isHasAircond() ? "✅ Available" : "❌ Unvailable" %></li>
                    <li>Television: <%= h.isHasTv() ? "✅ Available" : "❌ Unvailable" %></li>
                    <li>Kitchen: <%= h.isHasKitchen() ? "✅ Available" : "❌ Unvailable" %></li>
                    <li>Washing Machine: <%= h.isHasWashingMachine() ? "✅ Available" : "❌ Unvailable" %></li>
                </ul>
            </div>
        </div>
    </div>
    
    <div class="booking-section">

        <!-- Kalendar -->
        <div class="availability-section">
            <h3>Booking Calendar</h3>
            <div id="calendar-nav">
                <button id="prevMonth">←</button>
                <span id="calendar-title"></span>
                <button id="nextMonth">→</button>
            </div>
            <div class="calendar-grid" id="calendarGrid">
                <!-- Tarikh akan diisi oleh JS -->
            </div>
        </div>

        <!-- Borang Tempahan -->
        
        <%
        String msg = request.getParameter("msg");
        if (msg != null) {
        %>
        <div class="alert-box">
            <% if ("invalid_date".equals(msg)) { %>
                ❗ <strong>Invalid date:</strong> Check-out must be after check-in.
            <% } else if ("before_today".equals(msg)) { %>
                📅 <strong>Missed date:</strong> You cannot book a date before today.
            <% } else if ("date_unavailable".equals(msg)) { %>
                🚫 <strong>Date booked:</strong> Please select another date that is still empty.
            <% } else if ("fail".equals(msg)) { %>
                ⚠️ <strong>Error:</strong> The order failed to process. Please try again.
            <% } %>
        </div>
            <% } %>

        
        <div class="booking-form-section">
            <% if (userId != null && "customer".equals(userType)) { %>
            <h3>Book Now</h3>
            <form action="BookNowServlet" method="post" class="booking-form">
                <input type="hidden" name="homestay_id" value="<%= h.getHomestayId() %>">

                <label>Check In Date:</label>
                <input type="date" name="check_in" id="checkIn" min="<%= java.time.LocalDate.now() %>" required>

                <label>Check Out Tarikh:</label>
                <input type="date" name="check_out" id="checkOut" min="<%= java.time.LocalDate.now().plusDays(1) %>" required>

                <label>Number of Guests:</label>
                <input type="number" name="total_guests" id="totalGuests" min="1" max="<%= h.getMaxGuests() %>" required>

                <button type="submit">Book Now</button>
            </form>
            <% } else { %>
                <p>Please <a href="login.jsp">log in</a> as a customer to make a reservation.</p>
            <% } %>
        </div>

    </div>

    <!-- Overlay & Popup -->
    <div id="overlay" style="display:none;"></div>
    <div id="bookingPopup" class="popup-box" style="display:none;">
        <h3>Confirm Booking</h3>
        <p><strong>Homestay:</strong> <span id="popupHomestay"></span></p>
        <p><strong>Check-in:</strong> <span id="popupCheckIn"></span></p>
        <p><strong>Check-out:</strong> <span id="popupCheckOut"></span></p>
        <p><strong>Guest:</strong> <span id="popupGuests"></span></p>
        <p><strong>Total:</strong> <span id="popupTotal"></span></p>
        <div class="popup-actions">
            <button id="cancelBooking">Cancel</button>
            <button id="confirmBooking">Confirm</button>
        </div>
    </div>

   
        
    <!-- Footer -->
    <jsp:include page="components/footer.jsp" />

    </body>
</html>
