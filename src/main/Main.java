package main;
 
import ui.LoginFrame; 
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                LoginFrame loginScreen = new LoginFrame();
                loginScreen.setVisible(true);
            } catch (Exception e) {
                System.out.println("Lỗi khởi chạy Spotify: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}