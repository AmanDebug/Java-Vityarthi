public class Microcontroller extends Equipment {
    private String architecture;
    private int clockSpeedMHz;
    private boolean hasWirelessCapabilities;

    // Constructor mapping to the superclass
    public Microcontroller(String equipmentId, String name, int totalQuantity, 
                           String architecture, int clockSpeedMHz, boolean hasWirelessCapabilities) {
        // Call to the parent class (Equipment) constructor
        super(equipmentId, name, totalQuantity);
        this.architecture = architecture;
        this.clockSpeedMHz = clockSpeedMHz;
        this.hasWirelessCapabilities = hasWirelessCapabilities;
    }

    // Providing implementation for the abstract method
    @Override
    public String getCategory() {
        return "Microcontroller Development Board";
    }

    // Polymorphism: Overriding the display method to include specific details
    @Override
    public void displayDetails() {
        super.displayDetails(); // Call the parent method
        System.out.println("   Architecture: " + architecture);
        System.out.println("   Clock Speed: " + clockSpeedMHz + " MHz");
        System.out.println("   Wireless Ready: " + (hasWirelessCapabilities ? "Yes" : "No"));
        System.out.println("   Category: " + getCategory());
        System.out.println("-------------------------------------------------");
    }

    // Subclass-specific getters
    public String getArchitecture() {
        return architecture;
    }

    public int getClockSpeedMHz() {
        return clockSpeedMHz;
    }
}