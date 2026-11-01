
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleDAO {

    // 1. Add Vehicle
    public void addVehicle(Vehicle v) {
        String sql = "INSERT INTO vehicles " +
                "(brand, model, type, rent_per_day, available) " +
                "VALUES (?, ?, ?, ?, 1)";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, v.getBrand());
            ps.setString(2, v.getModel());
            ps.setString(3, v.getType());
            ps.setDouble(4, v.getRentPerDay());

            ps.executeUpdate();
            System.out.println("Vehicle added successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding vehicle: " + e.getMessage());
        }
    }

    // 2. View Vehicles
    public void viewVehicles() {
        String sql = "SELECT * FROM vehicles ORDER BY vehicle_id";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n========== VEHICLE LIST ==========");

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println("ID: " + rs.getInt("vehicle_id"));
                System.out.println("Brand: " + rs.getString("brand"));
                System.out.println("Model: " + rs.getString("model"));
                System.out.println("Type: " + rs.getString("type"));
                System.out.println("Rent/day: Rs. " +
                        rs.getDouble("rent_per_day"));
                System.out.println("Available: " +
                        (rs.getInt("available") == 1 ? "Yes" : "No"));
                System.out.println("----------------------------------");
            }

            if (!found) {
                System.out.println("No vehicles found.");
            }

        } catch (SQLException e) {
            System.out.println("Error viewing vehicles: " + e.getMessage());
        }
    }

    // 3. Update Vehicle
    public void updateVehicle(int id, String brand,
                              String model, String type, double rent) {

        String sql = "UPDATE vehicles SET brand = ?, model = ?, " +
                "type = ?, rent_per_day = ? WHERE vehicle_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, brand);
            ps.setString(2, model);
            ps.setString(3, type);
            ps.setDouble(4, rent);
            ps.setInt(5, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Vehicle updated successfully!");
            } else {
                System.out.println("Vehicle ID not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error updating vehicle: " + e.getMessage());
        }
    }

    // 4. Delete Vehicle
    public void deleteVehicle(int id) {
        String sql = "DELETE FROM vehicles WHERE vehicle_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Vehicle deleted successfully!");
            } else {
                System.out.println("Vehicle ID not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error deleting vehicle: " + e.getMessage());
        }
    }
}
