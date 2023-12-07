package DAO;

import JDBC.ConnectionDB;
import models.Account;
import models.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountCrudOperations implements CrudOperations<Account> {
    private ConnectionDB connectionDB;

    public AccountCrudOperations() {
        this.connectionDB = new ConnectionDB();
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
                      resultSet.getDouble("amount"),
                      resultSet.getTimestamp("last_update_date"),
                      resultSet.getInt("transaction_id"),
                      resultSet.getInt("currency_id"),
                      resultSet.getString("type")
              );
              accounts.add(account);
          }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public Account save(Account account) {
        try (
                Connection connection = connectionDB.getConnection();
                PreparedStatement selectStatement = connection.prepareStatement(
                        "SELECT * FROM accounts WHERE name = ? AND transaction_id = ? AND currency_id = ? AND type = ?"
                )
        ) {
            selectStatement.setString(1, account.getName());
            selectStatement.setInt(2, account.getTransactionsId());
            selectStatement.setInt(3, account.getCurrencyId());
            selectStatement.setString(4, account.getType());

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    try (PreparedStatement updateStatement = connection.prepareStatement(
                            "UPDATE accounts SET amount = ?, last_update_date = ? WHERE name = ? AND transaction_id = ? AND currency_id = ? AND type = ?"
                    )) {
                        updateStatement.setDouble(1, account.getAmount());
                        updateStatement.setTimestamp(2, account.getLastUpdateDate());
                        updateStatement.setString(3, account.getName());
                        updateStatement.setInt(4, account.getTransactionsId());
                        updateStatement.setInt(5, account.getCurrencyId());
                        updateStatement.setString(6, account.getType());

                        updateStatement.executeUpdate();
                    }
                } else {
                    try (PreparedStatement insertStatement = connection.prepareStatement(
                            "INSERT INTO accounts (name, amount, last_update_date, transaction_id, currency_id, type) VALUES (?, ?, ?, ?, ?, ?)"
                    )) {
                        insertStatement.setString(1, account.getName());
                        insertStatement.setDouble(2, account.getAmount());
                        insertStatement.setTimestamp(3, account.getLastUpdateDate());
                        insertStatement.setInt(4, account.getTransactionsId());
                        insertStatement.setInt(5, account.getCurrencyId());
                        insertStatement.setString(6, account.getType());

                        insertStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    public Account doTransaction (Transaction transaction, Account account){

        TransactionCrudOperations transactionCrudOperations = new TransactionCrudOperations();
        AccountCrudOperations accountCrudOperations = new AccountCrudOperations();

        transactionCrudOperations.save(transaction);

        if(transaction.getTypeTransaction().equals("debit")){

            account.setAmount(account.getAmount() - transaction.getValue());
            account.setTransactionsId(transaction.getId());
            account.setLastUpdateDate(transaction.getDateTimeTransaction());
            accountCrudOperations.save(account);

        } else if (transaction.getTypeTransaction().equals("credit")) {

            account.setAmount(account.getAmount() + transaction.getValue());
            account.setTransactionsId(transaction.getId());
            accountCrudOperations.save(account);

        }
        return account;
    }
    public Account getAccountBalance (Timestamp timestamp){
        List<Transaction> transactionList = new ArrayList<>();

//        recuperer le compte concerner
//        recuperer tout les transaction lie compte conserner a partir de idTransaction
//        prendre le solde actuel du compte en question
//        remonter le solde jusqu'a la date donner c.a.d (+) pour debit et (-) pour credit

    }
    public List<Account> getAccountBalanceHistory (Timestamp startDate, Timestamp endDate, Account account){

//          recuperer le solde a la date startDate a l'aide de getAccountBalance
//          recuperer toute les transactions lie a ce compte durant le periode defini
//          recuperer le solde du compte a chaque transaction durant cette periode

    }
    public List<Account> transfert (Account crediteur , Account debiteur){

//            verifier si ce n est pas le meme :
//                - si oui : le transfert ne peut pas avoir lieu
//                - sinon:
//                    verifier si les deux comptes ont le meme devise
//                        -si oui:
//                            cree une nouvelle transaction pour chaque compte
//                                (type debit pour le crediteur et de type credit pour l'autre)
//                            effectuer un doTransaction() pour chaque compte pour mettre a jour leur solde
//
//                                    pour la fonction pour l'historique :
//                                         ... (cree l'entity d'abord)
//                        -si non:
//

    }
}
