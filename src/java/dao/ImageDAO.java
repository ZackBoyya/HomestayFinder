package dao;

import model.Image;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImageDAO {
    private final Connection conn;

    public ImageDAO(Connection conn) {
        this.conn = conn;
    }

    // ✅ Tambah gambar untuk homestay
    public boolean insert(Image img) throws SQLException {
        String sql = "INSERT INTO images (homestay_id, image_path) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, img.getHomestayId());
            stmt.setString(2, img.getImagePath());
            return stmt.executeUpdate() > 0;
        }
    }

    // ✅ Ambil semua gambar untuk homestay tertentu
    public List<Image> getImagesByHomestay(int homestayId) throws SQLException {
        List<Image> list = new ArrayList<>();
        String sql = "SELECT * FROM images WHERE homestay_id = ? ORDER BY uploaded_at ASC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, homestayId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Image img = new Image();
                    img.setImageId(rs.getInt("image_id"));
                    img.setHomestayId(rs.getInt("homestay_id"));
                    img.setImagePath(rs.getString("image_path"));
                    img.setUploadedAt(rs.getString("uploaded_at"));
                    list.add(img);
                }
            }
        }
        return list;
    }

    // ✅ Ambil gambar pertama homestay (utama)
    public String getFirstImagePath(int homestayId) throws SQLException {
        String sql = "SELECT image_path FROM images WHERE homestay_id = ? ORDER BY uploaded_at ASC LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, homestayId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("image_path");
                }
            }
        }
        return null;
    }
}
