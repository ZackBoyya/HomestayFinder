<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String fullName = (String) session.getAttribute("full_name");
  String userType = (String) session.getAttribute("user_type");
  Integer userId  = (Integer) session.getAttribute("user_id");
%>

<!DOCTYPE html>
<html>
    <head>
      <title>HomestayFinder - Homepage</title>
      <link rel="stylesheet" href="css/homepage.css">
      <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
      <script src="js/homepage.js"></script>
    </head>
    <body>

      <!-- Header / Navigation -->
      <div class="topnav">
        <div class="logo">HomestayFinder</div>

        <nav class="center-links">
          <a href="homepage.jsp" class="active">Homepage</a>
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

      <!-- Banner -->
      <section class="hero">
        <div class="hero-content">
          <h1>Find Your Dream Homestay</h1>
          <p>Find and book accommodation easily and quickly across Malaysia</p>
          <!--<a href="register.jsp" class="cta-button">Register Now</a>-->
        </div>
      </section>

      <!-- Ciri-ciri Utama -->
      <section class="features">
        <div class="feature-box">
          <img src="img/homestayIcon.png" alt="Pelbagai Homestay">
          <h3>Multiple Choices</h3>
          <p>Over 100+ homestays available nationwide</p>
        </div>
        <div class="feature-box">
          <img src="img/best-price.png" alt="Harga Berpatutan">
          <h3>Reasonable Price</h3>
          <p>Compare prices and book within your budget</p>
        </div>
        <div class="feature-box">
          <img src="img/easy-booking.png" alt="Tempahan Mudah">
          <h3>Easy Booking</h3>
          <p>Fast process, user-friendly system</p>
        </div>
      </section>

      <!-- Homestay Pilihan -->
      <section class="featured">
        <h2>Preferred Homestay</h2>
        <div class="homestay-list" id="homestayContainer">
          <!--list homestay akan diisi dengan Ajax -->
        </div>
      </section>
      
      <!--testimoni customer-->
        <section class="testimonials">
          <h2>What Our Customers Say</h2>
          <div class="testi-container">

            <div class="testi-card">
              <p class="testi-text">“The system is very easy to use, I was able to book a homestay in just a few minutes!”</p>
              <div class="testi-user">— Nurul, Kuala Terengganu</div>
            </div>
            <div class="testi-card">
              <p class="testi-text">“Many homestay options and very reasonable prices. Recommended!”</p>
              <div class="testi-user">— Hafiz, Melaka</div>
            </div>
            <div class="testi-card">
              <p class="testi-text">“The booking and payment process was very smooth. The homestay owner was also friendly!”</p>
              <div class="testi-user">— Aina, Johor Bahru</div>
            </div>
            <div class="testi-card">
              <p class="testi-text">“I managed to rent a homestay for a family party. Very satisfied.”</p>
              <div class="testi-user">— Farid, Kedah</div>
            </div>
            <div class="testi-card">
              <p class="testi-text">“This platform was very helpful when I was on vacation with my family in Terengganu.”</p>
              <div class="testi-user">— Syazana, Shah Alam</div>
            </div>
            <div class="testi-card">
                <p class="testi-text">“HomestayFinder helped me find a last minute homestay for our family trip. Thank you!”</p>
                <p class="testi-user">— Amirul, Seremban</p>
            </div>

          </div>
        </section>

      <!--FAQ-->
      <section class="faq">
        <h2>Frequently Asked Questions</h2>
        <div class="faq-item">
          <h4>What is HomestayFinder?</h4>
          <p>HomestayFinder is a platform for booking and managing homestay accommodation in Malaysia.</p>
        </div>
        <div class="faq-item">
          <h4>How do I register as a homestay owner?</h4>
          <p>Register an account and select the user type "Homestay Owner". You can start adding homestays after logging in.</p>
        </div>
        <div class="faq-item">
            <h4>How do I make a reservation?</h4>
            <p>Select a homestay, fill in the dates and number of guests, then click the book button.</p>
        </div>
        <div class="faq-item">
          <h4>Do I have to pay additional charges?</h4>
          <p>No, the price displayed is the final price including all charges.</p>
        </div>
        <div class="faq-item">
            <h4>How do I check the status of my booking?</h4>
            <p>You can check via the dashboard under the "My Bookings" menu.</p>
        </div>
      </section>


      <!-- Footer -->
      <footer class="footer">
          <jsp:include page="components/footer.jsp"/>
      </footer>

    </body>
</html>
