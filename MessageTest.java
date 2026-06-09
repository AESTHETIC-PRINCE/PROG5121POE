package prog5121poe;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * MessageTest.java – Unit tests for Message class.
 * PROG5121 POE Part 2 & 3
 *
 * Part 3 Test Data (4 messages):
 *  Message 1: +27834557896  | "Did you get the cake?"                              | Sent
 *  Message 2: +27838884567  | "It is dinner time!"                                 | Sent
 *  Message 3: +27834557896  | "Where are you? You are late! I have asked you to be on time" | Sent
 *  Message 4: +27833232123  | "Yoh, are you coming?"                               | Stored
 */
public class MessageTest {

    // ── Part 2 test data ──────────────────────────────────────────────────────
    private static final String RECIPIENT_1    = "+27718693002";
    private static final String MESSAGE_TEXT_1 = "Hi Mike, can you join us for dinner tonight?";
    private static final String RECIPIENT_2    = "0857595889";
    private static final String MESSAGE_TEXT_2 = "Hi Keegan, did you receive the payment?";

    // ── Part 3 test data ──────────────────────────────────────────────────────
    private static final String P3_RECIPIENT_1 = "+27834557896";
    private static final String P3_MESSAGE_1   = "Did you get the cake?";

    private static final String P3_RECIPIENT_2 = "+27838884567";
    private static final String P3_MESSAGE_2   = "It is dinner time!";

    private static final String P3_RECIPIENT_3 = "+27834557896";
    private static final String P3_MESSAGE_3   = "Where are you? You are late! I have asked you to be on time";

    private static final String P3_RECIPIENT_4 = "+27833232123";
    private static final String P3_MESSAGE_4   = "Yoh, are you coming?";

    private Message message1;
    private Message message2;

    @Before
    public void setUp() {
        Message.resetMessages();
        message1 = new Message(1, RECIPIENT_1, MESSAGE_TEXT_1);
        message2 = new Message(2, RECIPIENT_2, MESSAGE_TEXT_2);
    }

