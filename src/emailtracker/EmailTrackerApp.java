package emailtracker;

import emailtracker.model.Commitment;
import emailtracker.model.Email;
import emailtracker.service.EmailService;
import java.util.Scanner;

public class EmailTrackerApp {

    private static final EmailService service = new EmailService();

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        printBanner();
        seedDemoData();

        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": simulateIncomingEmail(); break;
                case "2": openEmailFlow(); break;
                case "3": addCommitmentFlow(); break;
                case "4": viewCommitmentsMenu(); break;
                case "5": markDoneFlow(); break;
                case "6": service.printMissedEmailSummary(); break;
                case "7": service.printStatsDashboard(); break;
                case "0": running = false; break;
                default: System.out.println(" Invalid option. Try again.");
            }
        }
        System.out.println("\n Goodbye!\n");
    }

    private static void simulateIncomingEmail() {
        System.out.println("\n--- Add Incoming Email ---");
        System.out.print(" Sender name : "); String sender = sc.nextLine().trim();
        System.out.print(" Subject     : "); String subject = sc.nextLine().trim();
        System.out.print(" Body        : "); String body = sc.nextLine().trim();
        Email e = service.addEmail(sender, subject, body);
        System.out.println(" Email added: " + e);
        System.out.print("\n Mark as missed (not seen)? (y/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("y")) {
            service.markEmailMissed(e.id);
            System.out.println(" Marked as missed.");
        }
    }

    private static void openEmailFlow() {
        service.printInbox();
        System.out.print(" Enter Email ID to open (e.g. E001): ");
        String id = sc.nextLine().trim().toUpperCase();
        Email email = service.getEmail(id);
        if (email == null) { System.out.println(" Email not found."); return; }
        service.markEmailSeen(id);
        System.out.println("\n--- Email Opened ---");
        System.out.println(" From    : " + email.senderName);
        System.out.println(" Subject : " + email.subject);
        System.out.println(" Body    : " + email.body);
        System.out.print("\n Add a commitment for this email? (y/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("y")) {
            addCommitmentForEmail(id, email.senderName);
        }
    }

    private static void addCommitmentFlow() {
        service.printInbox();
        System.out.print(" Enter Email ID: ");
        String id = sc.nextLine().trim().toUpperCase();
        Email email = service.getEmail(id);
        if (email == null) { System.out.println(" Email not found."); return; }
        addCommitmentForEmail(id, email.senderName);
    }

    private static void addCommitmentForEmail(String emailId, String senderName) {
        System.out.println("\n--- Add Commitment ---");
        System.out.println(" Sender: " + senderName);
        System.out.println(" 1. Natural language (for ex. I will send report by tomorrow)");
        System.out.println(" 2. Keywords (enter description + deadline separately)");
        System.out.print(" Choose (1/2): ");
        String type = sc.nextLine().trim();

        if (type.equals("1")) {
            System.out.print(" Type your commitment: ");
            String text = sc.nextLine().trim();
            Commitment c = service.addNaturalLanguageCommitment(emailId, senderName, text);
            System.out.println(" Added: " + c);
        } else {
            System.out.print(" Description: ");
            String desc = sc.nextLine().trim();
            System.out.print(" Deadline (e.g. tomorrow / friday / 25/12/2025): ");
            String keyword = sc.nextLine().trim();
            Commitment c = service.addKeywordCommitment(emailId, senderName, desc, keyword);
            System.out.println(" Added: " + c);
        }
    }

    private static void viewCommitmentsMenu() {
        System.out.println("\n 1. All commitments");
        System.out.println(" 2. Pending only");
        System.out.println(" 3. Overdue only");
        System.out.print(" Choose: ");
        String choice = sc.nextLine().trim();
        switch (choice) {
            case "1": service.printAllCommitments(); break;
            case "2": service.printPendingCommitments(); break;
            case "3":
                var overdue = service.getCommitmentManager().getOverdue();
                System.out.println("\n--- OVERDUE ---");
                if (overdue.isEmpty()) System.out.println(" None overdue.");
                else for (var c : overdue) System.out.println(" " + c);
                break;
            default: System.out.println(" Invalid.");
        }
    }

    private static void markDoneFlow() {
        service.printPendingCommitments();
        System.out.print(" Enter Commitment ID (e.g. C001): ");
        String id = sc.nextLine().trim().toUpperCase();
        boolean ok = service.markCommitmentDone(id);
        System.out.println(ok ? " Marked as done!" : " Not found.");
    }

    private static void printMainMenu() {
        System.out.println("\n+--------------------------------------+");
        System.out.println("|     EMAIL COMMITMENT TRACKER          |");
        System.out.println("+--------------------------------------+");
        System.out.println("|  1. Add incoming email               |");
        System.out.println("|  2. Open an email                     |");
        System.out.println("|  3. Add commitment to email           |");
        System.out.println("|  4. View commitments                  |");
        System.out.println("|  5. Mark commitment as done          |");
        System.out.println("|  6. Missed email summary              |");
        System.out.println("|  7. Statistics dashboard              |");
        System.out.println("|  0. Exit                              |");
        System.out.println("+-------------------------------------+");
        System.out.print("  Choice: ");
    }

    private static void printBanner() {
        System.out.println("\n================================================");
        System.out.println("  FollowUp Ghost- Smart Email Notification Tracker ");
        System.out.println("================================================");
    }

    private static void seedDemoData() {
        System.out.println("\n Loading demo data...");
        Email e1 = service.addEmail("Rahul Sharma", "Project Report Due", "Please send the report by tomorrow.");
        Email e2 = service.addEmail("Priya Mehta", "Meeting Reminder", "Team meet this Friday, confirm attendance.");
        Email e3 = service.addEmail("Khushi Chitlange", "Invoice Pending", "Invoice overdue. Please process asap.");
        Email e4 = service.addEmail("Palak Chandak", "Code Review", "Review my pull request by end of week.");

        service.markEmailMissed(e3.id);
        service.markEmailMissed(e4.id);

        service.markEmailSeen(e1.id);
        service.addNaturalLanguageCommitment(e1.id, "Rahul Sharma", "I will send the report by tomorrow");

        service.markEmailSeen(e2.id);
        service.addKeywordCommitment(e2.id, "Priya Mehta", "Confirm attendance for team meet", "friday");

        System.out.println("4 emails, 2 commitments, 2 missed.\n");
    }
}