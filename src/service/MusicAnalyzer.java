package service;

import dto.SongStatsDTO;
import dto.TrackDTO;
import model.Track;
import java.util.*;
import java.util.stream.*;

public class MusicAnalyzer {
    public static List<TrackDTO> toTrackDTOs(List<Track> tracks) {
        return tracks.stream()
            .filter(t -> t.getTrackName() != null)
            .map(t -> new TrackDTO(
                t.getTrackId(),
                t.getTrackName(),
                t.getArtistNames() != null ? t.getArtistNames() : "Unknown",
                t.getGenreName()   != null ? t.getGenreName()   : "Unknown",
                t.getPopularity(),
                t.getDurationFormatted()
            ))
            .collect(Collectors.toList());
    }
    public static List<SongStatsDTO> analyzeByGenre(List<Track> tracks) {
        return tracks.stream()
            .filter(t -> t.getGenreName() != null)
            .collect(Collectors.groupingBy(Track::getGenreName))
            .entrySet().stream()
            .map(entry -> {
                var list = entry.getValue();
                // var keyword - type inference
                var avgPop   = list.stream().mapToInt(Track::getPopularity).average().orElse(0);
                var avgEnergy = list.stream().mapToDouble(Track::getEnergy).average().orElse(0);
                var avgDance  = list.stream().mapToDouble(Track::getDanceability).average().orElse(0);
                return new SongStatsDTO(
                    entry.getKey(), list.size(), avgPop, avgEnergy, avgDance
                );
            })
            .sorted(Comparator.comparingDouble(SongStatsDTO::avgPopularity).reversed())
            .collect(Collectors.toList());
    }
    public static List<Track> getTopTracks(List<Track> tracks, int n) {
        return tracks.stream()
            .sorted(Comparator.comparingInt(Track::getPopularity).reversed())
            .limit(n)
            .collect(Collectors.toList());
    }
    public static List<Track> filterByPopularity(List<Track> tracks, int min, int max) {
        return tracks.stream()
            .filter(t -> t.getPopularity() >= min && t.getPopularity() <= max)
            .collect(Collectors.toList());
    }
    public static Optional<Track> findMostPopular(List<Track> tracks) {
        return tracks.stream()
            .max(Comparator.comparingInt(Track::getPopularity));
    }
    public static String classifyPopularity(int popularity) {
        return switch (popularity / 20) {
            case 5    -> "🔥 Viral";
            case 4    -> "⭐ Very Popular";
            case 3    -> "👍 Popular";
            case 2    -> "😐 Average";
            case 1    -> "📉 Below Average";
            default   -> "❄️ Unknown";
        };
    }
    public static Map<Boolean, Long> countByExplicit(List<Track> tracks) {
        return tracks.stream()
            .collect(Collectors.partitioningBy(
                Track::isExplicit,
                Collectors.counting()
            ));
    }
}