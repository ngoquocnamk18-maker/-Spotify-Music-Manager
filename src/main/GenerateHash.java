package main;

import org.mindrot.jbcrypt.BCrypt;

public class GenerateHash {
    public static void main(String[] args) {
        String hashAdmin  = BCrypt.hashpw("admin123",  BCrypt.gensalt());
        String hashViewer = BCrypt.hashpw("viewer123", BCrypt.gensalt());
        
        System.out.println("Admin hash:  " + hashAdmin);
        System.out.println("Viewer hash: " + hashViewer);
        
        // In ra câu SQL luôn
        System.out.println("\n-- Copy đoạn SQL này vào Workbench:");
        System.out.println("DELETE FROM users;");
        System.out.println("INSERT INTO users (username, password_hash, role, full_name) VALUES");
        System.out.println("('admin', '" + hashAdmin + "', 'admin', 'Administrator'),");
        System.out.println("('viewer', '" + hashViewer + "', 'viewer', 'Demo Viewer');");
    }
}