package edu.newpaltz.library.models;
public abstract class User{

    private String firstName;
    private String lastName;
    private String uName;
    private String userPassword;

    public User(String fName, String lName, String uName, String uPassword){
        this.firstName = fName;
        this.lastName = lName;
        this.uName = uName;
        this.userPassword = uPassword;
    }

    public String getFirstName(){
        return this.firstName;
    }

    public String getLastName(){
        return this.lastName;
    }

    public String getUName(){
        return this.uName;
    }
    public String getUPass(){
        return this.userPassword;
    }
    
}
