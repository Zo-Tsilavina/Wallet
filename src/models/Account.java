package models;

import java.sql.Timestamp;
import java.util.List;

public class Account {

    private int id;
    private String name;
    private double amount;
    private Timestamp lastUpdateDate;
    private List<Integer> transactionsId;
    private int currencyId;
    private String type;

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", lastUpdateDate=" + lastUpdateDate +
                ", transactionsId=" + transactionsId +
                ", currencyId=" + currencyId +
                ", type='" + type + '\'' +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setLastUpdateDate(Timestamp lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public void setTransactionsId(List<Integer> transactionsId) {
        this.transactionsId = transactionsId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public Timestamp getLastUpdateDate() {
        return lastUpdateDate;
    }

    public List<Integer> getTransactionsId() {
        return transactionsId;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public String getType() {
        return type;
    }

    public Account(int id, String name, double amount, Timestamp lastUpdateDate, List<Integer> transactionsId, int currencyId, String type) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.lastUpdateDate = lastUpdateDate;
        this.transactionsId = transactionsId;
        this.currencyId = currencyId;
        this.type = type;
    }
}
