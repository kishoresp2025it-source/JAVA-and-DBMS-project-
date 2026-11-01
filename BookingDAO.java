
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingDAO {

    // RENT VEHICLE
    public void rentVehicle(int customerId, int vehicleId, int days) {

        if (days <= 0) {
            System.out.println("Rental days must be greater than zero.");
            return;
        }

        String customerSQL =
            "SELECT customer_id FROM customers WHERE customer_id = ?";

        String vehicleSQL =
            "SELECT rent_per_day, available FROM vehicles WHERE vehicle_id = ?";

        String bookingSQL = """
            INSERT INTO bookings
            (customer_id, vehicle_id, rental_days, total_amount, status)
            VALUES (?, ?, ?, ?, 'Active')
            """;

        String updateVehicleSQL =
            "UPDATE vehicles SET available = 0 WHERE vehicle_id = ?";

        try (Connection con = Database.getConnection()) {

            con.setAutoCommit(false);

            try {
                // Verify customer
                try (PreparedStatement ps = con.prepareStatement(customerSQL)) {
                    ps.setInt(1, customerId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            System.out.println("Customer not found!");
                            con.rollback();
                            return;
                        }
                    }
                }

                // Verify vehicle
                double rentPerDay;

                try (PreparedStatement ps = con.prepareStatement(vehicleSQL)) {
                    ps.setInt(1, vehicleId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            System.out.println("Vehicle not found!");
                            con.rollback();
                            return;
                        }

                        rentPerDay = rs.getDouble("rent_per_day");

                        if (rs.getInt("available") != 1) {
                            System.out.println("Vehicle is not available!");
                            con.rollback();
                            return;
                        }
                    }
                }

                double totalAmount = rentPerDay * days;

                // Insert booking
                try (PreparedStatement ps = con.prepareStatement(bookingSQL)) {
                    ps.setInt(1, customerId);
                    ps.setInt(2, vehicleId);
                    ps.setInt(3, days);
                    ps.setDouble(4, totalAmount);
                    ps.executeUpdate();
                }

                // Update vehicle availability
                try (PreparedStatement ps = con.prepareStatement(updateVehicleSQL)) {
                    ps.setInt(1, vehicleId);
                    ps.executeUpdate();
                }

                con.commit();

                System.out.println("\nVehicle rented successfully!");
                System.out.println("Rental days: " + days);
                System.out.println("Rent per day: Rs. " + rentPerDay);
                System.out.println("Total amount: Rs. " + totalAmount);

            } catch (SQLException e) {
                con.rollback();
                System.out.println("Booking failed: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    // RETURN VEHICLE
    public void returnVehicle(int bookingId) {

        String findSQL = """
            SELECT vehicle_id FROM bookings
            WHERE booking_id = ? AND status = 'Active'
            """;

        String returnSQL =
            "UPDATE bookings SET status = 'Returned' WHERE booking_id = ?";

        String vehicleSQL =
            "UPDATE vehicles SET available = 1 WHERE vehicle_id = ?";

        try (Connection con = Database.getConnection()) {

            con.setAutoCommit(false);

            try {
                int vehicleId;

                try (PreparedStatement ps = con.prepareStatement(findSQL)) {
                    ps.setInt(1, bookingId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            System.out.println(
                                "Active booking not found!"
                            );
                            con.rollback();
                            return;
                        }

                        vehicleId = rs.getInt("vehicle_id");
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(returnSQL)) {
                    ps.setInt(1, bookingId);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = con.prepareStatement(vehicleSQL)) {
                    ps.setInt(1, vehicleId);
                    ps.executeUpdate();
                }

                con.commit();

                System.out.println("Vehicle returned successfully!");
                System.out.println("Booking ID: " + bookingId);
                System.out.println("Vehicle is now available.");

            } catch (SQLException e) {
                con.rollback();
                System.out.println("Return failed: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    // VIEW BOOKINGS
    public void viewBookings(String filter) {

        String sql = """
            SELECT b.booking_id,
                   c.name,
                   v.brand,
                   v.model,
                   b.rental_days,
                   b.total_amount,
                   b.booking_date,
                   b.status
            FROM bookings b
            JOIN customers c ON b.customer_id = c.customer_id
            JOIN vehicles v ON b.vehicle_id = v.vehicle_id
            """;

        if ("Active".equalsIgnoreCase(filter)) {
            sql += " WHERE b.status = 'Active'";
        } else if ("Returned".equalsIgnoreCase(filter)) {
            sql += " WHERE b.status = 'Returned'";
        }

        sql += " ORDER BY b.booking_id";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n========== BOOKING LIST ==========");

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println("\nBooking ID: " +
                    rs.getInt("booking_id"));

                System.out.println("Customer: " +
                    rs.getString("name"));

                System.out.println("Vehicle: " +
                    rs.getString("brand") + " " +
                    rs.getString("model"));

                System.out.println("Rental days: " +
                    rs.getInt("rental_days"));

                System.out.println("Total amount: Rs. " +
                    rs.getDouble("total_amount"));

                System.out.println("Booking date: " +
                    rs.getString("booking_date"));

                System.out.println("Status: " +
                    rs.getString("status"));

                System.out.println("----------------------------------");
            }

            if (!found) {
                System.out.println("No bookings found.");
            }

        } catch (SQLException e) {
            System.out.println("Error viewing bookings: " + e.getMessage());
        }
    }
}
