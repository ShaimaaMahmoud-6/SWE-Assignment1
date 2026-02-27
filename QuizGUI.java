import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.net.URI;
import java.awt.Desktop;

public class QuizGUI extends JFrame {

    private UserProfile user;

    private String[] questions = {
            "Do you like to take requests from a person or company and turn them into a product?",
            "Do you enjoy planning and managing projects from start to finish?",
            "Do you enjoy working with data and logic without a visible interface?",
            "Do you like building systems that handle information behind the scenes?",
            "Do you enjoy designing interfaces, choosing colors, and arranging elements on web pages or apps?",
            "Do you like making apps look nice and easy to use for people?",
            "Do you like to create algorithms that learn and act like a human?",
            "Do you enjoy experimenting with systems that can adapt and improve themselves?",
            "Do you like analyzing data to get results, statistics, and make predictions?",
            "Do you enjoy finding patterns in data to help companies make better decisions?"
    };

    private String[] tracks = {
            "Software Engineering", "Software Engineering",
            "Backend", "Backend",
            "Front End", "Front End",
            "ML/AI", "ML/AI",
            "Data Analytics", "Data Analytics"
    };

    private int currentQuestion = 0;
    private JLabel questionLabel;
    private JButton yesButton;
    private JButton noButton;
    private JButton detailsButton;
    private JPanel mainPanel;

    public QuizGUI() {

        super("Track Recommendation Quiz");
        user = new UserProfile("User");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 550);
        setLocationRelativeTo(null);

        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;


