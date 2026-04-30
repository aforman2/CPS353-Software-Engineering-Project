import java.util.Date;

public class Loan {

    // INITIAL VARS
    private String loanID;
    private Date checkoutDate;
    private Date dueDate;

    // CONSTRUCTOR
    public Loan(String id, Date cD, Date dD) {
        this.loanID = id;
        this.checkoutDate = cD;
        this.dueDate = dD;
    }

    // GETS AND SETS
    public String getLoanID() {
        return loanID;
    }

    public void setLoanID(String l) {
        loanID = l;
    }

    public Date getCheckouDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(Date cd) {
        checkoutDate = cd;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dD) {
        dueDate = dD;
    }

    // MAIN METHODS

    public boolean isOverdue() {
        if (checkoutDate.getTime() >= dueDate.getTime()) {
            return true;
        }
        return false;
    }

}
