import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() {
        try {
            String url = "jdbc:sqlserver://localhost;databaseName=SoigneDB;user=DB-admin;password=123456;encrypt=true;trustServerCertificate=true;";
            return DriverManager.getConnection(url);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}