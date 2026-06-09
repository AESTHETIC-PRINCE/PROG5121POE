# QuickChat – PROG5121 POE

**Student:** Mandisa Treavor Makhubela
**Student Number:** ST10511929
**Module:** Programming 1A – PROG5121
**Year:** 2026

---

## Project Overview

QuickChat is a Java console-based messaging application built across three parts of the PROG5121 Portfolio of Evidence (POE). The application allows users to register, log in, send messages, store messages, and manage their message history.

---

## Features by Part

### Part 1 – Registration and Login
- User registration with username and password validation
- Username must contain an underscore and be no more than 5 characters
- Password must be at least 8 characters with an uppercase letter, number, and special character
- Login authentication with status messages
- Cell phone number validation

### Part 2 – QuickChat Messaging
- Welcome to QuickChat message after successful login
- Numeric menu: Send Messages | Coming Soon | Quit
- Message class with auto-generated Message ID (10 digits) and Message Hash
- Message Hash format: `[first 2 of ID]:[messageNumber]:[FIRSTWORD][LASTWORD]`
- Send, Disregard, or Store messages
- Messages stored to `messages.json`
- Unit tests for all validations

### Part 3 – Store Data and Display
- Arrays populated at runtime (no hard-coding):
  - Sent Messages
  - Disregarded Messages
  - Stored Messages (loaded from JSON)
  - Message Hashes
  - Message IDs
- New menu option: **Stored Messages** with:
  - Display sender and recipient of all stored messages
  - Display the longest stored message
  - Search for a message by ID
  - Search all messages for a particular recipient
  - Delete a message using its hash
  - Display full message report

---

## Project Structure

```
PROG5121POE/
├── src/
│   └── prog5121poe/
│       ├── App.java          - Main application entry point
│       ├── Login.java        - User registration and authentication
│       └── Message.java      - Message class with all features
├── test/
│   └── prog5121poe/
│       ├── LoginTest.java    - Unit tests for Login class
│       └── MessageTest.java  - Unit tests for Message class
├── .github/
│   └── workflows/
│       └── java-ci.yml       - GitHub Actions CI/CD pipeline
├── messages.json             - Stored messages file (auto-generated)
├── build.xml                 - Apache Ant build file
└── README.md
```

---

## How to Run

### In NetBeans
1. Open the project: **File → Open Project → select PROG5121POE**
2. Right-click `App.java` → **Run File**
3. Follow the prompts to register and log in

### Using Ant (Command Line)
```bash
ant run
```

### Run Tests
```bash
ant test
```
Or right-click `MessageTest.java` in NetBeans → **Test File**

---

## Test Data (Part 3)

| # | Recipient | Message | Flag |
|---|-----------|---------|------|
| 1 | +27834557896 | Did you get the cake? | Sent |
| 2 | +27838884567 | It is dinner time! | Sent |
| 3 | +27834557896 | Where are you? You are late! I have asked you to be on time | Sent |
| 4 | +27833232123 | Yoh, are you coming? | Stored |

---

## Technologies Used

- **Java 17**
- **Apache Ant** – Build tool
- **JUnit 4** – Unit testing
- **GitHub Actions** – CI/CD pipeline
- **JSON** – Message storage

---

## GitHub Actions CI/CD

The project uses GitHub Actions to automatically compile and run all unit tests on every push to `main` and `KhanbanTasks` branches.

Workflow file: `.github/workflows/java-ci.yml`

---

