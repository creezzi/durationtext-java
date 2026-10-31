package io.github.creezzi.durationtext;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class DurationTextTest {
    @Test
    void parsesCommonForms() {
        assertEquals(Duration.ofMinutes(90), DurationText.parse("1h30m"));
        assertEquals(Duration.ofMinutes(90), DurationText.parse("1h 30m"));
        assertEquals(Duration.ofMinutes(90), DurationText.parse("90m"));
        assertEquals(Duration.ofHours(36), DurationText.parse("1.5d"));
        assertEquals(Duration.ofDays(15), DurationText.parse("2w 1d"));
        assertEquals(Duration.ofMillis(250), DurationText.parse("250ms"));
        assertEquals(Duration.ofSeconds(5), DurationText.parse("  5S "));
    }

    @Test
    void rejectsGarbage() {
        for (String bad : new String[] {"", "abc", "5x", "h", "1h 30", "1h-30m", "5"}) {
            assertThrows(IllegalArgumentException.class, () -> DurationText.parse(bad), bad);
        }
    }

    @Test
    void formats() {
        assertEquals("0s", DurationText.format(Duration.ZERO));
        assertEquals("59s", DurationText.format(Duration.ofSeconds(59)));
        assertEquals("1m", DurationText.format(Duration.ofSeconds(60)));
        assertEquals("1h 1m 1s", DurationText.format(Duration.ofSeconds(3661)));
        assertEquals("1d 2h 3m 4s", DurationText.format(Duration.ofSeconds(93784)));
        assertEquals("-1m 30s", DurationText.format(Duration.ofSeconds(-90)));
    }

    @Test
    void roundTrips() {
        for (long s : new long[] {1, 59, 3600, 90061}) {
            assertEquals(Duration.ofSeconds(s), DurationText.parse(DurationText.format(Duration.ofSeconds(s))));
        }
    }
}
