package repository;

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
    private static ConnectionDB connectionDB;

    public CurrencyCrudOperations(ConnectionDB connectionDB) {
        this.connectionDB = connectionDB;
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
                        resultSet.getString("symbol")
                );
                currencies.add(currency);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currencies;
    }

    @Override
    public void create(Currency currency) {
        try(
                Connection connection = connectionDB.getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO currency (name, symbol) VALUES (?, ?)")
        ) {
            statement.setString(1, currency.getName());
            statement.setString(2, currency.getSymbol());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateById(Currency currency, int id) {
        try (Connection connection = connectionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE currency SET name = ?, symbol = ? WHERE currency_id = ?");
        ){
            statement.setString(1, currency.getName());
            statement.setString(2, currency.getSymbol());
            statement.setInt(3, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
