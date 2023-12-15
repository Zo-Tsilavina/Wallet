import DAO.*;
import models.Account;
import models.Currency;
import models.Transaction;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
//        AccountCrudOperations accountCrudOperations = new AccountCrudOperations();
//        List<Integer> transaction = new ArrayList<>();
//        Instant instant = Instant.now();
//        Timestamp timestamp = Timestamp.from(instant);
//
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
//
//        // Appel de la méthode transfer avec les identifiants des comptes
//        System.out.println(accountCrudOperations.transfer(1, 3, 500.00));

        TransactionCategoryCrudOperations transactionCategoryCrudOperations = new TransactionCategoryCrudOperations();
        System.out.println(transactionCategoryCrudOperations.allAccountTransactionByCategory(1, Timestamp.valueOf("2023-12-02 02:00:00"), Timestamp.valueOf("2023-12-15 13:49:08.727148")));
    }

}