package ui.panels;

import dao.GenreDAO;
import dao.TrackDAO;
import model.Genre;
import model.Track;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.UUID;

public class TrackDialog extends JDialog {

    private Track track;
    private boolean saved = false;
    private TrackDAO trackDAO;
    private GenreDAO genreDAO;

    // Form fields
    private JTextField txtName, txtPopularity, txtDuration,
                       txtDanceability, txtEnergy, txtTempo;
    private JComboBox<Genre> cmbGenre;
    private JCheckBox chkExplicit;

    public TrackDialog(Frame parent, Track track) {
        super(parent, track == null ? "➕ Add Track" : "✏️ Edit Track", true);
        this.track    = track;
        this.trackDAO = new TrackDAO();
        this.genreDAO = new GenreDAO();
        initUI();
        if (track != null) fillForm();
    }

    private void initUI() {
        setSize(480, 460);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBackground(new Color(40, 40, 40));
        main.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Form
        JPanel form = new JPanel(new GridLayout(8, 2, 8, 10));
        form.setBackground(new Color(40, 40, 40));

        // Track Name
        form.add(makeLabel("Track Name *:"));
        txtName = new JTextField();
        form.add(txtName);

        // Genre
        form.add(makeLabel("Genre *:"));
        cmbGenre = new JComboBox<>();
        List<Genre> genres = genreDAO.findAll();
        genres.forEach(cmbGenre::addItem);
        form.add(cmbGenre);

        // Popularity
        form.add(makeLabel("Popularity (0-100) *:"));
        txtPopularity = new JTextField();
        form.add(txtPopularity);

        // Duration
        form.add(makeLabel("Duration (ms) *:"));
        txtDuration = new JTextField();
        form.add(txtDuration);

        // Danceability
        form.add(makeLabel("Danceability (0.0-1.0):"));
        txtDanceability = new JTextField("0.5");
        form.add(txtDanceability);

        // Energy
        form.add(makeLabel("Energy (0.0-1.0):"));
        txtEnergy = new JTextField("0.5");
        form.add(txtEnergy);

        // Tempo
        form.add(makeLabel("Tempo (BPM):"));
        txtTempo = new JTextField("120.0");
        form.add(txtTempo);

        // Explicit
        form.add(makeLabel("Explicit:"));
        chkExplicit = new JCheckBox();
        chkExplicit.setBackground(new Color(40, 40, 40));
        form.add(chkExplicit);

        main.add(form, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(new Color(40, 40, 40));

        JButton btnSave   = new JButton("💾 Save");
        JButton btnCancel = new JButton("Cancel");

        btnSave.setBackground(new Color(30, 215, 96));
        btnSave.setForeground(Color.BLACK);
        btnSave.setFont(new Font("Arial", Font.BOLD, 13));
        btnCancel.setBackground(new Color(80, 80, 80));
        btnCancel.setForeground(Color.WHITE);

        btnSave.addActionListener(e -> handleSave());
        btnCancel.addActionListener(e -> dispose());

        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);
        main.add(btnPanel, BorderLayout.SOUTH);

        add(main);
    }

    private JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.PLAIN, 13));
        return lbl;
    }

    private void fillForm() {
        txtName.setText(track.getTrackName());
        txtPopularity.setText(String.valueOf(track.getPopularity()));
        txtDuration.setText(String.valueOf(track.getDurationMs()));
        txtDanceability.setText(String.valueOf(track.getDanceability()));
        txtEnergy.setText(String.valueOf(track.getEnergy()));
        txtTempo.setText(String.valueOf(track.getTempo()));
        chkExplicit.setSelected(track.isExplicit());

        // Set genre
        for (int i = 0; i < cmbGenre.getItemCount(); i++) {
            if (cmbGenre.getItemAt(i).getGenreId() == track.getGenreId()) {
                cmbGenre.setSelectedIndex(i);
                break;
            }
        }
    }

    private void handleSave() {
        // Validate
        if (txtName.getText().trim().isEmpty()) {
            showError("Track name không được để trống!");
            return;
        }
        if (!isValidInt(txtPopularity.getText(), 0, 100)) {
            showError("Popularity phải là số từ 0 đến 100!");
            return;
        }
        if (!isValidInt(txtDuration.getText(), 1, 9999999)) {
            showError("Duration phải là số nguyên dương (milliseconds)!");
            return;
        }
        if (!isValidDouble(txtDanceability.getText(), 0.0, 1.0)) {
            showError("Danceability phải từ 0.0 đến 1.0!");
            return;
        }
        if (!isValidDouble(txtEnergy.getText(), 0.0, 1.0)) {
            showError("Energy phải từ 0.0 đến 1.0!");
            return;
        }

        // Build track object
        Track t = (track == null) ? new Track() : track;
        if (track == null) {
            // Generate unique ID cho track mới
            t.setTrackId(UUID.randomUUID().toString().replace("-", "").substring(0, 22));
        }
        t.setTrackName(txtName.getText().trim());
        t.setGenreId(((Genre) cmbGenre.getSelectedItem()).getGenreId());
        t.setPopularity(Integer.parseInt(txtPopularity.getText().trim()));
        t.setDurationMs(Integer.parseInt(txtDuration.getText().trim()));
        t.setDanceability(Double.parseDouble(txtDanceability.getText().trim()));
        t.setEnergy(Double.parseDouble(txtEnergy.getText().trim()));
        t.setTempo(Double.parseDouble(txtTempo.getText().trim()));
        t.setExplicit(chkExplicit.isSelected());
        t.setAlbumId(1); // default album

        // Save or Update
        boolean ok = (track == null) ? trackDAO.save(t) : trackDAO.update(t);

        if (ok) {
            saved = true;
            JOptionPane.showMessageDialog(this, "✅ Lưu thành công!");
            dispose();
        } else {
            showError("❌ Lưu thất bại! Kiểm tra lại.");
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private boolean isValidInt(String val, int min, int max) {
        try {
            int n = Integer.parseInt(val.trim());
            return n >= min && n <= max;
        } catch (NumberFormatException e) { return false; }
    }

    private boolean isValidDouble(String val, double min, double max) {
        try {
            double d = Double.parseDouble(val.trim());
            return d >= min && d <= max;
        } catch (NumberFormatException e) { return false; }
    }

    public boolean isSaved() { return saved; }
    public Track getTrack()  { return track; }
}