package emailtracker.model;

import java.time.LocalDateTime;

public class Email {
    public String id;
    public String senderName;
    public String subject;
    public String body;
    public LocalDateTime receivedAt;
    public boolean isSeen;
    public boolean hasCommitment;

    public Email(String id, String senderName, String subject, String body) {
        this.id = id;
        this.senderName = senderName;
        this.subject = subject;
        this.body = body;
        this.receivedAt = LocalDateTime.now();
        this.isSeen = false;
        this.hasCommitment = false;
    }

    public String toString() {
        return "[" + id + "] From: " + senderName + " | Subject: " + subject + " | Seen: " + (isSeen ? "Yes" : "No");
    }
}