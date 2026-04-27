package LMS;

import java.util.Scanner;

public class LibraryManagementSystem {
    public static void main(String[] args) {

        initialize(); // Open UI for program

        Scanner keyboard = new Scanner(System.in);

        // To be replaced with the code for the button:
        System.out.println("Login or Create Account?");
        String userInput = keyboard.next();

        //

        // User is prompted after initialization.
        if (userInput == "Login") {
            user.login(username, password); //
        } else if (userInput == "Create Account") {
            user.createAccount();
        } else {
            System.out.println("Unknown Error occurred.");
        }

        keyboard.close();

    }

}
