package dao;

import model.Homestay;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HomestayDAO {

    private final Connection conn;

    public HomestayDAO(Connection conn) {
        this.conn = conn;
    }

    // Tambah homestay dan return homestay_id baru
    public int insert(Homestay h) throws SQLException {
        String sql = "INSERT INTO homestays (user_id, name, description, address, city, state, price_per_night, max_guests, " +
                     "has_wifi, has_parking, has_aircond, has_tv, has_kitchen, has_washing_machine, num_bedrooms, num_bathrooms) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, h.getUserId());
            stmt.setString(2, h.getName());
            stmt.setString(3, h.getDescription());
            stmt.setString(4, h.getAddress());
            stmt.setString(5, h.getCity());
            stmt.setString(6, h.getState());
            stmt.setDouble(7, h.getPricePerNight());
            stmt.setInt(8, h.getMaxGuests());
            stmt.setBoolean(9, h.isHasWifi());
            stmt.setBoolean(10, h.isHasParking());
            stmt.setBoolean(11, h.isHasAircond());
            stmt.setBoolean(12, h.isHasTv());
            stmt.setBoolean(13, h.isHasKitchen());
            stmt.setBoolean(14, h.isHasWashingMachine());
            stmt.setInt(15, h.getNumBedrooms());
            stmt.setInt(16, h.getNumBathrooms());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Gagal tambah homestay, tiada baris dimasukkan.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // homestay_id baru
                } else {
                    throw new SQLException("Gagal dapatkan ID homestay.");
                }
            }
        }
    }



    // ✅ Dapatkan senarai homestay milik owner tertentu
    public List<Homestay> getByOwner(int ownerId) throws SQLException {
        List<Homestay> list = new ArrayList<>();
        String sql = "SELECT * FROM homestays WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ownerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Homestay h = new Homestay();
                    h.setHomestayId(rs.getInt("homestay_id"));
                    h.setUserId(rs.getInt("user_id"));
                    h.setName(rs.getString("name"));
                    h.setDescription(rs.getString("description"));
                    h.setAddress(rs.getString("address"));
                    h.setCity(rs.getString("city"));
                    h.setState(rs.getString("state"));
                    h.setPricePerNight(rs.getDouble("price_per_night"));
                    h.setMaxGuests(rs.getInt("max_guests"));
                    h.setCreatedAt(rs.getString("created_at"));
                    list.add(h);
                }
            }
        }
        return list;
    }

    // ✅ Kira bilangan homestay milik owner
    public int countByOwner(int ownerId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM homestays WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ownerId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }
    
    // Ambil semua homestay milik owner tertentu
    public List<Homestay> getHomestaysByOwner(int ownerId) throws SQLException {
        List<Homestay> list = new ArrayList<>();
        String sql = "SELECT h.*, i.image_path " +
                     "FROM homestays h " +
                     "LEFT JOIN ( " +
                     "   SELECT i1.homestay_id, i1.image_path " +
                     "   FROM images i1 " +
                     "   INNER JOIN ( " +
                     "     SELECT homestay_id, MIN(uploaded_at) AS first_upload " +
                     "     FROM images GROUP BY homestay_id " +
                     "   ) i2 ON i1.homestay_id = i2.homestay_id AND i1.uploaded_at = i2.first_upload " +
                     ") i ON h.homestay_id = i.homestay_id " +
                     "WHERE h.user_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ownerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Homestay h = new Homestay();
                h.setHomestayId(rs.getInt("homestay_id"));
                h.setUserId(rs.getInt("user_id"));
                h.setName(rs.getString("name"));
                h.setDescription(rs.getString("description"));
                h.setAddress(rs.getString("address"));
                h.setCity(rs.getString("city"));
                h.setState(rs.getString("state"));
                h.setPricePerNight(rs.getDouble("price_per_night"));
                h.setMaxGuests(rs.getInt("max_guests"));
                h.setCreatedAt(rs.getString("created_at"));
                h.setImagePath(rs.getString("image_path"));
                h.setHasWifi(rs.getBoolean("has_wifi"));
                h.setHasParking(rs.getBoolean("has_parking"));
                h.setHasAircond(rs.getBoolean("has_aircond"));
                h.setHasTv(rs.getBoolean("has_tv"));
                h.setHasKitchen(rs.getBoolean("has_kitchen"));
                h.setHasWashingMachine(rs.getBoolean("has_washing_machine"));
                h.setNumBedrooms(rs.getInt("num_bedrooms"));
                h.setNumBathrooms(rs.getInt("num_bathrooms"));

                list.add(h);
            }
        }
        return list;
    }


    //Update homestay details
    public boolean update(Homestay h) throws SQLException {
        String sql = "UPDATE homestays SET name = ?, description = ?, address = ?, city = ?, state = ?, price_per_night = ?, max_guests = ?, " +
                     "has_wifi = ?, has_parking = ?, has_aircond = ?, has_tv = ?, has_kitchen = ?, has_washing_machine = ?, " +
                     "num_bedrooms = ?, num_bathrooms = ? " +
                     "WHERE homestay_id = ? AND user_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, h.getName());
            stmt.setString(2, h.getDescription());
            stmt.setString(3, h.getAddress());
            stmt.setString(4, h.getCity());
            stmt.setString(5, h.getState());
            stmt.setDouble(6, h.getPricePerNight());
            stmt.setInt(7, h.getMaxGuests());
            stmt.setBoolean(8, h.isHasWifi());
            stmt.setBoolean(9, h.isHasParking());
            stmt.setBoolean(10, h.isHasAircond());
            stmt.setBoolean(11, h.isHasTv());
            stmt.setBoolean(12, h.isHasKitchen());
            stmt.setBoolean(13, h.isHasWashingMachine());
            stmt.setInt(14, h.getNumBedrooms());
            stmt.setInt(15, h.getNumBathrooms());
            stmt.setInt(16, h.getHomestayId());
            stmt.setInt(17, h.getUserId());

            return stmt.executeUpdate() > 0;
        }
    }

    
    //Delete Homestay
    public boolean delete(int homestayId, int ownerId) throws SQLException {
        String sql = "DELETE FROM homestays WHERE homestay_id = ? AND user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, homestayId);
            stmt.setInt(2, ownerId);
            return stmt.executeUpdate() > 0;
        }
    }
    
    // ✅ Dapatkan maklumat homestay berdasarkan homestay_id
    public Homestay getById(int id) throws SQLException {
        String sql = "SELECT h.*, " +
                     "       (SELECT image_path FROM images WHERE homestay_id = h.homestay_id ORDER BY uploaded_at ASC LIMIT 1) AS image_path " +
                     "FROM homestays h " +
                     "WHERE h.homestay_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Homestay h = new Homestay();
                    h.setHomestayId(rs.getInt("homestay_id"));
                    h.setUserId(rs.getInt("user_id"));
                    h.setName(rs.getString("name"));
                    h.setDescription(rs.getString("description"));
                    h.setAddress(rs.getString("address"));
                    h.setCity(rs.getString("city"));
                    h.setState(rs.getString("state"));
                    h.setPricePerNight(rs.getDouble("price_per_night"));
                    h.setMaxGuests(rs.getInt("max_guests"));
                    h.setHasWifi(rs.getBoolean("has_wifi"));
                    h.setHasParking(rs.getBoolean("has_parking"));
                    h.setHasAircond(rs.getBoolean("has_aircond"));
                    h.setHasTv(rs.getBoolean("has_tv"));
                    h.setHasKitchen(rs.getBoolean("has_kitchen"));
                    h.setHasWashingMachine(rs.getBoolean("has_washing_machine"));
                    h.setNumBedrooms(rs.getInt("num_bedrooms"));
                    h.setNumBathrooms(rs.getInt("num_bathrooms"));
                    h.setCreatedAt(rs.getString("created_at"));
                    h.setImagePath(rs.getString("image_path")); // from subquery
                    return h;
                }
            }
        }
        return null;
    }


    // Dapatkan homestay rawak (bersama gambar pertama)
    public List<Homestay> getRandomHomestays(int limit) throws SQLException {
        List<Homestay> list = new ArrayList<>();
        String sql = "SELECT h.*, " +
                     "  (SELECT i.image_path FROM images i WHERE i.homestay_id = h.homestay_id ORDER BY i.uploaded_at ASC LIMIT 1) AS image_path " +
                     "FROM homestays h " +
                     "ORDER BY RAND() LIMIT ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Homestay h = new Homestay();
                    h.setHomestayId(rs.getInt("homestay_id"));
                    h.setName(rs.getString("name"));
                    h.setCity(rs.getString("city"));
                    h.setState(rs.getString("state"));
                    h.setPricePerNight(rs.getDouble("price_per_night"));
                    h.setImagePath(rs.getString("image_path"));
                    list.add(h);
                }
            }
        }
        return list;
    }

    // Cari homestay berdasarkan lokasi + penapis
    public List<Homestay> searchHomestays(String keyword, String wifi, String aircond, String minBeds) throws SQLException {
        List<Homestay> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
            "SELECT h.*, i.image_path " +
            "FROM homestays h " +
            "LEFT JOIN ( " +
            "   SELECT i1.homestay_id, i1.image_path " +
            "   FROM images i1 " +
            "   INNER JOIN (SELECT homestay_id, MIN(uploaded_at) AS first_upload FROM images GROUP BY homestay_id) i2 " +
            "   ON i1.homestay_id = i2.homestay_id AND i1.uploaded_at = i2.first_upload " +
            ") i ON h.homestay_id = i.homestay_id " +
            "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        // Carian lokasi
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (LOWER(h.city) LIKE ? OR LOWER(h.state) LIKE ?) ");
            String like = "%" + keyword.toLowerCase() + "%";
            params.add(like);
            params.add(like);
        }

        // Penapis: wifi
        if ("on".equalsIgnoreCase(wifi)) {
            sql.append("AND h.has_wifi = 1 ");
        }

        // Penapis: aircond
        if ("on".equalsIgnoreCase(aircond)) {
            sql.append("AND h.has_aircond = 1 ");
        }

        // Penapis: bilangan bilik tidur minimum
        if (minBeds != null && !minBeds.trim().isEmpty()) {
            try {
                int min = Integer.parseInt(minBeds);
                sql.append("AND h.num_bedrooms >= ? ");
                params.add(min);
            } catch (NumberFormatException ignore) {}
        }

        sql.append("ORDER BY h.homestay_id DESC");

        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Homestay h = new Homestay();
                h.setHomestayId(rs.getInt("homestay_id"));
                h.setUserId(rs.getInt("user_id"));
                h.setName(rs.getString("name"));
                h.setDescription(rs.getString("description"));
                h.setAddress(rs.getString("address"));
                h.setCity(rs.getString("city"));
                h.setState(rs.getString("state"));
                h.setPricePerNight(rs.getDouble("price_per_night"));
                h.setMaxGuests(rs.getInt("max_guests"));
                h.setCreatedAt(rs.getString("created_at"));
                h.setImagePath(rs.getString("image_path"));

                // Kemudahan
                h.setHasWifi(rs.getBoolean("has_wifi"));
                h.setHasAircond(rs.getBoolean("has_aircond"));
                h.setNumBedrooms(rs.getInt("num_bedrooms"));

                list.add(h);
            }
        }

        return list;
    }
    
    //Senaraikan semua homestay yang ada dalam database
    public List<Homestay> getAllHomestays() throws SQLException {
        List<Homestay> list = new ArrayList<>();
        String sql = "SELECT h.*, i.image_path " +
                     "FROM homestays h " +
                     "LEFT JOIN ( " +
                     "  SELECT i1.homestay_id, i1.image_path " +
                     "  FROM images i1 " +
                     "  INNER JOIN (SELECT homestay_id, MIN(uploaded_at) AS first_upload FROM images GROUP BY homestay_id) i2 " +
                     "  ON i1.homestay_id = i2.homestay_id AND i1.uploaded_at = i2.first_upload " +
                     ") i ON h.homestay_id = i.homestay_id " +
                     "ORDER BY h.homestay_id DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Homestay h = new Homestay();
                h.setHomestayId(rs.getInt("homestay_id"));
                h.setUserId(rs.getInt("user_id"));
                h.setName(rs.getString("name"));
                h.setDescription(rs.getString("description"));
                h.setAddress(rs.getString("address"));
                h.setCity(rs.getString("city"));
                h.setState(rs.getString("state"));
                h.setPricePerNight(rs.getDouble("price_per_night"));
                h.setMaxGuests(rs.getInt("max_guests"));
                h.setCreatedAt(rs.getString("created_at"));
                h.setImagePath(rs.getString("image_path"));
                list.add(h);
            }
        }

        return list;
    }
    // ❌ Tambahan lain seperti update/delete boleh dibuat kemudian
}
