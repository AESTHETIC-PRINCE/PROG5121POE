package prog5121;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * LoginTest.java - Unit tests for the Login class.
 * PROG5121 POE Part 1 & 2
 * Login constructor: (firstName, lastName, username, password, cellPhoneNumber)
 */
public class LoginTest {

    private Login validUser;

    @Before
    public void setUp() {
        Login.resetUsers();
        // Valid user: username has underscore + <= 5 chars, strong password, SA number
        validUser = new Login("Kyle", "Smith", "kyl_1", "Ch&8sec@ke99!", "+27838968976");
    }

    @After
    public void tearDown() {
        Login.resetUsers();
    }

    // ── Username tests ────────────────────────────────────────────────────────

    @Test
    public void testUsernameCorrectlyFormatted() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&8sec@ke99!", "+27838968976");
        assertTrue("Expected true: kyl_1 has underscore and is 5 chars",
                login.checkUserName());
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        Login login = new Login("Kyle", "Smith", "kylesmith", "Ch&8sec@ke99!", "+27838968976");
        assertFalse("Expected false: kylesmith has no underscore and exceeds 5 chars",
                login.checkUserName());
    }

    // ── Password tests ────────────────────────────────────────────────────────

    @Test
    public void testPasswordMeetsComplexity() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&8sec@ke99!", "+27838968976");
        assertTrue("Expected true: password meets all complexity requirements",
                login.checkPasswordComplexity());
    }

    @Test
    public void testPasswordDoesNotMeetComplexity() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "password", "+27838968976");
        assertFalse("Expected false: 'password' has no uppercase, digit or special char",
                login.checkPasswordComplexity());
    }

    // ── Cell phone tests ──────────────────────────────────────────────────────

    @Test
    public void testCellPhoneCorrectlyFormatted() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&8sec@ke99!", "+27838968976");
        assertTrue("Expected true: +27838968976 is correctly formatted",
                login.checkCellPhoneNumber());
    }

    @Test
    public void testCellPhoneIncorrectlyFormatted() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&8sec@ke99!", "0838968976");
        assertFalse("Expected false: 0838968976 has no international code",
                login.checkCellPhoneNumber());
    }

    // ── Login tests ───────────────────────────────────────────────────────────

    @Test
    public void testLoginSuccessful() {
        validUser.registerUser();
        boolean result = validUser.loginUser("kyl_1", "Ch&8sec@ke99!");
        assertTrue("Expected true: correct credentials should login successfully", result);
    }

    @Test
    public void testLoginFailed() {
        validUser.registerUser();
        boolean result = validUser.loginUser("kyl_1", "wrongpassword");
        assertFalse("Expected false: wrong password should fail login", result);
    }

    // ── Login status message tests ────────────────────────────────────────────

    @Test
    public void testLoginStatusSuccessMessage() {
        validUser.registerUser();
        validUser.loginUser("kyl_1", "Ch&8sec@ke99!");
        String status = validUser.returnLoginStatus("kyl_1", "Ch&8sec@ke99!");
        assertTrue("Success message should contain the user's name",
                status.contains("Kyle"));
    }

    @Test
    public void testLoginStatusFailMessage() {
        validUser.registerUser();
        String status = validUser.returnLoginStatus("kyl_1", "wrongpassword");
        assertTrue("Fail message should say incorrect",
                status.toLowerCase().contains("incorrect"));
    }
}
