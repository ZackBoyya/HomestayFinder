package controller;

import dao.HomestayDAO;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/DeleteHomestayServlet")
public class DeleteHomestayServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Pastikan user login
        HttpSession session = request.getSession(false);
        Integer ownerId = (session != null) ? (Integer) session.getAttribute("user_id") : null;
        if (ownerId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {

            // 2. Ambil homestay_id dari parameter
            int homestayId = Integer.parseInt(request.getParameter("homestay_id"));

            // 3. Panggil DAO untuk padam homestay
            HomestayDAO dao = new HomestayDAO(conn);
            dao.delete(homestayId, ownerId); // pastikan hanya padam milik sendiri

            // 4. Redirect semula ke senarai
            response.sendRedirect("MyHomestayServlet");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
