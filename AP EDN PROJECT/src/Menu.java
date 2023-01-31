import java.util.*;

public class Menu {

    Scanner keyboard = new Scanner(System.in);
    Bank bank = new Bank();
    boolean exit;

    public static void main(String[] args) {

        Menu menu = new Menu();
        menu.runMenu();
    }


    public void runMenu() {
        printHeader();
        while (!exit) {
            printMenu();
            int choice = getMenuChoice();
            performAction(choice);
        }
    }

    private void printHeader() {
        System.out.println("+----------------------------------------------+");
        System.out.println("|                                              |");
        System.out.println("|             Welcome to Ali's bank            |");
        System.out.println("|                                              |");
        System.out.println("+----------------------------------------------+");
    }

    private void printMenu() {
        displayHeader("Please make a selection");
        System.out.println("1. Create a new Account");
        System.out.println("2. Make a deposit");
        System.out.println("3. Make a withdrawal");
        System.out.println("4. List account balance");
        System.out.println("0. Exit");
    }

    private int getMenuChoice() {
        int choice = -1;
        do {
            System.out.println("Enter your choice: ");
            try {
                choice = Integer.parseInt(keyboard.nextLine());

            } catch (NumberFormatException exception) {
                System.out.println("Invalid selection. Numbers only please.");
            }
            if (choice < 0 || choice > 4) {
                System.out.println("Choice outside of range. please chose again.");
            }
        } while (choice < 0 || choice > 4);
        return choice;
    }

    private void performAction(int choice) {
        switch (choice) {
            case 0:
                System.out.println("Thank you for using our application.");
                System.exit(0);
                break;

            case 1: {
                try {

                    createAccount();
                } catch (InvalidAccountTypeException exception) {
                    System.out.println("Account was not created successfully.");
                }
                break;
            }
            case 2:
                makeDeposit();
                break;

            case 3:
                makeWithdrawal();
                break;

            case 4:
                listBalances();
                break;

            default:
                System.out.println("Unknown error has occurred.");
        }
    }

    private String askQuestion(String question, List<String> answers) {
        String response = "";
        Scanner input = new Scanner(System.in);
        boolean choices = ((answers == null) || answers.size() == 0) ? false : true;
        boolean firstRun = true;
        do {
            if (!firstRun) {
                System.out.println("Invalid selection. Please try again.");
            }
            System.out.print(question);
            if (choices) {
                System.out.print("(");
                for (int i = 0; i < answers.size() - 1; ++i) {
                    System.out.print(answers.get(i) + "/");
                }
                System.out.print(answers.get(answers.size() - 1));
                System.out.print("): ");
            }

            response = input.nextLine();
            firstRun = false;
            if (!choices) {
                break;
            }
        } while (!answers.contains(response));
        return response;
    }

    private double getDeposit(String accountType) {
        double initialDeposit = 0;
        boolean valid = false;
        while (!valid) {
            System.out.print("Please enter initial deposit: ");
            try {
                initialDeposit = Double.parseDouble(keyboard.nextLine());
            } catch (NumberFormatException exception) {
                System.out.println("Deposit must be a number.");
            }
            if (accountType.equalsIgnoreCase("checking")) {
                if (initialDeposit < 100) {
                    System.out.println("Checking accounts require a minimum of 100 dollars to open.");
                } else {
                    valid = true;
                }
            } else if (accountType.equalsIgnoreCase("saving")) {
                if (initialDeposit < 50) {
                    System.out.println("Checking accounts require a minimum of 50 dollars to open.");
                } else {
                    valid = true;
                }
            }
        }
        return initialDeposit;
    }

    private void createAccount() throws InvalidAccountTypeException {
        displayHeader("Create an Account");
        String accountType = askQuestion("Please enter an account type: ", Arrays.asList("checking", "saving"));
        String firstName = askQuestion("Please enter your first name: ", null);
        String lastName = askQuestion("Please enter your last name: ", null);
        String ssn = askQuestion("Please enter your social security number: ", null);
        double initialDeposit = getDeposit(accountType);

        Account account;
        if (accountType.equalsIgnoreCase("checking")) {
            account = new Checking(initialDeposit);
        } else if (accountType.equalsIgnoreCase("saving")) {
            account = new Saving(initialDeposit);
        } else {
            throw new InvalidAccountTypeException();
        }
        Customer customer = new Customer(firstName, lastName, ssn, account);
        bank.addCustomer(customer);
    }

    private double getDollarAmount(String question) {
        System.out.println(question);
        double amount = 0;
        try {
            amount = Double.parseDouble(keyboard.nextLine());
        } catch (NumberFormatException exception) {
            amount = 0;
        }
        return amount;
    }

    private void makeDeposit() {
        displayHeader("Make a Deposit");
        int account = selectAccount();
        if (account >= 0) {
            double amount = getDollarAmount("How much would you like to deposit?: ");
            bank.getCustomer(account).getAccount().deposit(amount);
        }
    }

    private void makeWithdrawal() {
        displayHeader("Make a Withdrawal");
        int account = selectAccount();
        if (account >= 0) {
            double amount = getDollarAmount("How much would you like to withdraw?: ");
            bank.getCustomer(account).getAccount().withdrawal(amount);
        }
    }

    private void listBalances() {
        displayHeader("List Account Details");
        int account = selectAccount();
        if (account >= 0) {
            displayHeader("Account Details");
            System.out.println(bank.getCustomer(account).getAccount());
        }
    }

    private void displayHeader(String message) {
        System.out.println();
        int width = message.length() + 6;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("+");
        for (int i = 0; i < width; ++i) {
            stringBuilder.append("-");
        }
        stringBuilder.append("+");
        System.out.println(stringBuilder.toString());
        System.out.println("|   " + message + "   |");
        System.out.println(stringBuilder.toString());

    }

    private int selectAccount() {
        ArrayList<Customer> customers = bank.getCustomers();
        if (customers.size() <= 0) {
            System.out.println("No customers at your bank.");
            return -1;
        }
        System.out.println("Select an account: ");
        for (int i = 0; i < customers.size(); i++) {
            System.out.println("\t" + (i + 1) + ") " + customers.get(i).basicInfo());
        }
        int account;
        System.out.println("Please enter your selection: ");
        try {
            account = Integer.parseInt(keyboard.nextLine()) - 1;
        } catch (NumberFormatException exception) {
            account = -1;
        }
        if (account < 0 || account > customers.size()) {
            System.out.println("Invalid account selected.");
            account = -1;
        }
        return account;
    }
}