package DAO;

import JDBC.ConnectionDB;
import models.Account;
import models.Transaction;
import models.TransactionCategory;

import java.sql.*;
import java.util.*;

public class TransactionCategoryCrudOperations implements CrudOperations<TransactionCategory> {
    private ConnectionDB connectionDB;

    public TransactionCategoryCrudOperations() {
        this.connectionDB = new ConnectionDB();
    }
    private final String transactionCategoryIdCol = "transaction_category_id";
    private final String transactionCategoryNameCol = "name";
    private final String transactionCategoryTypeCol = "type";



    @Override
    public List<TransactionCategory> findAll() {
        List<TransactionCategory> transactionsCategories = new ArrayList<>();

        try (
                Connection connection = connectionDB.getConnection();
                PreparedStatement statement= connection.prepareStatement("SELECT * FROM transaction_categories");
                ResultSet resultSet = statement.executeQuery()
        ){
            while (resultSet.next()){
                TransactionCategory transactionCategory = new TransactionCategory(
                        resultSet.getInt(transactionCategoryIdCol),
                        resultSet.getString(transactionCategoryNameCol),
                        resultSet.getString(transactionCategoryTypeCol)
                );
                transactionsCategories.add(transactionCategory);
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactionsCategories;
    }

    @Override
    public TransactionCategory findById(int id) {
        TransactionCategory transactionCategory = null;
        try (Connection connection = connectionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM transaction_categories WHERE transaction_category_id = ?");

        ) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    transactionCategory = new TransactionCategory(
                            resultSet.getInt(transactionCategoryIdCol),
                            resultSet.getString(transactionCategoryNameCol),
                            resultSet.getString(transactionCategoryTypeCol)
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactionCategory;
    }

    @Override
    public TransactionCategory save(TransactionCategory transactionCategory) {
        try (
                Connection connection = connectionDB.getConnection();
                PreparedStatement selectStatement = connection.prepareStatement(
                        "SELECT * FROM transaction_categories WHERE transaction_category_id = ? AND name = ?"
                )
        ) {
            selectStatement.setInt(1, transactionCategory.getId());
            selectStatement.setString(2, transactionCategory.getName());

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("this transaction category already exist");
                    return transactionCategory;
                } else {
                    try (PreparedStatement insertStatement = connection.prepareStatement(
                            "INSERT INTO transaction_categories (transaction_category_id, name, type) VALUES (?, ?, ?)"
                    )) {
                        insertStatement.setInt(1, transactionCategory.getId());
                        insertStatement.setString(2, transactionCategory.getName());
                        insertStatement.setString(3,transactionCategory.getType());

                        insertStatement.executeUpdate();
                    }
                }
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactionCategory;
    }
    public Map<String, Double> allAccountTransactionByCategory (int accountId, Timestamp startDate, Timestamp endDate){

            AccountCrudOperations accountCrudOperations = new AccountCrudOperations();
            Account account = accountCrudOperations.findById(accountId);
        List<Integer> allTransactionId = account.getTransactionsId();
        Map<String, Double> allAccountTransactionByCategory = new HashMap<>();

            allTransactionId.forEach(transactionId -> {

                TransactionCrudOperations transactionCrudOperations = new TransactionCrudOperations();
                Transaction currentTransaction = transactionCrudOperations.findById(transactionId);
                int currentTransactionCategoryId = currentTransaction.getTransactionCategoryId();

                TransactionCategoryCrudOperations transactionCategoryCrudOperations = new TransactionCategoryCrudOperations();
                TransactionCategory currentTransactionCategory = transactionCategoryCrudOperations.findById(currentTransactionCategoryId);
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
