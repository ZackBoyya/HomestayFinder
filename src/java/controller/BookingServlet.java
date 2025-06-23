package controller;

import dao.BookingDAO;
import model.Booking;
import util.DBUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/BookingServlet")
public class BookingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Integer ownerId = (session != null) ? (Integer) session.getAttribute("user_id") : null;

        if (ownerId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {
            BookingDAO bookingDao = new BookingDAO(conn);

            // Ambil semua tempahan yang berkaitan dengan homestay owner ini
            List<Booking> bookings = bookingDao.getBookingsByOwnerId(ownerId);

            request.setAttribute("bookings", bookings);
            request.getRequestDispatcher("manageBookings.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
