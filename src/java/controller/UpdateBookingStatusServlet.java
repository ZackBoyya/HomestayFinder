package controller;

import dao.BookingDAO;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/UpdateBookingStatusServlet")
public class UpdateBookingStatusServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //Dapat parameter dari form
        String bookingIdStr = request.getParameter("booking_id");
        String action = request.getParameter("action"); // "approve" atau "reject"

        //Jika parameter tidak lengkap, redirect balik ke BookingServlet
        if (bookingIdStr == null || action == null) {
            response.sendRedirect("BookingServlet");
            return;
        }

        int bookingId = Integer.parseInt(bookingIdStr);
        //Tentukan status mengikut action
        String newStatus = action.equals("approve") ? "approve" : "reject";

        try (Connection conn = DBUtil.getConnection()) {
            BookingDAO dao = new BookingDAO(conn);
            dao.updateStatus(bookingId, newStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("BookingServlet");
    }
}
