public class Saving extends Account {
    private static String accountType = "Saving";

    Saving(double initialDeposit) {
        this.setBalance(initialDeposit);
        this.checkInterest(0);
    }

    @Override
    public String toString() {
        return "\nAccount Type: " + accountType + " Account\n" +
                "Account Number: " + this.getAccountNumber() + "\n" +
                "Balance: " + this.getBalance() + "\n" +
                "Interest Rate: " + this.getInterest() + "%\n";
    }

}
