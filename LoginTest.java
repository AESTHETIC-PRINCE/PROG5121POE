import org.junit.Test;
import static org.junit.Assert.*;
/**
 * LoginTest.java
 * Author:TREAVOR MAKHUBELA
 * Student Number: ST10511929
 * Date: April 2026
 * Purpose: Unit tests to verify the Login class
 * works correctly for PROG5121 Part 1
 * LoginTest.java - Unit tests for the Login class.
 * Uses JUnit 4 (compatible with NetBeans Ant projects).
 */
public class LoginTest {

    private Login validUser() {
        return new Login("Kyle", "Smith", "kyl_1", "Ch&8sec@ke99!", "+27838968976");
    }
//TEST1-9//
    @Test
    public void testUsernameCorrectlyFormatted() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&8sec@ke99!", "+27838968976");
        assertTrue("Expected true: kyl_1 has underscore and is 5 chars",
                login.checkUserName());
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        Login login = new Login("Kyle", "Smith", "kyle!!!!!!!", "Ch&8sec@ke99!", "+27838968976");
        assertFalse("Expected false: no underscore and too long",
                login.checkUserName());
    }

    @Test
    public void testPasswordMeetsComplexity() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue("Expected true: password meets all complexity rules",
                login.checkPasswordComplexity());
    }

    @Test
    public void testPasswordDoesNotMeetComplexity() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "password", "+27838968976");
        assertFalse("Expected false: password fails complexity rules",
                login.checkPasswordComplexity());
    }

    @Test
    public void testCellPhoneCorrectlyFormatted() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&8sec@ke99!", "+27838968976");
        assertTrue("Expected true: +27838968976 is correctly formatted",
                login.checkCellPhoneNumber());
    }

    @Test
    public void testCellPhoneIncorrectlyFormatted() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&8sec@ke99!", "08966553");
        assertFalse("Expected false: no international code",
                login.checkCellPhoneNumber());
    }

    @Test
    public void testLoginSuccessful() {
        Login login = validUser();
        assertTrue("Expected true: correct credentials",
                login.loginUser("kyl_1", "Ch&8sec@ke99!"));
    }

    @Test
    public void testLoginFailed() {
        Login login = validUser();
        assertFalse("Expected false: wrong password",
                login.loginUser("kyl_1", "wrongpassword"));
    }

    @Test
    public void testLoginStatusSuccessMessage() {
        Login login = validUser();
        String status = login.returnLoginStatus("kyl_1", "Ch&8sec@ke99!");
        assertEquals("Welcome Kyle Smith it is great to see you.", status);
    }

    @Test
    public void testLoginStatusFailMessage() {
        Login login = validUser();
        String status = login.returnLoginStatus("kyl_1", "wrongpassword");
        assertEquals("Username or password incorrect, please try again.", status);
    }
}
