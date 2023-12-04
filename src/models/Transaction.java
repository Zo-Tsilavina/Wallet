package models;

import java.sql.Timestamp;
import java.util.Objects;

public class Transaction {
    private int id;
    private Double value;
    private String description;
    private int accountId;
    private String type;
    private Timestamp date_transaction;


    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", value=" + value +
                ", description='" + description + '\'' +
                ", accountId=" + accountId +
                ", date_transaction=" + date_transaction +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id == that.id && accountId == that.accountId && Objects.equals(value, that.value) && Objects.equals(description, that.description) && Objects.equals(date_transaction, that.date_transaction) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, description, accountId, date_transaction, type);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public Timestamp getDate_transaction() {
        return date_transaction;
    }

    public void setDate_transaction(Timestamp date_transaction) {
        this.date_transaction = date_transaction;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Transaction(int id, Double value, String description, int accountId, Timestamp date_transaction, String type) {
        this.id = id;
        this.value = value;
        this.description = description;
        this.accountId = accountId;
        this.date_transaction = date_transaction;
        this.type = type;
    }
}
