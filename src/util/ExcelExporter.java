package util;

import model.Track;
import javax.swing.*;
import java.io.*;
import java.util.List;

public class ExcelExporter {
    public static void exportTracks(List<Track> tracks, JFrame parent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Lưu file CSV (mở bằng Excel)");
        chooser.setSelectedFile(new File("spotify_tracks.csv"));
        if (chooser.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) return;
        File file = chooser.getSelectedFile();
        if (!file.getName().endsWith(".csv")) {
            file = new File(file.getAbsolutePath() + ".csv");
        }
        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
            pw.print('\uFEFF');
            pw.println("No.,Track Name,Artist,Album,Genre," +
                       "Popularity,Duration,Explicit," +
                       "Danceability,Energy,Tempo");
            for (int i = 0; i < tracks.size(); i++) {
                Track t = tracks.get(i);
                pw.printf("%d,\"%s\",\"%s\",\"%s\",\"%s\",%d,%s,%s,%.3f,%.3f,%.1f%n",
                    i + 1,
                    safe(t.getTrackName()),
                    safe(t.getArtistNames()),
                    safe(t.getAlbumName()),
                    safe(t.getGenreName()),
                    t.getPopularity(),
                    t.getDurationFormatted(),
                    t.isExplicit() ? "Yes" : "No",
                    t.getDanceability(),
                    t.getEnergy(),
                    t.getTempo()
                );
            }
            pw.println();
            pw.printf("Total Tracks:,%d,,Avg Popularity:,%.1f%n",
                tracks.size(),
                tracks.stream().mapToInt(Track::getPopularity).average().orElse(0)
            );
            JOptionPane.showMessageDialog(parent,
                "✅ Xuất file thành công!\n" + file.getAbsolutePath() +
                "\n\nMở bằng Excel để xem!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent,
                "❌ Lỗi: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private static String safe(String s) {
        if (s == null) return "";
        return s.replace("\"", "''");
    }
}