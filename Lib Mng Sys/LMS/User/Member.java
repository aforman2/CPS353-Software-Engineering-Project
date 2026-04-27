package LMS.User;

import java.util.List;

public class Member {

    // INITIAL VALS

    private String cardNum;
    private String name;
    private String email;
    private List<Loan> checkedOutLoans = new List<Loan>();
    private List<Reservation> reservations = new List<>();
    protected FineStrategy fineStrategy = new FineStrategy();

    // ABSTRACT METHODS

    public void update(String message) {

    }

    public void bookSearch() {

    }

    public Reservation placeHold(LibraryItem item) {

    }

    public void checkoutItem(Copy copy) {

    }

    public void returnItem(Copy copy) {

    }

    public void renewLoan(Loan loan) {

    }

    // GETS AND SETS

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Loan> getCheckedOutLoans() {
        return checkedOutLoans;
    }

    public void setCheckedOutLoans(List<Loan> checkedOutLoans) {
        this.checkedOutLoans = checkedOutLoans;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public FineStrategy getFineStrategy() {
        return fineStrategy;
    }

    public void setFineStrategy(FineStrategy fineStrategy) {
        this.fineStrategy = fineStrategy;
    }

}