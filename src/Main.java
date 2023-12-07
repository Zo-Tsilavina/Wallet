import DAO.AccountCrudOperations;
import models.Account;

import java.sql.Timestamp;


public class Main {
    public static void main(String[] args) {
        AccountCrudOperations accountCrudOperations = new AccountCrudOperations();
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
        System.out.println(accountCrudOperations.findAll());
    }
}