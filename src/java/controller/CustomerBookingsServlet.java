package controller;

import dao.BookingDAO;
import model.Booking;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/CustomerBookingsServlet")
public class CustomerBookingsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Semak sesi login
        HttpSession session = request.getSession(false);
        Integer customerId = (session != null) ? (Integer) session.getAttribute("user_id") : null;

        if (customerId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {

            // 2. Dapatkan senarai tempahan milik customer
            BookingDAO bookingDAO = new BookingDAO(conn);
            List<Booking> bookings = bookingDAO.getByCustomer(customerId);

            // 3. Hantar ke JSP
            request.setAttribute("bookings", bookings);
            request.getRequestDispatcher("customerBookings.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
