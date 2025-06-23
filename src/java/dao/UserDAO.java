package dao;

import java.sql.*;
import model.User;

public class UserDAO {

    private final Connection conn;
    
    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    //check login 
    public User checkLogin(String email, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setUserId(rs.getInt("user_id"));
                    u.setFullName(rs.getString("full_name"));
                    u.setEmail(rs.getString("email"));
                    u.setPassword(rs.getString("password"));
                    u.setPhone(rs.getString("phone"));
                    u.setUserType(rs.getString("user_type"));
                    u.setCreatedAt(rs.getString("created_at"));
                    return u;
                }
            }
        }
        return null;
    }

    //register user
    public boolean insert(User u) throws SQLException {
        String sql = "INSERT INTO users (full_name, email, password, phone, user_type) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, u.getFullName());
            stmt.setString(2, u.getEmail());
            stmt.setString(3, u.getPassword());
            stmt.setString(4, u.getPhone());
            stmt.setString(5, u.getUserType());
            return stmt.executeUpdate() > 0;
        }
    }

    //Check kewujudan email
    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    //Update Password
    public boolean updatePassword(String email, String newPassword) throws SQLException {
        String sql = "UPDATE users SET password = ? WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPassword); // Plaintext, ubah kepada hash jika mahu
            stmt.setString(2, email);
            return stmt.executeUpdate() > 0;
        }
    }


    //Update profile
    public boolean updateProfile(int userId, String phone, String password) throws SQLException {
        String sql = "UPDATE users SET phone = ?, password = ? WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, phone);
            stmt.setString(2, password);
            stmt.setInt(3, userId);
            return stmt.executeUpdate() > 0;
        }
    }

    //Ambill user detail
    public User getById(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setUserId(rs.getInt("user_id"));
                    u.setFullName(rs.getString("full_name"));
                    u.setEmail(rs.getString("email"));
                    u.setPassword(rs.getString("password"));
                    u.setPhone(rs.getString("phone"));
                    u.setUserType(rs.getString("user_type"));
                    u.setCreatedAt(rs.getString("created_at"));
                    return u;
                }
            }
        }
        return null;
    }
}
