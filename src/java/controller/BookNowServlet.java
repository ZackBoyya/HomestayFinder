package controller;

import dao.BookingDAO;
import model.Booking;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;

@WebServlet("/BookNowServlet")
public class BookNowServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Integer customerId = (session != null) ? (Integer) session.getAttribute("user_id") : null;

        if (customerId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {

            int homestayId      = Integer.parseInt(request.getParameter("homestay_id"));
            LocalDate checkIn   = LocalDate.parse(request.getParameter("check_in"));
            LocalDate checkOut  = LocalDate.parse(request.getParameter("check_out"));
            int totalGuests     = Integer.parseInt(request.getParameter("total_guests"));

            //1. Pastikan check-in tidak sebelum hari ini
            LocalDate today = LocalDate.now();
            if (checkIn.isBefore(today)) {
                response.sendRedirect("homestayDetail.jsp?id=" + homestayId + "&msg=before_today");
                return;
            }

            //2. Pastikan check-in sebelum check-out
            long days = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
            if (days <= 0) {
                response.sendRedirect("HomestayDetailServlet?id=" + homestayId + "&msg=invalid_date");
                return;
            }

            //3. Check sama ada tarikh telah ditempah
            BookingDAO dao = new BookingDAO(conn);
            boolean available = dao.isDateRangeAvailable(homestayId, checkIn.toString(), checkOut.toString());

            if (!available) {
                response.sendRedirect("HomestayDetailServlet?id=" + homestayId + "&msg=date_unavailable");
                return;
            }

            //4. Simpan tempahan
            Booking booking = new Booking();
            booking.setHomestayId(homestayId);
            booking.setUserId(customerId);
            booking.setCheckIn(checkIn.toString());
            booking.setCheckOut(checkOut.toString());
            booking.setTotalGuests(totalGuests);
            booking.setStatus("pending");

            boolean success = dao.insert(booking, days);

            if (success) {
                response.sendRedirect("CustomerBookingsServlet?msg=success");
            } else {
                response.sendRedirect("HomestayDetailServlet?id=" + homestayId + "&msg=fail");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
