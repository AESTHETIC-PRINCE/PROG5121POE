import java.util.Scanner;

/**
 * App.java – Entry point for the Registration and Login application.
 * Run this class to interact with the system via the console.
 */
public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Welcome to the Registration System ===\n");

        // ── Collect registration details ─────────────────────────────────────
        System.out.print("Enter your first name    : ");
        String firstName = scanner.nextLine();

        System.out.print("Enter your last name     : ");
        String lastName = scanner.nextLine();

        System.out.print("Enter a username         : ");
        String username = scanner.nextLine();

        System.out.print("Enter a password         : ");
        String password = scanner.nextLine();

        System.out.print("Enter cell phone number  : ");
        String cellNumber = scanner.nextLine();

        // ── Register ─────────────────────────────────────────────────────────
        Login user = new Login(firstName, lastName, username, password, cellNumber);
        String registrationResult = user.registerUser();
        System.out.println("\n--- Registration Result ---");
        System.out.println(registrationResult);

        // ── Only attempt login if registration succeeded ──────────────────────
        if (registrationResult.contains("successfully captured")) {
            System.out.println("\n=== Login ===");
            System.out.print("Enter your username : ");
            String loginUser = scanner.nextLine();

            System.out.print("Enter your password : ");
            String loginPass = scanner.nextLine();

            System.out.println("\n--- Login Result ---");
            System.out.println(user.returnLoginStatus(loginUser, loginPass));
        }

        scanner.close();
    }
}

