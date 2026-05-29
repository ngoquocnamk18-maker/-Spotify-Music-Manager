package dto;

public record SongStatsDTO(
 String genreName,
 long trackCount,
 double avgPopularity,
 double avgEnergy,
 double avgDanceability
) {
 public String formatted() {
     return String.format(
         "Genre: %-15s | Tracks: %3d | Avg Pop: %.1f | Energy: %.2f | Dance: %.2f",
         genreName, trackCount, avgPopularity, avgEnergy, avgDanceability
     );
 }
}