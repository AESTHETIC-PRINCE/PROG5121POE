package prog5121;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Message class – PROG5121 POE Part 2
 *
 * Methods required by rubric:
 *  boolean checkMessageID()
 *  String  checkRecipientCell()
 *  String  createMessageHash()
 *  String  SentMessage()           <- interactive (console)
 *  String  SentMessage(int choice) <- for unit tests
 *  String  printMessages()         <- static
 *  int     returnTotalMessages()   <- static
 *  void    storeMessage()          <- writes to JSON
 */
public class Message {

    // ── Instance fields ───────────────────────────────────────────────────────
    private final String messageID;
    private final int    messageNumber;
    private final String recipient;
    private final String messageText;
    private final String messageHash;

    // ── Static session state ──────────────────────────────────────────────────
    private static int totalMessagesSent = 0;
    private static final ArrayList<Message> sentMessages   = new ArrayList<>();
    private static final ArrayList<Message> storedMessages = new ArrayList<>();

    // ── Constructor ───────────────────────────────────────────────────────────
    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient     = recipient;
        this.messageText   = messageText;
        this.messageID     = generateMessageID();
        this.messageHash   = createMessageHash();
    }

    // ── Private helper ────────────────────────────────────────────────────────
    private String generateMessageID() {
        Random rand  = new Random();
        long   value = (long) (rand.nextDouble() * 10_000_000_000L);
        return String.format("%010d", value);
    }

    // ── Required methods ──────────────────────────────────────────────────────

    /**
     * Checks the message ID is no more than 10 characters.
     */
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    /**
     * Checks recipient cell number:
     * - Must start with '+' (international code)
     * - Must be no more than 10 characters
     */
    public String checkRecipientCell() {
    if (recipient == null || recipient.trim().isEmpty()) {
        return "Cell phone number is incorrectly formatted or does not contain "
             + "an international code. Please correct the number and try again.";
    }
    if (!recipient.startsWith("+")) {
        return "Cell phone number is incorrectly formatted or does not contain "
             + "an international code. Please correct the number and try again.";
    }
    // Strip the '+' and country code (first 3 chars e.g. +27)
    // then check the remaining local number is no more than 10 digits
    String localNumber = recipient.substring(1).replaceAll("[^0-9]", "");
    if (localNumber.length() < 10 || localNumber.length() > 12) {
        return "Cell phone number is incorrectly formatted or does not contain "
             + "an international code. Please correct the number and try again.";
    }
    return "Cell phone number successfully captured.";
}
    /**
     * Creates and returns the Message Hash (ALL CAPS).
     * Format: [first 2 of ID]:[messageNumber]:[FIRSTWORD][LASTWORD]
     * Example: 00:1:HITONIGHT
     */
    public String createMessageHash() {
        if (messageText == null || messageText.trim().isEmpty()) return "";

        String[] words    = messageText.trim().split("\\s+");
        String firstWord  = words[0].replaceAll("[^a-zA-Z]", "");
        String lastWord   = words[words.length - 1].replaceAll("[^a-zA-Z]", "");
        String idPrefix   = messageID.substring(0, 2);

        return (idPrefix + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    /**
     * Interactive version – reads choice from console.
     * 1 = Send, 2 = Disregard, 3 = Store
     */
    public String SentMessage() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nWhat would you like to do with this message?");
        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");
        System.out.print("Enter choice: ");

        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            choice = 2;
        }
        return SentMessage(choice);
    }

    /**
     * Non-interactive overload used by unit tests.
     * 1 = Send, 2 = Disregard, 3 = Store
     */
    public String SentMessage(int choice) {
        switch (choice) {
            case 1:
                sentMessages.add(this);
                totalMessagesSent++;
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                storedMessages.add(this);
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option. Please choose 1, 2, or 3.";
        }
    }

    /**
     * Returns all sent messages as a formatted string.
     * Order: Message ID, Message Hash, Recipient, Message
     */
    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages have been sent yet.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\n========== SENT MESSAGES ==========\n");
        for (Message m : sentMessages) {
            sb.append("Message ID   : ").append(m.messageID).append("\n");
            sb.append("Message Hash : ").append(m.messageHash).append("\n");
            sb.append("Recipient    : ").append(m.recipient).append("\n");
            sb.append("Message      : ").append(m.messageText).append("\n");
            sb.append("───────────────────────────────────\n");
        }
        return sb.toString();
    }

    /**
     * Returns total number of messages sent this session.
     */
    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    /**
     * Stores message to messages.json (research task).
     */
    public void storeMessage() {
        String json = "{\n"
            + "  \"messageID\"     : \"" + messageID + "\",\n"
            + "  \"messageNumber\" : "   + messageNumber + ",\n"
            + "  \"recipient\"     : \"" + recipient + "\",\n"
            + "  \"message\"       : \"" + messageText.replace("\"", "\\\"") + "\",\n"
            + "  \"messageHash\"   : \"" + messageHash + "\"\n"
            + "}";

        try (FileWriter fw = new FileWriter("messages.json", true)) {
            fw.write(json + ",\n");
        } catch (IOException e) {
            System.out.println("Error saving to JSON: " + e.getMessage());
        }
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public String getMessageID()     { return messageID;     }
    public int    getMessageNumber() { return messageNumber; }
    public String getRecipient()     { return recipient;     }
    public String getMessageText()   { return messageText;   }
    public String getMessageHash()   { return messageHash;   }

    public static ArrayList<Message> getSentMessages()   { return sentMessages;   }
    public static ArrayList<Message> getStoredMessages() { return storedMessages; }

    /** Reset static state between unit tests. */
    public static void resetMessages() {
        sentMessages.clear();
        storedMessages.clear();
        totalMessagesSent = 0;
    }
}
