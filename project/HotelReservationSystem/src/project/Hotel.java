package project;
import java.sql.*;
public class Hotel {

	public static void main(String[] args) throws SQLException {
		String url="jdbc:mysql://localhost:3306/hotel_reservation";
		String username="root";
		String password="";
		
		Connection con= DriverManager.getConnection(url, username , password)
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            printMenu();
            int choice = Integer.parseInt(sc.nextLine());

            try {
                switch (choice) {
                    case 1 -> addCustomer();
                    case 2 -> bookRoom();
                    case 3 -> viewReservations();
                    case 4 -> cancelBooking();
                    case 5 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    private static void printMenu() {
        System.out.println("\n--- Hotel Reservation System ---");
        System.out.println("1. Add Customer");
        System.out.println("2. Book Room");
        System.out.println("3. View Reservations");
        System.out.println("4. Cancel Booking");
        System.out.println("5. Exit");
        System.out.print("Choose option: ");
    }

    private static void addCustomer() throws SQLException {
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Phone: ");
        String phone = sc.nextLine();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO customers (name, phone) VALUES (?, ?)")) {
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.executeUpdate();
            System.out.println("Customer added.");
        }
    }

    private static void bookRoom() throws SQLException {
        System.out.print("Customer ID: ");
        int custId = Integer.parseInt(sc.nextLine());
        System.out.print("Room Number: ");
        String roomNo = sc.nextLine();
        System.out.print("Check-in Date (YYYY-MM-DD): ");
        String checkIn = sc.nextLine();
        System.out.print("Check-out Date (YYYY-MM-DD): ");
        String checkOut = sc.nextLine();

        try (Connection conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT room_id FROM rooms WHERE room_number = ? AND is_booked = FALSE");
            ps.setString(1, roomNo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int roomId = rs.getInt("room_id");

                PreparedStatement resStmt = conn.prepareStatement(
                        "INSERT INTO reservations (customer_id, room_id, check_in, check_out) VALUES (?, ?, ?, ?)");
                resStmt.setInt(1, custId);
                resStmt.setInt(2, roomId);
                resStmt.setString(3, checkIn);
                resStmt.setString(4, checkOut);
                resStmt.executeUpdate();

                PreparedStatement updateRoom = conn.prepareStatement(
                        "UPDATE rooms SET is_booked = TRUE WHERE room_id = ?");
                updateRoom.setInt(1, roomId);
                updateRoom.executeUpdate();

                System.out.println("Room booked successfully.");
            } else {
                System.out.println("Room not available.");
            }
        }
    }

    private static void viewReservations() throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT r.reservation_id, c.name, rm.room_number, r.check_in, r.check_out " +
                             "FROM reservations r JOIN customers c ON r.customer_id = c.id " +
                             "JOIN rooms rm ON r.room_id = rm.room_id");
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\nReservations:");
            while (rs.next()) {
                System.out.printf("ID: %d | %s | Room: %s | From: %s | To: %s\n",
                        rs.getInt("reservation_id"),
                        rs.getString("name"),
                        rs.getString("room_number"),
                        rs.getDate("check_in"),
                        rs.getDate("check_out"));
            }
        }
    }

    private static void cancelBooking() throws SQLException {
        System.out.print("Reservation ID: ");
        int resId = Integer.parseInt(sc.nextLine());

        try (Connection conn = getConnection()) {
            PreparedStatement getRoom = conn.prepareStatement(
                    "SELECT room_id FROM reservations WHERE reservation_id = ?");
            getRoom.setInt(1, resId);
            ResultSet rs = getRoom.executeQuery();

            if (rs.next()) {
                int roomId = rs.getInt("room_id");

                PreparedStatement del = conn.prepareStatement(
                        "DELETE FROM reservations WHERE reservation_id = ?");
                del.setInt(1, resId);
                del.executeUpdate();

                PreparedStatement updateRoom = conn.prepareStatement(
                        "UPDATE rooms SET is_booked = FALSE WHERE room_id = ?");
                updateRoom.setInt(1, roomId);
                updateRoom.executeUpdate();

                System.out.println("Reservation cancelled.");
            } else {
                System.out.println("Reservation not found.");
            }
        }
    }
		    }CREATE DATABASE IF NOT EXISTS hotel_db;

USE hotel_db;

CREATE TABLE IF NOT EXISTS customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    phone VARCHAR(15)
);

CREATE TABLE IF NOT EXISTS rooms (
    room_id INT AUTO_INCREMENT PRIMARY KEY,
    room_number VARCHAR(10) UNIQUE,
    type VARCHAR(50),
    is_booked BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS reservations (
    reservation_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    room_id INT,
    check_in DATE,
    check_out DATE,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (room_id) REFERENCES rooms(room_id)
);
