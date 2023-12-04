import repository.AccountCrudOperations;
import repository.CrudOperations;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        AccountCrudOperations accountCrudOperations = new AccountCrudOperations();
        accountCrudOperations.findAll();
    }
}