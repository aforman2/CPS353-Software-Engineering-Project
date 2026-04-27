import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Login {
    static ArrayList<String> userNames = new ArrayList<>();
    static File userFolder = new File("userFolder.csv");
    static boolean loginStatus = false;
    static String currentUser = "None";
    static File currentFile;

    static void initalizeLogin() throws IOException {
        Scanner sc = new Scanner(userFolder);
        String line = "";
        while (sc.hasNext()) {
            line = sc.nextLine();
            userNames.add(line);
        }
        sc.close();
    }

    static void loginOptions() {

    }

    static void userLogin() {

    }

    static void createUser() {

    }

}