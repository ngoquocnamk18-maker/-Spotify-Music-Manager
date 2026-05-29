package ui;

import dao.UserDAO;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblStatus;
    private UserDAO userDAO;
    public LoginFrame() {
        userDAO = new UserDAO();
        initUI();
    }
    private void initUI() {
        setTitle("Spotify Manager — Login");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 30, 30));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 10, 8, 10);
        g.fill = GridBagConstraints.HORIZONTAL;
        JLabel title = new JLabel("🎵 Spotify Manager", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(30, 215, 96));
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        panel.add(title, g);
        JLabel lblUser = new JLabel("Username:");
        lblUser.setForeground(Color.WHITE);
        g.gridx = 0; g.gridy = 1; g.gridwidth = 1;
        panel.add(lblUser, g);
        txtUsername = new JTextField(15);
        g.gridx = 1; g.gridy = 1;
        panel.add(txtUsername, g);
        JLabel lblPass = new JLabel("Password:");
        lblPass.setForeground(Color.WHITE);
        g.gridx = 0; g.gridy = 2;
        panel.add(lblPass, g);
        txtPassword = new JPasswordField(15);
        g.gridx = 1; g.gridy = 2;
        panel.add(txtPassword, g);
        btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(30, 215, 96));
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        g.gridx = 0; g.gridy = 3; g.gridwidth = 2;
        panel.add(btnLogin, g);
        lblStatus = new JLabel("", SwingConstants.CENTER);
        lblStatus.setForeground(Color.RED);
        g.gridx = 0; g.gridy = 4; g.gridwidth = 2;
        panel.add(lblStatus, g);
        add(panel);
        btnLogin.addActionListener(e -> handleLogin());
        txtPassword.addActionListener(e -> handleLogin()); // Enter = login
    }
    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        if (username.isEmpty() || password.isEmpty()) {
            lblStatus.setText("⚠ Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        btnLogin.setEnabled(false);
        lblStatus.setForeground(Color.YELLOW);
        lblStatus.setText("Đang đăng nhập...");
        SwingWorker<Optional<User>, Void> worker = new SwingWorker<>() {
            @Override
            protected Optional<User> doInBackground() {
                return userDAO.findByUsername(username);
            }
            @Override
            protected void done() {
                try {
                    Optional<User> result = get();
                    if (result.isPresent()) {
                        User user = result.get();
                        if (BCrypt.checkpw(password, user.getPasswordHash())) {
                            userDAO.updateLastLogin(user.getUserId());
                            lblStatus.setForeground(new Color(30, 215, 96));
                            lblStatus.setText("✅ Đăng nhập thành công!");
                            Timer t = new Timer(500, ev -> {
                                new MainFrame(user).setVisible(true);
                                dispose();
                            });
                            t.setRepeats(false);
                            t.start();
                        } else {
                            lblStatus.setForeground(Color.RED);
                            lblStatus.setText("❌ Sai mật khẩu!");
                            btnLogin.setEnabled(true);
                        }
                    } else {
                        lblStatus.setForeground(Color.RED);
                        lblStatus.setText("❌ Không tìm thấy tài khoản!");
                        btnLogin.setEnabled(true);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        worker.execute();
    }
}