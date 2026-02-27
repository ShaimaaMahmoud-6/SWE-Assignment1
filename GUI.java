import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class GUI extends JFrame {
    public static final Color darkBackground = new Color(18, 18, 18);
    public static final Color electricBlue = new Color(0, 150, 255);

    public GUI() {
        this.setSize(600, 500);
        this.setTitle("SOIGNÉ - Medical System");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(darkBackground);
        this.setLayout(new GridBagLayout());

        JLabel l1 = new JLabel("SOIGNÉ");
        l1.setForeground(electricBlue);
        l1.setFont(new Font("Serif", Font.BOLD, 40));
        JLabel tagline = new JLabel("Premium Care for a Distinguished Lifestyle");
        tagline.setForeground(Color.white);
        tagline.setFont(new Font("Serif", Font.ITALIC, 18));

        
        JButton b1 = createStyledButton(" Sign Up ");
        JButton b2 = createStyledButton(" Sign In ");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); gbc.gridx = 0;
        gbc.gridy = 0; this.add(l1, gbc);
        gbc.gridy = 1; this.add(tagline, gbc); 
        gbc.gridy = 2; this.add(b1, gbc);
        gbc.gridy = 3; this.add(b2, gbc);

        b1.addActionListener(e -> Main.handleSignUp(this));
        b2.addActionListener(e -> Main.handleSignIn(this));

        this.setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(darkBackground);
        btn.setForeground(electricBlue);
        btn.setBorder(new LineBorder(electricBlue, 2));
        btn.setPreferredSize(new Dimension(200, 40));
        return btn;
    }
}


class DoctorDashboard extends JFrame {
    public DoctorDashboard(String name, String specialty, String phone) {
        setTitle("SOIGNÉ - Doctor Portal");
        setSize(500, 400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(GUI.darkBackground);
        setLayout(new FlowLayout());

        JLabel welcome = new JLabel("Welcome Dr. " + name);
        welcome.setForeground(GUI.electricBlue);
        welcome.setFont(new Font("Arial", Font.BOLD, 22));
        add(welcome);

        setVisible(true);
    }
}


class PatientDashboard extends JFrame {
    public PatientDashboard(String name, String target) {
        setTitle("SOIGNÉ - Patient Portal");
        setSize(500, 400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(GUI.darkBackground);
        setLayout(new FlowLayout());

        JLabel welcome = new JLabel("Welcome, " + name);
        welcome.setForeground(GUI.electricBlue);
        welcome.setFont(new Font("Arial", Font.BOLD, 22));
        add(welcome);

        JButton findBtn = new JButton("Find Doctor");
        findBtn.addActionListener(e -> Main.findDoctorForPatient(target, this));
        add(findBtn);

        setVisible(true);
    }
}