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

@WebServlet("/SearchHomestayServlet")
public class SearchHomestayServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String search = request.getParameter("search");
        String wifi   = request.getParameter("wifi");
        String aircond = request.getParameter("aircond");
        String kitchen = request.getParameter("kitchen");

        try (Connection conn = DBUtil.getConnection()) {
            HomestayDAO dao = new HomestayDAO(conn);
            List<Homestay> homestays;

            boolean adaFilter = search != null || wifi != null || aircond != null || kitchen != null;

            if (adaFilter) {
                homestays = dao.searchHomestays(search, wifi, aircond, kitchen);
            } else {
                homestays = dao.getAllHomestays(); // jika tiada parameter, display semua homestay
            }

            request.setAttribute("homestays", homestays);
            request.setAttribute("search", search);
            request.getRequestDispatcher("searchHomestay.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
