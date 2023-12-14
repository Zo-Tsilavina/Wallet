package DAO;

import JDBC.ConnectionDB;
import models.Account;
import models.Transaction;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
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

    public Account doTransaction (Transaction transaction, Account account){

        TransactionCrudOperations transactionCrudOperations = new TransactionCrudOperations();
        AccountCrudOperations accountCrudOperations = new AccountCrudOperations();

            if(transaction.getTypeTransaction().equals("debit")){
                if((account.getAmount()-transaction.getValue() < 0)) {
                    if (account.getType().equals("compte bancaire")) {
                        account.setAmount(account.getAmount() - transaction.getValue());

                    }else {
                        throw new RuntimeException("transaction failed, solde insufisante");
                    }

                }else {
                    account.setAmount(account.getAmount() - transaction.getValue());
                }

            } else if (transaction.getTypeTransaction().equals("credit")) {
                account.setAmount(account.getAmount() + transaction.getValue());

            }else{
                throw new RuntimeException("transaction failed");
            }

        List<Integer> transactionIdList = account.getTransactionsId();
        transactionIdList.add(transaction.getId());
        account.setTransactionsId(transactionIdList);
        account.setLastUpdateDate(transaction.getDateTimeTransaction());
        accountCrudOperations.save(account);
        transactionCrudOperations.save(transaction);

        return account;
    }

    public Account getAccountBalance (Timestamp timestamp, Account account){

        List<Integer> currentTransactionIdList = new ArrayList<>();
        List<Integer> transactionIdList = account.getTransactionsId();

        for (int transactionId : transactionIdList) {
            currentTransactionIdList.add(transactionId);
            TransactionCrudOperations transactionCrudOperations = new TransactionCrudOperations();
            Transaction currentTransaction = transactionCrudOperations.findById(transactionId);
            Timestamp currentTransactionTimestamp = currentTransaction.getDateTimeTransaction();
            if (timestamp.after(currentTransactionTimestamp) || timestamp.equals(currentTransactionTimestamp)) {

                if (currentTransaction.getTypeTransaction().equals("credit")) {
                    account.setAmount(account.getAmount() + currentTransaction.getValue());
                    account.setLastUpdateDate(currentTransactionTimestamp);
                    account.setTransactionsId(currentTransactionIdList);
                } else if (currentTransaction.getTypeTransaction().equals("debit")) {
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

    public List<Account> getAccountBalanceHistory (Timestamp startDate, Timestamp endDate, Account account){

//          recuperer le solde a la date startDate a l'aide de getAccountBalance
//          recuperer toute les transactions lie a ce compte durant le periode defini
//          recuperer le solde du compte a chaque transaction durant cette periode

        List<Account> accounts = new ArrayList<>();
        List<Integer> transactionIdList = account.getTransactionsId();
        List<Integer> currentTransactionIdList = new ArrayList<>();

        for (int transactionId : transactionIdList) {

            TransactionCrudOperations transactionCrudOperations = new TransactionCrudOperations();
            Transaction currentTransaction = transactionCrudOperations.findById(transactionId);
            Timestamp currentTransactionTimestamp = currentTransaction.getDateTimeTransaction();

            currentTransactionIdList.add(transactionId);
            if (currentTransactionTimestamp.before(startDate)){

                if (currentTransaction.getTypeTransaction().equals("credit")) {

                    account.setAmount(account.getAmount() + currentTransaction.getValue());
                    account.setLastUpdateDate(currentTransactionTimestamp);
                    account.setTransactionsId(currentTransactionIdList);

                } else if (currentTransaction.getTypeTransaction().equals("debit")) {

                    account.setAmount(account.getAmount() - currentTransaction.getValue());
                    account.setLastUpdateDate(currentTransactionTimestamp);
                    account.setTransactionsId(currentTransactionIdList);
                }

            }else if ((startDate.before(currentTransactionTimestamp) || startDate.equals(currentTransactionTimestamp))
                    && (endDate.after(currentTransactionTimestamp) || endDate.equals(currentTransactionTimestamp))) {

                if (currentTransaction.getTypeTransaction().equals("credit")) {
                    account.setAmount(account.getAmount() + currentTransaction.getValue());
                    account.setLastUpdateDate(currentTransactionTimestamp);
                    account.setTransactionsId(currentTransactionIdList);
                } else if (currentTransaction.getTypeTransaction().equals("debit")) {

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
    public List<Account> transfer (Account creditor , Account debtor, double amount) {

        AccountCrudOperations accountCrudOperations = new AccountCrudOperations();
        TransactionCrudOperations transactionCrudOperations = new TransactionCrudOperations();

        int creditorCurrencyId = creditor.getCurrencyId();
        int debtorCurrencyId = debtor.getCurrencyId();
        int randomNumber = (int) Math.random();
        Instant instant = Instant.now();
        Timestamp timestamp = Timestamp.from(instant);

        if (creditor.equals(debtor)) {

            throw new RuntimeException();

        } else if (creditorCurrencyId == debtorCurrencyId) {

            Transaction transactionCreditor = new Transaction(
                    randomNumber,
                    "transfer",
                    amount,
                    timestamp,
                    "debit"
            );
            accountCrudOperations.doTransaction(transactionCreditor, creditor);

            Transaction transactionDebtor = new Transaction(
                    randomNumber,
                    "transfer",
                    amount,
                    timestamp,
                    "credit"
            );
            accountCrudOperations.doTransaction(transactionDebtor, debtor);

        }
//            verifier si ce n est pas le meme :
//                - si oui : le transfert ne peut pas avoir lieu
//                - sinon:
//                    verifier si les deux comptes ont le meme devise
//                        -si oui:
//                            cree une nouvelle transaction pour chaque compte
//                                (type debit pour le creditor et de type credit pour l'autre)
//                            effectuer un doTransaction() pour chaque compte pour mettre a jour leur solde
//
//                                    pour la fonction pour l'historique :
//                                         ... (cree l'entity d'abord)
//                        -si non:
        return null;
    }
}
