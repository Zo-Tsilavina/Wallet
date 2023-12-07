import repository.AccountCrudOperations;
import repository.CurrencyCrudOperations;
import repository.TransactionCrudOperations;

public class Main {
    public static void main(String[] args) {
        AccountCrudOperations accountCrudOperations = new AccountCrudOperations();
        System.out.println(accountCrudOperations.findAll());
        CurrencyCrudOperations currencyCrudOperations = new CurrencyCrudOperations();
        System.out.println(currencyCrudOperations.findAll());
        TransactionCrudOperations transactionCrudOperations = new TransactionCrudOperations();
        System.out.println(transactionCrudOperations.findAll());
    }
}