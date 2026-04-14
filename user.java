import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


public class user{

    private String firstName;
    private String lastName;
    private String userType;
    private File userProperites;

    public user(String fName, String lName, String uType, File uProp){
        this.firstName = fName;
        this.lastName = lName;
        this.userType = uType;
        this.userProperites = uProp;
    }

    public static user userBuilder(String uName) throws FileNotFoundException{
        String filePName = "C:\\seProj\\" +uName +"\\userProperties.txt";
        File pfile = new File(filePName);
        Scanner sc = new Scanner(pfile);
        String TfirstName = sc.nextLine();
        String TlastName = sc.nextLine();
        String TuserType = sc.nextLine();
        sc.close();
        return new user(TfirstName, TlastName, TuserType, pfile);
    } 

    public void createUser(){

    }

    public void printInfo(){
        System.out.println(this.firstName);
        System.out.println(this.lastName);
        System.out.println(this.userType);
    }

}
