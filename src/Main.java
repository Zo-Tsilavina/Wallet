import DAO.*;
import models.Account;
import models.Currency;
import models.Transaction;
import models.TransferHistory;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
//        TransactionCrudOperations transactionCrudOperations = new TransactionCrudOperations();
//
//        AccountCrudOperations accountCrudOperations = new AccountCrudOperations();
//        List<Integer> transaction = new ArrayList<>();
//        transaction.add(50);
//        Instant instant = Instant.now();
//        Timestamp timestamp = Timestamp.from(instant);


        // ======================= FINDBY ID TRANSACTION
//        System.out.println(transactionCrudOperations.findById(3));

        //  ======================= SAVE ACCOUNT
//                Account debtor = new Account(
//                100,
//                "test",
//                500.00,
//                timestamp,
//                transaction,
//                2,
//                "compte bancaire"
//        );
//        System.out.println(accountCrudOperations.save(debtor));

                // ======================== FIND ALL
//        System.out.println(accountCrudOperations.findAll());

                // ======================== FIND BY ID
//         System.out.println(accountCrudOperations.findById(100));

                // ======================= TRANSFER OR DO TRANSACTION
//        Account debtor = new Account(
//                1,
//                "tsila",
//                500.00,
//                timestamp,
//                transaction,
//                2,
//                "compte bancaire"
//        );
//
//        Account creditor = new Account(
//                2, // Identifiant unique du compte créditeur
//                "junka",
//                500.00,
//                timestamp,
//                transaction,
//                2,
//                "compte epargne"
//        );
//        System.out.println(accountCrudOperations.transfer(3, 1, 1000.00));

                        // ===================== DO TRANSACTION
//        Transaction doTransaction = new Transaction(
//                101,
//                "test",
//                10000.00,
//                timestamp,
//                2
//        );
//        System.out.println(accountCrudOperations.doTransaction(doTransaction, 100));

                        // ===================== GET ACCOUNT BALANCE
//        System.out.println(accountCrudOperations.getAccountBalance(timestamp, 100));
//    TransactionCategoryCrudOperations transactionCategoryCrudOperations = new TransactionCategoryCrudOperations();
//    transactionCategoryCrudOperations.findById(11);

//        // Appel de la méthode transfer avec les identifiants des comptes
//        System.out.println(accountCrudOperations.transfer(1, 3, 500.00));

//        TransactionCategoryCrudOperations transactionCategoryCrudOperations = new TransactionCategoryCrudOperations();
//        System.out.println(transactionCategoryCrudOperations.allAccountTransactionByCategory(1, Timestamp.valueOf("2023-12-02 02:00:00"), Timestamp.valueOf("2023-12-15 13:49:08.727148")));





            AccountCrudOperations accountCrudOperations = new AccountCrudOperations();
            System.out.println(accountCrudOperations.findAll());

            CurrencyCrudOperations currencyCrudOperations = new CurrencyCrudOperations();
            System.out.println(currencyCrudOperations.findAll());

            TransactionCrudOperations transactionCrudOperations = new TransactionCrudOperations();
            System.out.println(transactionCrudOperations.findAll());

            TransactionCategoryCrudOperations transactionCategoryCrudOperations =new TransactionCategoryCrudOperations();
            System.out.println(transactionCategoryCrudOperations.findAll());

            TransferHistoryCrudOperations transferHistoryCrudOperations = new TransferHistoryCrudOperations();
            System.out.println(transferHistoryCrudOperations.findAll());

    }

}