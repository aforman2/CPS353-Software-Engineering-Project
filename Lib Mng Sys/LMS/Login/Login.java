package LMS.Login;

import java.util.Scanner;

public class Login {
    public static void main(String[] args) {

        Scanner keyboard = new Scanner(System.in);
        System.out.println("Enter username:\n");
        String username = keyboard.nextLine();
        System.out.println("Enter password:\n");
        String password = keyboard.nextLine();

        keyboard.close();
    }

}
