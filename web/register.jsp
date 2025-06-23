<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
  response.setHeader("Cache-Control","no-cache, no-store, must-revalidate");
  response.setHeader("Pragma","no-cache");
  response.setDateHeader ("Expires", 0);
%>


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>HomestayFinder - Register</title>
        <link rel="stylesheet" href="css/register.css">
        <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
        <script src="js/register.js"></script>

    </head>
    <body>
        <div class="register-container">
            <h2>Create an Account</h2>
            
            <form action="RegisterServlet" method="post">
                <input type="text" name="full_name" placeholder="Full Name" required>
                <input type="email" name="email" placeholder="Email Address" required>
                <input type="password" name="password" placeholder="Password" required>
                <input type="text" name="phone" placeholder="Phone Number">
                <select name="user_type" required>
                    <option value="">Select Role</option>
                    <option value="customer">Customer</option>
                    <option value="homestay_owner">Homestay Owner</option>
                </select>
                <button type="submit" class="btn-register">Register</button>
            </form>
            
            <div class="footer-text">
                <p>Already have an account? <a href="login.jsp">Login</a></p>
            </div>
        </div>
        
        <!-- Popup untuk mesej -->
        <div id="popupBox" class="popup">
          <div class="popup-content">
            <p id="popupMessage"></p>
            <button id="popupClose">Tutup</button>
          </div>
        </div>
    </body>
</html>
