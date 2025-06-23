package controller;

import dao.HomestayDAO;
import model.Homestay;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/HomestayDetailServlet")
public class HomestayDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam == null) {
            response.sendRedirect("SearchHomestayServlet");
            return;
        }

        try {
            int homestayId = Integer.parseInt(idParam);

            try (Connection conn = DBUtil.getConnection()) {
                HomestayDAO dao = new HomestayDAO(conn);
                Homestay homestay = dao.getById(homestayId);

                if (homestay != null) {
                    request.setAttribute("homestay", homestay);

                    // ✅ TAMBAH: Bawa mesej ke JSP
                    String msg = request.getParameter("msg");
                    if (msg != null) {
                        request.setAttribute("msg", msg);
                    }

                    request.getRequestDispatcher("homestayDetail.jsp").forward(request, response);
                } else {
                    response.sendRedirect("SearchHomestayServlet");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}

