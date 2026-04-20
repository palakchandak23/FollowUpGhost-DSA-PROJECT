package emailtracker.service;

import emailtracker.model.Email;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MissedEmailTracker {

    // DSA: Queue for FIFO processing of missed emails
    private final Queue<Email> missedQueue = new LinkedList<>();

    private static final String[] KEYWORDS = {
        "urgent", "asap", "deadline", "due", "meeting",
        "invoice", "payment", "submit", "review", "approve",
        "confirm", "respond", "reply", "action required",
        "important", "reminder"
    };

    public void addMissedEmail(Email email) {
        missedQueue.offer(email);
    }

    public ArrayList<String> getSummaries() {
        ArrayList<String> summaries = new ArrayList<>();
        // Process queue without destroying it
        ArrayList<Email> temp = new ArrayList<>(missedQueue);
        for (Email email : temp) {
            summaries.add(buildSummary(email));
        }
        return summaries;
    }

    private String buildSummary(Email email) {
        String combined = (email.subject + " " + email.body).toLowerCase();
        ArrayList<String> found = new ArrayList<>();
        for (String kw : KEYWORDS) {
            if (combined.contains(kw)) found.add(kw);
        }
        String tag = found.isEmpty() ? "[INFO]" : "[ACTION NEEDED]";
        String keywords = found.isEmpty() ? "none" : String.join(", ", found);
        return tag + " From: " + email.senderName + " | Subject: " + email.subject + " | Keywords: " + keywords;
    }

    public int getMissedCount() { return missedQueue.size(); }
}