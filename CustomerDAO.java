import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {

    public void addCustomer(Customer customer) {
        String sql = "INSERT INTO customers (name, phone, email) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getPhone());
            pstmt.setString(3, customer.getEmail());

            pstmt.executeUpdate();

            System.out.println("Customer registered successfully!");

        } catch (SQLException e) {
            System.out.println("Error registering customer: " + e.getMessage());
        }
    }

    public void viewCustomers() {
        String sql = "SELECT * FROM customers";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("\n===== CUSTOMER LIST =====");

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println("Customer ID: " + rs.getInt("customer_id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Phone: " + rs.getString("phone"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("-------------------------");
            }

            if (!found) {
                System.out.println("No customers registered yet.");
            }

        } catch (SQLException e) {
            System.out.println("Error viewing customers: " + e.getMessage());
        }
    }
}

