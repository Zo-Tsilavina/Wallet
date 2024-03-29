package DAO;

import AutoCrudOperation.AutoCrudOp;
import JDBC.ConnectionDB;
import models.Account;
import models.Transaction;
import models.TransactionCategory;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class AccountCrudOperations implements CrudOperations<Account> {
    private final ConnectionDB connectionDB;

    public AccountCrudOperations() {
        this.connectionDB = new ConnectionDB();
    }

    @Override
    public List<Account> findAll() throws SQLException {
        AutoCrudOp<Account> autoCrudOp = new AutoCrudOp<>(Account.class);
        return autoCrudOp.findAll();
    }

    @Override
    public Account findById(int id) throws SQLException {
        AutoCrudOp<Account> autoCrudOp = new AutoCrudOp<>(Account.class);
        return autoCrudOp.findById(id);
    }


    @Override
    public Account save(Account account) {
        AutoCrudOp<Account> autoCrudOp = new AutoCrudOp<>(Account.class);
        return autoCrudOp.save(account);
    }

    public Account doTransaction (Transaction transaction, int accountId) throws SQLException {

        TransactionCrudOperations transactionCrudOperations = new TransactionCrudOperations();
        AccountCrudOperations accountCrudOperations = new AccountCrudOperations();
        TransactionCategoryCrudOperations transactionCategoryCrudOperations = new TransactionCategoryCrudOperations();
        TransactionCategory transactionCategory = transactionCategoryCrudOperations.findById(transaction.getTransactionCategoryId());
        Account account = accountCrudOperations.findById(accountId);

            if(transactionCategory.getType().equals("debit")){
                if((account.getAmount()-transaction.getValue() < 0)) {
                    if (account.getType().equals("compte bancaire")) {
                        account.setAmount(account.getAmount() - transaction.getValue());

                    }else {
                        throw new RuntimeException("transaction failed");
                    }

                }else {
                    account.setAmount(account.getAmount() - transaction.getValue());
                }

            } else if (transactionCategory.getType().equals("credit")) {
                account.setAmount(account.getAmount() + transaction.getValue());

            }else{
                throw new RuntimeException("transaction failed");
            }

        List<Integer> transactionIdList = account.getTransactionsId();
        transactionIdList.add(transaction.getId());
        account.setTransactionsId(transactionIdList);
        account.setLastUpdateDate(transaction.getDateTimeTransaction());

        AccountCrudOperations accountCrudOperations1 = new AccountCrudOperations();
        accountCrudOperations1.save(account);

        TransactionCrudOperations transactionCrudOperations1 = new TransactionCrudOperations();
        transactionCrudOperations1.save(transaction);

        return account;
    }

    public Account getAccountBalance (Timestamp timestamp, int accountId) throws SQLException {
        AccountCrudOperations accountCrudOperations = new AccountCrudOperations();
        Account account = accountCrudOperations.findById(accountId);
        List<Integer> currentTransactionIdList = new ArrayList<>();
        List<Integer> transactionIdList = account.getTransactionsId();

        for (int transactionId : transactionIdList) {

            currentTransactionIdList.add(transactionId);
            TransactionCrudOperations transactionCrudOperations = new TransactionCrudOperations();
            Transaction currentTransaction = transactionCrudOperations.findById(transactionId);

            Timestamp currentTransactionTimestamp = currentTransaction.getDateTimeTransaction();
            TransactionCategoryCrudOperations transactionCategoryCrudOperations = new TransactionCategoryCrudOperations();
            TransactionCategory currentTransactionCategory = transactionCategoryCrudOperations.findById(transactionId);


            if (timestamp.after(currentTransactionTimestamp) || timestamp.equals(currentTransactionTimestamp)) {

                if (currentTransactionCategory.getType().equals("credit")) {
                    account.setAmount(account.getAmount() + currentTransaction.getValue());
                    account.setLastUpdateDate(currentTransactionTimestamp);
                    account.setTransactionsId(currentTransactionIdList);
                } else if (currentTransactionCategory.getType().equals("debit")) {
                    account.setAmount(account.getAmount() - currentTransaction.getValue());
                    account.setLastUpdateDate(currentTransactionTimestamp);
                    account.setTransactionsId(currentTransactionIdList);
                }
            } else {
                break;
            }
        }
        return account;
    }

    public List<Account> getAccountBalanceHistory (Timestamp startDate, Timestamp endDate, int accountId) throws SQLException {

        AccountCrudOperations accountCrudOperations = new AccountCrudOperations();
        Account account = accountCrudOperations.findById(accountId);
        List<Account> accounts = new ArrayList<>();
        List<Integer> transactionIdList = account.getTransactionsId();
        List<Integer> currentTransactionIdList = new ArrayList<>();

        for (int transactionId : transactionIdList) {

            TransactionCrudOperations transactionCrudOperations = new TransactionCrudOperations();
            Transaction currentTransaction = transactionCrudOperations.findById(transactionId);
            Timestamp currentTransactionTimestamp = currentTransaction.getDateTimeTransaction();
            TransactionCategoryCrudOperations transactionCategoryCrudOperations = new TransactionCategoryCrudOperations();
            TransactionCategory currentTransactionCategory = transactionCategoryCrudOperations.findById(transactionId);


            currentTransactionIdList.add(transactionId);
            if (currentTransactionTimestamp.before(startDate)){

                if (currentTransactionCategory.getType().equals("credit")) {

                    account.setAmount(account.getAmount() + currentTransaction.getValue());
                    account.setLastUpdateDate(currentTransactionTimestamp);
                    account.setTransactionsId(currentTransactionIdList);

                } else if (currentTransactionCategory.getType().equals("debit")) {

                    account.setAmount(account.getAmount() - currentTransaction.getValue());
                    account.setLastUpdateDate(currentTransactionTimestamp);
                    account.setTransactionsId(currentTransactionIdList);
                }

            }else if ((startDate.before(currentTransactionTimestamp) || startDate.equals(currentTransactionTimestamp))
                    && (endDate.after(currentTransactionTimestamp) || endDate.equals(currentTransactionTimestamp))) {

                if (currentTransactionCategory.getType().equals("credit")) {
                    account.setAmount(account.getAmount() + currentTransaction.getValue());
                    account.setLastUpdateDate(currentTransactionTimestamp);
                    account.setTransactionsId(currentTransactionIdList);
                } else if (currentTransactionCategory.getType().equals("debit")) {

                    account.setAmount(account.getAmount() - currentTransaction.getValue());
                    account.setLastUpdateDate(currentTransactionTimestamp);
                    account.setTransactionsId(currentTransactionIdList);
                }
                Account accountToShow = new Account(
                        account.getId(),
                        account.getName(),
                        account.getAmount(),
                        account.getLastUpdateDate(),
                        account.getTransactionsId(),
                        account.getCurrencyId(),
                        account.getType()
                );
                accounts.add(accountToShow);
            } else {
                System.out.println("no transaction from "+ startDate + " to "+ endDate);
                break;
            }
        }
        return accounts;

    }
    public List<Account> transfer(int creditorId, int debtorId, double amount) throws SQLException {

        AccountCrudOperations accountCrudOperations = new AccountCrudOperations();
        Account creditor = accountCrudOperations.findById(creditorId);
        AccountCrudOperations accountCrudOperations1 = new AccountCrudOperations();
        Account debtor = accountCrudOperations1.findById(debtorId);
        int randomInt = new Random().nextInt(1001);

        if (creditor.equals(debtor)) {
            throw new RuntimeException("Le créditeur et le débiteur sont identiques.");
        }

        int creditorCurrencyId = creditor.getCurrencyId();
        int debtorCurrencyId = debtor.getCurrencyId();

        if (creditorCurrencyId == debtorCurrencyId) {
            Instant instant = Instant.now();
            Timestamp timestamp = Timestamp.from(instant);

            if (creditor.getAmount() >= amount) {

                double newCreditorAmount = creditor.getAmount() - amount;
                creditor.setAmount(newCreditorAmount);

                double newDebtorAmount = debtor.getAmount() + amount;
                debtor.setAmount(newDebtorAmount);

                Transaction transactionCreditor = new Transaction(
                        randomInt,
                        "transfer",
                        amount,
                        timestamp,
                        12
                );
                AccountCrudOperations accountCrudOperations2 = new AccountCrudOperations();
                accountCrudOperations2.doTransaction(transactionCreditor, creditor.getId());
                int randomInt2 = new Random().nextInt(1001);

                Transaction transactionDebtor = new Transaction(
                        randomInt2,
                        "transfer",
                        amount,
                        timestamp,
                        11
                );
                AccountCrudOperations accountCrudOperations3 = new AccountCrudOperations();
                accountCrudOperations3.doTransaction(transactionDebtor, debtor.getId());

                return Arrays.asList(creditor, debtor);
            } else {
                throw new RuntimeException("Le créditeur n'a pas suffisamment de fonds pour la transaction.");
            }
        } else {
            throw new UnsupportedOperationException("Transfert entre devises différentes non pris en charge pour le moment.");
        }
    }
}
