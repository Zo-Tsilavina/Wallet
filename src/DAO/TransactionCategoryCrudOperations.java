package DAO;

import AutoCrudOperation.AutoCrudOp;
import JDBC.ConnectionDB;
import models.Account;
import models.Transaction;
import models.TransactionCategory;

import java.sql.*;
import java.util.*;

public class TransactionCategoryCrudOperations implements CrudOperations<TransactionCategory> {
    private final ConnectionDB connectionDB;

    public TransactionCategoryCrudOperations() {
        this.connectionDB = new ConnectionDB();
    }


    @Override
    public List<TransactionCategory> findAll() throws SQLException {
        AutoCrudOp<TransactionCategory> autoCrudOp = new AutoCrudOp<>(TransactionCategory.class);
        return autoCrudOp.findAll();
    }

    @Override
    public TransactionCategory findById(int id) throws SQLException {
        AutoCrudOp<TransactionCategory> autoCrudOp = new AutoCrudOp<>(TransactionCategory.class);
        return autoCrudOp.findById(id);
    }

    @Override
    public TransactionCategory save(TransactionCategory transactionCategory) {
        AutoCrudOp<TransactionCategory> autoCrudOp = new AutoCrudOp<>(TransactionCategory.class);
        return autoCrudOp.save(transactionCategory);
    }
    public Map<String, Double> allAccountTransactionByCategory (int accountId, Timestamp startDate, Timestamp endDate) throws SQLException {

            AccountCrudOperations accountCrudOperations = new AccountCrudOperations();
            Account account = accountCrudOperations.findById(accountId);
        List<Integer> allTransactionId = account.getTransactionsId();
        Map<String, Double> allAccountTransactionByCategory = new HashMap<>();

            allTransactionId.forEach(transactionId -> {

                TransactionCrudOperations transactionCrudOperations = new TransactionCrudOperations();
                Transaction currentTransaction = null;

                try {
                    currentTransaction = transactionCrudOperations.findById(transactionId);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                assert currentTransaction != null;
                int currentTransactionCategoryId = currentTransaction.getTransactionCategoryId();

                TransactionCategoryCrudOperations transactionCategoryCrudOperations = new TransactionCategoryCrudOperations();
                TransactionCategory currentTransactionCategory = null;

                try {
                    currentTransactionCategory = transactionCategoryCrudOperations.findById(currentTransactionCategoryId);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                String currentTransactionCategoryName = currentTransactionCategory.getName();
                Timestamp currentTransactionTimestamp = currentTransaction.getDateTimeTransaction();

                if ((startDate.before(currentTransactionTimestamp) || startDate.equals(currentTransactionTimestamp))
                        && (endDate.after(currentTransactionTimestamp) || endDate.equals(currentTransactionTimestamp))) {

                    if (allAccountTransactionByCategory.containsKey(currentTransactionCategoryName)) {

                        Double oldValue = allAccountTransactionByCategory.get(currentTransactionCategoryName);
                        allAccountTransactionByCategory.put(currentTransactionCategoryName, oldValue + currentTransaction.getValue());

                    }else {
                        allAccountTransactionByCategory.put(currentTransactionCategoryName, currentTransaction.getValue());
                    }
                }
            });
        return allAccountTransactionByCategory;
    }
}
