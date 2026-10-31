package io.github.creezzi.durationtext;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Human-friendly durations: units ms, s, m, h, d, w (e.g. "2w 1d", "90m", "1.5h"). */
public final class DurationText {
    private static final Pattern PART = Pattern.compile("\\s*(\\d+(?:\\.\\d+)?)\\s*(ms|[smhdw])\\s*");

    private DurationText() {}

    public static Duration parse(String text) {
        String s = text.trim().toLowerCase();
        if (s.isEmpty()) {
            throw new IllegalArgumentException("empty duration");
        }
        double nanos = 0;
        Matcher m = PART.matcher(s);
        int pos = 0;
        while (pos < s.length()) {
            m.region(pos, s.length());
            if (!m.lookingAt()) {
                throw new IllegalArgumentException("cannot parse duration: " + text);
            }
            nanos += Double.parseDouble(m.group(1)) * unitNanos(m.group(2));
            pos = m.end();
        }
        return Duration.ofNanos(Math.round(nanos));
    }

    private static double unitNanos(String unit) {
        return switch (unit) {
            case "ms" -> 1_000_000d;
            case "s" -> 1e9;
            case "m" -> 60e9;
            case "h" -> 3600e9;
            case "d" -> 86400e9;
            case "w" -> 604800e9;
            default -> throw new IllegalArgumentException("unit " + unit);
        };
    }

    public static String format(Duration d) {
        if (d.isNegative()) {
            return "-" + format(d.negated());
        }
        long total = Math.round(d.toMillis() / 1000.0);
        if (total == 0) {
            return "0s";
        }
        StringBuilder sb = new StringBuilder();
        long[] sizes = {86400, 3600, 60, 1};
        String[] names = {"d", "h", "m", "s"};
        for (int i = 0; i < sizes.length; i++) {
            long q = total / sizes[i];
            total %= sizes[i];
            if (q > 0) {
                if (sb.length() > 0) {
                    sb.append(' ');
                }
                sb.append(q).append(names[i]);
            }
        }
        return sb.toString();
    }
}
