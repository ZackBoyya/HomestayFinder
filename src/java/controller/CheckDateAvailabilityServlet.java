package controller;

import dao.BookingDAO;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.time.LocalDate;

@WebServlet("/CheckDateAvailabilityServlet")
public class CheckDateAvailabilityServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int homestayId = Integer.parseInt(request.getParameter("homestay_id"));
        String date = request.getParameter("date");

        boolean available = true;

        try (Connection conn = DBUtil.getConnection()) {
            BookingDAO dao = new BookingDAO(conn);
            available = dao.isDateAvailable(homestayId, date); // pastikan fungsi ini wujud
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.setContentType("application/json");
        response.getWriter().write("{\"available\":" + available + "}");
    }
}

