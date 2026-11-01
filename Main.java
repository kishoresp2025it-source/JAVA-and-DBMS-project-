
import java.util.Scanner;

public class Main {

    private static Scanner sc = new Scanner(System.in);

    private static VehicleDAO vehicleDAO = new VehicleDAO();
    private static CustomerDAO customerDAO = new CustomerDAO();
    private static BookingDAO bookingDAO = new BookingDAO();
    private static PaymentDAO paymentDAO = new PaymentDAO();

    public static void main(String[] args) {

        Database.initialize();

        int choice;

        do {
            System.out.println("\n================================");
            System.out.println("       WHEELCRAFT SYSTEM");
            System.out.println("================================");
            System.out.println("1. Add Vehicle");
            System.out.println("2. View Vehicles");
            System.out.println("3. Update Vehicle");
            System.out.println("4. Delete Vehicle");
            System.out.println("5. Register Customer");
            System.out.println("6. View Customers");
            System.out.println("7. Rent a Vehicle");
            System.out.println("8. Return Vehicle");
            System.out.println("9. View Bookings");
            System.out.println("10. Make Payment");
            System.out.println("11. View Payment History");
            System.out.println("0. Exit");
            System.out.println("================================");

            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    addVehicle();
                    break;

                case 2:
                    vehicleDAO.viewVehicles();
                    break;

                case 3:
                    updateVehicle();
                    break;

                case 4:
                    deleteVehicle();
                    break;

                case 5:
                    registerCustomer();
                    break;

                case 6:
                    customerDAO.viewCustomers();
                    break;

                case 7:
                    rentVehicle();
                    break;

                case 8:
                    returnVehicle();
                    break;

                case 9:
                    bookingDAO.viewBookings("All");
                    break;
                case 10:
                    makePayment(); 
                    break;
                case 11: 
                    paymentDAO.viewPayments();
                    break;
                case 0:
                    System.out.println("Thank you for using WheelCraft!");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 0);

        sc.close();
    }

    private static void addVehicle() {

        System.out.println("\n--- ADD VEHICLE ---");

        System.out.print("Enter brand: ");
        String brand = sc.nextLine();

        System.out.print("Enter model: ");
        String model = sc.nextLine();

        System.out.print("Enter type (Car/Bike/Van): ");
        String type = sc.nextLine();

        System.out.print("Enter rent per day (Rs.): ");
        double rent = sc.nextDouble();
        sc.nextLine();

        Vehicle vehicle = new Vehicle(0, brand, model, type, rent);

        vehicleDAO.addVehicle(vehicle);
    }

    private static void updateVehicle() {

        System.out.println("\n--- UPDATE VEHICLE ---");

        System.out.print("Enter vehicle ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter new brand: ");
        String brand = sc.nextLine();

        System.out.print("Enter new model: ");
        String model = sc.nextLine();

        System.out.print("Enter new type (Car/Bike/Van): ");
        String type = sc.nextLine();

        System.out.print("Enter new rent per day: ");
        double rent = sc.nextDouble();
        sc.nextLine();

        vehicleDAO.updateVehicle(id, brand, model, type, rent);
    }

    private static void deleteVehicle() {

        System.out.println("\n--- DELETE VEHICLE ---");

        System.out.print("Enter vehicle ID to delete: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Confirm deletion (y/n): ");
        String confirm = sc.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            vehicleDAO.deleteVehicle(id);
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private static void registerCustomer() {

        System.out.println("\n--- REGISTER CUSTOMER ---");

        System.out.print("Enter customer name: ");
        String name = sc.nextLine();

        System.out.print("Enter phone number: ");
        String phone = sc.nextLine();

        System.out.print("Enter email: ");
        String email = sc.nextLine();

        Customer customer = new Customer(name, phone, email);

        customerDAO.addCustomer(customer);
    }

    private static void rentVehicle() {

        System.out.println("\n--- RENT A VEHICLE ---");

        System.out.print("Enter customer ID: ");
        int customerId = sc.nextInt();

        System.out.print("Enter vehicle ID: ");
        int vehicleId = sc.nextInt();

        System.out.print("Enter rental days: ");
        int days = sc.nextInt();
        sc.nextLine();

        bookingDAO.rentVehicle(customerId, vehicleId, days);
    }

    private static void returnVehicle() {

        System.out.println("\n--- RETURN VEHICLE ---");

        System.out.print("Enter booking ID: ");
        int bookingId = sc.nextInt();
        sc.nextLine();

        bookingDAO.returnVehicle(bookingId);
    }
private static void makePayment() {

    System.out.println("\n--- MAKE PAYMENT ---");

    System.out.print("Enter booking ID: ");
    int bookingId = sc.nextInt();

    System.out.println("1. Cash");
    System.out.println("2. UPI");
    System.out.println("3. Card");

    System.out.print("Select payment method: ");
    int method = sc.nextInt();
    sc.nextLine();

    String paymentMethod;

    switch (method) {
        case 1:
            paymentMethod = "Cash";
            break;
        case 2:
            paymentMethod = "UPI";
            break;
        case 3:
            paymentMethod = "Card";
            break;
        default:
            System.out.println("Invalid payment method!");
            return;
    }

    paymentDAO.makePayment(bookingId, paymentMethod);
}
}
