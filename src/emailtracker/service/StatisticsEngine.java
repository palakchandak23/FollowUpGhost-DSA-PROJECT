package emailtracker.service;

import emailtracker.model.Commitment;
import emailtracker.model.Email;
import java.util.ArrayList;
import java.util.HashMap;

public class StatisticsEngine {

    public void printDashboard(CommitmentManager cm, MissedEmailTracker met, ArrayList<Email> allEmails) {
        System.out.println("\n" + "=".repeat(55));
        System.out.println("           STATISTICS DASHBOARD");
        System.out.println("=".repeat(55));

        int total = cm.getTotalCount();
        int done = cm.getDoneCount();
        int pending = total - done;
        int overdue = cm.getOverdue().size();

        System.out.println(" Total Commitments  : " + total);
        System.out.println(" Completed          : " + done);
        System.out.println(" Pending            : " + pending);
        System.out.println(" Overdue            : " + overdue);
        System.out.println(" Missed Emails      : " + met.getMissedCount());
        System.out.println(" Total Emails       : " + allEmails.size());

        if (total > 0) {
            double rate = (done * 100.0) / total;
            System.out.printf(" Completion Rate    : %.1f%%%n", rate);
            System.out.print(" Progress           : [");
            int bars = (int)(rate / 5);
            for (int i = 0; i < 20; i++) System.out.print(i < bars ? "#" : "-");
            System.out.println("]");
        }

        // DSA: HashMap to count commitments per sender
        HashMap<String, Integer> senderCount = new HashMap<>();
        for (Commitment c : cm.getAll()) {
            senderCount.put(c.senderName, senderCount.getOrDefault(c.senderName, 0) + 1);
        }

        System.out.println("\n Commitments by Sender:");
        for (String sender : senderCount.keySet()) {
            System.out.println("   " + sender + " : " + senderCount.get(sender));
        }

        System.out.println("\n Pending Commitments:");
        ArrayList<Commitment> pendingList = cm.getPending();
        if (pendingList.isEmpty()) {
            System.out.println("   None!");
        } else {
            for (Commitment c : pendingList) {
                System.out.println("   " + c);
            }
        }

        System.out.println("=".repeat(55));
    }
}