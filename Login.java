/**
 * Login class handles user registration and authentication.
 * PROG5121 - Part 1: Registration and Login Feature

 * Author:TREAVOR MAKHUBELA

 * Student Number: ST10511929

 * Date: April 2026

 * Description: Login class handles user registration

 * and authentication for PROG5121 Part 1

 */
public class Login {

    // ── Fields ───────────────────────────────────────────────────────────────
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String cellPhoneNumber;

    // ── Constructor ──────────────────────────────────────────────────────────
    public Login(String firstName, String lastName,
                 String username, String password, String cellPhoneNumber) {
        this.firstName     = firstName;
        this.lastName      = lastName;
        this.username      = username;
        this.password      = password;
        this.cellPhoneNumber = cellPhoneNumber;
    }

    // ── Validation Methods ───────────────────────────────────────────────────

    /**
     * Checks that the username contains an underscore (_)
     * AND is no more than five characters long.
     *
     * @return true if valid, false otherwise
     */
    public boolean checkUserName() {
        return username.contains("_") && username.length() <= 5;
    }

    /**
     * Checks that the password meets complexity rules:
     *  - At least 8 characters long
     *  - Contains a capital letter
     *  - Contains a number
     *  - Contains a special character
     *
     * @return true if valid, false otherwise
     */
    public boolean checkPasswordComplexity() {
        if (password.length() < 8) return false;

        boolean hasUpper   = false;
        boolean hasDigit   = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c))        hasUpper   = true;
            else if (Character.isDigit(c))       hasDigit   = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }

        return hasUpper && hasDigit && hasSpecial;
    }

    /**
     * Uses a regular expression to check the cell phone number.
     * The number must:
     *  - Start with an international country code (e.g. +27)
     *  - Be no more than ten characters after the country code
     *
     * Regex breakdown:
     *   ^\+       → starts with a literal '+'
     *   \d{1,3}   → country code (1-3 digits, e.g. 27 for South Africa)
     *   \d{7,10}$ → local number (7-10 digits)
     *
     * Reference: Java Pattern docs – https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html
     *
     * @return true if valid, false otherwise
     */
    public boolean checkCellPhoneNumber() {
        // Source: standard international phone number regex pattern
        return cellPhoneNumber.matches("^\\+\\d{1,3}\\d{7,10}$");
    }

    // ── Registration Method ──────────────────────────────────────────────────

    /**
     * Attempts to register the user by validating all fields in order.
     * Returns the appropriate success or failure message.
     *
     * @return String message describing the registration outcome
     */
    public String registerUser() {
        if (!checkUserName()) {
            return "Username is not correctly formatted; please ensure that your "
                 + "username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted; please ensure that the "
                 + "password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber()) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }
        return "Username successfully captured.\n"
             + "Password successfully captured.\n"
             + "Cell number successfully captured.";
    }

    // ── Login Methods ────────────────────────────────────────────────────────

    /**
     * Verifies whether the entered username and password match the stored details.
     *
     * @param enteredUsername the username typed by the user
     * @param enteredPassword the password typed by the user
     * @return true if credentials match, false otherwise
     */
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        return this.username.equals(enteredUsername)
            && this.password.equals(enteredPassword);
    }

    /**
     * Returns a human-readable login status message.
     * On success: personalised welcome message.
     * On failure: generic error to avoid revealing which field was wrong.
     *
     * @param enteredUsername the username typed by the user
     * @param enteredPassword the password typed by the user
     * @return String login status message
     */
    public String returnLoginStatus(String enteredUsername, String enteredPassword) {
        if (loginUser(enteredUsername, enteredPassword)) {
            return "Welcome " + firstName + " " + lastName + " it is great to see you.";
        }
        return "Username or password incorrect, please try again.";
    }

    // ── Getters (needed by unit tests) ───────────────────────────────────────
    public String getUsername()      { return username; }
    public String getPassword()      { return password; }
    public String getFirstName()     { return firstName; }
    public String getLastName()      { return lastName; }
}
