package emailtracker.service;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.HashMap;

public class DeadlineParser {

    private static final HashMap<String, Integer> KEYWORDS = new HashMap<>();
    private static final HashMap<String, DayOfWeek> DAYS = new HashMap<>();

    static {
        KEYWORDS.put("today", 0);
        KEYWORDS.put("tonight", 0);
        KEYWORDS.put("tomorrow", 1);
        KEYWORDS.put("tmrw", 1);
        KEYWORDS.put("tmr", 1);
        KEYWORDS.put("this week", 5);
        KEYWORDS.put("next week", 7);
        KEYWORDS.put("asap", 0);
        KEYWORDS.put("urgent", 0);

        DAYS.put("monday", DayOfWeek.MONDAY);
        DAYS.put("tuesday", DayOfWeek.TUESDAY);
        DAYS.put("wednesday", DayOfWeek.WEDNESDAY);
        DAYS.put("thursday", DayOfWeek.THURSDAY);
        DAYS.put("friday", DayOfWeek.FRIDAY);
        DAYS.put("saturday", DayOfWeek.SATURDAY);
        DAYS.put("sunday", DayOfWeek.SUNDAY);
        DAYS.put("mon", DayOfWeek.MONDAY);
        DAYS.put("tue", DayOfWeek.TUESDAY);
        DAYS.put("wed", DayOfWeek.WEDNESDAY);
        DAYS.put("thu", DayOfWeek.THURSDAY);
        DAYS.put("fri", DayOfWeek.FRIDAY);
        DAYS.put("sat", DayOfWeek.SATURDAY);
        DAYS.put("sun", DayOfWeek.SUNDAY);
    }

    public LocalDate parse(String input) {
        if (input == null || input.isBlank()) return LocalDate.now().plusDays(1);
        String lower = input.trim().toLowerCase();

        for (String keyword : KEYWORDS.keySet()) {
            if (lower.contains(keyword)) {
                return LocalDate.now().plusDays(KEYWORDS.get(keyword));
            }
        }

        for (String day : DAYS.keySet()) {
            if (lower.contains(day)) {
                return nextDay(DAYS.get(day));
            }
        }

        try {
            if (lower.matches("\\d{2}/\\d{2}/\\d{4}")) {
                String[] p = lower.split("/");
                return LocalDate.of(Integer.parseInt(p[2]), Integer.parseInt(p[1]), Integer.parseInt(p[0]));
            }
            if (lower.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return LocalDate.parse(lower);
            }
        } catch (Exception ignored) {}

        return LocalDate.now().plusDays(1);
    }

    private LocalDate nextDay(DayOfWeek target) {
        LocalDate d = LocalDate.now().plusDays(1);
        while (d.getDayOfWeek() != target) d = d.plusDays(1);
        return d;
    }

    public String extractKeywords(String input) {
        if (input == null) return "";
        String lower = input.toLowerCase();
        StringBuilder found = new StringBuilder();
        for (String kw : KEYWORDS.keySet()) {
            if (lower.contains(kw)) found.append(kw).append(" ");
        }
        for (String kw : DAYS.keySet()) {
            if (lower.contains(kw)) found.append(kw).append(" ");
        }
        return found.toString().trim();
    }
}