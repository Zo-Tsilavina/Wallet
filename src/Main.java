import DAO.AccountCrudOperations;

public class Main {
    public static void main(String[] args) {
        AccountCrudOperations accountCrudOperations = new AccountCrudOperations();
        System.out.println(accountCrudOperations.findAll());
    }
}