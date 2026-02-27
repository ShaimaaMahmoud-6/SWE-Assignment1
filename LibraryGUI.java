import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LibraryGUI extends JFrame {

    private Library library;

    private JTextField bookIdField, titleField, authorField;
    private JTextField memberIdField, memberNameField;
    private JTextField borrowMemberIdField, borrowBookIdField;
    private JTextField returnMemberIdField, returnBookIdField;

    private JTextArea outputArea;

    public LibraryGUI() {

        library = new Library();

        setTitle("Library System");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel addBookPanel = new JPanel(new GridLayout(4,2,5,5));
        addBookPanel.setBorder(BorderFactory.createTitledBorder("Add Book"));
        panel.add(new JLabel("Book ID:"));
        bookIdField = new JTextField();
        panel.add(bookIdField);

        panel.add(new JLabel("Title:"));
        titleField = new JTextField();
        panel.add(titleField);

        panel.add(new JLabel("Author:"));
        authorField = new JTextField();
        panel.add(authorField);

        JButton addBookBtn = new JButton("Add Book");
        panel.add(addBookBtn);

        panel.add(new JLabel("Member ID:"));
        memberIdField = new JTextField();
        panel.add(memberIdField);

        panel.add(new JLabel("Member Name:"));
        memberNameField = new JTextField();
        panel.add(memberNameField);

        JButton registerBtn = new JButton("Register Member");
        panel.add(registerBtn);

        panel.add(new JLabel("Borrow Member ID:"));
        borrowMemberIdField = new JTextField();
        panel.add(borrowMemberIdField);

        panel.add(new JLabel("Borrow Book ID:"));
        borrowBookIdField = new JTextField();
        panel.add(borrowBookIdField);

        JButton borrowBtn = new JButton("Borrow Book");
        panel.add(borrowBtn);

        panel.add(new JLabel("Return Member ID:"));
        returnMemberIdField = new JTextField();
        panel.add(returnMemberIdField);

        panel.add(new JLabel("Return Book ID:"));
        returnBookIdField = new JTextField();
        panel.add(returnBookIdField);

        JButton returnBtn = new JButton("Return Book");
        panel.add(returnBtn);

        JButton displayBtn = new JButton("Display Available Books");
        panel.add(displayBtn);

        add(panel, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // ================= BUTTON ACTIONS =================

        addBookBtn.addActionListener(e -> {
            int id = Integer.parseInt(bookIdField.getText());
            String title = titleField.getText();
            String author = authorField.getText();

            library.addBook(new Book(id, title, author));
            outputArea.append("Book added successfully\n");
        });

        registerBtn.addActionListener(e -> {
            int id = Integer.parseInt(memberIdField.getText());
            String name = memberNameField.getText();

            library.addMember(new Member(id, name));
            outputArea.append("Member registered successfully\n");
        });

        borrowBtn.addActionListener(e -> {
            int mId = Integer.parseInt(borrowMemberIdField.getText());
            int bId = Integer.parseInt(borrowBookIdField.getText());

            Member member = library.searchMemberById(mId);
            Book book = library.searchBookById(bId);

            if(member != null && book != null && book.isAvailable()){
                String msg = member.borrowBook(book);
                outputArea.append(msg + "\n");
            } else {
                outputArea.append("Borrow failed\n");
            }
        });

        returnBtn.addActionListener(e -> {
            int mId = Integer.parseInt(returnMemberIdField.getText());
            int bId = Integer.parseInt(returnBookIdField.getText());

            Member member = library.searchMemberById(mId);
            Book book = library.searchBookById(bId);

            if(member != null && book != null){
                String msg = member.returnBook(book);
                outputArea.append(msg + "\n");
            } else {
                outputArea.append("Return failed\n");
            }
        });

        displayBtn.addActionListener(e -> {
            outputArea.setText("");
            outputArea.append("Available Books:\n");

            for(int i = 0; i < library.getBooks().size(); i++){
                Book b = library.getBooks().get(i);
                if(b.isAvailable()){
                    outputArea.append("ID: " + b.getId() + " | Title: " + b.getTitle() + "\n");
                }
            }
        });

        setVisible(true);
    }
}