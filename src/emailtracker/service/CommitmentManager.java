package emailtracker.service;

import emailtracker.model.Commitment;
import java.time.LocalDate;
import java.util.ArrayList;

public class CommitmentManager {

    // DSA: ArrayList to store all commitments
    private final ArrayList<Commitment> commitments = new ArrayList<>();
    private int idCounter = 1;

    public Commitment addCommitment(String emailId, String senderName, String description, LocalDate deadline) {
        String id = "C" + String.format("%03d", idCounter++);
        Commitment c = new Commitment(id, emailId, senderName, description, deadline);
        commitments.add(c);
        return c;
    }

    public boolean markDone(String commitmentId) {
        for (Commitment c : commitments) {
            if (c.id.equals(commitmentId)) {
                c.isDone = true;
                return true;
            }
        }
        return false;
    }

    // Sort by deadline using bubble sort (basic DSA)
    public ArrayList<Commitment> getAllSortedByDeadline() {
        ArrayList<Commitment> sorted = new ArrayList<>(commitments);
        int n = sorted.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (sorted.get(j).deadline.isAfter(sorted.get(j + 1).deadline)) {
                    Commitment temp = sorted.get(j);
                    sorted.set(j, sorted.get(j + 1));
                    sorted.set(j + 1, temp);
                }
            }
        }
        return sorted;
    }

    public ArrayList<Commitment> getPending() {
        ArrayList<Commitment> pending = new ArrayList<>();
        for (Commitment c : commitments) {
            if (!c.isDone) pending.add(c);
        }
        return pending;
    }

    public ArrayList<Commitment> getOverdue() {
        ArrayList<Commitment> overdue = new ArrayList<>();
        for (Commitment c : commitments) {
            if (!c.isDone && c.deadline.isBefore(LocalDate.now())) overdue.add(c);
        }
        return overdue;
    }

    public ArrayList<Commitment> getAll() { return commitments; }

    public int getTotalCount() { return commitments.size(); }

    public int getDoneCount() {
        int count = 0;
        for (Commitment c : commitments) if (c.isDone) count++;
        return count;
    }
}
