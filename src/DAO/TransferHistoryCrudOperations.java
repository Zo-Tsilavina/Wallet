package DAO;

import JDBC.ConnectionDB;
import models.Account;
import models.TransferHistory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransferHistoryCrudOperations implements CrudOperations<TransferHistory> {
    private ConnectionDB connectionDB;
    private final String transferHistoryIdCol = "transfer_history_id";
    private final String debtorTransactionIdCol = "debtor_transaction_id";
    private final String creditorTransactionIdCol = "creditor_transaction_id";
    private final String transferDateCol = "transfer_date";

    public TransferHistoryCrudOperations() {
        this.connectionDB = new ConnectionDB();
    }
    @Override
    public List<TransferHistory> findAll() {
        List<TransferHistory> transferHistories = new ArrayList<>();

        try (
                Connection connection = connectionDB.getConnection();
                PreparedStatement statement= connection.prepareStatement("SELECT * FROM transfer_history");
                ResultSet resultSet = statement.executeQuery()
        ){
            while (resultSet.next()){
                TransferHistory transferHistory = new TransferHistory(

                        resultSet.getInt(transferHistoryIdCol),
                        resultSet.getInt(debtorTransactionIdCol),
                        resultSet.getInt(creditorTransactionIdCol),
                        resultSet.getTimestamp(transferDateCol)

                );
                transferHistories.add(transferHistory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transferHistories;
    }

    @Override
    public TransferHistory findById(int id) {
        TransferHistory transferHistory = null;

        try (Connection connection = connectionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM transfer_history WHERE transfer_history_id = ?");

        ) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
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
                        
                            "INSERT INTO transfer_history (transfer_history_id, debtor_transaction_id ,creditor_transaction_id, transfer_date) VALUES (?, ?, ?, ?)"
                    
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
    public List<TransferHistory> getTransferHistories (Timestamp startDate, Timestamp endDate){

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
