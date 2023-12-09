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
        AccountCrudOperations accountCrudOperations = new AccountCrudOperations();
        List<Integer> transactionsId = new ArrayList<>();
        transactionsId.add(1);
        transactionsId.add(2);
        transactionsId.add(3);
        Account test = new Account(
                1,
                "tsilavina",
                0.00,
                Timestamp.valueOf("2023-07-03 12:30:21"),
                transactionsId,
                1,
                "espece"
        );
//        System.out.println(accountCrudOperations.save(test));
        Timestamp startDate = Timestamp.valueOf("2023-12-01 01:00:00");
        Timestamp endDate = Timestamp.valueOf("2023-12-06 04:50:00");

        System.out.println(accountCrudOperations.getAccountBalanceHistory(startDate, endDate, test));
    }
}