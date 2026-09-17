public abstract class Equipment {
    // Encapsulation: Private fields accessed via getters/setters
    private String equipmentId;
    private String name;
    private int totalQuantity;
    private int availableQuantity;

    public Equipment(String equipmentId, String name, int totalQuantity) {
        this.equipmentId = equipmentId;
        this.name = name;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = totalQuantity;
    }

    // Abstract method to enforce implementation in subclasses
    public abstract String getCategory();

    // Standard getters and setters
    public String getEquipmentId() {
        return equipmentId;
    }

    public String getName() {
        return name;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    // Method to handle checkout logic
    public boolean checkOut() {
        if (availableQuantity > 0) {
            availableQuantity--;
            return true;
        }
        return false;
    }

    public void returnItem() {
        if (availableQuantity < totalQuantity) {
            availableQuantity++;
        }
    }

    // Base method for polymorphism
    public void displayDetails() {
        System.out.println("ID: " + equipmentId + " | Name: " + name + 
                           " | Available: " + availableQuantity + "/" + totalQuantity);
    }
}