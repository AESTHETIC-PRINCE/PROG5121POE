package prog5121;

import java.util.Scanner;

public class App {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("=========================================");
        System.out.println("        Welcome to QuickChat             ");
        System.out.println("=========================================\n");

        System.out.println("--- Registration ---\n");
        System.out.print("Enter your first name        : ");
        String firstName = scanner.nextLine();
        System.out.print("Enter your last name         : ");
        String lastName = scanner.nextLine();
        System.out.print("Enter a username             : ");
        String username = scanner.nextLine();
        System.out.print("Enter a password             : ");
        String password = scanner.nextLine();
        System.out.print("Enter your cell phone number : ");
        String cellPhoneNumber = scanner.nextLine();

        Login user = new Login(firstName, lastName, username, password, cellPhoneNumber);
        String regResult = user.registerUser();
        System.out.println("\n" + regResult + "\n");

        if (!regResult.contains("successfully")) {
            System.out.println("Registration failed. Please restart and try again.");
            return;
        }

        System.out.println("--- Login ---\n");
        boolean loggedIn = false;
        int attempts = 0;

        while (!loggedIn && attempts < 3) {
            System.out.print("Username : ");
            String loginUser = scanner.nextLine();
            System.out.print("Password : ");
            String loginPass = scanner.nextLine();
            loggedIn = user.loginUser(loginUser, loginPass);
            System.out.println(user.returnLoginStatus(loginUser, loginPass) + "\n");
            attempts++;
            if (!loggedIn && attempts < 3) {
                System.out.println("Please try again.\n");
            }
        }

        if (!loggedIn) {
            System.out.println("Too many failed attempts. Goodbye.");
            return;
        }

        System.out.println("Welcome to QuickChat.\n");

        int numMessages = 0;
        while (numMessages <= 0) {
            System.out.print("How many messages would you like to send? ");
            try {
                numMessages = Integer.parseInt(scanner.nextLine().trim());
                if (numMessages <= 0) System.out.println("Please enter a number greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }

        boolean running = true;
        int msgCount = 0;

        while (running) {
            System.out.println("\n-----------------------------------------");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    if (msgCount >= numMessages) {
                        System.out.println("You have already sent all " + numMessages + " message(s).");
                        break;
                    }
                    while (msgCount < numMessages) {
                        msgCount++;
                        System.out.println("\n--- Message " + msgCount + " of " + numMessages + " ---");
                        String recipient;
                        while (true) {
                            System.out.print("Recipient cell number (e.g. +27...): ");
                            recipient = scanner.nextLine().trim();
                            Message temp = new Message(msgCount, recipient, "temp");
                            String check = temp.checkRecipientCell();
                            if (check.equals("Cell phone number successfully captured.")) {
                                System.out.println(check);
                                break;
                            }
                            System.out.println(check);
                        }
                        String messageText;
                        while (true) {
                            System.out.print("Enter your message (max 250 characters): ");
                            messageText = scanner.nextLine();
                            if (messageText.length() <= 250) {
                                System.out.println("Message sent.");
                                break;
                            }
                            System.out.println("Message exceeds 250 characters by "
                                    + (messageText.length() - 250) + "; please reduce the size.");
                        }
                        Message msg = new Message(msgCount, recipient, messageText);
                        System.out.println("\nMessage Details:");
                        System.out.println("  Message ID   : " + msg.getMessageID());
                        System.out.println("  Message Hash : " + msg.getMessageHash());
                        System.out.println("  Recipient    : " + msg.getRecipient());
                        System.out.println("  Message      : " + msg.getMessageText());
                        System.out.println(msg.SentMessage());
                    }
                    break;
                case "2":
                    System.out.println("\nComing Soon.");
                    break;
                case "3":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please enter 1, 2, or 3.");
            }
        }

        System.out.println("\n=========================================");
        System.out.println("Total messages sent: " + Message.returnTotalMessages());
        System.out.println(Message.printMessages());
        System.out.println("Thank you for using QuickChat. Goodbye!");
    }
}
