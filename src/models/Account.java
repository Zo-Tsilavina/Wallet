package models;

import java.time.LocalDate;

public class Account {
    private int id;
    private String name;
    private double amount;
    private LocalDate lastUpdateDate;
    private int transactionsId;
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

    public void setLastUpdateDate(LocalDate lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public void setTransactionsId(int transactionsId) {
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

    public LocalDate getLastUpdateDate() {
        return lastUpdateDate;
    }

    public int getTransactionsId() {
        return transactionsId;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public String getType() {
        return type;
    }

    public Account(int id, String name, double amount, LocalDate lastUpdateDate, int transactionsId, int currencyId, String type) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.lastUpdateDate = lastUpdateDate;
        this.transactionsId = transactionsId;
        this.currencyId = currencyId;
        this.type = type;
    }
}
