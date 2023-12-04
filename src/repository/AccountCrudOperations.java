package repository;

import JDBC.ConnectionDB;
import models.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountCrudOperations implements CrudOperations<Account>{
    private static ConnectionDB connectionDB;

    public AccountCrudOperations() {
        this.connectionDB = connectionDB;
    }

    @Override
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        try (
                Connection connection = connectionDB.getConnection();
                PreparedStatement statement= connection.prepareStatement("SELECT * FROM accounts");
                ResultSet resultSet = statement.executeQuery()
        ){
          while (resultSet.next()){
              Account account = new Account(
                      resultSet.getInt("account_id"),
                      resultSet.getString("name"),
                      resultSet.getInt("current_id")
              );
              accounts.add(account);
          }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public void create(Account account) {
        try(
                Connection connection = connectionDB.getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO accounts (name, currency_id) VALUES (?, ?)")
        ) {
            statement.setString(1, account.getName());
            statement.setInt(2, account.getCurrencyId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateById(Account account, int id) {
        try (Connection connection = connectionDB.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE accounts SET name = ? WHERE account_id = ?");
        ){
            statement.setString(1, account.getName());
            statement.setInt(2, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
