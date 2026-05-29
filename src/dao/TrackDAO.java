package dao;


import db.DatabaseConnection;
import model.Track;
import java.sql.*;
import java.util.*;

public class TrackDAO implements BaseDAO<Track> {
    private Connection conn;
    public TrackDAO() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }
    @Override
    public Optional<Track> findById(String id) {
        String sql = "SELECT t.*, a.album_name, g.genre_name, " +
                     "GROUP_CONCAT(ar.artist_name SEPARATOR ', ') as artist_names " +
                     "FROM tracks t " +
                     "LEFT JOIN albums a ON t.album_id = a.album_id " +
                     "LEFT JOIN genres g ON t.genre_id = g.genre_id " +
                     "LEFT JOIN track_artists ta ON t.track_id = ta.track_id " +
                     "LEFT JOIN artists ar ON ta.artist_id = ar.artist_id " +
                     "WHERE t.track_id = ? GROUP BY t.track_id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }
    @Override
    public List<Track> findAll() {
        return findAll(1, 1000);
    }
    @Override
    public List<Track> findAll(int page, int pageSize) {
        List<Track> list = new ArrayList<>();
        String sql = "SELECT t.*, a.album_name, g.genre_name, " +
                     "GROUP_CONCAT(ar.artist_name SEPARATOR ', ') as artist_names " +
                     "FROM tracks t " +
                     "LEFT JOIN albums a ON t.album_id = a.album_id " +
                     "LEFT JOIN genres g ON t.genre_id = g.genre_id " +
                     "LEFT JOIN track_artists ta ON t.track_id = ta.track_id " +
                     "LEFT JOIN artists ar ON ta.artist_id = ar.artist_id " +
                     "GROUP BY t.track_id " +
                     "ORDER BY t.popularity DESC " +
                     "LIMIT ? OFFSET ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pageSize);
            ps.setInt(2, (page - 1) * pageSize);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    public List<Track> search(String keyword) {
        List<Track> list = new ArrayList<>();
        String sql = "SELECT t.*, a.album_name, g.genre_name, " +
                     "GROUP_CONCAT(ar.artist_name SEPARATOR ', ') as artist_names " +
                     "FROM tracks t " +
                     "LEFT JOIN albums a ON t.album_id = a.album_id " +
                     "LEFT JOIN genres g ON t.genre_id = g.genre_id " +
                     "LEFT JOIN track_artists ta ON t.track_id = ta.track_id " +
                     "LEFT JOIN artists ar ON ta.artist_id = ar.artist_id " +
                     "WHERE t.track_name LIKE ? OR ar.artist_name LIKE ? " +
                     "GROUP BY t.track_id LIMIT 100";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    public List<Track> findByGenre(int genreId) {
        List<Track> list = new ArrayList<>();
        String sql = "SELECT t.*, a.album_name, g.genre_name, " +
                     "GROUP_CONCAT(ar.artist_name SEPARATOR ', ') as artist_names " +
                     "FROM tracks t " +
                     "LEFT JOIN albums a ON t.album_id = a.album_id " +
                     "LEFT JOIN genres g ON t.genre_id = g.genre_id " +
                     "LEFT JOIN track_artists ta ON t.track_id = ta.track_id " +
                     "LEFT JOIN artists ar ON ta.artist_id = ar.artist_id " +
                     "WHERE t.genre_id = ? " +
                     "GROUP BY t.track_id ORDER BY t.popularity DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, genreId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    @Override
    public boolean save(Track t) {
        String sql = "INSERT INTO tracks (track_id, track_name, album_id, genre_id, " +
                     "popularity, duration_ms, explicit, danceability, energy, " +
                     "loudness, tempo, valence) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getTrackId());
            ps.setString(2, t.getTrackName());
            ps.setInt(3, t.getAlbumId());
            ps.setInt(4, t.getGenreId());
            ps.setInt(5, t.getPopularity());
            ps.setInt(6, t.getDurationMs());
            ps.setBoolean(7, t.isExplicit());
            ps.setDouble(8, t.getDanceability());
            ps.setDouble(9, t.getEnergy());
            ps.setDouble(10, t.getLoudness());
            ps.setDouble(11, t.getTempo());
            ps.setDouble(12, t.getValence());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    @Override
    public boolean update(Track t) {
        String sql = "UPDATE tracks SET track_name=?, popularity=?, " +
                     "danceability=?, energy=?, tempo=?, valence=? " +
                     "WHERE track_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getTrackName());
            ps.setInt(2, t.getPopularity());
            ps.setDouble(3, t.getDanceability());
            ps.setDouble(4, t.getEnergy());
            ps.setDouble(5, t.getTempo());
            ps.setDouble(6, t.getValence());
            ps.setString(7, t.getTrackId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    @Override
    public boolean delete(String id) {
        try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM tracks WHERE track_id=?")) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    private Track mapRow(ResultSet rs) throws SQLException {
        Track t = new Track();
        t.setTrackId(rs.getString("track_id"));
        t.setTrackName(rs.getString("track_name"));
        t.setAlbumId(rs.getInt("album_id"));
        t.setGenreId(rs.getInt("genre_id"));
        t.setPopularity(rs.getInt("popularity"));
        t.setDurationMs(rs.getInt("duration_ms"));
        t.setExplicit(rs.getBoolean("explicit"));
        t.setDanceability(rs.getDouble("danceability"));
        t.setEnergy(rs.getDouble("energy"));
        t.setLoudness(rs.getDouble("loudness"));
        t.setTempo(rs.getDouble("tempo"));
        t.setValence(rs.getDouble("valence"));
        t.setAlbumName(rs.getString("album_name"));
        t.setGenreName(rs.getString("genre_name"));
        t.setArtistNames(rs.getString("artist_names"));
        return t;
    }
    public int countAll() {
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM tracks")) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }
}