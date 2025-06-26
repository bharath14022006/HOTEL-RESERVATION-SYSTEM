import java.sql.*;
import java.util.Scanner;

public class HotelReservationJDBC {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/HotelReservationSystem";
    private static final String DB_USER = "root";
    private static final String DB_PASS = ""; 

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in);
             Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {

            while (true) {
                System.out.println("\n--- Hotel Reservation Menu ---");
                System.out.println("1. View Available Rooms");
                System.out.println("2. Book a Room");
                System.out.println("3. View Customers");
                System.out.println("4. Exit");
                System.out.print("Enter choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> viewAvailableRooms(conn);
                    case 2 -> bookRoom(conn, scanner);
                    case 3 -> viewCustomers(conn);
                    case 4 -> {
                        System.out.println("Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid option.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void viewAvailableRooms(Connection conn) throws SQLException {
        String sql = "SELECT room_number FROM rooms WHERE is_booked = FALSE";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Available Rooms:");
            while (rs.next()) {
                System.out.println("Room " + rs.getInt("room_number"));
            }
        }
    }

    static void bookRoom(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();
        System.out.print("Enter check-in date (YYYY-MM-DD): ");
        String checkIn = scanner.nextLine();
        System.out.print("Enter check-out date (YYYY-MM-DD): ");
        String checkOut = scanner.nextLine();
        System.out.print("Enter room number: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine();

        String customerSQL = "INSERT INTO customers (name, email, phone, check_in, check_out) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement customerStmt = conn.prepareStatement(customerSQL, Statement.RETURN_GENERATED_KEYS);
        customerStmt.setString(1, name);
        customerStmt.setString(2, email);
        customerStmt.setString(3, phone);
        customerStmt.setDate(4, Date.valueOf(checkIn));
        customerStmt.setDate(5, Date.valueOf(checkOut));
        customerStmt.executeUpdate();

        ResultSet keys = customerStmt.getGeneratedKeys();
        if (keys.next()) {
            int customerId = keys.getInt(1);

            String roomSQL = "UPDATE rooms SET is_booked = TRUE, guest_id = ? WHERE room_number = ?";
            PreparedStatement roomStmt = conn.prepareStatement(roomSQL);
            roomStmt.setInt(1, customerId);
            roomStmt.setInt(2, roomNumber);
            int updated = roomStmt.executeUpdate();

            if (updated > 0) {
                System.out.println("Room " + roomNumber + " successfully booked for " + name + ".");
            } else {
                System.out.println("Room booking failed. Please check the room number.");
            }
        }
    }

    static void viewCustomers(Connection conn) throws SQLException {
        String sql = "SELECT * FROM customers";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("--- Customer List ---");
            while (rs.next()) {
                System.out.printf("ID: %d | Name: %s | Check-in: %s | Check-out: %s%n",
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getDate("check_in"),
                        rs.getDate("check_out"));
            }
        }
    }
}
