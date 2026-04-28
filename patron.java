import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public static void createPatron(String fName, String lName, String uName, String uPassword) throws IOException{
        Files.createDirectories(Path.of(uName));
        Files.createFile(Path.of(uName +"\\userProperties.txt"));
        Files.createFile(Path.of(uName +"\\userBooks.txt"));
        FileWriter pWrite = new FileWriter(uName +"\\userProperties.txt");
        pWrite.write("name: "+uName +"\n");
        pWrite.write("lastName: "+lName+"\n");
        pWrite.write("password: "+ uPassword);
        pWrite.close();
    }

    public void checkoutBook(String bookName){

    }

    public void returnBook(String bookName){

    }

     public void viewMyBooks() throws IOException{
        JFrame frame = new JFrame("My Books");
        JTextArea textArea = new JTextArea(4, 15);
        textArea.setEditable(false); // Keeps it read-only
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 24));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        
        String content = Files.readAllLines(Paths.get(this.getUName()+"\\userBooks.txt")).stream().collect(Collectors.joining("\n"));
        textArea.setText(content);
        

        frame.add(new JScrollPane(textArea));
        frame.setLocationRelativeTo(null); 
        frame.pack();
        frame.setVisible(true);
    }

    public void printPatronInfo(){
        
    }


}
