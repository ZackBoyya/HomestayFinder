package controller;

import dao.HomestayDAO;
import model.Homestay;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/EditHomestayServlet")
public class EditHomestayServlet extends HttpServlet {

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
            int id = Integer.parseInt(request.getParameter("id"));

            HomestayDAO dao = new HomestayDAO(conn);
            Homestay h = dao.getById(id);

            // Pastikan hanya owner yang boleh edit homestay sendiri
            if (h != null && h.getUserId() == ownerId) {
                request.setAttribute("homestay", h);
                request.getRequestDispatcher("editHomestay.jsp").forward(request, response);
            } else {
                response.sendRedirect("MyHomestayServlet");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    // kemaskini homestay lepas submit form
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Integer ownerId = (session != null) ? (Integer) session.getAttribute("user_id") : null;
        if (ownerId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {

            int homestayId      = Integer.parseInt(request.getParameter("homestay_id"));
            String name         = request.getParameter("name");
            String description  = request.getParameter("description");
            String address      = request.getParameter("address");
            String city         = request.getParameter("city");
            String state        = request.getParameter("state");
            double price        = Double.parseDouble(request.getParameter("price_per_night"));
            int maxGuests       = Integer.parseInt(request.getParameter("max_guests"));
            boolean hasWifi           = request.getParameter("has_wifi") != null;
            boolean hasParking        = request.getParameter("has_parking") != null;
            boolean hasAircond        = request.getParameter("has_aircond") != null;
            boolean hasTv             = request.getParameter("has_tv") != null;
            boolean hasKitchen        = request.getParameter("has_kitchen") != null;
            boolean hasWashingMachine = request.getParameter("has_washing_machine") != null;
            int numBedrooms           = Integer.parseInt(request.getParameter("num_bedrooms"));
            int numBathrooms          = Integer.parseInt(request.getParameter("num_bathrooms"));


            Homestay homestay = new Homestay();
            homestay.setHomestayId(homestayId);
            homestay.setUserId(ownerId);
            homestay.setName(name);
            homestay.setDescription(description);
            homestay.setAddress(address);
            homestay.setCity(city);
            homestay.setState(state);
            homestay.setPricePerNight(price);
            homestay.setMaxGuests(maxGuests);
            homestay.setHasWifi(hasWifi);
            homestay.setHasParking(hasParking);
            homestay.setHasAircond(hasAircond);
            homestay.setHasTv(hasTv);
            homestay.setHasKitchen(hasKitchen);
            homestay.setHasWashingMachine(hasWashingMachine);
            homestay.setNumBedrooms(numBedrooms);
            homestay.setNumBathrooms(numBathrooms);
            
            HomestayDAO dao = new HomestayDAO(conn);
            dao.update(homestay);

            response.sendRedirect("MyHomestayServlet");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
