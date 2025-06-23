package controller;

import dao.UserDAO;
import model.User;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/UpdateProfileServlet")
public class UpdateProfileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Integer userId = (session != null) ? (Integer) session.getAttribute("user_id") : null;

        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Ambil data dari form
        String phone = request.getParameter("phone");
        String newPassword = request.getParameter("new_password");
        String confirmPassword = request.getParameter("confirm_password");

        if (!newPassword.equals(confirmPassword)) {
            response.sendRedirect("editProfile.jsp?msg=fail");
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {
            UserDAO dao = new UserDAO(conn);

            // Kemas kini nombor telefon dan kata laluan
            boolean success = dao.updateProfile(userId, phone, newPassword);

            if (success) {
                // Kemas kini nombor telefon dalam sesion
                session.setAttribute("phone", phone);
                response.sendRedirect("editProfile.jsp?msg=success");
            } else {
                response.sendRedirect("editProfile.jsp?msg=fail");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("editProfile.jsp?msg=fail");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // pastikan user log-in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // hanya paparkan form
        request.getRequestDispatcher("editProfile.jsp").forward(request, response);
    }

}
