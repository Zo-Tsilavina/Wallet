package DAO;

import AutoCrudOperation.AutoCrudOp;
import JDBC.ConnectionDB;
import models.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TransactionCrudOperations implements CrudOperations<Transaction>{
    private final ConnectionDB connectionDB;

    public TransactionCrudOperations() {
        this.connectionDB = new ConnectionDB();
    }

    @Override
    public List<Transaction> findAll() throws SQLException {
        AutoCrudOp<Transaction> autoCrudOp = new AutoCrudOp<>(Transaction.class);
        return autoCrudOp.findAll();
    }

    @Override
    public Transaction findById(int id) {
        Transaction transaction = null;
        try (Connection connection = connectionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM transaction WHERE id = ?");

        ) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String transactionLabelCol = "label";
                    String transactionIdCol = "id";
                    String transactionValueCol = "value";
                    String transactionDateCol = "date_time_transaction";
                    String transactionCategoryIdCol = "transaction_category_id";
                    transaction = new Transaction(
                            resultSet.getInt(transactionIdCol),
                            resultSet.getString(transactionLabelCol),
                            resultSet.getDouble(transactionValueCol),
                            resultSet.getTimestamp(transactionDateCol),
                            resultSet.getInt(transactionCategoryIdCol)
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transaction;
    }

    @Override
    public Transaction save(Transaction transaction) {
        try (
                Connection connection = connectionDB.getConnection();
                PreparedStatement selectStatement = connection.prepareStatement(
                        "SELECT * FROM transaction WHERE id = ? AND label = ? AND value = ? AND date_time_transaction = ? AND transaction_category_id = ?"
                )
        ) {
            selectStatement.setInt(1, transaction.getId());
            selectStatement.setString(2, transaction.getLabel());
            selectStatement.setDouble(3, transaction.getValue());
            selectStatement.setTimestamp(4, transaction.getDateTimeTransaction());
            selectStatement.setInt(5, transaction.getTransactionCategoryId());

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    try (PreparedStatement updateStatement = connection.prepareStatement(
                            "UPDATE transaction SET value = ?, date_time_transaction = ? WHERE id = ? AND label = ? AND transaction_category_id = ?"
                    )) {
                        updateStatement.setDouble(1, transaction.getValue());
                        updateStatement.setTimestamp(2, transaction.getDateTimeTransaction());
                        updateStatement.setInt(3,transaction.getId());
                        updateStatement.setString(4, transaction.getLabel());
                        updateStatement.setInt(5, transaction.getTransactionCategoryId());

                        updateStatement.executeUpdate();
                    }
                } else {
                    try (PreparedStatement insertStatement = connection.prepareStatement(
                            "INSERT INTO transaction (id, label, value, date_time_transaction, transaction_category_id) VALUES (?, ?, ?, ?, ?)"
                    )) {
                        insertStatement.setInt(1,transaction.getId());
                        insertStatement.setString(2, transaction.getLabel());
                        insertStatement.setDouble(3, transaction.getValue());
                        insertStatement.setTimestamp(4, transaction.getDateTimeTransaction());
                        insertStatement.setInt(5, transaction.getTransactionCategoryId());


                        insertStatement.executeUpdate();
                    }
                }
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transaction;
    }
}
