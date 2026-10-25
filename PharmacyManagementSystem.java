import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PharmacyManagementSystem {

    static Scanner sc = new Scanner(System.in);
    static HashMap<Integer, Medicine> medicines = new HashMap<>();
    static ArrayList<String> salesRecords = new ArrayList<>();

    static class Medicine {
        int id;
        String name;
        String category;
        double price;
        int quantity;
        String expiryDate;

        Medicine(int id, String name, String category,
                 double price, int quantity, String expiryDate) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.price = price;
            this.quantity = quantity;
            this.expiryDate = expiryDate;
        }

        void display() {
            System.out.printf(
                "%-8d %-20s %-15s Rs.%-10.2f %-10d %-12s%n",
                id, name, category, price, quantity, expiryDate
            );
        }
    }

    public static void main(String[] args) {
        medicines.put(101, new Medicine(
            101, "Paracetamol", "Tablet", 20.00, 100, "12/2027"));
        medicines.put(102, new Medicine(
            102, "Amoxicillin", "Capsule", 60.00, 50, "08/2027"));
        medicines.put(103, new Medicine(
            103, "Cetirizine", "Tablet", 15.00, 30, "10/2027"));

        int choice;

        do {
            System.out.println("\\n==================================");
            System.out.println(" PHARMACY MANAGEMENT SYSTEM");
            System.out.println("==================================");
            System.out.println("1. Add Medicine");
            System.out.println("2. View All Medicines");
            System.out.println("3. Search Medicine");
            System.out.println("4. Update Medicine Stock");
            System.out.println("5. Generate Bill");
            System.out.println("6. View Low-Stock Medicines");
            System.out.println("7. View Sales Records");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            choice = readInt();

            switch (choice) {
                case 1: addMedicine(); break;
                case 2: viewMedicines(); break;
                case 3: searchMedicine(); break;
                case 4: updateStock(); break;
                case 5: generateBill(); break;
                case 6: viewLowStock(); break;
                case 7: viewSalesRecords(); break;
                case 8:
                    System.out.println("Thank you for using the system!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 8);

        sc.close();
    }

    static int readInt() {
        while (!sc.hasNextInt()) {
            System.out.print("Enter a valid whole number: ");
            sc.next();
        }
        int value = sc.nextInt();
        sc.nextLine();
        return value;
    }

    static double readDouble() {
        while (!sc.hasNextDouble()) {
            System.out.print("Enter a valid number: ");
            sc.next();
        }
        double value = sc.nextDouble();
        sc.nextLine();
        return value;
    }

    static void addMedicine() {
        System.out.print("Enter medicine ID: ");
        int id = readInt();

        if (medicines.containsKey(id)) {
            System.out.println("Medicine ID already exists.");
            return;
        }

        System.out.print("Enter medicine name: ");
        String name = sc.nextLine().trim();

        System.out.print("Enter category: ");
        String category = sc.nextLine().trim();

        System.out.print("Enter price: ");
        double price = readDouble();

        System.out.print("Enter quantity: ");
        int quantity = readInt();

        System.out.print("Enter expiry date (MM/YYYY): ");
        String expiry = sc.nextLine().trim();

        if (name.isEmpty() || category.isEmpty() || expiry.isEmpty()
                || price < 0 || quantity < 0) {
            System.out.println("Invalid details. Medicine not added.");
            return;
        }

        medicines.put(id, new Medicine(
            id, name, category, price, quantity, expiry));
        System.out.println("Medicine added successfully.");
    }

    static void viewMedicines() {
        if (medicines.isEmpty()) {
            System.out.println("No medicines available.");
            return;
        }
        printHeader();
        for (Medicine m : medicines.values()) {
            m.display();
        }
    }

    static void printHeader() {
        System.out.printf("%-8s %-20s %-15s %-13s %-10s %-12s%n",
                "ID", "Name", "Category", "Price", "Quantity", "Expiry");
        System.out.println(
                "--------------------------------------------------------------------------");
    }

    static void searchMedicine() {
        System.out.print("Enter medicine ID: ");
        int id = readInt();
        Medicine m = medicines.get(id);

        if (m != null) {
            printHeader();
            m.display();
        } else {
            System.out.println("Medicine not found.");
        }
    }

    static void updateStock() {
        System.out.print("Enter medicine ID: ");
        int id = readInt();
        Medicine m = medicines.get(id);

        if (m == null) {
            System.out.println("Medicine not found.");
            return;
        }

        System.out.println("Current stock: " + m.quantity);
        System.out.println("1. Add stock");
        System.out.println("2. Set stock quantity");
        System.out.print("Choose an option: ");
        int option = readInt();

        if (option == 1) {
            System.out.print("Enter quantity to add: ");
            int amount = readInt();
            if (amount <= 0) {
                System.out.println("Quantity must be positive.");
                return;
            }
            m.quantity += amount;
            System.out.println("Stock increased successfully.");
        } else if (option == 2) {
            System.out.print("Enter new stock quantity: ");
            int amount = readInt();
            if (amount < 0) {
                System.out.println("Quantity cannot be negative.");
                return;
            }
            m.quantity = amount;
            System.out.println("Stock updated successfully.");
        } else {
            System.out.println("Invalid option.");
            return;
        }
        System.out.println("Updated stock: " + m.quantity);
    }

    static void generateBill() {
        HashMap<Integer, Integer> cart = new HashMap<>();
        System.out.println("\\nEnter medicine IDs and quantities.");
        System.out.println("Enter ID 0 to finish.");

        while (true) {
            System.out.print("Medicine ID: ");
            int id = readInt();
            if (id == 0) break;

            Medicine m = medicines.get(id);
            if (m == null) {
                System.out.println("Medicine not found.");
                continue;
            }

            System.out.println("Medicine: " + m.name);
            System.out.println("Available stock: " + m.quantity);
            System.out.print("Enter quantity: ");
            int quantity = readInt();

            if (quantity <= 0) {
                System.out.println("Quantity must be positive.");
                continue;
            }

            int requested = cart.getOrDefault(id, 0) + quantity;
            if (requested > m.quantity) {
                System.out.println("Insufficient stock. Item not added.");
                continue;
            }

            cart.put(id, requested);
            System.out.println("Item added to bill.");
        }

        if (cart.isEmpty()) {
            System.out.println("No items selected. Bill cancelled.");
            return;
        }

        double total = 0;
        System.out.println("\\n========== PHARMACY BILL ==========");
        System.out.printf("%-20s %-8s %-10s %-10s%n",
                "Medicine", "Qty", "Price", "Amount");
        System.out.println("-----------------------------------");

        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            Medicine m = medicines.get(entry.getKey());
            int quantity = entry.getValue();
            double amount = m.price * quantity;
            System.out.printf("%-20s %-8d %-10.2f %-10.2f%n",
                    m.name, quantity, m.price, amount);
            total += amount;
        }

        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            Medicine m = medicines.get(entry.getKey());
            m.quantity -= entry.getValue();
        }

        System.out.println("-----------------------------------");
        System.out.printf("Total Amount: Rs. %.2f%n", total);
        System.out.println("Bill generated successfully.");
        System.out.println("===================================");

        salesRecords.add("Total: Rs. " + String.format("%.2f", total)
                + " | Items: " + cart.size());
    }

    static void viewLowStock() {
        boolean found = false;
        System.out.println("\\nMedicines with stock below 10:");
        printHeader();

        for (Medicine m : medicines.values()) {
            if (m.quantity < 10) {
                m.display();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No low-stock medicines found.");
        }
    }

    static void viewSalesRecords() {
        System.out.println("\\n========== SALES RECORDS ==========");
        if (salesRecords.isEmpty()) {
            System.out.println("No sales recorded.");
            return;
        }
        for (int i = 0; i < salesRecords.size(); i++) {
            System.out.println((i + 1) + ". " + salesRecords.get(i));
        }
    }
}
