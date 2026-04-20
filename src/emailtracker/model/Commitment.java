package emailtracker.model;

import java.time.LocalDate;

public class Commitment {
    public String id;
    public String emailId;
    public String senderName;
    public String description;
    public LocalDate deadline;
    public boolean isDone;

    public Commitment(String id, String emailId, String senderName, String description, LocalDate deadline) {
        this.id = id;
        this.emailId = emailId;
        this.senderName = senderName;
        this.description = description;
        this.deadline = deadline;
        this.isDone = false;
    }

    public String toString() {
        long daysLeft = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), deadline);
        String status = isDone ? "[DONE]" : "[PENDING]";
        String urgency = daysLeft < 0 ? " *** OVERDUE ***" : (daysLeft == 0 ? " *** DUE TODAY ***" : " (" + daysLeft + " days left)");
        return status + " " + description + " | From: " + senderName + " | Deadline: " + deadline + urgency;
    }
}