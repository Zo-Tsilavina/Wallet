package DAO;

import JDBC.ConnectionDB;
import models.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionCrudOperations implements CrudOperations<Transaction>{
    private ConnectionDB connectionDB;

    public TransactionCrudOperations() {
        this.connectionDB = new ConnectionDB();
    }


    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        try (
                Connection connection = connectionDB.getConnection();
                PreparedStatement statement= connection.prepareStatement("SELECT * FROM transactions");
                ResultSet resultSet = statement.executeQuery()
        ){
            while (resultSet.next()){
                Transaction transaction = new Transaction(
                        resultSet.getInt("transaction_id"),
                        resultSet.getString("label"),
                        resultSet.getDouble("value"),
                        resultSet.getTimestamp("date_time_transaction"),
                        resultSet.getString("type_transaction")
                );
                transactions.add(transaction);
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public Transaction save(Transaction transaction) {
        try (
                Connection connection = connectionDB.getConnection();
                PreparedStatement selectStatement = connection.prepareStatement(
                        "SELECT * FROM transactions WHERE label = ? AND value = ? AND date_time_transaction = ? AND type_transaction = ?"
                )
        ) {
            selectStatement.setString(1, transaction.getLabel());
            selectStatement.setDouble(2, transaction.getValue());
            selectStatement.setTimestamp(3, transaction.getDateTimeTransaction());
            selectStatement.setString(4, transaction.getTypeTransaction());

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    try (PreparedStatement updateStatement = connection.prepareStatement(
                            "UPDATE transactions SET value = ?, date_time_transaction = ? WHERE label = ? AND type_transaction = ?"
                    )) {
                        updateStatement.setDouble(1, transaction.getValue());
                        updateStatement.setTimestamp(2, transaction.getDateTimeTransaction());
                        updateStatement.setString(3, transaction.getLabel());
                        updateStatement.setString(4, transaction.getTypeTransaction());

                        updateStatement.executeUpdate();
                    }
                } else {
                    try (PreparedStatement insertStatement = connection.prepareStatement(
                            "INSERT INTO transactions (label, value, date_time_transaction, type_transaction) VALUES (?, ?, ?, ?)"
                    )) {
                        insertStatement.setString(1, transaction.getLabel());
                        insertStatement.setDouble(2, transaction.getValue());
                        insertStatement.setTimestamp(3, transaction.getDateTimeTransaction());
                        insertStatement.setString(4, transaction.getTypeTransaction());


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