    @After
    public void tearDown() {
        Message.resetMessages();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PART 2 TESTS
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    public void testMessageLength_WithinLimit_Success() {
        String result = (MESSAGE_TEXT_1.length() <= 250)
                ? "Message ready to send."
                : "Message exceeds 250 characters by "
                  + (MESSAGE_TEXT_1.length() - 250) + "; please reduce the size.";
        assertEquals("Message ready to send.", result);
    }

    @Test
    public void testMessageLength_ExceedsLimit_Failure() {
        String longMsg = "A".repeat(260);
        String result = (longMsg.length() <= 250)
                ? "Message ready to send."
                : "Message exceeds 250 characters by "
                  + (longMsg.length() - 250) + "; please reduce the size.";
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.", result);
    }

    @Test
    public void testRecipientCell_ValidInternationalCode_Success() {
        assertEquals("Cell phone number successfully captured.", message1.checkRecipientCell());
    }

    @Test
    public void testRecipientCell_NoInternationalCode_Failure() {
        assertEquals(
            "Cell phone number is incorrectly formatted or does not contain "
          + "an international code. Please correct the number and try again.",
            message2.checkRecipientCell());
    }

    @Test
    public void testMessageHash_EndsWithHITONIGHT() {
        assertTrue("Hash should end with HITONIGHT", message1.getMessageHash().endsWith("HITONIGHT"));
    }

    @Test
    public void testMessageHash_IsAllUppercase() {
        String hash = message1.getMessageHash();
        assertEquals(hash, hash.toUpperCase());
    }

    @Test
    public void testMessageID_IsExactlyTenDigits() {
        assertEquals(10, message1.getMessageID().length());
    }

    @Test
    public void testCheckMessageID_IsValid() {
        assertTrue(message1.checkMessageID());
    }

    @Test
    public void testSentMessage_Send() {
        assertEquals("Message successfully sent.", message1.SentMessage(1));
    }

    @Test
    public void testSentMessage_Disregard() {
        assertEquals("Press 0 to delete the message.", message2.SentMessage(2));
    }

    @Test
    public void testSentMessage_Store() {
        Message m = new Message(3, "+27831234567", "Test store");
        assertEquals("Message successfully stored.", m.SentMessage(3));
    }

    @Test
    public void testReturnTotalMessages_OneSentOneDiscarded() {
        message1.SentMessage(1);
        message2.SentMessage(2);
        assertEquals(1, Message.returnTotalMessages());
    }

    @Test
    public void testAllMessageHashes_InLoop_AreUppercase() {
        Message[] messages = {message1, message2};
        for (Message m : messages) {
            String hash = m.getMessageHash();
            assertNotNull(hash);
            assertFalse(hash.isEmpty());
            assertEquals(hash, hash.toUpperCase());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PART 3 TESTS
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Test: Sent Messages array correctly populated.
     * System should return "Did you get the cake?", "It is dinner time!"
     */
    @Test
    public void testSentMessagesArray_CorrectlyPopulated() {
        Message m1 = new Message(1, P3_RECIPIENT_1, P3_MESSAGE_1);
        Message m2 = new Message(2, P3_RECIPIENT_2, P3_MESSAGE_2);
        Message m3 = new Message(3, P3_RECIPIENT_3, P3_MESSAGE_3);
        Message m4 = new Message(4, P3_RECIPIENT_4, P3_MESSAGE_4);

        m1.SentMessage(1);
        m2.SentMessage(1);
        m3.SentMessage(1);
        m4.SentMessage(3); // stored

        assertEquals(3, Message.getSentMessages().size());
        assertEquals(P3_MESSAGE_1, Message.getSentMessages().get(0).getMessageText());
        assertEquals(P3_MESSAGE_2, Message.getSentMessages().get(1).getMessageText());
    }

    /**
     * Test: Disregarded Messages array populated correctly.
     */
    @Test
    public void testDisregardedMessagesArray_CorrectlyPopulated() {
        Message m1 = new Message(1, P3_RECIPIENT_1, P3_MESSAGE_1);
        Message m2 = new Message(2, P3_RECIPIENT_2, P3_MESSAGE_2);
        m1.SentMessage(2); // disregard
        m2.SentMessage(1); // send

        assertEquals(1, Message.getDisregardedMessages().size());
        assertEquals(P3_MESSAGE_1, Message.getDisregardedMessages().get(0).getMessageText());
    }

    /**
     * Test: Message Hashes array populated when messages are sent.
     */
    @Test
    public void testMessageHashesArray_CorrectlyPopulated() {
        Message m1 = new Message(1, P3_RECIPIENT_1, P3_MESSAGE_1);
        m1.SentMessage(1);
        assertFalse("Hashes array should not be empty", Message.getMessageHashes().isEmpty());
        assertEquals(m1.getMessageHash(), Message.getMessageHashes().get(0));
    }

    /**
     * Test: Display longest message.
     * With 4 test messages, message 3 is the longest.
     */
    @Test
    public void testGetLongestMessage_ReturnsCorrectMessage() {
        Message m1 = new Message(1, P3_RECIPIENT_1, P3_MESSAGE_1);
        Message m2 = new Message(2, P3_RECIPIENT_2, P3_MESSAGE_2);
        Message m3 = new Message(3, P3_RECIPIENT_3, P3_MESSAGE_3);
        m1.SentMessage(1);
        m2.SentMessage(1);
        m3.SentMessage(1);

        String result = Message.getLongestMessage();
        assertTrue("Longest message should contain message 3 text",
                result.contains(P3_MESSAGE_3));
    }

    /**
     * Test: Search messages by recipient.
     * Recipient +27834557896 has 2 messages (message 1 and 3).
     */
    @Test
    public void testSearchByRecipient_ReturnsCorrectMessages() {
        Message m1 = new Message(1, P3_RECIPIENT_1, P3_MESSAGE_1);
        Message m2 = new Message(2, P3_RECIPIENT_2, P3_MESSAGE_2);
        Message m3 = new Message(3, P3_RECIPIENT_3, P3_MESSAGE_3);
        m1.SentMessage(1);
        m2.SentMessage(1);
        m3.SentMessage(1);

        String result = Message.searchByRecipient(P3_RECIPIENT_1);
        assertTrue("Result should contain message 1", result.contains(P3_MESSAGE_1));
        assertTrue("Result should contain message 3", result.contains(P3_MESSAGE_3));
    }

    @Test
    public void testSearchByRecipient_NotFound() {
        String result = Message.searchByRecipient("+27999999999");
        assertTrue(result.contains("No messages found"));
    }

    /**
     * Test: Delete message using hash.
     * "Where are you? You are late! I have asked you to be on time" successfully deleted.
     */
    @Test
    public void testDeleteByHash_SuccessfullyDeletes() {
        Message m3 = new Message(3, P3_RECIPIENT_3, P3_MESSAGE_3);
        m3.SentMessage(1);

        String hashToDelete = m3.getMessageHash();
        String result = Message.deleteByHash(hashToDelete);
        assertTrue("Result should confirm deletion",
                result.contains("successfully deleted"));
        assertTrue("Sent messages should now be empty",
                Message.getSentMessages().isEmpty());
    }

    @Test
    public void testDeleteByHash_NotFound() {
        String result = Message.deleteByHash("FAKEHASH");
        assertTrue(result.contains("not found"));
    }

    /**
     * Test: Display report shows all message details.
     */
    @Test
    public void testDisplayReport_ContainsAllFields() {
        Message m1 = new Message(1, P3_RECIPIENT_1, P3_MESSAGE_1);
        m1.SentMessage(1);

        String report = Message.displayReport();
        assertTrue("Report should contain message hash",  report.contains(m1.getMessageHash()));
        assertTrue("Report should contain recipient",     report.contains(P3_RECIPIENT_1));
        assertTrue("Report should contain message text",  report.contains(P3_MESSAGE_1));
    }

    /**
     * Test: Search by message ID returns correct result.
     */
    @Test
    public void testSearchByMessageID_Found() {
        Message m1 = new Message(1, P3_RECIPIENT_1, P3_MESSAGE_1);
        m1.SentMessage(1);

        String result = Message.searchByMessageID(m1.getMessageID());
        assertTrue("Should contain recipient", result.contains(P3_RECIPIENT_1));
        assertTrue("Should contain message",   result.contains(P3_MESSAGE_1));
    }

    @Test
    public void testSearchByMessageID_NotFound() {
        String result = Message.searchByMessageID("0000000000");
        assertTrue(result.contains("not found"));
    }
}
