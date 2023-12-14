package models;

import java.sql.Timestamp;
import java.util.Objects;

public class Transaction {
    private int id;
    private String label;
    private Double value;
    private Timestamp dateTimeTransaction;
    private int transactionCategoryId;

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", value=" + value +
                ", dateTimeTransaction=" + dateTimeTransaction +
                ", transactionCategoryId=" + transactionCategoryId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id == that.id && transactionCategoryId == that.transactionCategoryId && Objects.equals(label, that.label) && Objects.equals(value, that.value) && Objects.equals(dateTimeTransaction, that.dateTimeTransaction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label, value, dateTimeTransaction, transactionCategoryId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Timestamp getDateTimeTransaction() {
        return dateTimeTransaction;
    }

    public void setDateTimeTransaction(Timestamp dateTimeTransaction) {
        this.dateTimeTransaction = dateTimeTransaction;
    }

    public int getTransactionCategoryId() {
        return transactionCategoryId;
    }

    public void setTransactionCategoryId(int transactionCategoryId) {
        this.transactionCategoryId = transactionCategoryId;
    }

    public Transaction(int id, String label, Double value, Timestamp dateTimeTransaction, int transactionCategoryId) {
        this.id = id;
        this.label = label;
        this.value = value;
        this.dateTimeTransaction = dateTimeTransaction;
        this.transactionCategoryId = transactionCategoryId;
    }
}
