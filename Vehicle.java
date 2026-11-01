
public class Vehicle {

    private int vehicleId;
    private String brand;
    private String model;
    private String type;
    private double rentPerDay;
    private boolean available;

    public Vehicle(int vehicleId, String brand, String model,
                   String type, double rentPerDay) {
        this.vehicleId = vehicleId;
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.rentPerDay = rentPerDay;
        this.available = true;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getType() {
        return type;
    }

    public double getRentPerDay() {
        return rentPerDay;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void displayDetails() {
        System.out.println("Vehicle ID: " + vehicleId);
        System.out.println("Brand: " + brand);
        System.out.println("Model: " + model);
        System.out.println("Type: " + type);
        System.out.println("Rent per day: Rs. " + rentPerDay);
        System.out.println("Available: " + (available ? "Yes" : "No"));
        System.out.println("--------------------------------");
    }
}
