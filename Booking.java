
public class Booking {

    private int bookingId;
    private int customerId;
    private int vehicleId;
    private int rentalDays;
    private double totalAmount;
    private String status;

    public Booking(int customerId, int vehicleId,
                   int rentalDays, double totalAmount) {

        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.rentalDays = rentalDays;
        this.totalAmount = totalAmount;
        this.status = "Active";
    }

    public int getBookingId() {
        return bookingId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }
}
