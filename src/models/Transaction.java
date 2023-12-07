package models;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Objects;

public class Transaction {
    private int id;
    private String label;
    private Double value;
    private LocalDate dateTimeTransaction;
    private String typeTransaction;

    public Transaction(int id, String label, Double value, LocalDate dateTimeTransaction, String typeTransaction) {
        this.id = id;
        this.label = label;
        this.value = value;
        this.dateTimeTransaction = dateTimeTransaction;
        this.typeTransaction = typeTransaction;
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

    public LocalDate getDateTimeTransaction() {
        return dateTimeTransaction;
    }

    public void setDateTimeTransaction(LocalDate dateTimeTransaction) {
        this.dateTimeTransaction = dateTimeTransaction;
    }

    public String getTypeTransaction() {
        return typeTransaction;
    }

    public void setTypeTransaction(String typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id == that.id && Objects.equals(label, that.label) && Objects.equals(value, that.value) && Objects.equals(dateTimeTransaction, that.dateTimeTransaction) && Objects.equals(typeTransaction, that.typeTransaction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label, value, dateTimeTransaction, typeTransaction);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", value=" + value +
                ", dateTimeTransaction=" + dateTimeTransaction +
                ", typeTransaction='" + typeTransaction + '\'' +
                '}';
    }
}
