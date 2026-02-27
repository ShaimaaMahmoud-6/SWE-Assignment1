import java.util.Scanner;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

class Book{    
    private int id;
    private String title;
    private String author;
    private boolean isAvailable;

    public Book(int id, String title, String author){
        this.id = id;
        this.title = title;
        this.author = author;
        this.isAvailable = true;
    }
    
    public int getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public boolean isAvailable(){
        return isAvailable;
    }

    public void borrowBook(){
        isAvailable = false;
    }

    public void returnBook(){
        isAvailable = true;
    }

    public void displayInfo(){ 
        System.out.println("ID: " + id);
        System.out.println("Title: " + title);
        System.out.println("Author: " + author);
        System.out.println("Available: " + isAvailable);
    }
}

class Member {
    private int memberId;
    private String name;
    private ArrayList<Book> borrowedBooks;

    public Member(int memberId, String name) {
        this.memberId = memberId;
        this.name = name;
        borrowedBooks = new ArrayList<>();
    }

    public int getMemberId() {
        return memberId;
    }

    public String borrowBook(Book book) {
        if (borrowedBooks.size() >= 3) {
            return("You cannot borrow more than 3 books.");
        }

        borrowedBooks.add(book);
        book.borrowBook();
        return("Book borrowed successfully.");
    }

    public String returnBook(Book book) {
        if (borrowedBooks.remove(book)) {
            book.returnBook();
            return("Book returned successfully.");
        } else {
            return("This member did not borrow this book.");
        }
    }
}

class Library {
    private ArrayList<Book> books;
    private ArrayList<Member> members;

    public Library() {
        books = new ArrayList<>();
        members = new ArrayList<>();
    }

    public String addBook(Book book) {
        books.add(book);
        return("Book added successfully.");
    }

    public String addMember(Member member) {
        members.add(member);
        return("Member registered successfully.");
    }

    public Book searchBookById(int id) {
        for (Book b : books) {
            if (b.getId() == id) {
                return b;
            }
        }
        return null;
    }

    public Member searchMemberById(int id) {
        for (Member m : members) {
            if (m.getMemberId() == id) {
                return m;
            }
        }
        return null;
    }

    public void displayAvailableBooks() {
        for (Book b : books) {
            if (b.isAvailable()) {
                b.displayInfo();
            }
        }
    }

    public ArrayList<Book> getBooks(){
        return books;
    }
}

public class LibraryMain {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Choose mode:");
        System.out.println("1) Console");
        System.out.println("2) GUI");
        System.out.print("Enter choice: ");

        int choice1 = sc.nextInt();

        if (choice1 == 2) {
            SwingUtilities.invokeLater(() -> new LibraryGUI());
            return;
        }

        Scanner input = new Scanner(System.in);
        Library library = new Library();

        int choice;

        do {
            System.out.println("\n===== Library Menu =====");
            System.out.println("1. Add Book");
            System.out.println("2. Register Member");
            System.out.println("3. Borrow Book");
            System.out.println("4. Return Book");
            System.out.println("5. Display Available Books");
            System.out.println("6. Exit");
            System.out.print("Choose: ");

            choice = input.nextInt();

            switch (choice) {

                case 1:
                    System.out.print("Enter Book ID: ");
                    int bookId = input.nextInt();
                    input.nextLine();

                    System.out.print("Enter Title: ");
                    String title = input.nextLine();

                    System.out.print("Enter Author: ");
                    String author = input.nextLine();

                    Book newBook = new Book(bookId, title, author);
                    System.out.println(library.addBook(newBook));
                    break;

                case 2:
                    System.out.print("Enter Member ID: ");
                    int memberId = input.nextInt();
                    input.nextLine();

                    System.out.print("Enter Name: ");
                    String name = input.nextLine();

                    Member newMember = new Member(memberId, name);
                    System.out.println(library.addMember(newMember));
                    break;

                case 3:
                    System.out.print("Enter Member ID: ");
                    int mId = input.nextInt();

                    System.out.print("Enter Book ID: ");
                    int bId = input.nextInt();

                    Member member = library.searchMemberById(mId);
                    Book book = library.searchBookById(bId);

                    if (member != null && book != null && book.isAvailable()) {
                        System.out.println(member.borrowBook(book));
                    } else {
                        System.out.println("Invalid member or book not available.");
                    }
                    break;

                case 4:
                    System.out.print("Enter Member ID: ");
                    int mIdReturn = input.nextInt();

                    System.out.print("Enter Book ID: ");
                    int bIdReturn = input.nextInt();

                    Member memberReturn = library.searchMemberById(mIdReturn);
                    Book bookReturn = library.searchBookById(bIdReturn);

                    if (memberReturn != null && bookReturn != null) {
                        System.out.println(memberReturn.returnBook(bookReturn));
                    } else {
                        System.out.println("Invalid member or book.");
                    }
                    break;

                case 5:
                    library.displayAvailableBooks();
                    break;

                case 6:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 6);

        input.close();
        sc.close();
    }
}