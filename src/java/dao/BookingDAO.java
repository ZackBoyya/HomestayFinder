package dao;

import model.Booking;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class BookingDAO {

    private Connection conn;

    public BookingDAO(Connection conn) {
        this.conn = conn;
    }

    // 🟩 Tambah tempahan baru
    public boolean insertBooking(Booking b) throws SQLException {
        String sql = "INSERT INTO bookings (user_id, homestay_id, check_in, check_out, total_price, total_guests, status, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, b.getUserId());
            stmt.setInt(2, b.getHomestayId());
            stmt.setString(3, b.getCheckIn());
            stmt.setString(4, b.getCheckOut());
            stmt.setDouble(5, b.getTotalPrice());
            stmt.setInt(6, b.getTotalGuests());
            stmt.setString(7, b.getStatus());

            return stmt.executeUpdate() > 0;
        }
    }

    // 🟨 Dapatkan semua tempahan untuk homestay owner
    public List<Booking> getBookingsByOwnerId(int ownerId) throws SQLException {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT b.*, u.full_name, h.name AS homestay_name, " +
                     "       COALESCE(img.image_path, 'img/placeholder.png') AS image_path " +
                     "FROM bookings b " +
                     "JOIN users u ON b.user_id = u.user_id " +
                     "JOIN homestays h ON b.homestay_id = h.homestay_id " +
                     "LEFT JOIN ( " +
                     "  SELECT i1.homestay_id, i1.image_path " +
                     "  FROM images i1 " +
                     "  INNER JOIN ( " +
                     "     SELECT homestay_id, MIN(uploaded_at) AS first_upload " +
                     "     FROM images GROUP BY homestay_id " +
                     "  ) i2 ON i1.homestay_id = i2.homestay_id " +
                     "     AND i1.uploaded_at = i2.first_upload " +
                     ") img ON h.homestay_id = img.homestay_id " +
                     "WHERE h.user_id = ? " +
                     "ORDER BY b.check_in ASC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ownerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Booking b = new Booking();
                    b.setBookingId(rs.getInt("booking_id"));
                    b.setHomestayId(rs.getInt("homestay_id"));
                    b.setUserId(rs.getInt("user_id"));
                    b.setCheckIn(rs.getString("check_in"));
                    b.setCheckOut(rs.getString("check_out"));
                    b.setTotalGuests(rs.getInt("total_guests"));
                    b.setTotalPrice(rs.getDouble("total_price"));
                    b.setStatus(rs.getString("status"));
                    b.setCreatedAt(rs.getString("created_at"));
                    b.setCustomerName(rs.getString("full_name"));     // tambahan dalam model
                    b.setHomestayName(rs.getString("homestay_name")); // tambahan
                    b.setImagePath(rs.getString("image_path"));       // tambahan
                    list.add(b);
                }
            }
        }
        return list;
    }


    // 🟦 Kemaskini status tempahan
    public boolean updateStatus(int bookingId, String newStatus) throws SQLException {
        String sql = "UPDATE bookings SET status = ? WHERE booking_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, bookingId);
            return stmt.executeUpdate() > 0;
        }
    }

    // 🔍 Dapatkan tempahan berdasarkan ID
    public Booking getBookingById(int bookingId) throws SQLException {
        String sql = "SELECT * FROM bookings WHERE booking_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Booking b = new Booking();
                    b.setBookingId(rs.getInt("booking_id"));
                    b.setUserId(rs.getInt("user_id"));
                    b.setHomestayId(rs.getInt("homestay_id"));
                    b.setCheckIn(rs.getString("check_in"));
                    b.setCheckOut(rs.getString("check_out"));
                    b.setTotalPrice(rs.getDouble("total_price"));
                    b.setTotalGuests(rs.getInt("total_guests"));
                    b.setStatus(rs.getString("status"));
                    b.setCreatedAt(rs.getString("created_at"));
                    return b;
                }
            }
        }
        return null;
    }
    
    //Check Senarai booking by customer
    public List<Booking> getByCustomer(int userId) throws SQLException {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT b.*, h.name AS homestay_name, img.image_path " +
                     "FROM bookings b " +
                     "JOIN homestays h ON b.homestay_id = h.homestay_id " +
                     "LEFT JOIN ( " +
                     "  SELECT i1.homestay_id, i1.image_path " +
                     "  FROM images i1 " +
                     "  INNER JOIN ( " +
                     "    SELECT homestay_id, MIN(uploaded_at) AS first_upload " +
                     "    FROM images GROUP BY homestay_id " +
                     "  ) i2 ON i1.homestay_id = i2.homestay_id AND i1.uploaded_at = i2.first_upload " +
                     ") img ON h.homestay_id = img.homestay_id " +
                     "WHERE b.user_id = ? " +
                     "ORDER BY b.check_in ASC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Booking b = new Booking();
                    b.setBookingId(rs.getInt("booking_id"));
                    b.setHomestayId(rs.getInt("homestay_id"));
                    b.setHomestayName(rs.getString("homestay_name"));
                    b.setCheckIn(rs.getString("check_in"));
                    b.setCheckOut(rs.getString("check_out"));
                    b.setTotalPrice(rs.getDouble("total_price"));
                    b.setTotalGuests(rs.getInt("total_guests"));
                    b.setStatus(rs.getString("status"));
                    b.setImagePath(rs.getString("image_path"));
                    list.add(b);
                }
            }
        }

        return list;
    }

    //check tarikh kosong
    public boolean isDateAvailable(int homestayId, String date) throws SQLException {
        String sql = "SELECT 1 FROM bookings WHERE homestay_id = ? AND ? BETWEEN check_in AND check_out";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, homestayId);
            stmt.setString(2, date);
            ResultSet rs = stmt.executeQuery();
            return !rs.next(); // true jika tiada tempahan
        }
    }


    //booking
    public boolean insert(Booking booking, long days) throws SQLException {
        // 1. Ambil harga per malam dari table homestays
        String priceQuery = "SELECT price_per_night FROM homestays WHERE homestay_id = ?";
        double pricePerNight = 0.0;

        try (PreparedStatement stmt = conn.prepareStatement(priceQuery)) {
            stmt.setInt(1, booking.getHomestayId());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    pricePerNight = rs.getDouble("price_per_night");
                } else {
                    throw new SQLException("Homestay tidak dijumpai.");
                }
            }
        }

        // 2. Kira jumlah harga keseluruhan
        double totalPrice = pricePerNight * days;

        // 3. Simpan ke dalam table bookings
        String insertSQL = "INSERT INTO bookings (homestay_id, user_id, check_in, check_out, total_guests, status, total_price) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
            stmt.setInt(1, booking.getHomestayId());
            stmt.setInt(2, booking.getUserId());
            stmt.setString(3, booking.getCheckIn());
            stmt.setString(4, booking.getCheckOut());
            stmt.setInt(5, booking.getTotalGuests());
            stmt.setString(6, booking.getStatus());
            stmt.setDouble(7, totalPrice);

            return stmt.executeUpdate() > 0;
        }
    }
    
    //check untuk mengelak pertindihan tarikh
    public boolean isDateRangeAvailable(int homestayId, String checkIn, String checkOut) throws SQLException {
        String sql = "SELECT 1 FROM bookings WHERE homestay_id = ? AND status != 'cancelled' AND " +
                     "(check_in < ? AND check_out > ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, homestayId);
            stmt.setString(2, checkOut);  // keluar lebih awal dari tarikh keluar baru
            stmt.setString(3, checkIn);   // masuk lebih lambat dari tarikh masuk baru
            ResultSet rs = stmt.executeQuery();
            return !rs.next(); // true jika tiada pertindihan
        }
    }


}
