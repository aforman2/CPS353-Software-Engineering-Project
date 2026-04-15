package Users;

public interface Account {

    // Takes in a username and password to be checked with an account in the system
    public boolean authenticate(String user, String pass);

    // Allows logged-in user to reset their password
    public void resetPassword();

}