package controller;

import dao.BookingDAO;
import dao.HomestayDAO;
import model.Booking;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/OwnerDashboardServlet")
public class OwnerDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /* 1. Pastikan owner telah login */
        HttpSession session = request.getSession(false);
        Integer ownerId = (session != null) ? (Integer) session.getAttribute("user_id") : null;
        if (ownerId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {

            /* 2. DAO */
            HomestayDAO homestayDao = new HomestayDAO(conn);
            BookingDAO  bookingDao  = new BookingDAO(conn);

            /* 3‑a. Bilangan homestay milik owner */
            int totalHomestay = homestayDao.countByOwner(ownerId);

            /* 3‑b. 5 tempahan terdekat milik owner */
            List<Booking> latestBookings = bookingDao.getBookingsByOwnerId(ownerId); 
            if (latestBookings.size() > 5) {
                latestBookings = latestBookings.subList(0, 5);
            }

            /* 4. Hantar data ke JSP */
            request.setAttribute("totalHomestay",  totalHomestay);
            request.setAttribute("tempahanTerkini", latestBookings);

            request.getRequestDispatcher("ownerDashboard.jsp").forward(request, response);

        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
