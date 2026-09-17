import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AuthManager auth = new AuthManager();
        InventoryManager inventory = new InventoryManager();

        System.out.println("==================================================");
        System.out.println("   Welcome to the VITyarthi Hardware Lab System   ");
        System.out.println("==================================================");

        while (true) {
            System.out.println("\n--- Login System ---");
            System.out.print("Username (or type 'exit' to quit): ");
            String username = scanner.nextLine();

            if (username.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the system. Goodbye!");
                break;
            }

            System.out.print("Password: ");
            String password = scanner.nextLine();

            // Attempt login
            AuthManager.UserSession session = auth.login(username, password);

            if (session != null) {
                boolean loggedIn = true;
                
                while (loggedIn) {
                    if (session.role.equals("ADMIN")) {
                        System.out.println("\n--- Admin Dashboard ---");
                        System.out.println("1. View Inventory");
                        System.out.println("2. Add New Equipment");
                        System.out.println("3. Logout");
                        System.out.print("Select an option: ");
                        
                        String choice = scanner.nextLine();
                        switch (choice) {
                            case "1":
                                inventory.viewAllEquipment();
                                break;
                            case "2":
                                try {
                                    System.out.print("Enter Equipment ID (e.g., FPGA-001): ");
                                    String id = scanner.nextLine();
                                    System.out.print("Enter Name: ");
                                    String name = scanner.nextLine();
                                    System.out.print("Enter Category (e.g., Board): ");
                                    String category = scanner.nextLine();
                                    System.out.print("Enter Total Quantity: ");
                                    int qty = Integer.parseInt(scanner.nextLine());
                                    
                                    System.out.print("Enter Architecture (or press Enter to skip): ");
                                    String arch = scanner.nextLine();
                                    System.out.print("Enter Clock Speed in MHz (or 0): ");
                                    int clock = Integer.parseInt(scanner.nextLine());
                                    System.out.print("Wireless Ready? (true/false): ");
                                    boolean wireless = Boolean.parseBoolean(scanner.nextLine());

                                    inventory.addEquipment(id, name, category, qty, arch.isEmpty() ? null : arch, clock, wireless);
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid number format. Please try adding the equipment again.");
                                }
                                break;
                            case "3":
                                loggedIn = false;
                                System.out.println("Logging out of Admin session...");
                                break;
                            default:
                                System.out.println("Invalid option. Try again.");
                        }
                    } else if (session.role.equals("STUDENT")) {
                        System.out.println("\n--- Student Dashboard ---");
                        System.out.println("1. View Inventory");
                        System.out.println("2. Checkout Equipment");
                        System.out.println("3. Logout");
                        System.out.print("Select an option: ");
                        
                        String choice = scanner.nextLine();
                        switch (choice) {
                            case "1":
                                inventory.viewAllEquipment();
                                break;
                            case "2":
                                System.out.print("Enter the Equipment ID you want to check out: ");
                                String equipId = scanner.nextLine();
                                inventory.checkoutItem(session.userId, equipId);
                                break;
                            case "3":
                                loggedIn = false;
                                System.out.println("Logging out of Student session...");
                                break;
                            default:
                                System.out.println("Invalid option. Try again.");
                        }
                    }
                }
            }
        }
        scanner.close();
    }
}