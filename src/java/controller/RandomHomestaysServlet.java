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

@WebServlet("/RandomHomestaysServlet")
public class RandomHomestaysServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (Connection conn = DBUtil.getConnection()) {
            HomestayDAO dao = new HomestayDAO(conn);
            List<Homestay> randomList = dao.getRandomHomestays(5); // ambil 5 homestay random

            request.setAttribute("randomHomestays", randomList);
            request.getRequestDispatcher("components/randomHomestayDisplay.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Ralat pelayan.");
        }
    }
}
