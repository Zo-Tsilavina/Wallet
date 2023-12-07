
package DAO;

import JDBC.ConnectionDB;
import models.Account;
import models.Currency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyCrudOperations implements CrudOperations<Currency>{
    private ConnectionDB connectionDB;

    public CurrencyCrudOperations() {
        this.connectionDB = new ConnectionDB();
    }
    @Override
    public List<Currency> findAll() {
        List<Currency> currencies = new ArrayList<>();
        try (
                Connection connection = connectionDB.getConnection();
                PreparedStatement statement= connection.prepareStatement("SELECT * FROM currency");
                ResultSet resultSet = statement.executeQuery()
        ){
            while (resultSet.next()){
                Currency currency = new Currency(
                        resultSet.getInt("currency_id"),
                        resultSet.getString("name"),
                        resultSet.getString("code")
                );
                currencies.add(currency);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currencies;
    }

    @Override
    public Currency save(Currency currency) {
        try (
                Connection connection = connectionDB.getConnection();
                PreparedStatement selectStatement = connection.prepareStatement(
                        "SELECT * FROM currency WHERE name = ? AND code = ?"
                )
        ) {
            selectStatement.setString(1, currency.getName());
            selectStatement.setString(2, currency.getCode());

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
                            "INSERT INTO currency (name, code) VALUES (?, ?)"
                    )) {
                        insertStatement.setString(1, currency.getName());
                        insertStatement.setString(2, currency.getCode());

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
