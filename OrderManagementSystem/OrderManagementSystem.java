import java.util.*;

// Enum to represent user roles
enum UserRole {
    CUSTOMER, ADMIN, SUPPORT
}

// Enum to represent order status
enum OrderStatus {
    PLACED, ON_THE_WAY, DELIVERED
}

// Enum to represent feedback
enum Feedback {
    GOOD, AVERAGE, POOR
}

// Class to represent a user
class User {
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private UserRole role;

    public User(String firstName, String lastName, String mobileNumber, UserRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.role = role;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public UserRole getRole() {
        return role;
    }
}

// Class to manage orders
class OrderManager {
    private List<Order> orders;

    public OrderManager() {
        this.orders = new ArrayList<>();
    }

    public void placeOrder(User user, List<String> products, String location) {
        int orderId = generateOrderId();
        Order order = new Order(orderId, user, products, location);
        orders.add(order);
        System.out.println("Order placed successfully.");
        // Generate random product ID for the order and ask for feedback
        int productId = generateProductId();
        System.out.println("Selected product ID: " + productId);
        System.out.println("Your order ID: " + orderId);
       // FeedbackHandler feedbackHandler = new FeedbackHandler(productId);
        //feedbackHandler.askForFeedback();

        // Ask for delivery status
        System.out.println("Was your order delivered? (yes/no)");
        Scanner scanner = new Scanner(System.in);
        String deliveryStatus = scanner.next();
        if (deliveryStatus.equalsIgnoreCase("yes")) {
            System.out.println("Thank you for confirming delivery.");
            updateOrderStatus(orderId, OrderStatus.DELIVERED);
        } else if (deliveryStatus.equalsIgnoreCase("no")) {
            System.out.println("We are sorry to hear that your order is not delivered.");
            System.out.println("Please wait for 5 minutes. Your order is on the way.");
            // Simulate 5-minute delay
            //try {
            //    Thread.sleep(300000); // 5 minutes = 5 * 60 * 1000 milliseconds
            //} catch (InterruptedException e) {
              //  e.printStackTrace();
            //}
            // Update order status to reflect that the order is still on the way
            updateOrderStatus(orderId, OrderStatus.ON_THE_WAY);
	

        } else {
            System.out.println("Invalid input for delivery status. Exiting...");
        }
        FeedbackHandler feedbackHandler = new FeedbackHandler(productId);
        feedbackHandler.askForFeedback();
    }

    public void updateOrderStatus(int orderId, OrderStatus status) {
        for (Order order : orders) {
            if (order.getOrderId() == orderId) {
                order.setStatus(status);
                System.out.println("Order status updated successfully.");
                return;
            }
        }
        System.out.println("Order not found.");
    }

    // Helper method to generate a random order ID
    private int generateOrderId() {
        return new Random().nextInt(1000) + 1000;
    }

    // Helper method to generate a random product ID
    private int generateProductId() {
        return new Random().nextInt(1000); // Example: Random product ID
    }
}

// Class to represent an order
class Order {
    private int orderId;
    private User user;
    private List<String> products;
    private String location;
    private OrderStatus status;

    public Order(int orderId, User user, List<String> products, String location) {
        this.orderId = orderId;
        this.user = user;
        this.products = products;
        this.location = location;
        this.status = OrderStatus.PLACED;
    }

    public int getOrderId() {
        return orderId;
    }

    public User getUser() {
        return user;
    }

    public List<String> getProducts() {
        return products;
    }

    public String getLocation() {
        return location;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}

// Class to handle feedback
class FeedbackHandler {
    private int productId;

    public FeedbackHandler(int productId) {
        this.productId = productId;
    }

    public void askForFeedback() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please provide feedback for product with ID " + productId + ":");
        System.out.println("1. Good");
        System.out.println("2. Average");
        System.out.println("3. Poor");
        int feedbackChoice = scanner.nextInt();
        Feedback feedback;
        switch (feedbackChoice) {
            case 1:
                feedback = Feedback.GOOD;
                break;
            case 2:
                feedback = Feedback.AVERAGE;
                break;
            case 3:
                feedback = Feedback.POOR;
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        System.out.println("Thank you for your feedback.");
    }
}

class OrderManagementSystem {
    private static Map<String, User> users = new HashMap<>();
    private static OrderManager orderManager = new OrderManager();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Simulated database initialization with some sample users
        initializeDatabase();

