package DAO;

import AutoCrudOperation.AutoCrudOp;
import JDBC.ConnectionDB;
import models.Account;
import models.Transaction;
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
    public TransferHistory findById(int id) throws SQLException {
        AutoCrudOp<TransferHistory> autoCrudOp = new AutoCrudOp<>(TransferHistory.class);
        return autoCrudOp.findById(id);
    }

    @Override
    public TransferHistory save(TransferHistory transferHistory) {
        AutoCrudOp<TransferHistory> autoCrudOp = new AutoCrudOp<>(TransferHistory.class);
        return autoCrudOp.save(transferHistory);
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

                Double transferAmount = null;
                try {
                    transferAmount = transactionCrudOperations.findById(transferHistory.getCreditorTransactionId()).getValue();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

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
