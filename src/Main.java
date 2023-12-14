import DAO.AccountCrudOperations;
import DAO.CurrencyCrudOperations;
import DAO.TransactionCrudOperations;
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
        AccountCrudOperations accountCrudOperations = new AccountCrudOperations();
//        List<Integer> transaction = new ArrayList<>();
//        Instant instant = Instant.now();
//        Timestamp timestamp = Timestamp.from(instant);
//        Account debtor = new Account(
//            3,
//            "tsila",
//            500.00,
//            timestamp,
//                transaction,
//                2,
//                "compte bancaire"
//        );
//
//        Account creditor = new Account(
//                3,
//                "junka",
//                500.00,
//                timestamp,
//                transaction,
//                2,
//                "compte epargne"
//        );
//        System.out.println(accountCrudOperations.transfer(creditor, debtor, 500.00));
    }

}