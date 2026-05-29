package db;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConnection - Singleton Pattern
 * Đảm bảo chỉ có 1 kết nối duy nhất tới MySQL
 */
public class DatabaseConnection {

    // Thông tin kết nối - anh chỉnh lại nếu cần
    private static final String URL      = "jdbc:mysql://localhost:3306/spotify_manager"
                                         + "?useSSL=false&allowPublicKeyRetrieval=true"
                                         + "&serverTimezone=UTC";
    private static final String USER     = "root";
    private static final String PASSWORD = "123456";

    // Instance duy nhất (Singleton)
    private static DatabaseConnection instance;
    private Connection connection;

    // Constructor private - không cho tạo từ bên ngoài
    private DatabaseConnection() {
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Kết nối MySQL thành công!");
        } catch (SQLException e) {
            System.err.println("❌ Lỗi kết nối: " + e.getMessage());
        }
    }

    // Lấy instance duy nhất
    public static DatabaseConnection getInstance() {
        if (instance == null || isConnectionClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // Lấy connection để dùng trong DAO
    public Connection getConnection() {
        return connection;
    }

    // Kiểm tra connection còn sống không
    private static boolean isConnectionClosed() {
        try {
            return instance.connection == null || instance.connection.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }

    // Đóng kết nối khi tắt app
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