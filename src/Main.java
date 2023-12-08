import DAO.AccountCrudOperations;
import DAO.CurrencyCrudOperations;
import DAO.TransactionCrudOperations;
import models.Account;
import models.Currency;
import models.Transaction;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
//         TEST FOR ACCOUNT CRUD OPERATIONS
        AccountCrudOperations accountCrudOperations = new AccountCrudOperations();
//
        List<Integer> transactionsId = new ArrayList<>();
        transactionsId.add(1);

        Account test = new Account(
                2,
                "Junka",
                15500.00,
                Timestamp.valueOf("2025-05-06 08:30:00"),
                transactionsId,
                1,
                "compte bancaire"
        );

////        System.out.println(accountCrudOperations.save(test));
//            System.out.println(accountCrudOperations.findAll());
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
//        TransactionCrudOperations transactionCrudOperations = new TransactionCrudOperations();
//        System.out.println(transactionCrudOperations.findAll());
        Transaction transaction = new Transaction(
                15,
                "pret bancaire",
                16500.00,
                Timestamp.valueOf("2025-06-07 08:30:00"),
                "debit"
        );
//        System.out.println(transactionCrudOperations.save(transaction));
        System.out.println(accountCrudOperations.doTransaction(transaction, test));
    }
}