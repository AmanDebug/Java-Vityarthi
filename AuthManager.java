import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthManager {

    // A simple inner class to hold the authenticated user's session data
    public static class UserSession {
        int userId;
        String username;
        String role;

        public UserSession(int userId, String username, String role) {
            this.userId = userId;
            this.username = username;
            this.role = role;
        }
    }

    /**
     * Authenticates a user against the database.
     * Note: In a production environment, the incoming password must be hashed 
     * (e.g., using BCrypt) before comparing it to the database hash.
     */
    public UserSession login(String username, String password) {
        String query = "SELECT user_id, role FROM Users WHERE username = ? AND password_hash = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            // We use the dummy data 'hashed_pw_1' etc. for this simulation
            stmt.setString(2, password); 
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("user_id");
                    String role = rs.getString("role");
                    
                    System.out.println("Login successful! Welcome, " + username + ".");
                    System.out.println("Access Level: " + role);
                    return new UserSession(id, username, role);
                } else {
                    System.out.println("Login failed: Invalid username or password.");
                    return null;
                }
            }
        } catch (SQLException e) {
            System.out.println("Authentication error: " + e.getMessage());
            return null;
        }
    }

    // Main method to test the authentication logic
    public static void main(String[] args) {
        AuthManager auth = new AuthManager();
        
        System.out.println("--- Testing Admin Login ---");
        // Testing with the dummy data we inserted in schema.sql
        UserSession adminSession = auth.login("admin_aman", "hashed_pw_1");
        
        if (adminSession != null && adminSession.role.equals("ADMIN")) {
            System.out.println("Admin privileges granted. You can now add or remove lab equipment.\n");
        }

        System.out.println("--- Testing Student Login ---");
        UserSession studentSession = auth.login("student_john", "hashed_pw_2");
        
        if (studentSession != null && studentSession.role.equals("STUDENT")) {
            System.out.println("Student privileges granted. You can view inventory and check out items.\n");
        }

        System.out.println("--- Testing Invalid Login ---");
        auth.login("admin_aman", "wrong_password");
    }
}