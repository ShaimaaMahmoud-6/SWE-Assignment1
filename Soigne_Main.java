import java.sql.*;
import javax.swing.*;
public class Soigne_Main {
    public static void main(String[] args) {
        new GUI();
    }
    public static void handleSignUp(JFrame parent) {
        String[] options = {"Patient", "Doctor"};
        int choice = JOptionPane.showOptionDialog(null, "Register as:", "Sign Up",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (choice == -1) return;
        String type = (choice == 0) ? "Patient" : "Doctor";
        String name = JOptionPane.showInputDialog("Name:");
        String email = JOptionPane.showInputDialog("Email:");
        String pass = JOptionPane.showInputDialog("Password:");
        String phone = JOptionPane.showInputDialog("Phone:");
        String extra = (type.equals("Patient")) ? 
                       JOptionPane.showInputDialog("Specialty Needed:") : 
                       JOptionPane.showInputDialog("Your Specialty:");

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                JOptionPane.showMessageDialog(parent, "Connection failed! Check SQL Server."); 
                return;
            }
            String table = (type.equals("Patient")) ? "Patients" : "Doctors";
            String colExtra = (type.equals("Patient")) ? "TargetSpeciality" : "Specialty";
            String sql = "INSERT INTO " + table + " (Name, Email, Password, Phone, " + colExtra + ") VALUES (?, ?, ?, ?, ?)";
            
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, pass);
            pst.setString(4, phone);
            pst.setString(5, extra);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(parent, "Registered Successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parent, "Error: " + ex.getMessage());
        }
    }

    public static void handleSignIn(JFrame parent) {
        String email = JOptionPane.showInputDialog("Email:");
        String pass = JOptionPane.showInputDialog("Password:");
        if (email == null || pass == null) return;

        String[] roles = {"Patient", "Doctor"};
        int role = JOptionPane.showOptionDialog(null, "Login as:", "Sign In",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, roles, roles[0]);

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                JOptionPane.showMessageDialog(parent, "Connection failed!");
                return;
            }
            String table = (role == 0) ? "Patients" : "Doctors";

            String sql = "SELECT * FROM " + table + " WHERE Email = ? AND Password = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, email);
            pst.setString(2, pass);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                parent.dispose();
                if (role == 1) {
                    new DoctorDashboard(rs.getString("Name"), rs.getString("Specialty"), rs.getString("Phone"));
                } else {
                    new PatientDashboard(rs.getString("Name"), rs.getString("TargetSpeciality"));
                }
            } else {
                JOptionPane.showMessageDialog(parent, "Invalid Email or Password!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void findDoctorForPatient(String specialty, JFrame parent) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT Name, Phone FROM Doctors WHERE Specialty LIKE ?";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, "%" + specialty + "%");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(parent, "Found: Dr. " + rs.getString("Name") + "\nPhone: " + rs.getString("Phone"));
            } else {
                JOptionPane.showMessageDialog(parent, "No doctor found for this specialty.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}