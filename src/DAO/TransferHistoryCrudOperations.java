package DAO;

import AutoCrudOperation.AutoCrudOp;
import JDBC.ConnectionDB;
import models.Account;
import models.TransferHistory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransferHistoryCrudOperations implements CrudOperations<TransferHistory> {
    private final ConnectionDB connectionDB;

    public TransferHistoryCrudOperations() {
        this.connectionDB = new ConnectionDB();
    }
    @Override
    public List<TransferHistory> findAll() throws SQLException {
        AutoCrudOp<TransferHistory> autoCrudOp = new AutoCrudOp<>(TransferHistory.class);
        return autoCrudOp.findAll();
    }

    @Override
    public TransferHistory findById(int id) {
        TransferHistory transferHistory = null;

        try (Connection connection = connectionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM transfer_history WHERE id = ?");

        ) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            String transferHistoryIdCol = "id";
            String debtorTransactionIdCol = "debtor_transaction_id";
            String creditorTransactionIdCol = "creditor_transaction_id";
            String transferDateCol = "transfer_date";
            transferHistory = new TransferHistory(

                    resultSet.getInt(transferHistoryIdCol),
                    resultSet.getInt(debtorTransactionIdCol),
                    resultSet.getInt(creditorTransactionIdCol),
                    resultSet.getTimestamp(transferDateCol)

            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transferHistory;
    }

    @Override
    public TransferHistory save(TransferHistory transferHistory) {
        try (
                Connection connection = connectionDB.getConnection();
                PreparedStatement insertStatement = connection.prepareStatement(
                        
                            "INSERT INTO transfer_history (id, debtor_transaction_id ,creditor_transaction_id, transfer_date) VALUES (?, ?, ?, ?)"
                    
                )) {
                        insertStatement.setInt(1,transferHistory.getId());
                        insertStatement.setInt(2,transferHistory.getDebtorTransactionId());
                        insertStatement.setInt(3,transferHistory.getCreditorTransactionId());
                        insertStatement.setTimestamp(4, transferHistory.getTransferDate());
                        
                        insertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transferHistory;
    }
    public List<TransferHistory> getTransferHistories (Timestamp startDate, Timestamp endDate) throws SQLException {

        TransferHistoryCrudOperations transferHistoryCrudOperations = new TransferHistoryCrudOperations();
        AccountCrudOperations accountCrudOperations = new AccountCrudOperations();

        List<TransferHistory> allTransferHistory = transferHistoryCrudOperations.findAll();
        List<Account> allAccount = accountCrudOperations.findAll();

        List<TransferHistory> periodTransferHistories = new ArrayList<>();

        allTransferHistory.forEach(transferHistory -> {

            Timestamp transferHistoryDate = transferHistory.getTransferDate();

            if ((startDate.before(transferHistoryDate) || startDate.equals(transferHistoryDate))
                    && (endDate.after(transferHistoryDate) || endDate.equals(transferHistoryDate))) {

                TransactionCrudOperations transactionCrudOperations = new TransactionCrudOperations();

                Account accountDebtor = (Account) allAccount.stream().filter(account -> {
                    int debtorTransactionId = transferHistory.getDebtorTransactionId();

                    return account.getTransactionsId().contains(debtorTransactionId);
                });

                Account accountCreditor = (Account) allAccount.stream().filter(account -> {
                    int creditorTransactionId = transferHistory.getCreditorTransactionId();

                    return account.getTransactionsId().contains(creditorTransactionId);
                });

                Double transferAmount = transactionCrudOperations.findById(transferHistory.getCreditorTransactionId()).getValue();

                Timestamp transferDate = transferHistory.getTransferDate();

                System.out.println(
                        "i) Le compte débiteur: "+ accountDebtor +

                                "\n ii) Le compte créditeur: "+ accountCreditor +

                                "\n iii) Le montant du transfert"+ transferAmount +

                                "\n iv) La date du transfert"+ transferDate
                );
                periodTransferHistories.add(transferHistory);
            }
        });

        return periodTransferHistories;
    }
}
