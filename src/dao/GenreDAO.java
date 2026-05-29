package dao;

import db.DatabaseConnection;
import model.Genre;
import java.sql.*;
import java.util.*;

public class GenreDAO {
    private Connection conn;
    public GenreDAO() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }
    public List<Genre> findAll() {
        List<Genre> list = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(
                "SELECT * FROM genres ORDER BY genre_name")) {
            while (rs.next()) {
                list.add(new Genre(
                    rs.getInt("genre_id"),
                    rs.getString("genre_name")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    public Optional<Genre> findById(String id) {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM genres WHERE genre_id=?")) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(new Genre(
                rs.getInt("genre_id"), rs.getString("genre_name")));
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }
}