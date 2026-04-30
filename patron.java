import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class patron extends user{

    private String[] booksChecked = new String[3];

    public patron(String fName, String lName, String uName, String uPass, String[] uBooksChecked) {
        super(fName, lName, uName, uPass);
        this.booksChecked = uBooksChecked;

    }
    public static patron userBuilder(String userName) throws FileNotFoundException{
        
        return new patron(null,null,null,null,null);
    }

    public void checkoutBook(){

    }

    public void returnBook(){

    }


}
