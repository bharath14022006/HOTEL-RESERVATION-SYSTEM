import java.util.*;

class Room {
    int number;
    boolean isBooked;
    String guestName;

    public Room(int number) {
        this.number = number;
        this.isBooked = false;
        this.guestName = "";
    }
}

public class HotelReservationSystem {
    static Map<Integer, Room> rooms = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        initializeRooms();

        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Hotel Reservation Menu ---");
            System.out.println("1. Book a Room");
            System.out.println("2. View Reservations");
            System.out.println("3. Check Room Availability");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> bookRoom(scanner);
                    case 2 -> viewReservations();
                    case 3 -> checkAvailability();
                    case 4 -> {
                        System.out.println("Thank you for using the Hotel Reservation System!");
                        exit = true;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); 
            }
        }
        scanner.close();
    }

    static void initializeRooms() {
        for (int i = 101; i <= 110; i++) {
            rooms.put(i, new Room(i));
        }
    }

    static void bookRoom(Scanner scanner) {
        System.out.print("Enter room number to book: ");
        int number = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Room room = rooms.get(number);
        if (room == null) {
            System.out.println("Room number " + number + " does not exist.");
        } else if (room.isBooked) {
            System.out.println("Room " + number + " is already booked.");
        } else {
            System.out.print("Enter guest name: ");
            room.guestName = scanner.nextLine();
            room.isBooked = true;
            System.out.println("Room " + number + " successfully booked for " + room.guestName + ".");
        }
    }

    static void viewReservations() {
        System.out.println("\n--- Current Reservations ---");
        boolean hasReservations = false;
        for (Room room : rooms.values()) {
            if (room.isBooked) {
                System.out.println("Room " + room.number + " - " + room.guestName);
                hasReservations = true;
            }
        }
        if (!hasReservations) {
            System.out.println("No reservations found.");
        }
    }

    static void checkAvailability() {
        System.out.println("\n--- Available Rooms ---");
        boolean hasAvailability = false;
        for (Room room : rooms.values()) {
            if (!room.isBooked) {
                System.out.println("Room " + room.number);
                hasAvailability = true;
            }
        }
        if (!hasAvailability) {
            System.out.println("No rooms available.");
        }
    }
}
