package test;


import dto.TrackDTO;
import dto.SongStatsDTO;
import model.Track;
import service.MusicAnalyzer;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MusicAnalyzer Tests")
public class TestMusicAnalyzer {

    private List<Track> sampleTracks;

    @BeforeEach
    void setUp() {
        // Tạo dữ liệu mẫu cho test
        sampleTracks = new ArrayList<>();

        Track t1 = new Track();
        t1.setTrackId("id001");
        t1.setTrackName("Shape of You");
        t1.setArtistNames("Ed Sheeran");
        t1.setGenreName("pop");
        t1.setPopularity(95);
        t1.setDanceability(0.8);
        t1.setEnergy(0.7);
        t1.setTempo(96.0);
        t1.setDurationMs(234000);
        t1.setExplicit(false);

        Track t2 = new Track();
        t2.setTrackId("id002");
        t2.setTrackName("Blinding Lights");
        t2.setArtistNames("The Weeknd");
        t2.setGenreName("pop");
        t2.setPopularity(90);
        t2.setDanceability(0.5);
        t2.setEnergy(0.8);
        t2.setTempo(171.0);
        t2.setDurationMs(200000);
        t2.setExplicit(false);

        Track t3 = new Track();
        t3.setTrackId("id003");
        t3.setTrackName("rockstar");
        t3.setArtistNames("Post Malone");
        t3.setGenreName("hiphop");
        t3.setPopularity(85);
        t3.setDanceability(0.6);
        t3.setEnergy(0.5);
        t3.setTempo(160.0);
        t3.setDurationMs(218000);
        t3.setExplicit(true);

        Track t4 = new Track();
        t4.setTrackId("id004");
        t4.setTrackName("Someone Like You");
        t4.setArtistNames("Adele");
        t4.setGenreName("acoustic");
        t4.setPopularity(88);
        t4.setDanceability(0.3);
        t4.setEnergy(0.3);
        t4.setTempo(67.0);
        t4.setDurationMs(285000);
        t4.setExplicit(false);

        Track t5 = new Track();
        t5.setTrackId("id005");
        t5.setTrackName("   "); // blank name
        t5.setArtistNames("Unknown");
        t5.setGenreName("pop");
        t5.setPopularity(10);
        t5.setDurationMs(180000);
        sampleTracks.addAll(List.of(t1, t2, t3, t4, t5));
    }
    // Test 1: getTopTracks trả về đúng số lượng
    @Test
    @DisplayName("Test getTopTracks returns correct count")
    void testGetTopTracksCount() {
        var top3 = MusicAnalyzer.getTopTracks(sampleTracks, 3);
        assertEquals(3, top3.size(), "Should return exactly 3 tracks");
    }
    // Test 2: getTopTracks sắp xếp đúng thứ tự
    @Test
    @DisplayName("Test getTopTracks returns highest popularity first")
    void testGetTopTracksOrder() {
        var top2 = MusicAnalyzer.getTopTracks(sampleTracks, 2);
        assertEquals("Shape of You", top2.get(0).getTrackName());
        assertEquals("Blinding Lights", top2.get(1).getTrackName());
    }
    // Test 3: filterByPopularity lọc đúng
    @Test
    @DisplayName("Test filterByPopularity filters correctly")
    void testFilterByPopularity() {
        var filtered = MusicAnalyzer.filterByPopularity(sampleTracks, 85, 95);
        assertEquals(4, filtered.size());
        assertTrue(filtered.stream()
            .allMatch(t -> t.getPopularity() >= 85 && t.getPopularity() <= 95));
    }
    // Test 4: findMostPopular trả về đúng
    @Test
    @DisplayName("Test findMostPopular returns correct track")
    void testFindMostPopular() {
        var most = MusicAnalyzer.findMostPopular(sampleTracks);
        assertTrue(most.isPresent());
        assertEquals("Shape of You", most.get().getTrackName());
        assertEquals(95, most.get().getPopularity());
    }
    // Test 5: classifyPopularity trả về đúng label
    @Test
    @DisplayName("Test classifyPopularity returns correct labels")
    void testClassifyPopularity() {
        assertEquals("🔥 Viral",        MusicAnalyzer.classifyPopularity(100));
        assertEquals("⭐ Very Popular",  MusicAnalyzer.classifyPopularity(80));
        assertEquals("👍 Popular",       MusicAnalyzer.classifyPopularity(60));
        assertEquals("😐 Average",       MusicAnalyzer.classifyPopularity(40));
        assertEquals("📉 Below Average", MusicAnalyzer.classifyPopularity(20));
    }
    // Test 6: countByExplicit đếm đúng
    @Test
    @DisplayName("Test countByExplicit counts correctly")
    void testCountByExplicit() {
        var counts = MusicAnalyzer.countByExplicit(sampleTracks);
        assertEquals(1L, counts.get(true),  "Should have 1 explicit track");
        assertEquals(4L, counts.get(false), "Should have 4 non-explicit tracks");
    }
    // Test 7: TrackDTO validation - tên trống throw exception
    @Test
    @DisplayName("Test TrackDTO throws on blank name")
    void testTrackDTOValidation() {
        assertThrows(IllegalArgumentException.class, () ->
            new TrackDTO("id", "  ", "artist", "genre", 50, "3:00")
        );
    }
    // Test 8: TrackDTO summary format đúng
    @Test
    @DisplayName("Test TrackDTO summary format")
    void testTrackDTOSummary() {
        var dto = new TrackDTO("id001", "Shape of You",
                               "Ed Sheeran", "pop", 95, "3:54");
        String summary = dto.summary();
        assertTrue(summary.contains("Shape of You"));
        assertTrue(summary.contains("Ed Sheeran"));
        assertTrue(summary.contains("95"));
    }
}