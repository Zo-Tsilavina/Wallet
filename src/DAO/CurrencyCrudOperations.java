
package DAO;

import AutoCrudOperation.AutoCrudOp;
import JDBC.ConnectionDB;
import models.Account;
import models.Currency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CurrencyCrudOperations implements CrudOperations<Currency> {
    private final ConnectionDB connectionDB;

    public CurrencyCrudOperations() {
        this.connectionDB = new ConnectionDB();
    }

    @Override
    public List<Currency> findAll() throws SQLException {
        AutoCrudOp<Currency> autoCrudOp = new AutoCrudOp<>(Currency.class);
        return autoCrudOp.findAll();
    }

    @Override
    public Currency findById(int id) throws SQLException {
        AutoCrudOp<Currency> autoCrudOp = new AutoCrudOp<>(Currency.class);
        return autoCrudOp.findById(id);
    }

    @Override
    public Currency save(Currency currency) {
        AutoCrudOp<Currency> autoCrudOp = new AutoCrudOp<>(Currency.class);
        return autoCrudOp.save(currency);
    }
}
