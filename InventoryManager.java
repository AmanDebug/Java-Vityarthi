import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryManager {

    // 1. READ: Fetch and display all available equipment
    public void viewAllEquipment() {
        String query = "SELECT equipment_id, name, category, available_quantity, total_quantity FROM Equipment";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            System.out.println("\n--- Lab Inventory Status ---");
            System.out.printf("%-10s | %-30s | %-15s | %s\n", "ID", "Name", "Category", "Stock");
            System.out.println("-----------------------------------------------------------------------");
            
            while (rs.next()) {
                String id = rs.getString("equipment_id");
                String name = rs.getString("name");
                String category = rs.getString("category");
                int available = rs.getInt("available_quantity");
                int total = rs.getInt("total_quantity");
                
                System.out.printf("%-10s | %-30s | %-15s | %d/%d\n", id, name, category, available, total);
            }
            System.out.println("-----------------------------------------------------------------------\n");
            
        } catch (SQLException e) {
            System.out.println("Error fetching equipment: " + e.getMessage());
        }
    }

    // 2. CREATE: Add new equipment to the database
    public void addEquipment(String id, String name, String category, int quantity, String arch, int clockSpeed, boolean wireless) {
        String query = "INSERT INTO Equipment (equipment_id, name, category, total_quantity, available_quantity, architecture, clock_speed_mhz, wireless_ready) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setString(3, category);
            stmt.setInt(4, quantity);
            stmt.setInt(5, quantity); // Initially, available == total
            stmt.setString(6, arch);
            stmt.setInt(7, clockSpeed);
            stmt.setBoolean(8, wireless);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Successfully added: " + name);
            }
            
        } catch (SQLException e) {
            System.out.println("Error adding equipment: " + e.getMessage());
        }
    }

    // 3. TRANSACTION WORKFLOW: Check out an item
    public boolean checkoutItem(int userId, String equipmentId) {
        // Step 1: Check if item is available
        String checkStockQuery = "SELECT available_quantity FROM Equipment WHERE equipment_id = ?";
        String updateStockQuery = "UPDATE Equipment SET available_quantity = available_quantity - 1 WHERE equipment_id = ?";
        String logTransactionQuery = "INSERT INTO Transactions (user_id, equipment_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Disable auto-commit to ensure transaction atomicity (ACID properties)
            conn.setAutoCommit(false);
            
            try (PreparedStatement checkStmt = conn.prepareStatement(checkStockQuery)) {
                checkStmt.setString(1, equipmentId);
                ResultSet rs = checkStmt.executeQuery();
                
                if (rs.next() && rs.getInt("available_quantity") > 0) {
                    // Step 2: Decrease available quantity
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateStockQuery)) {
                        updateStmt.setString(1, equipmentId);
                        updateStmt.executeUpdate();
                    }
                    
                    // Step 3: Log the transaction
                    try (PreparedStatement logStmt = conn.prepareStatement(logTransactionQuery)) {
                        logStmt.setInt(1, userId);
                        logStmt.setString(2, equipmentId);
                        logStmt.executeUpdate();
                    }
                    
                    // Commit the transaction
                    conn.commit();
                    System.out.println("Checkout successful for equipment ID: " + equipmentId);
                    return true;
                } else {
                    System.out.println("Checkout failed: Item " + equipmentId + " is out of stock or does not exist.");
                    return false;
                }
            } catch (SQLException ex) {
                conn.rollback(); // Undo changes if something fails
                throw ex;
            } finally {
                conn.setAutoCommit(true); // Restore default behavior
            }
        } catch (SQLException e) {
            System.out.println("Database transaction error: " + e.getMessage());
            return false;
        }
    }

    // Main method for testing the logic
    public static void main(String[] args) {
        InventoryManager manager = new InventoryManager();
        
        // Display current stock
        manager.viewAllEquipment();
        
        // Add a new sensor to demonstrate the Create operation
        System.out.println("Adding new equipment...");
        manager.addEquipment("SENS-001", "Ultrasonic Distance Sensor", "Sensor", 20, null, 0, false);
        
        // Test a checkout process (assuming user_id 2 exists from our SQL insert)
        System.out.println("\nTesting checkout process for Student (ID: 2)...");
        manager.checkoutItem(2, "AI-002"); // Student 2 checks out a Jetson Orin Nano
        
        // Display updated stock to verify the transaction worked
        manager.viewAllEquipment();
    }
}