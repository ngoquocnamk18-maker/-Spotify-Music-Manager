package dto;

public record TrackDTO(
 String trackId,
 String trackName,
 String artistNames,
 String genreName,
 int popularity,
 String duration
) {
 public TrackDTO {
     if (trackName == null || trackName.isBlank())
         throw new IllegalArgumentException("Track name cannot be blank");
     if (popularity < 0 || popularity > 100)
         throw new IllegalArgumentException("Popularity must be 0-100");
 }

 public String summary() {
     return trackName + " by " + artistNames + " [" + genreName + "] ⭐" + popularity;
 }
}