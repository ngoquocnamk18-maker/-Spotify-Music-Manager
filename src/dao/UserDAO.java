package dao;

import db.DatabaseConnection;
import model.User;
import java.sql.*;
import java.util.Optional;

public class UserDAO {
    private Connection conn;
    public UserDAO() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }
    public Optional<User> findByUsername(String username) {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM users WHERE username=?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setUsername(rs.getString("username"));
                u.setPasswordHash(rs.getString("password_hash"));
                u.setRole(rs.getString("role"));
                u.setFullName(rs.getString("full_name"));
                return Optional.of(u);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }
    public boolean updateLastLogin(int userId) {
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE users SET last_login=NOW() WHERE user_id=?")) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    public boolean updatePassword(int userId, String newHash) {
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE users SET password_hash=? WHERE user_id=?")) {
            ps.setString(1, newHash);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}
