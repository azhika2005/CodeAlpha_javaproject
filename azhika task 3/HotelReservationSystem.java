import java.io.*;
import java.util.*;
import java.util.Scanner.*;

enum RoomType {
    STANDARD, DELUXE, SUITE
}

class Room {
    int roomNumber;
    RoomType type;
    boolean isAvailable;

    public Room(int roomNumber, RoomType type) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.isAvailable = true;
    }

    @Override
    public String toString() {
        return "Room " + roomNumber + " (" + type + ") - " + (isAvailable ? "Available" : "Booked");
    }
}

class Reservation {
    String guestName;
    int roomNumber;
    RoomType roomType;
    double price;
    String status;

    public Reservation(String guestName, int roomNumber, RoomType roomType, double price, String status) {
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.price = price;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Guest: " + guestName + ", Room: " + roomNumber + ", Type: " + roomType +
                ", Price: $" + price + ", Status: " + status;
    }
}

class Hotel {
    List<Room> rooms = new ArrayList<>();
    List<Reservation> reservations = new ArrayList<>();

    public Hotel() {
        // Initialize some rooms
        for (int i = 101; i <= 110; i++) {
            if (i <= 104)
                rooms.add(new Room(i, RoomType.STANDARD));
            else if (i <= 107)
                rooms.add(new Room(i, RoomType.DELUXE));
            else
                rooms.add(new Room(i, RoomType.SUITE));
        }
        loadReservationsFromFile("reservations.txt");
    }

    public void searchAvailableRooms(RoomType type) {
        System.out.println("--- Available Rooms (" + type + ") ---");
        for (Room room : rooms) {
            if (room.type == type && room.isAvailable) {
                System.out.println(room);
            }
        }
    }

    public void makeReservation(String guestName, RoomType type) {
        for (Room room : rooms) {
            if (room.type == type && room.isAvailable) {
                double price = getPrice(type);
                System.out.println("Room " + room.roomNumber + " is available for $" + price + " per night.");
                System.out.print("Do you want to confirm booking? (yes/no): ");
                Scanner sc = new Scanner(System.in);
                String confirm = sc.nextLine();
                if (confirm.equalsIgnoreCase("yes")) {
                    room.isAvailable = false;
                    Reservation res = new Reservation(guestName, room.roomNumber, type, price, "Confirmed");
                    reservations.add(res);
                    saveReservationsToFile("reservations.txt");
                    System.out.println("Booking confirmed!");
                } else {
                    System.out.println("Booking cancelled.");
                }
                return;
            }
        }
        System.out.println("No available rooms of type " + type + ".");
    }

    public void cancelReservation(String guestName) {
        for (Reservation res : reservations) {
            if (res.guestName.equalsIgnoreCase(guestName) && res.status.equals("Confirmed")) {
                res.status = "Cancelled";
                for (Room room : rooms) {
                    if (room.roomNumber == res.roomNumber) {
                        room.isAvailable = true;
                    }
                }
                saveReservationsToFile("reservations.txt");
                System.out.println("Reservation cancelled for " + guestName);
                return;
            }
        }
        System.out.println("No confirmed reservation found for " + guestName);
    }

    public void viewReservations() {
        System.out.println("--- All Reservations ---");
        for (Reservation res : reservations) {
            System.out.println(res);
        }
    }

    private double getPrice(RoomType type) {
        switch (type) {
            case STANDARD: return 100;
            case DELUXE: return 150;
            case SUITE: return 250;
            default: return 0;
        }
    }

    private void saveReservationsToFile(String filename) {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
            for (Reservation res : reservations) {
                out.println(res.guestName + "," + res.roomNumber + "," + res.roomType + "," +
                        res.price + "," + res.status);
            }
        } catch (IOException e) {
            System.out.println("Error saving reservations: " + e.getMessage());
        }
    }

    private void loadReservationsFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) return;

        try (Scanner in = new Scanner(file)) {
            while (in.hasNextLine()) {
                String[] data = in.nextLine().split(",");
                if (data.length == 5) {
                    String name = data[0];
                    int roomNumber = Integer.parseInt(data[1]);
                    RoomType type = RoomType.valueOf(data[2]);
                    double price = Double.parseDouble(data[3]);
                    String status = data[4];
                    reservations.add(new Reservation(name, roomNumber, type, price, status));
                    if (status.equals("Confirmed")) {
                        for (Room room : rooms) {
                            if (room.roomNumber == roomNumber) {
                                room.isAvailable = false;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading reservations: " + e.getMessage());
        }
    }
}
public class HotelReservationSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Hotel hotel = new Hotel();

        while (true) {
            System.out.println("\n=== Hotel Reservation System ===");
            System.out.println("1. Search Available Rooms");
            System.out.println("2. Make Reservation");
            System.out.println("3. Cancel Reservation");
            System.out.println("4. View All Reservations");
            System.out.println("5. Exit");
            System.out.print("Choose option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // clear buffer

            switch (option) {
                case 1:
                    System.out.print("Enter room type (STANDARD, DELUXE, SUITE): ");
                    RoomType type = RoomType.valueOf(scanner.nextLine().toUpperCase());
                    hotel.searchAvailableRooms(type);
                    break;
                case 2:
                    System.out.print("Enter your name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter room type (STANDARD, DELUXE, SUITE): ");
                    RoomType resType = RoomType.valueOf(scanner.nextLine().toUpperCase());
                    hotel.makeReservation(name, resType);
                    break;
                case 3:
                    System.out.print("Enter your name to cancel: ");
                    String cancelName = scanner.nextLine();
                    hotel.cancelReservation(cancelName);
                    break;
                case 4:
                    hotel.viewReservations();
                    break;
                case 5:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
