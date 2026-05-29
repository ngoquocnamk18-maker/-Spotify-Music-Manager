package ui.panels;

import dao.TrackDAO;
import model.Track;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MusicChartPanel extends JPanel {
    private TrackDAO trackDAO;
    public MusicChartPanel() {
        this.trackDAO = new TrackDAO();
        setLayout(new GridLayout(1, 2, 10, 0));
        setBackground(new Color(40, 40, 40));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        loadCharts();
    }
    public void loadCharts() {
        removeAll();
        SwingWorker<List<Track>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Track> doInBackground() {
                return trackDAO.findAll(1, 2000);
            }
            @Override
            protected void done() {
                try {
                    List<Track> tracks = get();
                    add(buildBarChart(tracks));
                    add(buildPieChart(tracks));
                    revalidate();
                    repaint();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }
    private ChartPanel buildBarChart(List<Track> tracks) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Double> avgPopByGenre = tracks.stream()
            .filter(t -> t.getGenreName() != null)
            .collect(Collectors.groupingBy(
                Track::getGenreName,
                Collectors.averagingInt(Track::getPopularity)
            ));
        avgPopByGenre.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .limit(10)
            .forEach(e -> dataset.addValue(
                Math.round(e.getValue()),
                "Avg Popularity",
                e.getKey()
            ));
        JFreeChart chart = ChartFactory.createBarChart(
            "Top 10 Genres by Avg Popularity",
            "Genre", "Avg Popularity",
            dataset,
            PlotOrientation.VERTICAL,
            false, true, false
        );
        chart.setBackgroundPaint(new Color(40, 40, 40));
        chart.getTitle().setPaint(Color.WHITE);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(50, 50, 50));
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.getRenderer().setSeriesPaint(0, new Color(30, 215, 96));
        plot.getDomainAxis().setTickLabelPaint(Color.WHITE);
        plot.getDomainAxis().setLabelPaint(Color.WHITE);
        plot.getRangeAxis().setTickLabelPaint(Color.WHITE);
        plot.getRangeAxis().setLabelPaint(Color.WHITE);
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        return new ChartPanel(chart);
    }
    private ChartPanel buildPieChart(List<Track> tracks) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        tracks.stream()
            .filter(t -> t.getGenreName() != null)
            .collect(Collectors.groupingBy(
                Track::getGenreName,
                Collectors.counting()
            ))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(8)
            .forEach(e -> dataset.setValue(e.getKey(), e.getValue()));

        JFreeChart chart = ChartFactory.createPieChart(
            "Track Distribution by Genre",
            dataset, true, true, false
        );
        chart.setBackgroundPaint(new Color(40, 40, 40));
        chart.getTitle().setPaint(Color.WHITE);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(50, 50, 50));
        plot.setLabelBackgroundPaint(new Color(60, 60, 60));
        plot.setLabelPaint(Color.WHITE);
        plot.setOutlinePaint(null);
        Color[] colors = {
            new Color(30, 215, 96),  new Color(30, 150, 255),
            new Color(255, 165, 0),  new Color(220, 50, 50),
            new Color(180, 100, 255),new Color(255, 220, 0),
            new Color(0, 200, 200),  new Color(255, 100, 150)
        };
        var keys = dataset.getKeys();
        for (int i = 0; i < keys.size(); i++) {
            plot.setSectionPaint((Comparable<?>) keys.get(i), colors[i % colors.length]);
        }
        chart.getLegend().setBackgroundPaint(new Color(40, 40, 40));
        chart.getLegend().setItemPaint(Color.WHITE);
        return new ChartPanel(chart);
    }
}