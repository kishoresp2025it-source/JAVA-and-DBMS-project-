import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String URL = "jdbc:sqlite:wheelcraft.db";

    public static Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(URL);

        try (Statement stmt = con.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        } catch (SQLException e) {
            con.close();
            throw e;
        }

        return con;
    }

    public static void initialize() {

        String vehicleSQL = """
            CREATE TABLE IF NOT EXISTS vehicles (
                vehicle_id INTEGER PRIMARY KEY AUTOINCREMENT,
                brand TEXT NOT NULL,
                model TEXT NOT NULL,
                type TEXT NOT NULL,
                rent_per_day REAL NOT NULL,
                available INTEGER NOT NULL DEFAULT 1
            )
            """;

        String customerSQL = """
            CREATE TABLE IF NOT EXISTS customers (
                customer_id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                phone TEXT NOT NULL,
                email TEXT NOT NULL
            )
            """;

        String bookingSQL = """
            CREATE TABLE IF NOT EXISTS bookings (
                booking_id INTEGER PRIMARY KEY AUTOINCREMENT,
                customer_id INTEGER NOT NULL,
                vehicle_id INTEGER NOT NULL,
                rental_days INTEGER NOT NULL,
                total_amount REAL NOT NULL,
                booking_date TEXT DEFAULT CURRENT_TIMESTAMP,
                status TEXT NOT NULL DEFAULT 'Active',
                FOREIGN KEY (customer_id)
                    REFERENCES customers(customer_id),
                FOREIGN KEY (vehicle_id)
                    REFERENCES vehicles(vehicle_id)
            )
            """;

        String paymentSQL = """
            CREATE TABLE IF NOT EXISTS payments (
                payment_id INTEGER PRIMARY KEY AUTOINCREMENT,
                booking_id INTEGER NOT NULL UNIQUE,
                amount REAL NOT NULL CHECK (amount > 0),
                payment_method TEXT NOT NULL
                    CHECK (payment_method IN ('Cash', 'UPI', 'Card')),
                payment_status TEXT NOT NULL DEFAULT 'Paid',
                payment_date TEXT DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (booking_id)
                    REFERENCES bookings(booking_id)
            )
            """;

        try (Connection con = getConnection();
             Statement stmt = con.createStatement()) {

            stmt.execute(vehicleSQL);
            stmt.execute(customerSQL);
            stmt.execute(bookingSQL);
            stmt.execute(paymentSQL);

            System.out.println("Database connected successfully!");
            System.out.println(
                "Vehicles, Customers, Bookings and Payments tables are ready!"
            );

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}