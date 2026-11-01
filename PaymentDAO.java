import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PaymentDAO {

    public void makePayment(int bookingId, String paymentMethod) {

        String checkSQL = """
            SELECT total_amount, status
            FROM bookings
            WHERE booking_id = ?
            """;

        String insertSQL = """
            INSERT INTO payments
            (booking_id, amount, payment_method, payment_status)
            VALUES (?, ?, ?, 'Paid')
            """;

        try (Connection con = Database.getConnection()) {

            double amount;

            try (PreparedStatement check =
                     con.prepareStatement(checkSQL)) {

                check.setInt(1, bookingId);

                try (ResultSet rs = check.executeQuery()) {

                    if (!rs.next()) {
                        System.out.println("Booking not found!");
                        return;
                    }

                    if (!rs.getString("status").equalsIgnoreCase("Active")) {
                        System.out.println("This booking is not active.");
                        return;
                    }

                    amount = rs.getDouble("total_amount");
                }
            }

            try (PreparedStatement insert =
                     con.prepareStatement(insertSQL)) {

                insert.setInt(1, bookingId);
                insert.setDouble(2, amount);
                insert.setString(3, paymentMethod);

                insert.executeUpdate();

                System.out.println("\nPayment successful!");
                System.out.println("Booking ID: " + bookingId);
                System.out.println("Amount: Rs. " + amount);
                System.out.println("Method: " + paymentMethod);
                System.out.println("Status: Paid");
            }

        } catch (SQLException e) {

            if (e.getMessage().contains("UNIQUE")) {
                System.out.println("Payment already exists for this booking!");
            } else {
                System.out.println("Payment error: " + e.getMessage());
            }
        }
    }

    public void viewPayments() {

        String sql = """
            SELECT p.payment_id,
                   p.booking_id,
                   c.name,
                   p.amount,
                   p.payment_method,
                   p.payment_status,
                   p.payment_date
            FROM payments p
            JOIN bookings b ON p.booking_id = b.booking_id
            JOIN customers c ON b.customer_id = c.customer_id
            ORDER BY p.payment_id
            """;

        try (Connection con = Database.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n========== PAYMENT HISTORY ==========");

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println("------------------------------------");
                System.out.println("Payment ID: " + rs.getInt("payment_id"));
                System.out.println("Booking ID: " + rs.getInt("booking_id"));
                System.out.println("Customer: " + rs.getString("name"));
                System.out.println("Amount: Rs. " + rs.getDouble("amount"));
                System.out.println("Method: " + rs.getString("payment_method"));
                System.out.println("Status: " + rs.getString("payment_status"));
                System.out.println("Date: " + rs.getString("payment_date"));
            }

            if (!found) {
                System.out.println("No payment records found.");
            }

        } catch (SQLException e) {
            System.out.println("Payment history error: " + e.getMessage());
        }
    }
}
