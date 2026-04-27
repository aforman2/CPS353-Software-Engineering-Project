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
        File pfile = new File(userName +"\\userProperties.txt");
        Scanner sc = new Scanner(pfile);
        File pbooks = new File(userName +"\\userBooks.txt");
        Scanner scBooks = new Scanner(pbooks);
        String[] info = new String[3];
        String[] TbooksChecked = new String[3];

        while (sc.hasNext()) {
        String label = sc.next(); 
    
        if (sc.hasNext()) {
            String value = sc.next();
        
            if (label.equals("name:")) info[0] = value;
            else if (label.equals("lastName:")) info[1] = value;
            else if (label.equals("password:")) info[2] = value;
            }
        }  
        
        for (int i = 0; i <3; i++){
            if (scBooks.hasNext()){
                TbooksChecked[i] = scBooks.nextLine();
            }else{
                TbooksChecked[i] = "-";
            }
        }
        sc.close();
        scBooks.close();
        return new patron(info[0],info[1],userName,info[2],TbooksChecked);
    }

    public static void createPatron(String fName, String lName, String uName, String uPassword){

    }

    public static void savePatron(){

    }

    public void listAllBooks(){
        
    }

    public void checkoutBook(){

    }

    public void returnBook(){

    }

    public void printPatronInfo(){
        System.out.println(this.getFirstName());
        System.out.println(this.getLastName());
        System.out.println(this.getUName());
        System.out.println(this.getUPass());
    }

    public void listCheckedOutBooks(){
        System.out.println("\nBooks Checked Out:");
        for(int i = 0; i < booksChecked.length; i++){
            System.out.println(booksChecked[i]);
        }
    }


}
