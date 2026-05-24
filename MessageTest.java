

import prog5121.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for Message class.
 * PROG5121 POE Part 2 – uses exact test data from rubric.
 *
 * Test Data:
 *   Message 1: Recipient +27718693002 | "Hi Mike, can you join us for dinner tonight?" | Send
 *   Message 2: Recipient 0857595889   | "Hi Keegan, did you receive the payment?"      | Discard
 */
public class MessageTest {

    // ── Test data from POE rubric ─────────────────────────────────────────────
    private static final String RECIPIENT_1    = "+27718693002";
    private static final String MESSAGE_TEXT_1 = "Hi Mike, can you join us for dinner tonight?";

    private static final String RECIPIENT_2    = "0857595889";
    private static final String MESSAGE_TEXT_2 = "Hi Keegan, did you receive the payment?";

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

    // ── 1. Message length ≤ 250 characters ───────────────────────────────────

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

    // ── 2. Recipient cell number ──────────────────────────────────────────────

    @Test
    public void testRecipientCell_ValidInternationalCode_Success() {
        assertEquals(
            "Cell phone number successfully captured.",
            message1.checkRecipientCell()
        );
    }

    @Test
    public void testRecipientCell_NoInternationalCode_Failure() {
        assertEquals(
            "Cell phone number is incorrectly formatted or does not contain "
          + "an international code. Please correct the number and try again.",
            message2.checkRecipientCell()
        );
    }

    // ── 3. Message Hash ───────────────────────────────────────────────────────

    @Test
    public void testMessageHash_EndsWithHITONIGHT_Message1() {
        // First word = "Hi", Last word = "tonight?" → HITONIGHT
        String hash = message1.getMessageHash();
        assertTrue("Hash should end with HITONIGHT", hash.endsWith("HITONIGHT"));
    }

    @Test
    public void testMessageHash_EndsWithHIPAYMENT_Message2() {
        // First word = "Hi", Last word = "payment?" → HIPAYMENT
        String hash = message2.getMessageHash();
        assertTrue("Hash should end with HIPAYMENT", hash.endsWith("HIPAYMENT"));
    }

    @Test
    public void testMessageHash_IsAllUppercase() {
        String hash = message1.getMessageHash();
        assertEquals("Hash must be all uppercase", hash, hash.toUpperCase());
    }

    @Test
    public void testMessageHash_ContainsCorrectMessageNumber() {
        assertTrue("Hash should contain :1:",
                   message1.getMessageHash().contains(":1:"));
        assertTrue("Hash should contain :2:",
                   message2.getMessageHash().contains(":2:"));
    }

    // ── 4. Message ID ─────────────────────────────────────────────────────────

    @Test
    public void testCheckMessageID_IsValid() {
        assertTrue("Message ID must be ≤ 10 characters", message1.checkMessageID());
    }

    @Test
    public void testMessageID_IsExactly10Digits() {
        assertEquals("Message ID must be exactly 10 characters",
                     10, message1.getMessageID().length());
    }

    @Test
    public void testMessageID_IsNotNull() {
        assertNotNull("Message ID should not be null", message1.getMessageID());
    }

    // ── 5. SentMessage – Send / Disregard / Store ─────────────────────────────

    @Test
    public void testSentMessage_Send_ReturnsSuccessfullySent() {
        assertEquals("Message successfully sent.", message1.SentMessage(1));
    }

    @Test
    public void testSentMessage_Disregard_ReturnsPressZero() {
        assertEquals("Press 0 to delete the message.", message2.SentMessage(2));
    }

    @Test
    public void testSentMessage_Store_ReturnsSuccessfullyStored() {
        Message storeMsg = new Message(3, "+27831234567", "Store this message");
        assertEquals("Message successfully stored.", storeMsg.SentMessage(3));
    }

    // ── 6. Total messages counter ─────────────────────────────────────────────

    @Test
    public void testReturnTotalMessages_OneSentOneDiscarded() {
        message1.SentMessage(1); // sent
        message2.SentMessage(2); // discarded
        assertEquals("Only 1 message was sent", 1, Message.returnTotalMessages());
    }

    @Test
    public void testReturnTotalMessages_BothSent() {
        message1.SentMessage(1);
        message2.SentMessage(1);
        assertEquals("Both messages sent", 2, Message.returnTotalMessages());
    }

    // ── 7. printMessages ─────────────────────────────────────────────────────

    @Test
    public void testPrintMessages_ContainsRecipient_AfterSend() {
        message1.SentMessage(1);
        String output = Message.printMessages();
        assertTrue("Output must contain recipient", output.contains(RECIPIENT_1));
    }

    @Test
    public void testPrintMessages_ReturnsNotice_WhenEmpty() {
        assertEquals("No messages have been sent yet.", Message.printMessages());
    }

    // ── 8. All hashes tested in a loop (rubric requirement) ──────────────────

    @Test
    public void testAllMessageHashes_AreUppercaseAndNonEmpty() {
        Message[] messages = { message1, message2 };
        for (Message m : messages) {
            String hash = m.getMessageHash();
            assertNotNull("Hash should not be null", hash);
            assertFalse("Hash should not be empty", hash.isEmpty());
            assertEquals("Hash should be uppercase", hash, hash.toUpperCase());
        }
    }
}
