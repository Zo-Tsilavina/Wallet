package repository;

import JDBC.ConnectionDB;
import models.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionCrudOperations implements CrudOperations<Transaction>{
    private static ConnectionDB connectionDB;

    public TransactionCrudOperations(ConnectionDB connectionDB) {
        this.connectionDB = connectionDB;
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        try (
                Connection connection = connectionDB.getConnection();
                PreparedStatement statement= connection.prepareStatement("SELECT * FROM transaction");
                ResultSet resultSet = statement.executeQuery()
        ){
            while (resultSet.next()){
                Transaction transaction = new Transaction(
                        resultSet.getInt("transaction_id"),
                        resultSet.getDouble("value"),
                        resultSet.getString("description"),
                        resultSet.getInt("account_id"),
                        resultSet.getTimestamp("date_transaction"),
                        resultSet.getString("type")
                );
                transactions.add(transaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public void create(Transaction transaction) {
        try(
                Connection connection = connectionDB.getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO transaction (value, description, account_id, type, date_transaction) VALUES (?, ?, ?, ?, ?)")
        ) {
            statement.setDouble(1, transaction.getValue());
            statement.setString(2, transaction.getDescription());
            statement.setInt(3, transaction.getAccountId());
            statement.setString(4, transaction.getType());
            statement.setTimestamp(5, transaction.getDate_transaction());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateById(Transaction transaction, int id) {
        try (Connection connection = connectionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE transaction SET value = ?, description = ?, account_id= ?, type = ?, date_transaction = ? WHERE transaction_id = ?");
        ){
            statement.setDouble(1, transaction.getValue());
            statement.setString(2, transaction.getDescription());
            statement.setInt(3, transaction.getAccountId());
            statement.setString(4, transaction.getType());
            statement.setTimestamp(5, transaction.getDate_transaction());
            statement.setInt(6, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
