package app;

import model.Ticket;
import model.TicketPriority;
import model.TicketStatus;
import service.TicketService;
import util.CsvStore;
import util.Input;

import java.io.IOException;
import java.util.List;

public class Main {

    private static final String DATA_FILE = "tickets.csv";

    public static void main(String[] args) {
        var service = new TicketService();
        var in = new Input();
        var store = new CsvStore();

        // Load existing tickets
        try {
            List<Ticket> loaded = store.load(DATA_FILE);
            service.setInitial(loaded);
            if (!loaded.isEmpty())
                System.out.println("Loaded " + loaded.size() + " tickets from " + DATA_FILE);
        } catch (IOException e) {
            System.out.println("Load failed: " + e.getMessage());
        }

        while (true) {
            System.out.println("\n==== Ticket App ====");
            System.out.println("1) Create  2) List  3) View  4) Search");
            System.out.println("5) Filters 6) Edit  7) Delete 8) Save  0) Exit");
            int choice = in.readInt("Choose: ");

            try {
                switch (choice) {
                    case 1 -> createTicket(service, in);
                    case 2 -> listTickets(service);
                    case 3 -> viewTicket(service, in);
                    case 4 -> searchTickets(service, in);
                    case 5 -> filtersMenu(service, in);
                    case 6 -> editTicket(service, in);   // includes status update now
                    case 7 -> deleteTicket(service, in);
                    case 8 -> save(service, store);
                    case 0 -> { save(service, store); return; }
                    default -> System.out.println("Invalid option.");
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    // =========================
    // Menus
    // =========================
    private static void filtersMenu(TicketService service, Input in) {
        System.out.println("\n-- Filters --");
        System.out.println("1) By Status");
        System.out.println("2) By Priority");
        System.out.println("0) Back");
        int ch = in.readInt("Choose: ");

        switch (ch) {
            case 1 -> filterStatus(service, in);
            case 2 -> filterPriority(service, in);
            case 0 -> { return; }
            default -> System.out.println("Invalid option.");
        }
    }

    // =========================
    // Create / Read / Update / Delete
    // =========================
    private static void createTicket(TicketService service, Input in) {
        System.out.println("\n-- Create Ticket --");
        String customer = in.readNonEmpty("Customer: ");
        String branch = in.readNonEmpty("Branch: ");
        String equipment = in.readNonEmpty("Equipment: ");
        String issue = in.readNonEmpty("Issue: ");
        String remarks = in.readOptional("Remarks (optional): ");

        TicketPriority pr = readPriority(in);

        // NEW: Choose status during create
        TicketStatus st = readStatus(in);

        Ticket t = service.create(customer, branch, equipment, issue, remarks, pr, st);
        System.out.println("Created:\n" + t.prettyLine());
    }

    private static void listTickets(TicketService service) {
        System.out.println("\n-- Tickets --");
        List<Ticket> all = service.allSorted();
        if (all.isEmpty()) {
            System.out.println("No tickets yet.");
            return;
        }
        all.forEach(t -> System.out.println(t.prettyLine()));
    }

    private static void viewTicket(TicketService service, Input in) {
        int id = in.readInt("Ticket ID: ");
        Ticket t = service.getById(id);
        if (t == null) System.out.println("Not found.");
        else System.out.println("\n" + t);
    }

    private static void searchTickets(TicketService service, Input in) {
        String k = in.readNonEmpty("Keyword: ");
        List<Ticket> res = service.search(k);
        System.out.println("\n-- Results (" + res.size() + ") --");
        if (res.isEmpty()) System.out.println("No matches.");
        else res.forEach(t -> System.out.println(t.prettyLine()));
    }

    private static void filterStatus(TicketService service, Input in) {
        TicketStatus st = readStatus(in);
        List<Ticket> res = service.filterStatus(st);
        System.out.println("\n-- " + st + " (" + res.size() + ") --");
        if (res.isEmpty()) System.out.println("No tickets with that status.");
        else res.forEach(t -> System.out.println(t.prettyLine()));
    }

    private static void filterPriority(TicketService service, Input in) {
        TicketPriority pr = readPriority(in);
        List<Ticket> res = service.filterPriority(pr);
        System.out.println("\n-- " + pr + " (" + res.size() + ") --");
        if (res.isEmpty()) System.out.println("No tickets with that priority.");
        else res.forEach(t -> System.out.println(t.prettyLine()));
    }

    private static void editTicket(TicketService service, Input in) {
        int id = in.readInt("Ticket ID: ");
        Ticket t = service.getById(id);
        if (t == null) {
            System.out.println("Not found.");
            return;
        }

        System.out.println("\n-- Edit Ticket (leave blank to keep) --");
        String customer = in.readOptional("Customer (" + t.getCustomer() + "): ");
        String branch = in.readOptional("Branch (" + t.getBranch() + "): ");
        String equipment = in.readOptional("Equipment (" + t.getEquipment() + "): ");
        String issue = in.readOptional("Issue (" + t.getIssue() + "): ");
        String remarks = in.readOptional("Remarks (" + t.getRemarks() + "): ");

        System.out.println("Priority options: LOW, MEDIUM, HIGH, URGENT (blank = keep)");
        String prRaw = in.readOptional("Priority (" + t.getPriority() + "): ");
        TicketPriority pr = null;
        if (!prRaw.isBlank()) pr = TicketPriority.valueOf(prRaw.trim().toUpperCase());

        // NEW: Status update moved into Edit
        System.out.println("Status options: NEW, IN_PROGRESS, ON_HOLD, RESOLVED, CLOSED (blank = keep)");
        String stRaw = in.readOptional("Status (" + t.getStatus() + "): ");
        TicketStatus st = null;
        if (!stRaw.isBlank()) st = TicketStatus.valueOf(stRaw.trim().toUpperCase());

        boolean ok = service.editTicket(
                id,
                customer.isBlank() ? null : customer,
                branch.isBlank() ? null : branch,
                equipment.isBlank() ? null : equipment,
                issue.isBlank() ? null : issue,
                remarks,    // allow empty remarks intentionally
                pr,
                st          // status change (enforced by service rules)
        );

        System.out.println(ok ? "Edited." : "Edit rejected (ticket not found or status transition not allowed).");
    }

    private static void deleteTicket(TicketService service, Input in) {
        int id = in.readInt("Ticket ID to delete: ");
        boolean ok = service.delete(id);
        System.out.println(ok ? "Deleted." : "Not found.");
    }

    private static void save(TicketService service, CsvStore store) {
        try {
            store.save(DATA_FILE, service.getAllRaw());
            System.out.println("Saved to " + DATA_FILE);
        } catch (IOException e) {
            System.out.println("Save failed: " + e.getMessage());
        }
    }

    // =========================
    // Helpers
    // =========================
    private static TicketPriority readPriority(Input in) {
        System.out.println("Priority: 1=LOW, 2=MEDIUM, 3=HIGH, 4=URGENT");
        int p = in.readIntRange("Choose priority: ", 1, 4);
        return switch (p) {
            case 1 -> TicketPriority.LOW;
            case 2 -> TicketPriority.MEDIUM;
            case 3 -> TicketPriority.HIGH;
            default -> TicketPriority.URGENT;
        };
    }

    private static TicketStatus readStatus(Input in) {
        System.out.println("Status: 1=NEW, 2=IN_PROGRESS, 3=ON_HOLD, 4=RESOLVED, 5=CLOSED");
        int s = in.readIntRange("Choose status: ", 1, 5);
        return switch (s) {
            case 1 -> TicketStatus.NEW;
            case 2 -> TicketStatus.IN_PROGRESS;
            case 3 -> TicketStatus.ON_HOLD;
            case 4 -> TicketStatus.RESOLVED;
            default -> TicketStatus.CLOSED;
        };
    }
}