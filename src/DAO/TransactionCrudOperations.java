package DAO;

import AutoCrudOperation.AutoCrudOp;
import JDBC.ConnectionDB;
import models.Account;
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
    public Transaction findById(int id) throws SQLException {
        AutoCrudOp<Transaction> autoCrudOp = new AutoCrudOp<>(Transaction.class);
        return autoCrudOp.findById(id);
    }

    @Override
    public Transaction save(Transaction transaction) {
        AutoCrudOp<Transaction> autoCrudOp = new AutoCrudOp<>(Transaction.class);
        return autoCrudOp.save(transaction);
    }
}
