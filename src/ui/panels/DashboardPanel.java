package ui.panels;

import dao.TrackDAO;
import dao.GenreDAO;
import dto.SongStatsDTO;
import model.User;
import service.MusicAnalyzer;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DashboardPanel extends JPanel {
    private User currentUser;
    private TrackDAO trackDAO;
    private GenreDAO genreDAO;
    public DashboardPanel(User user) {
        this.currentUser = user;
        this.trackDAO = new TrackDAO();
        this.genreDAO = new GenreDAO();
        initUI();
    }
    private void initUI() {
        setLayout(new BorderLayout(0, 5));
        setBackground(new Color(40, 40, 40));
        // Title
        JLabel title = new JLabel("📊 Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        add(title, BorderLayout.NORTH);
        // KPI Cards
        JPanel kpiPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        kpiPanel.setBackground(new Color(40, 40, 40));
        kpiPanel.setBorder(BorderFactory.createEmptyBorder(5, 30, 5, 30));
        int totalTracks = trackDAO.countAll();
        int totalGenres = genreDAO.findAll().size();
        kpiPanel.add(createKpiCard("🎵 Total Tracks",
            String.valueOf(totalTracks), new Color(30, 215, 96)));
        kpiPanel.add(createKpiCard("🎸 Total Genres",
            String.valueOf(totalGenres), new Color(30, 150, 255)));
        kpiPanel.add(createKpiCard("👤 Logged in as",
            currentUser.getRole().toUpperCase(), new Color(255, 165, 0)));
        add(kpiPanel, BorderLayout.CENTER);
        // South: Charts + Stats
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(new Color(40, 40, 40));
        // Charts
        MusicChartPanel chartPanel = new MusicChartPanel();
        chartPanel.setPreferredSize(new Dimension(0, 320));
        southPanel.add(chartPanel, BorderLayout.CENTER);
        // Stats text area
        JTextArea statsArea = new JTextArea(4, 0);
        statsArea.setEditable(false);
        statsArea.setBackground(new Color(30, 30, 30));
        statsArea.setForeground(new Color(30, 215, 96));
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        statsArea.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        SwingWorker<List<SongStatsDTO>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<SongStatsDTO> doInBackground() {
                var tracks = trackDAO.findAll(1, 2000);
                return MusicAnalyzer.analyzeByGenre(tracks);
            }
            @Override
            protected void done() {
                try {
                    var stats = get();
                    var sb = new StringBuilder("📊 Genre Statistics (Top 5):\n");
                    sb.append("─".repeat(75)).append("\n");
                    stats.stream().limit(5).forEach(s ->
                        sb.append(s.formatted()).append("\n")
                    );
                    statsArea.setText(sb.toString());
                } catch (Exception e) { e.printStackTrace(); }
            }
        };
        worker.execute();
        southPanel.add(new JScrollPane(statsArea), BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);
    }
    private JPanel createKpiCard(String label, String value, Color color) {
        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBackground(new Color(50, 50, 50));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        JLabel valLabel = new JLabel(value, SwingConstants.CENTER);
        valLabel.setFont(new Font("Arial", Font.BOLD, 32));
        valLabel.setForeground(color);
        JLabel lblLabel = new JLabel(label, SwingConstants.CENTER);
        lblLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        lblLabel.setForeground(Color.WHITE);
        card.add(valLabel);
        card.add(lblLabel);
        return card;
    }
}