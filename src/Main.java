import DAO.AccountCrudOperations;
import DAO.CurrencyCrudOperations;
import DAO.TransactionCrudOperations;
import models.Account;
import models.Currency;
import models.Transaction;

import java.sql.Timestamp;


public class Main {
    public static void main(String[] args) {
        // TEST FOR ACCOUNT CRUD OPERATIONS
//        AccountCrudOperations accountCrudOperations = new AccountCrudOperations();
//        Account test = new Account(
//                3,
//                "BOA",
//                10000.00,
//                Timestamp.valueOf("2025-05-05 08:30:00"),
//                1,
//                1,
//                "espece"
//        );
//        System.out.println(accountCrudOperations.save(test));
//        System.out.println(accountCrudOperations.findAll());

//        TEST FOR CURRENCY CRUD OPERATIONS
//        CurrencyCrudOperations currencyCrudOperations = new CurrencyCrudOperations();
//        System.out.println(currencyCrudOperations.findAll());
//        Currency test = new Currency(
//                3,
//                "Yen",
//                "JPY"
//        );
//        System.out.println(currencyCrudOperations.save(test));

        // TEST FOR TRANSACTION CRUD OPERATIONS
        TransactionCrudOperations transactionCrudOperations = new TransactionCrudOperations();
        System.out.println(transactionCrudOperations.findAll());
//        Transaction transaction = new Transaction(
//                3,
//
//                "pret bancaire",
//                500.00,
//                Timestamp.valueOf("2025-05-05 08:30:00"),
//                "credit"
//        );
//        System.out.println(transactionCrudOperations.save(transaction));
    }
}