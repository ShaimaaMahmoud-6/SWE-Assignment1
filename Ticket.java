package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Ticket {
    private final int id;
    private String customer;
    private String branch;
    private String equipment;
    private String issue;
    private String remarks;

    private TicketStatus status;
    private TicketPriority priority;

    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Ticket(int id,
                  String customer,
                  String branch,
                  String equipment,
                  String issue,
                  String remarks,
                  TicketPriority priority,
                  TicketStatus status,
                  LocalDateTime createdAt,
                  LocalDateTime updatedAt) {
        this.id = id;
        this.customer = customer;
        this.branch = branch;
        this.equipment = equipment;
        this.issue = issue;
        this.remarks = remarks;
        this.priority = priority;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Ticket(int id, String customer, String branch, String equipment, String issue, String remarks, TicketPriority priority) {
        this(id, customer, branch, equipment, issue, remarks, priority, TicketStatus.NEW, LocalDateTime.now(), LocalDateTime.now());
    }

    public int getId() { return id; }
    public String getCustomer() { return customer; }
    public String getBranch() { return branch; }
    public String getEquipment() { return equipment; }
    public String getIssue() { return issue; }
    public String getRemarks() { return remarks; }
    public TicketStatus getStatus() { return status; }
    public TicketPriority getPriority() { return priority; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setCustomer(String customer) { this.customer = customer; touch(); }
    public void setBranch(String branch) { this.branch = branch; touch(); }
    public void setEquipment(String equipment) { this.equipment = equipment; touch(); }
    public void setIssue(String issue) { this.issue = issue; touch(); }
    public void setRemarks(String remarks) { this.remarks = remarks; touch(); }
    public void setPriority(TicketPriority priority) { this.priority = priority; touch(); }
    public void setStatus(TicketStatus status) { this.status = status; touch(); }

    private void touch() { this.updatedAt = LocalDateTime.now(); }

    public String prettyLine() {
        return String.format("#%d | %s | %s | %s | %s | %s | %s",
                id, customer, branch, equipment, priority, status, createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }

    @Override
    public String toString() {
        return "Ticket #" + id +
                "\nCustomer: " + customer +
                "\nBranch: " + branch +
                "\nEquipment: " + equipment +
                "\nIssue: " + issue +
                "\nRemarks: " + remarks +
                "\nPriority: " + priority +
                "\nStatus: " + status +
                "\nCreated: " + createdAt +
                "\nUpdated: " + updatedAt;
    }
}