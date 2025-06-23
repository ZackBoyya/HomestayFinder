package controller;

import dao.UserDAO;
import util.DBUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/ForgotPasswordServlet")
public class ForgotPasswordServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //Ambil parameter dari borang
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirm");

        //Check password sama dengan re-enter password
        if (!password.equals(confirm)) {
            response.sendRedirect("forgot.jsp?msg=notmatch");
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {
            UserDAO dao = new UserDAO(conn);

            //Check kewujudan email
            boolean exists = dao.emailExists(email);
            if (!exists) {
                response.sendRedirect("forgot.jsp?msg=notfound");
                return;
            }

            boolean success = dao.updatePassword(email, password);
            if (success) {
                response.sendRedirect("login.jsp?msg=reset_success");
            } else {
                response.sendRedirect("forgot.jsp?msg=fail");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
