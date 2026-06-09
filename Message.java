package prog5121poe;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Message class – PROG5121 POE Part 1, 2 & 3
 *
 * Part 3 additions:
 *  - disregardedMessages array
 *  - messageHashes array
 *  - messageIDs array
 *  - loadStoredMessages()    – reads JSON file into storedMessages array
 *  - getLongestMessage()     – finds and returns the longest message
 *  - searchByMessageID()     – search by ID, return recipient + message
 *  - searchByRecipient()     – find all messages for a recipient
 *  - deleteByHash()          – delete a message using its hash
 *  - displayReport()         – full report of all sent messages
 */
public class Message {

    // ── Instance fields ───────────────────────────────────────────────────────
    private String messageID;
    private final int    messageNumber;
    private final String recipient;
    private final String messageText;
    private String messageHash;

    // ── Static arrays (populated at runtime, no hard-coding) ─────────────────
    private static int totalMessagesSent = 0;
    private static final ArrayList<Message> sentMessages        = new ArrayList<>();
    private static final ArrayList<Message> disregardedMessages = new ArrayList<>();
    private static final ArrayList<Message> storedMessages      = new ArrayList<>();
    private static final ArrayList<String>  messageHashes       = new ArrayList<>();
    private static final ArrayList<String>  messageIDs          = new ArrayList<>();

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
     * Validates recipient cell number – must start with '+' (international code).
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
        String[] words   = messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^a-zA-Z]", "");
        String lastWord  = words[words.length - 1].replaceAll("[^a-zA-Z]", "");
        String idPrefix  = messageID.substring(0, 2);
        return (idPrefix + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    /**
     * Interactive version – prompts user to Send, Disregard, or Store.
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
     * Non-interactive overload for unit tests.
     * 1 = Send, 2 = Disregard, 3 = Store
     */
    public String SentMessage(int choice) {
        switch (choice) {
            case 1:
                sentMessages.add(this);
                messageHashes.add(this.messageHash);
                messageIDs.add(this.messageID);
                totalMessagesSent++;
                return "Message successfully sent.";
            case 2:
                disregardedMessages.add(this);
                return "Press 0 to delete the message.";
            case 3:
                storedMessages.add(this);
                messageHashes.add(this.messageHash);
                messageIDs.add(this.messageID);
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option. Please choose 1, 2, or 3.";
        }
    }

    /**
     * Returns all sent messages formatted (ID, Hash, Recipient, Message).
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
     * Stores message to messages.json (research task / Part 2).
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

    // ── Part 3 Methods ────────────────────────────────────────────────────────

    /**
     * Reads messages.json and loads them into the storedMessages array.
     */
    public static void loadStoredMessages() {
        try (BufferedReader br = new BufferedReader(new FileReader("messages.json"))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line.trim());
            }

            // Simple JSON parsing - split by "}," to get individual message blocks
            String[] blocks = content.toString().split("\\},");
            for (String block : blocks) {
                block = block.replace("{", "").replace("}", "").trim();
                if (block.isEmpty()) continue;

                String id        = extractJsonValue(block, "messageID");
                String numStr    = extractJsonValue(block, "messageNumber");
                String recipient = extractJsonValue(block, "recipient");
                String message   = extractJsonValue(block, "message");
                String hash      = extractJsonValue(block, "messageHash");

                if (message != null && recipient != null) {
                    int num = 0;
                    try { num = Integer.parseInt(numStr); } catch (Exception e) {}
                    Message m = new Message(num, recipient, message);
                    m.messageID   = id != null ? id : m.messageID;
                    m.messageHash = hash != null ? hash : m.messageHash;
                    storedMessages.add(m);
                }
            }
            System.out.println("Stored messages loaded: " + storedMessages.size());
        } catch (IOException e) {
            System.out.println("No stored messages file found.");
        }
    }

    /** Helper to extract a value from a simple JSON string. */
    private static String extractJsonValue(String block, String key) {
        String search = "\"" + key + "\"";
        int idx = block.indexOf(search);
        if (idx == -1) return null;
        int colon = block.indexOf(":", idx + search.length());
        if (colon == -1) return null;
        String rest = block.substring(colon + 1).trim();
        if (rest.startsWith("\"")) {
            int end = rest.indexOf("\"", 1);
            return end == -1 ? null : rest.substring(1, end);
        } else {
            // numeric
            int end = rest.indexOf(",");
            return end == -1 ? rest.trim() : rest.substring(0, end).trim();
        }
    }

    /**
     * Finds and returns the longest message from all messages (sent + stored).
     */
    public static String getLongestMessage() {
        ArrayList<Message> all = new ArrayList<>();
        all.addAll(sentMessages);
        all.addAll(storedMessages);

        if (all.isEmpty()) return "No messages available.";

        Message longest = all.get(0);
        for (Message m : all) {
            if (m.messageText.length() > longest.messageText.length()) {
                longest = m;
            }
        }
        return "Longest message (" + longest.messageText.length() + " chars):\n"
             + "  Recipient : " + longest.recipient + "\n"
             + "  Message   : " + longest.messageText;
    }

    /**
     * Searches all messages by ID and returns recipient + message.
     */
    public static String searchByMessageID(String id) {
        ArrayList<Message> all = new ArrayList<>();
        all.addAll(sentMessages);
        all.addAll(storedMessages);

        for (Message m : all) {
            if (m.messageID.equals(id)) {
                return "Recipient : " + m.recipient + "\nMessage   : " + m.messageText;
            }
        }
        return "Message ID not found.";
    }

    /**
     * Returns all messages stored for a particular recipient.
     */
    public static String searchByRecipient(String recipientSearch) {
        ArrayList<Message> all = new ArrayList<>();
        all.addAll(sentMessages);
        all.addAll(storedMessages);

        StringBuilder sb = new StringBuilder();
        boolean found = false;
        for (Message m : all) {
            if (m.recipient.equals(recipientSearch)) {
                sb.append("Message ID   : ").append(m.messageID).append("\n");
                sb.append("Message Hash : ").append(m.messageHash).append("\n");
                sb.append("Message      : ").append(m.messageText).append("\n");
                sb.append("───────────────────────────────────\n");
                found = true;
            }
        }
        return found ? sb.toString() : "No messages found for recipient: " + recipientSearch;
    }

    /**
     * Deletes a message from all arrays using its hash.
     * Returns a confirmation message.
     */
    public static String deleteByHash(String hash) {
        // Search sent messages
        for (int i = 0; i < sentMessages.size(); i++) {
            if (sentMessages.get(i).messageHash.equalsIgnoreCase(hash)) {
                String text = sentMessages.get(i).messageText;
                sentMessages.remove(i);
                messageHashes.remove(hash.toUpperCase());
                return "\"" + text + "\" successfully deleted.";
            }
        }
        // Search stored messages
        for (int i = 0; i < storedMessages.size(); i++) {
            if (storedMessages.get(i).messageHash.equalsIgnoreCase(hash)) {
                String text = storedMessages.get(i).messageText;
                storedMessages.remove(i);
                messageHashes.remove(hash.toUpperCase());
                return "\"" + text + "\" successfully deleted.";
            }
        }
        return "Hash not found. No message deleted.";
    }

    /**
     * Displays a full report of all sent messages (Hash, Recipient, Message).
     */
    public static String displayReport() {
        if (sentMessages.isEmpty() && storedMessages.isEmpty()) {
            return "No messages to display.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\n======= MESSAGE REPORT =======\n");

        ArrayList<Message> all = new ArrayList<>();
        all.addAll(sentMessages);
        all.addAll(storedMessages);

        for (Message m : all) {
            sb.append("Message Hash : ").append(m.messageHash).append("\n");
            sb.append("Recipient    : ").append(m.recipient).append("\n");
            sb.append("Message      : ").append(m.messageText).append("\n");
            sb.append("──────────────────────────────\n");
        }
        return sb.toString();
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public String getMessageID()     { return messageID;     }
    public int    getMessageNumber() { return messageNumber; }
    public String getRecipient()     { return recipient;     }
    public String getMessageText()   { return messageText;   }
    public String getMessageHash()   { return messageHash;   }

    public static ArrayList<Message> getSentMessages()        { return sentMessages;        }
    public static ArrayList<Message> getDisregardedMessages() { return disregardedMessages; }
    public static ArrayList<Message> getStoredMessages()      { return storedMessages;      }
    public static ArrayList<String>  getMessageHashes()       { return messageHashes;       }
    public static ArrayList<String>  getMessageIDs()          { return messageIDs;          }

    /** Resets all static state between unit tests. */
    public static void resetMessages() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
        totalMessagesSent = 0;
    }
}
