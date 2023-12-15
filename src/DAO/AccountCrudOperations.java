package DAO;

import JDBC.ConnectionDB;
import models.Account;
import models.Transaction;
import models.TransactionCategory;
import models.TransferHistory;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class AccountCrudOperations implements CrudOperations<Account> {
    private ConnectionDB connectionDB;

    public AccountCrudOperations() {
        this.connectionDB = new ConnectionDB();
    }

    @Override
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        try (
                Connection connection = connectionDB.getConnection();
                PreparedStatement statement= connection.prepareStatement("SELECT * FROM accounts");
                ResultSet resultSet = statement.executeQuery()
        ){
          while (resultSet.next()){

              String transactionsId = resultSet.getString("transactions_id");
              Account account = new Account(
                      resultSet.getInt("account_id"),
                      resultSet.getString("name"),
                      resultSet.getDouble("amount"),
                      resultSet.getTimestamp("last_update_date"),

                      Arrays.stream(transactionsId.split(","))
                              .map(Integer::parseInt)
                              .collect(Collectors.toList()),

                      resultSet.getInt("currency_id"),
                      resultSet.getString("type")
              );
              accounts.add(account);
              connection.close();
          }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public Account findById(int id) {
        Account account = null;
        try (Connection connection = connectionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts WHERE account_id = ?");
        ) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String transactionsId = resultSet.getString("transactions_id");
                    account = new Account(
                            resultSet.getInt("account_id"),
                            resultSet.getString("name"),
                            resultSet.getDouble("amount"),
                            resultSet.getTimestamp("last_update_date"),

                            Arrays.stream(transactionsId.split(","))
                                    .map(Integer::parseInt)
                                    .collect(Collectors.toList()),

                            resultSet.getInt("currency_id"),
                            resultSet.getString("type")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }


    @Override
    public Account save(Account account) {
        try (
                Connection connection = connectionDB.getConnection();
                PreparedStatement selectStatement = connection.prepareStatement(
                        "SELECT * FROM accounts WHERE name = ? AND currency_id = ? AND type = ?"
                )
        ) {
            selectStatement.setString(1, account.getName());
//            selectStatement.setString(2, account.getTransactionsId()
//                    .stream()
//                    .map(Object::toString)
//                    .collect(Collectors.joining(",")));
            selectStatement.setInt(2, account.getCurrencyId());
            selectStatement.setString(3, account.getType());

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    try (PreparedStatement updateStatement = connection.prepareStatement(
                            "UPDATE accounts SET amount = ?, transactions_id = ? , last_update_date = ? WHERE name = ? AND currency_id = ? AND type = ?"
                    )) {
                        updateStatement.setDouble(1, account.getAmount());
                        updateStatement.setString(2, account.getTransactionsId()
                                .stream()
                                .map(Object::toString)
                                .collect(Collectors.joining(",")));
                        updateStatement.setTimestamp(3, account.getLastUpdateDate());
                        updateStatement.setString(4, account.getName());

                        updateStatement.setInt(5, account.getCurrencyId());
                        updateStatement.setString(6, account.getType());

                        updateStatement.executeUpdate();
                    }
                } else {
                    try (PreparedStatement insertStatement = connection.prepareStatement(
                            "INSERT INTO accounts (name, amount, last_update_date, transactions_id, currency_id, type) VALUES (?, ?, ?, ?, ?, ?)"
                    )) {
                        insertStatement.setString(1, account.getName());
                        insertStatement.setDouble(2, account.getAmount());
                        insertStatement.setTimestamp(3, account.getLastUpdateDate());
                        insertStatement.setString(4, account.getTransactionsId()
                                .stream()
                                .map(Object::toString)
                                .collect(Collectors.joining(",")));
                        insertStatement.setInt(5, account.getCurrencyId());
                        insertStatement.setString(6, account.getType());

                        insertStatement.executeUpdate();
                    }
                }
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    public Account doTransaction (Transaction transaction, int accountId){

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

    public Account getAccountBalance (Timestamp timestamp, int accountId){
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

    public List<Account> getAccountBalanceHistory (Timestamp startDate, Timestamp endDate, int accountId){

//          recuperer le solde a la date startDate a l'aide de getAccountBalance
//          recuperer toute les transactions lie a ce compte durant le periode defini
//          recuperer le solde du compte a chaque transaction durant cette periode

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
    public List<Account> transfer(int creditorId, int debtorId, double amount) {

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

    public List<TransferHistory> getTransferHistories (Timestamp startDate, Timestamp endDate){

        TransferHistoryCrudOperations transferHistoryCrudOperations = new TransferHistoryCrudOperations();
        AccountCrudOperations accountCrudOperations = new AccountCrudOperations();

        List<TransferHistory> allTransferHistory = transferHistoryCrudOperations.findAll();
        List<Account> allAccount = accountCrudOperations.findAll();

        List<TransferHistory> periodTransferHistories = new ArrayList<>();

        allTransferHistory.forEach(transferHistory -> {

            Timestamp transferHistoryDate = transferHistory.getTransferDate();

            if ((startDate.before(transferHistoryDate) || startDate.equals(transferHistoryDate))
                    && (endDate.after(transferHistoryDate) || endDate.equals(transferHistoryDate))) {

                TransactionCrudOperations transactionCrudOperations = new TransactionCrudOperations();

                Account accountDebtor = (Account) allAccount.stream().filter(account -> {
                    int debtorTransactionId = transferHistory.getDebtorTransactionId();

                    return account.getTransactionsId().contains(debtorTransactionId);
                });

                Account accountCreditor = (Account) allAccount.stream().filter(account -> {
                    int creditorTransactionId = transferHistory.getCreditorTransactionId();

                    return account.getTransactionsId().contains(creditorTransactionId);
                });

                Double transferAmount = transactionCrudOperations.findById(transferHistory.getCreditorTransactionId()).getValue();

                Timestamp transferDate = transferHistory.getTransferDate();

                System.out.println(
                        "i) Le compte débiteur: "+ accountDebtor +

                                "\n ii) Le compte créditeur: "+ accountCreditor +

                                "\n iii) Le montant du transfert"+ transferAmount +

                                "\n iv) La date du transfert"+ transferDate
                );
                periodTransferHistories.add(transferHistory);
            }
        });

        return periodTransferHistories;
    }
}
