
package DAO;

import AutoCrudOperation.AutoCrudOp;
import JDBC.ConnectionDB;
import models.Currency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CurrencyCrudOperations implements CrudOperations<Currency>{
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
    public Currency findById(int id) {
        Currency currency = null;
        try (Connection connection = connectionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM currency WHERE id = ?");

        ) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                currency = new Currency(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("code")
                );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currency;
    }

    @Override
    public Currency save(Currency currency) {
        try (
                Connection connection = connectionDB.getConnection();
                PreparedStatement selectStatement = connection.prepareStatement(
                        "SELECT * FROM currency WHERE id = ? AND name = ? AND code = ?"
                )
        ) {
            selectStatement.setInt(1,currency.getId());
            selectStatement.setString(2, currency.getName());
            selectStatement.setString(3, currency.getCode());

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    try (PreparedStatement updateStatement = connection.prepareStatement(
                            "UPDATE currency SET name = ?, code = ?"
                    )) {
                        updateStatement.setString(1, currency.getName());
                        updateStatement.setString(2, currency.getCode());

                        updateStatement.executeUpdate();
                    }
                } else {
                    try (PreparedStatement insertStatement = connection.prepareStatement(
                            "INSERT INTO currency (id, name, code) VALUES (?, ?, ?)"
                    )) {
                        insertStatement.setInt(1,currency.getId());
                        insertStatement.setString(2, currency.getName());
                        insertStatement.setString(3, currency.getCode());

                        insertStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currency;
    }
}
