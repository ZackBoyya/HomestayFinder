<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
      <title>HomestayFinder - Forgot Password</title>
      <link rel="stylesheet" href="css/login.css">
    </head>
    <body>

    <div class="panel">
      <h2>Forgot Password</h2>
      
      <%
        String msg = request.getParameter("msg");
        if ("notmatch".equals(msg)) {
    %>
        <p style="color:red;">Password Not Match!</p>
    <% } else if ("notfound".equals(msg)) { %>
        <p style="color:red;">E-mel not found!</p>
    <% } else if ("fail".equals(msg)) { %>
        <p style="color:red;">Failed to reset password!</p>
    <% } %>

      
      <form action="ForgotPasswordServlet" method="post">
        <input type="email" name="email" placeholder="Registered Email" required>
        <input type="password" name="password" placeholder="New Password" required>
        <input type="password" name="confirm" placeholder="Re-enter Password" required>

        <button type="submit" class="btn-main">Reset Password</button>
      </form>
    </div>

    </body>
</html>
