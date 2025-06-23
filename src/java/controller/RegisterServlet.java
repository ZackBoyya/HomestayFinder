package controller;

import dao.UserDAO;
import model.User;
import util.DBUtil;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // 1. Ambil data borang
        String fullName = req.getParameter("full_name");
        String email    = req.getParameter("email").trim();
        String password = req.getParameter("password");
        String phone    = req.getParameter("phone");
        String userType = req.getParameter("user_type");

        try (Connection conn = DBUtil.getConnection()){
            UserDAO dao = new UserDAO(conn); 

            // 2. Semak jika e‑mel telah wujud
            if (dao.emailExists(email)) {
                res.sendRedirect("register.jsp?error=duplicate");
                return;
            }

            // 3. Sediakan objek User
            User user = new User();
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPassword(password);
            user.setPhone(phone);
            user.setUserType(userType);

            // 4. Masukkan ke database
            boolean success = dao.insert(user);

            if (success) {
                // Berjaya daftar
                res.sendRedirect("login.jsp?success=1");
            } else {
                // Gagal insert
                res.sendRedirect("register.jsp?error=fail");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            res.sendRedirect("register.jsp?error=fail");
        }
    }
}