                Color color1 = new Color(10, 20, 80);
                Color color2 = new Color(70, 130, 255);

                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        questionLabel = new JLabel(
                "<html><div style='text-align:center;'>" + questions[currentQuestion] + "</div></html>",
                SwingConstants.CENTER
        );
        questionLabel.setForeground(Color.WHITE);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 35));
        mainPanel.add(questionLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        yesButton = new JButton("YES");
        noButton = new JButton("NO");


        Color buttonColor = new Color(15, 45, 120);

        yesButton.setBackground(buttonColor);
        yesButton.setForeground(Color.WHITE);
        yesButton.setFont(new Font("Arial", Font.BOLD, 20));
        yesButton.setPreferredSize(new Dimension(140, 55));
        yesButton.setFocusPainted(false);
        yesButton.setBorderPainted(false);

        noButton.setBackground(buttonColor);
        noButton.setForeground(Color.WHITE);
        noButton.setFont(new Font("Arial", Font.BOLD, 20));
        noButton.setPreferredSize(new Dimension(140, 55));
        noButton.setFocusPainted(false);
        noButton.setBorderPainted(false);
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);


        mainPanel.add(buttonPanel, BorderLayout.SOUTH);


        detailsButton = new JButton("View Recommended Track Details");
        detailsButton.setEnabled(false);
        detailsButton.setBackground(buttonColor);
        detailsButton.setForeground(Color.WHITE);
        detailsButton.setFont(new Font("Arial", Font.BOLD, 18));
        detailsButton.setPreferredSize(new Dimension(350, 50));
        detailsButton.setFocusPainted(false);
        detailsButton.setBorderPainted(false);

        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.add(detailsButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        yesButton.addActionListener(e -> processAnswer(1));
        noButton.addActionListener(e -> processAnswer(2));

        detailsButton.addActionListener(e ->
                showTrackDetails(user.getRecommendedTracks())
        );
    }

    private void processAnswer(int answer) {

        if (answer == 1) {
            user.addPoints(tracks[currentQuestion], 1);
        }

        currentQuestion++;

        if (currentQuestion < questions.length) {
            questionLabel.setText(
                    "<html><div style='text-align:center;'>" +
                            questions[currentQuestion] +
                            "</div></html>"
            );
        } else {
            displayResults();
        }
    }

    private void displayResults() {

        yesButton.setEnabled(false);
        noButton.setEnabled(false);
        detailsButton.setEnabled(true);

        StringBuilder result = new StringBuilder(
                "<html><div style='text-align:center; font-size:18px;'>"
        );

        for (String track : new String[]{
                "Software Engineering", "Backend",
                "Front End", "ML/AI", "Data Analytics"}) {

            double percent = user.getTrackPercentage(track);
            result.append(track)
                    .append(": ")
                    .append(String.format("%.1f%%", percent))
                    .append("<br>");
        }

        result.append("<br><b>Recommended Track(s): ")
                .append(String.join(" & ", user.getRecommendedTracks()))
                .append("</b>");

        result.append("</div></html>");

        questionLabel.setText(result.toString());
    }

    private void showTrackDetails(List<String> tracks) {

        for (String track : tracks) {

            String message = "";

            switch (track) {

                case "Software Engineering":
                    message =
                            "<html><div style='font-size:15px;'>"
                                    + "<b>Software Engineering</b><br><br>"
                                    + "Brief: Build complete applications and manage software projects.<br><br>"
                                    + "<b>Languages:</b> Java, Python, C++<br><br>"
                                    + "<b>Basics You Will Learn:</b><br>"
                                    + "- OOP<br>"
                                    + "- Data Structures<br>"
                                    + "- Algorithms<br>"
                                    + "- Software Design Principles<br><br>"
                                    + "More info: <a href='https://roadmap.sh/software-engineer'>roadmap.sh</a>"
                                    + "</div></html>";
                    break;

                case "Backend":
                    message =
                            "<html><div style='font-size:15px;'>"
                                    + "<b>Backend Development</b><br><br>"
                                    + "Brief: Work with servers, databases and APIs behind the scenes.<br><br>"
                                    + "<b>Languages:</b> Java, Python, Node.js<br><br>"
                                    + "<b>Basics You Will Learn:</b><br>"
                                    + "- Databases (SQL)<br>"
                                    + "- APIs<br>"
                                    + "- Server Logic<br>"
                                    + "- Authentication<br><br>"
                                    + "More info: <a href='https://roadmap.sh/backend'>roadmap.sh</a>"
                                    + "</div></html>";
                    break;

                case "Front End":
                    message =
                            "<html><div style='font-size:15px;'>"
                                    + "<b>Front End Development</b><br><br>"
                                    + "Brief: Design and build user interfaces for web applications.<br><br>"
                                    + "<b>Languages:</b> HTML, CSS, JavaScript<br><br>"
                                    + "<b>Basics You Will Learn:</b><br>"
                                    + "- Responsive Design<br>"
                                    + "- UI/UX Basics<br>"
                                    + "- JavaScript Logic<br>"
                                    + "- Frameworks (React/Angular)<br><br>"
                                    + "More info: <a href='https://roadmap.sh/frontend'>roadmap.sh</a>"
                                    + "</div></html>";
                    break;

                case "ML/AI":
                    message =
                            "<html><div style='font-size:15px;'>"
                                    + "<b>Machine Learning / AI</b><br><br>"
                                    + "Brief: Build intelligent systems that learn from data.<br><br>"
                                    + "<b>Languages:</b> Python, R<br><br>"
                                    + "<b>Basics You Will Learn:</b><br>"
                                    + "- Linear Algebra<br>"
                                    + "- Statistics<br>"
                                    + "- Machine Learning Algorithms<br>"
                                    + "- Data Processing<br><br>"
                                    + "More info: <a href='https://roadmap.sh/machine-learning'>roadmap.sh</a>"
                                    + "</div></html>";
                    break;

                case "Data Analytics":
                    message =
                            "<html><div style='font-size:15px;'>"
                                    + "<b>Data Analytics</b><br><br>"
                                    + "Brief: Analyze data to extract insights and support decisions.<br><br>"
                                    + "<b>Languages:</b> Python, SQL, R<br><br>"
                                    + "<b>Basics You Will Learn:</b><br>"
                                    + "- Statistics<br>"
                                    + "- Excel<br>"
                                    + "- SQL Queries<br>"
                                    + "- Data Visualization<br><br>"
                                    + "More info: <a href='https://roadmap.sh/data-analyst'>roadmap.sh</a>"
                                    + "</div></html>";
                    break;
            }

            JEditorPane editorPane = new JEditorPane("text/html", message);
            editorPane.setEditable(false);
            editorPane.setOpaque(false);

            editorPane.addHyperlinkListener(e -> {
                if (e.getEventType() ==
                        javax.swing.event.HyperlinkEvent.EventType.ACTIVATED) {
                    try {
                        Desktop.getDesktop().browse(new java.net.URI(e.getURL().toString()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            JOptionPane.showMessageDialog(
                    this,
                    editorPane,
                    track + " Details",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        new QuizGUI().setVisible(true);
    }
}