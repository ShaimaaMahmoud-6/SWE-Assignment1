package util;

import model.Ticket;
import model.TicketPriority;
import model.TicketStatus;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple CSV persistence (no external libs).
 * Escapes commas/quotes/newlines safely.
 */
public class CsvStore {

    public void save(String path, List<Ticket> tickets) throws IOException {
        try (var out = new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8);
             var w = new BufferedWriter(out)) {

            // Header
            w.write("id,customer,branch,equipment,issue,remarks,priority,status,createdAt,updatedAt");
            w.newLine();

            for (Ticket t : tickets) {
                w.write(t.getId() + ","
                        + esc(t.getCustomer()) + ","
                        + esc(t.getBranch()) + ","
                        + esc(t.getEquipment()) + ","
                        + esc(t.getIssue()) + ","
                        + esc(t.getRemarks()) + ","
                        + t.getPriority() + ","
                        + t.getStatus() + ","
                        + t.getCreatedAt() + ","
                        + t.getUpdatedAt());
                w.newLine();
            }
        }
    }

    public List<Ticket> load(String path) throws IOException {
        File f = new File(path);
        if (!f.exists()) return new ArrayList<>();

        List<Ticket> list = new ArrayList<>();
        try (var in = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8);
             var r = new BufferedReader(in)) {

            String line = r.readLine(); // header
            if (line == null) return list;

            while ((line = r.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                List<String> cols = parseCsvLine(line);
                if (cols.size() < 10) continue;

                int id = Integer.parseInt(cols.get(0));
                String customer = cols.get(1);
                String branch = cols.get(2);
                String equipment = cols.get(3);
                String issue = cols.get(4);
                String remarks = cols.get(5);
                TicketPriority pr = TicketPriority.valueOf(cols.get(6));
                TicketStatus st = TicketStatus.valueOf(cols.get(7));
                LocalDateTime createdAt = LocalDateTime.parse(cols.get(8));
                LocalDateTime updatedAt = LocalDateTime.parse(cols.get(9));

                list.add(new Ticket(id, customer, branch, equipment, issue, remarks, pr, st, createdAt, updatedAt));
            }
        }
        return list;
    }

    // Escape for CSV
    private String esc(String s) {
        if (s == null) return "";
        boolean needs = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
        String v = s.replace("\"", "\"\"");
        return needs ? "\"" + v + "\"" : v;
    }

    // Minimal CSV parser for one line (handles quotes)
    private List<String> parseCsvLine(String line) {
        List<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQ = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (inQ) {
                if (c == '"') {
                    // escaped quote?
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        cur.append('"');
                        i++;
                    } else {
                        inQ = false;
                    }
                } else {
                    cur.append(c);
                }
            } else {
                if (c == ',') {
                    out.add(cur.toString());
                    cur.setLength(0);
                } else if (c == '"') {
                    inQ = true;
                } else {
                    cur.append(c);
                }
            }
        }
        out.add(cur.toString());
        return out;
    }
}