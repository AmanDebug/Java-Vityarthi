import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/vityarthi_lab"; 
    private static final String USER = "root"; 
    private static final String PASSWORD = "@amanP05"; 

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found!");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println("Attempting to connect to the database...");
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("Success! Java is officially connected to the vityarthi_lab database.");
        }
    }
}