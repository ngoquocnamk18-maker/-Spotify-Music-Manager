package model;

public class Track {
    private String trackId;
    private String trackName;
    private int albumId;
    private int genreId;
    private int popularity;
    private int durationMs;
    private boolean explicit;
    private double danceability;
    private double energy;
    private double loudness;
    private double tempo;
    private double valence;
    private String albumName;
    private String genreName;
    private String artistNames;
    public Track() {}
    public Track(String trackId, String trackName, int albumId,
                 int genreId, int popularity, int durationMs, boolean explicit,
                 double danceability, double energy, double loudness,
                 double tempo, double valence) {
        this.trackId = trackId;
        this.trackName = trackName;
        this.albumId = albumId;
        this.genreId = genreId;
        this.popularity = popularity;
        this.durationMs = durationMs;
        this.explicit = explicit;
        this.danceability = danceability;
        this.energy = energy;
        this.loudness = loudness;
        this.tempo = tempo;
        this.valence = valence;
    }
    public String getTrackId() { return trackId; }
    public void setTrackId(String trackId) { this.trackId = trackId; }
    public String getTrackName() { return trackName; }
    public void setTrackName(String trackName) { this.trackName = trackName; }
    public int getAlbumId() { return albumId; }
    public void setAlbumId(int albumId) { this.albumId = albumId; }
    public int getGenreId() { return genreId; }
    public void setGenreId(int genreId) { this.genreId = genreId; }
    public int getPopularity() { return popularity; }
    public void setPopularity(int popularity) { this.popularity = popularity; }
    public int getDurationMs() { return durationMs; }
    public void setDurationMs(int durationMs) { this.durationMs = durationMs; }
    public boolean isExplicit() { return explicit; }
    public void setExplicit(boolean explicit) { this.explicit = explicit; }
    public double getDanceability() { return danceability; }
    public void setDanceability(double danceability) { this.danceability = danceability; }
    public double getEnergy() { return energy; }
    public void setEnergy(double energy) { this.energy = energy; }
    public double getLoudness() { return loudness; }
    public void setLoudness(double loudness) { this.loudness = loudness; }
    public double getTempo() { return tempo; }
    public void setTempo(double tempo) { this.tempo = tempo; }
    public double getValence() { return valence; }
    public void setValence(double valence) { this.valence = valence; }
    public String getAlbumName() { return albumName; }
    public void setAlbumName(String albumName) { this.albumName = albumName; }
    public String getGenreName() { return genreName; }
    public void setGenreName(String genreName) { this.genreName = genreName; }
    public String getArtistNames() { return artistNames; }
    public void setArtistNames(String artistNames) { this.artistNames = artistNames; }
    public String getDurationFormatted() {
        int totalSec = durationMs / 1000;
        return String.format("%d:%02d", totalSec / 60, totalSec % 60);
    }
    @Override
    public String toString() { return trackName; }
}
