package ui;

import model.User;
import ui.panels.TrackPanel;
import ui.panels.DashboardPanel;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private User currentUser;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MainFrame(User user) {
        this.currentUser = user;
        initUI();
    }

    private void initUI() {
        setTitle("Spotify Manager — " + currentUser.getFullName());
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(createHeader(), BorderLayout.NORTH);
        add(createSidebar(), BorderLayout.WEST);
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.add(new DashboardPanel(currentUser), "dashboard");
        contentPanel.add(new TrackPanel(currentUser), "tracks");
        add(contentPanel, BorderLayout.CENTER);
        cardLayout.show(contentPanel, "dashboard");
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(18, 18, 18));
        header.setPreferredSize(new Dimension(0, 50));
        header.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        JLabel logo = new JLabel("🎵 Spotify Manager");
        logo.setFont(new Font("Arial", Font.BOLD, 18));
        logo.setForeground(new Color(30, 215, 96));
        header.add(logo, BorderLayout.WEST);
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(new Color(18, 18, 18));
        JLabel userInfo = new JLabel(
            currentUser.getFullName() + " [" + currentUser.getRole().toUpperCase() + "]"
        );
        userInfo.setForeground(Color.WHITE);
        rightPanel.add(userInfo);
        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(220, 50, 50));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBorderPainted(false);
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this, "Bạn có chắc muốn đăng xuất?",
                "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new LoginFrame().setVisible(true);
                dispose();
            }
        });
        rightPanel.add(btnLogout);
        header.add(rightPanel, BorderLayout.EAST);
        return header;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(25, 25, 25));
        sidebar.setPreferredSize(new Dimension(180, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        sidebar.add(createNavButton("📊 Dashboard", "dashboard"));
        sidebar.add(createNavButton("🎵 Tracks", "tracks"));
        sidebar.add(createNavButton("📈 Charts", "dashboard"));
        sidebar.add(Box.createVerticalGlue());
        return sidebar;
    }

    private JButton createNavButton(String text, String card) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(25, 25, 25));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(40, 40, 40));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(25, 25, 25));
            }
        });
        btn.addActionListener(e -> cardLayout.show(contentPanel, card));
        return btn;
    }
}    