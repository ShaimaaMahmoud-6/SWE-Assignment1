package service;

import model.Ticket;
import model.TicketPriority;
import model.TicketStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TicketService {
    private final List<Ticket> tickets = new ArrayList<>();
    private int nextId = 1;

    public void setInitial(List<Ticket> initial) {
        tickets.clear();
        tickets.addAll(initial);
        nextId = tickets.stream().map(Ticket::getId).max(Integer::compareTo).orElse(0) + 1;
    }

    // UPDATED: create accepts initial status
    public Ticket create(String customer, String branch, String equipment,
                         String issue, String remarks,
                         TicketPriority priority, TicketStatus status) {

        validate(customer, "customer");
        validate(branch, "branch");
        validate(equipment, "equipment");
        validate(issue, "issue");

        Ticket t = new Ticket(
                nextId++,
                customer.trim(),
                branch.trim(),
                equipment.trim(),
                issue.trim(),
                remarks == null ? "" : remarks.trim(),
                priority
        );

        // Allow selecting initial status on creation (no transition rules here)
        if (status != null) t.setStatus(status);

        tickets.add(t);
        return t;
    }

    public List<Ticket> allSorted() {
        List<Ticket> copy = new ArrayList<>(tickets);
        copy.sort(Comparator
                .comparing(Ticket::getPriority).reversed()
                .thenComparing(Ticket::getCreatedAt).reversed());
        return copy;
    }

    public Ticket getById(int id) {
        for (Ticket t : tickets) if (t.getId() == id) return t;
        return null;
    }

    public boolean delete(int id) {
        Ticket t = getById(id);
        if (t == null) return false;
        return tickets.remove(t);
    }

    public List<Ticket> getAllRaw() {
        return new ArrayList<>(tickets);
    }

    public List<Ticket> search(String keyword) {
        String k = keyword == null ? "" : keyword.trim().toLowerCase();
        List<Ticket> res = new ArrayList<>();
        for (Ticket t : tickets) {
            if (contains(t.getCustomer(), k) ||
                    contains(t.getBranch(), k) ||
                    contains(t.getEquipment(), k) ||
                    contains(t.getIssue(), k) ||
                    contains(t.getRemarks(), k)) {
                res.add(t);
            }
        }
        return res;
    }

    public List<Ticket> filterStatus(TicketStatus st) {
        List<Ticket> res = new ArrayList<>();
        for (Ticket t : tickets) if (t.getStatus() == st) res.add(t);
        return res;
    }

    public List<Ticket> filterPriority(TicketPriority pr) {
        List<Ticket> res = new ArrayList<>();
        for (Ticket t : tickets) if (t.getPriority() == pr) res.add(t);
        return res;
    }

    // Keep your transition rules here
    public boolean updateStatus(int id, TicketStatus newStatus) {
        Ticket t = getById(id);
        if (t == null) return false;

        TicketStatus cur = t.getStatus();
        if (!isAllowed(cur, newStatus)) return false;

        t.setStatus(newStatus);
        return true;
    }

    // UPDATED: edit now includes status update (with transition rules)
    public boolean editTicket(int id, String customer, String branch, String equipment,
                              String issue, String remarks,
                              TicketPriority priority, TicketStatus status) {

        Ticket t = getById(id);
        if (t == null) return false;

        if (customer != null && !customer.trim().isEmpty()) t.setCustomer(customer.trim());
        if (branch != null && !branch.trim().isEmpty()) t.setBranch(branch.trim());
        if (equipment != null && !equipment.trim().isEmpty()) t.setEquipment(equipment.trim());
        if (issue != null && !issue.trim().isEmpty()) t.setIssue(issue.trim());
        if (remarks != null) t.setRemarks(remarks.trim());
        if (priority != null) t.setPriority(priority);

        // Enforce transitions when changing status via edit
        if (status != null && status != t.getStatus()) {
            if (!updateStatus(id, status)) return false;
        }

        return true;
    }

    private void validate(String s, String field) {
        if (s == null || s.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " cannot be empty");
        }
        if (s.trim().length() < 2) {
            throw new IllegalArgumentException(field + " too short");
        }
    }

    private boolean contains(String s, String k) {
        return s != null && s.toLowerCase().contains(k);
    }

    private boolean isAllowed(TicketStatus from, TicketStatus to) {
        if (from == to) return true;

        return switch (from) {
            case NEW -> (to == TicketStatus.IN_PROGRESS || to == TicketStatus.ON_HOLD);
            case IN_PROGRESS -> (to == TicketStatus.ON_HOLD || to == TicketStatus.RESOLVED);
            case ON_HOLD -> (to == TicketStatus.IN_PROGRESS || to == TicketStatus.RESOLVED);
            case RESOLVED -> (to == TicketStatus.CLOSED || to == TicketStatus.IN_PROGRESS); // reopen
            case CLOSED -> false;
        };
    }
}