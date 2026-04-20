package emailtracker.service;

import emailtracker.model.Email;
import emailtracker.model.Commitment;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class EmailService {

    // DSA: HashMap for O(1) email lookup by ID
    private final HashMap<String, Email> emailStore = new HashMap<>();

    // DSA: ArrayList for ordered inbox
    private final ArrayList<Email> inbox = new ArrayList<>();

    private final CommitmentManager commitmentManager = new CommitmentManager();
    private final MissedEmailTracker missedTracker = new MissedEmailTracker();
    private final DeadlineParser deadlineParser = new DeadlineParser();
    private final StatisticsEngine statsEngine = new StatisticsEngine();

    private int emailCounter = 1;

    public Email addEmail(String senderName, String subject, String body) {
        String id = "E" + String.format("%03d", emailCounter++);
        Email email = new Email(id, senderName, subject, body);
        emailStore.put(id, email);
        inbox.add(email);
        return email;
    }

    public Email getEmail(String id) {
        return emailStore.get(id);
    }

    public void markEmailSeen(String id) {
        Email e = emailStore.get(id);
        if (e != null) e.isSeen = true;
    }

    public void markEmailMissed(String id) {
        Email e = emailStore.get(id);
        if (e != null && !e.isSeen) missedTracker.addMissedEmail(e);
    }

    public Commitment addNaturalLanguageCommitment(String emailId, String senderName, String text) {
        String keywords = deadlineParser.extractKeywords(text);
        LocalDate deadline = deadlineParser.parse(text);
        System.out.println(" Keywords found: [" + keywords + "] -> Deadline: " + deadline);
        Commitment c = commitmentManager.addCommitment(emailId, senderName, text, deadline);
        Email e = emailStore.get(emailId);
        if (e != null) e.hasCommitment = true;
        return c;
    }

    public Commitment addKeywordCommitment(String emailId, String senderName, String description, String dateKeyword) {
        LocalDate deadline = deadlineParser.parse(dateKeyword);
        System.out.println(" Deadline set to: " + deadline);
        Commitment c = commitmentManager.addCommitment(emailId, senderName, description, deadline);
        Email e = emailStore.get(emailId);
        if (e != null) e.hasCommitment = true;
        return c;
    }

    public boolean markCommitmentDone(String commitmentId) {
        return commitmentManager.markDone(commitmentId);
    }

    public void printAllCommitments() {
        ArrayList<Commitment> all = commitmentManager.getAllSortedByDeadline();
        System.out.println("\n" + "-".repeat(70));
        System.out.println(" ALL COMMITMENTS (sorted by deadline)");
        System.out.println("-".repeat(70));
        if (all.isEmpty()) System.out.println(" No commitments yet.");
        else for (Commitment c : all) System.out.println(" " + c);
        System.out.println("-".repeat(70));
    }

    public void printPendingCommitments() {
        ArrayList<Commitment> pending = commitmentManager.getPending();
        System.out.println("\n" + "-".repeat(70));
        System.out.println(" PENDING COMMITMENTS");
        System.out.println("-".repeat(70));
        if (pending.isEmpty()) System.out.println(" No pending commitments!");
        else for (Commitment c : pending) System.out.println(" " + c);
        System.out.println("-".repeat(70));
    }

    public void printMissedEmailSummary() {
        ArrayList<String> summaries = missedTracker.getSummaries();
        System.out.println("\n" + "-".repeat(70));
        System.out.println(" MISSED EMAIL SUMMARY (" + summaries.size() + " emails)");
        System.out.println("-".repeat(70));
        if (summaries.isEmpty()) System.out.println(" No missed emails.");
        else for (String s : summaries) System.out.println(" " + s);
        System.out.println("-".repeat(70));
    }

    public void printInbox() {
        System.out.println("\n" + "-".repeat(70));
        System.out.println(" INBOX (" + inbox.size() + " emails)");
        System.out.println("-".repeat(70));
        if (inbox.isEmpty()) System.out.println(" Inbox is empty.");
        else for (Email e : inbox) System.out.println(" " + e);
        System.out.println("-".repeat(70));
    }

    public void printStatsDashboard() {
        statsEngine.printDashboard(commitmentManager, missedTracker, inbox);
    }

    public ArrayList<Email> getInbox() { return inbox; }
    public CommitmentManager getCommitmentManager() { return commitmentManager; }
}