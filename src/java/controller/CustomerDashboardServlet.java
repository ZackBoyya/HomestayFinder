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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/CustomerDashboardServlet")
public class CustomerDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Semak sesi
        HttpSession session = request.getSession(false);
        Integer customerId = (session != null) ? (Integer) session.getAttribute("user_id") : null;

        if (customerId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 2. Sediakan data
        int aktif = 0, lalu = 0, tertunda = 0;
        List<Booking> tempahanTerkini = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection()) {
            BookingDAO dao = new BookingDAO(conn);

            List<Booking> semuaTempahan = dao.getByCustomer(customerId);

            LocalDate hariIni = LocalDate.now();

            for (Booking b : semuaTempahan) {
                LocalDate checkIn  = LocalDate.parse(b.getCheckIn());
                LocalDate checkOut = LocalDate.parse(b.getCheckOut());

                if ("pending".equals(b.getStatus())) {
                    tertunda++;
                } else if (checkOut.isBefore(hariIni)) {
                    lalu++;
                } else {
                    aktif++;
                }

                // Ambil 5 tempahan terdekat (bermula dari hari ini)
                if (checkOut.isAfter(hariIni.minusDays(1))) {
                    if (tempahanTerkini.size() < 5) {
                        tempahanTerkini.add(b);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
            return;
        }

        // 3. Hantar ke JSP
        request.setAttribute("aktif", aktif);
        request.setAttribute("lalu", lalu);
        request.setAttribute("tertunda", tertunda);
        request.setAttribute("tempahanTerkini", tempahanTerkini);

        request.getRequestDispatcher("customerDashboard.jsp").forward(request, response);
    }
}
