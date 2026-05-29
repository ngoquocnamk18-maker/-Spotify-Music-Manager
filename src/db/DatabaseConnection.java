package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnection {
    private static final String URL      = "jdbc:mysql://localhost:3306/spotify_manager"
                                         + "?useSSL=false&allowPublicKeyRetrieval=true"
                                         + "&serverTimezone=UTC";
    private static final String USER     = "root";
    private static final String PASSWORD = "123456";
    private static DatabaseConnection instance;
    private Connection connection;
    private DatabaseConnection() {
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Kết nối MySQL thành công!");
        } catch (SQLException e) {
            System.err.println("❌ Lỗi kết nối: " + e.getMessage());
        }
    }
    public static DatabaseConnection getInstance() {
        if (instance == null || isConnectionClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    public Connection getConnection() {
        return connection;
    }
    private static boolean isConnectionClosed() {
        try {
            return instance.connection == null || instance.connection.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔌 Đã đóng kết nối MySQL.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}