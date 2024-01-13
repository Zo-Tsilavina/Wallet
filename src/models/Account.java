package models;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

public class Account {

    private int id;
    private String name;
    private double amount;
    private Timestamp lastUpdateDate;
    private List<Integer> transactionsId;
    private int currencyId;
    private String type;

    public Account() {
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id && Double.compare(account.amount, amount) == 0 && currencyId == account.currencyId && Objects.equals(name, account.name) && Objects.equals(lastUpdateDate, account.lastUpdateDate) && Objects.equals(transactionsId, account.transactionsId) && Objects.equals(type, account.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, amount, lastUpdateDate, transactionsId, currencyId, type);
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
