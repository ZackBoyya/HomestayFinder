package controller;

import dao.HomestayDAO;
import model.Homestay;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/MyHomestayServlet")
public class MyHomestayServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Dapatkan sesion & user ID
        HttpSession session = request.getSession(false);
        Integer ownerId = (session != null)
                        ? (Integer) session.getAttribute("user_id")
                        : null;

        if (ownerId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {
            HomestayDAO dao = new HomestayDAO(conn);
            List<Homestay> list = dao.getHomestaysByOwner(ownerId);

            // 2. Hantar ke JSP
            request.setAttribute("homestays", list);
            request.getRequestDispatcher("myHomestays.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