        // Welcome message
        System.out.println("Welcome to the Order Management System!");

        // Select user role
        System.out.println("Select your role:");
        System.out.println("1. Customer");
        System.out.println("2. Admin");
        System.out.println("3. Support");
        int roleChoice = scanner.nextInt();

        UserRole role;
        switch (roleChoice) {
            case 1:
                role = UserRole.CUSTOMER;
                handleCustomer();
                break;
            case 2:
                role = UserRole.ADMIN;
                handleAdmin();
                break;
            case 3:
                role = UserRole.SUPPORT;
                System.out.println("GO TO ORDER PAGE PLEASE");
                break;
            default:
                System.out.println("Invalid choice. Exiting...");
                return;
        }
    }

    // Method to handle customer actions
    private static void handleCustomer() {
        Scanner scanner = new Scanner(System.in);

        // Login or register
        System.out.println("Enter your mobile number (10 digits): ");
        String mobileNumber = scanner.next();
        if (!isValidMobileNumber(mobileNumber)) {
            System.out.println("Invalid mobile number. Exiting...");
            return;
        }

        // Check if the user exists in the database
        User user = users.get(mobileNumber);
        if (user == null) {
            // Register a new user
            System.out.println("User not found. Please register.");
            System.out.println("Enter your first name: ");
            String firstName = scanner.next();
            System.out.println("Enter your last name: ");
            String lastName = scanner.next();
            // Simulate OTP generation and verification
            String otp = String.valueOf(new Random().nextInt(9999));
            System.out.println("An OTP has been sent to your mobile number: " + otp);
            System.out.println("Enter the OTP:");
            String enteredOtp = scanner.next();
            if (!enteredOtp.equals(otp)) {
                System.out.println("OTP verification failed. Exiting...");
                return;
            }
            user = new User(firstName, lastName, mobileNumber, UserRole.CUSTOMER);
            users.put(mobileNumber, user);
        }

        // Product selection
        System.out.println("Select a product:");
        System.out.println("1. Biryani");
        System.out.println("2. Tiffine");
        System.out.println("3. Roti");
        System.out.println("4. Milkshake");
        System.out.println("5. Softdrink");
        int productChoice = scanner.nextInt();
        List<String> products = new ArrayList<>();
        switch (productChoice) {
            case 1:
                products.add("Biryani");
                break;
            case 2:
                products.add("Tiffine");
                break;
            case 3:
                products.add("Roti");
                break;
            case 4:
                products.add("Milkshake");
                break;
            case 5:
                products.add("Softdrink");
                break;
            default:
                System.out.println("Invalid choice. Exiting...");
                return;
        }
        System.out.println("Enter your location to place the order: ");
        String location = scanner.next();
        orderManager.placeOrder(user, products, location);
    }

    // Method to handle admin actions
    private static void handleAdmin() {
        Scanner scanner = new Scanner(System.in);

        // Login with predefined mobile number for admin
        String adminMobileNumber = "9676034688";
        System.out.println("Enter your mobile number: ");
        String enteredMobileNumber = scanner.next();
        if (!enteredMobileNumber.equals(adminMobileNumber)) {
            System.out.println("Invalid mobile number for admin. Exiting...");
            return;
        }

        // Simulate OTP generation and verification
        String otp = String.valueOf(new Random().nextInt(9999));
        System.out.println("An OTP has been sent to your mobile number: " + otp);
        System.out.println("Enter the OTP:");
        String enteredOtp = scanner.next();
        if (!enteredOtp.equals(otp)) {
            System.out.println("OTP verification failed. Exiting...");
            return;
        }

        // List available items
        System.out.println("Available items:");
        System.out.println("1. Biryani");
        System.out.println("2. Tiffine");
        System.out.println("3. Roti");
        System.out.println("4. Milkshake");
        System.out.println("5. Softdrink");
    }

    // Helper method to validate mobile number format
    private static boolean isValidMobileNumber(String mobileNumber) {
        return mobileNumber.matches("\\d{10}");
    }

    // Simulated database initialization
    private static void initializeDatabase() {
        // Sample users
        users.put("1234567890", new User("John", "Doe", "1234567890", UserRole.CUSTOMER));
        users.put("9876543210", new User("Jane", "Doe", "9876543210", UserRole.ADMIN));
    }
}
