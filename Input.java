package util;

import java.util.Scanner;

public class Input {
    private final Scanner sc = new Scanner(System.in);

    public String readLine(String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }

    public String readNonEmpty(String prompt) {
        while (true) {
            String s = readLine(prompt).trim();
            if (!s.isEmpty()) return s;
            System.out.println("Value cannot be empty. Try again.");
        }
    }

    public int readInt(String prompt) {
        while (true) {
            String s = readLine(prompt).trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    public int readIntRange(String prompt, int min, int max) {
        while (true) {
            int v = readInt(prompt);
            if (v >= min && v <= max) return v;
            System.out.println("Enter a number between " + min + " and " + max + ".");
        }
    }

    public String readOptional(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }
}