package ui.panels;

import util.ExcelExporter;
import dao.TrackDAO;
import dao.GenreDAO;
import model.Track;
import model.Genre;
import model.User;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class TrackPanel extends JPanel {

    private User currentUser;
    private TrackDAO trackDAO;
    private GenreDAO genreDAO;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<Genre> cmbGenre;

    private static final String[] COLUMNS = {
        "Track ID", "Title", "Artist", "Album", "Genre", "Popularity", "Duration", "Explicit"
    };

    public TrackPanel(User user) {
        this.currentUser = user;
        this.trackDAO = new TrackDAO();
        this.genreDAO = new GenreDAO();
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(40, 40, 40));

        // Top toolbar
        add(createToolbar(), BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setBackground(new Color(50, 50, 50));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(70, 70, 70));
        table.getTableHeader().setBackground(new Color(30, 215, 96));
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.setSelectionBackground(new Color(30, 215, 96, 100));
        table.setFont(new Font("Arial", Font.PLAIN, 13));

        // Ẩn cột Track ID
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom buttons (chỉ admin mới thấy Add/Edit/Delete)
        if (currentUser.isAdmin()) {
            add(createButtonPanel(), BorderLayout.SOUTH);
        }
    }

    private JPanel createToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        toolbar.setBackground(new Color(30, 30, 30));

        // Search
        txtSearch = new JTextField(20);
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm bài hát, nghệ sĩ...");
        JButton btnSearch = new JButton("🔍 Search");
        btnSearch.setBackground(new Color(30, 215, 96));
        btnSearch.setForeground(Color.BLACK);
        btnSearch.addActionListener(e -> searchTracks());
        txtSearch.addActionListener(e -> searchTracks());

        // Genre filter
        JLabel lblGenre = new JLabel("Genre:");
        lblGenre.setForeground(Color.WHITE);
        cmbGenre = new JComboBox<>();
        cmbGenre.addItem(new Genre(0, "-- All Genres --"));
        genreDAO.findAll().forEach(cmbGenre::addItem);
        cmbGenre.addActionListener(e -> filterByGenre());

        // Refresh
        JButton btnRefresh = new JButton("🔄 Refresh");
        btnRefresh.addActionListener(e -> loadData());

        toolbar.add(txtSearch);
        toolbar.add(btnSearch);
        toolbar.add(new JSeparator(SwingConstants.VERTICAL));
        toolbar.add(lblGenre);
        toolbar.add(cmbGenre);
        toolbar.add(btnRefresh);

        return toolbar;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        panel.setBackground(new Color(30, 30, 30));

        JButton btnAdd    = new JButton("➕ Add");
        JButton btnEdit   = new JButton("✏️ Edit");
        JButton btnDelete = new JButton("🗑️ Delete");
        JButton btnExport = new JButton("📥 Export Excel");

        btnAdd.setBackground(new Color(30, 215, 96));
        btnAdd.setForeground(Color.BLACK);
        btnEdit.setBackground(new Color(30, 150, 255));
        btnEdit.setForeground(Color.WHITE);
        btnDelete.setBackground(new Color(220, 50, 50));
        btnDelete.setForeground(Color.WHITE);
        btnExport.setBackground(new Color(255, 165, 0));
        btnExport.setForeground(Color.BLACK);

        btnAdd.addActionListener(e -> addTrack());
        btnEdit.addActionListener(e -> editTrack());
        btnDelete.addActionListener(e -> deleteTrack());
        btnExport.addActionListener(e -> {
            var tracks = trackDAO.findAll(1, 1000);
            ExcelExporter.exportTracks(
                tracks,
                (JFrame) SwingUtilities.getWindowAncestor(this)
            );
        });

        panel.add(btnAdd);
        panel.add(btnEdit);
        panel.add(btnDelete);
        panel.add(btnExport);
        return panel;
    }

    private void loadData() {
        SwingWorker<List<Track>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Track> doInBackground() {
                return trackDAO.findAll(1, 500);
            }
            @Override
            protected void done() {
                try {
                    populateTable(get());
                } catch (Exception e) { e.printStackTrace(); }
            }
        };
        worker.execute();
    }

    private void searchTracks() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) { loadData(); return; }
        SwingWorker<List<Track>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Track> doInBackground() {
                return trackDAO.search(keyword);
            }
            @Override
            protected void done() {
                try { populateTable(get()); }
                catch (Exception e) { e.printStackTrace(); }
            }
        };
        worker.execute();
    }

    private void filterByGenre() {
        Genre selected = (Genre) cmbGenre.getSelectedItem();
        if (selected == null || selected.getGenreId() == 0) { loadData(); return; }
        SwingWorker<List<Track>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Track> doInBackground() {
                return trackDAO.findByGenre(selected.getGenreId());
            }
            @Override
            protected void done() {
                try { populateTable(get()); }
                catch (Exception e) { e.printStackTrace(); }
            }
        };
        worker.execute();
    }

    private void populateTable(List<Track> tracks) {
        tableModel.setRowCount(0);
        for (Track t : tracks) {
            tableModel.addRow(new Object[]{
                t.getTrackId(),
                t.getTrackName(),
                t.getArtistNames(),
                t.getAlbumName(),
                t.getGenreName(),
                t.getPopularity(),
                t.getDurationFormatted(),
                t.isExplicit() ? "Yes" : "No"
            });
        }
    }

    private void addTrack() {
        TrackDialog dialog = new TrackDialog(
            (Frame) SwingUtilities.getWindowAncestor(this), null
        );
        dialog.setVisible(true);
        if (dialog.isSaved()) loadData();
    }

    private void editTrack() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 bài hát!");
            return;
        }
        String trackId = (String) tableModel.getValueAt(row, 0);
        trackDAO.findById(trackId).ifPresent(track -> {
            TrackDialog dialog = new TrackDialog(
                (Frame) SwingUtilities.getWindowAncestor(this), track
            );
            dialog.setVisible(true);
            if (dialog.isSaved()) loadData();
        });
    }

    private void deleteTrack() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 bài hát!");
            return;
        }
        String trackId = (String) tableModel.getValueAt(row, 0);
        String name    = (String) tableModel.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(
            this, "Xóa bài: " + name + "?", "Confirm", JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            if (trackDAO.delete(trackId)) {
                tableModel.removeRow(row);
                JOptionPane.showMessageDialog(this, "✅ Đã xóa!");
            }
        }
    }
}