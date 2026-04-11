# PROG5121 - Part 1: Registration and Login Feature

## Author
- **Name:** TREAVOR MAKHUBELA
- **Student Number:** ST10511929
- **Date:** April 2026

## Project Description
This is a console-based Java application built for PROG5121 Part 1.
The application allows users to register and login using a 
username, password, and South African cell phone number.

## Features
- User registration with input validation
- Username must contain an underscore and be no more than 5 characters
- Password must be at least 8 characters with a capital letter, 
  number, and special character
- Cell phone number must contain an international code (e.g. +27)
- Login authentication with success and failure messages

## How to Run
1. Open the project in NetBeans
2. Run App.java as the main class
3. Follow the console prompts to register and login

## Unit Tests
Unit tests are written using JUnit 4 and can be found in LoginTest.java.

### Test Cases
- Username correctly formatted: kyl_1 → returns true
- Username incorrectly formatted: kyle!!!! → returns false
- Password meets complexity: Ch&8sec@ke99! → returns true
- Password fails complexity: password → returns false
- Cell phone correctly formatted: +27838968976 → returns true
- Cell phone incorrectly formatted: 08966553 → returns false
- Login successful → returns true
- Login failed → returns false

## Classes
- **Login.java** - Contains all validation and authentication methods
- **App.java** - Main entry point for the application
- **LoginTest.java** - JUnit 4 unit tests
