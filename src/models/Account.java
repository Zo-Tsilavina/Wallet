package models;

import java.time.LocalDate;

public class Account {
    private int id;
    private String nom;
    private double amount;
    private LocalDate lastUpdateDate;
    private int transactionsId;
    private int currencyId;
    private String type;

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", amount=" + amount +
                ", lastUpdateDate=" + lastUpdateDate +
                ", transactionsId=" + transactionsId +
                ", currencyId=" + currencyId +
                ", type='" + type + '\'' +
                '}';
    }

    public void setNom(String nom) {
        this.nom = nom;
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

    public String getNom() {
        return nom;
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

    public Account(int id, String nom, double amount, LocalDate lastUpdateDate, int transactionsId, int currencyId, String type) {
        this.id = id;
        this.nom = nom;
        this.amount = amount;
        this.lastUpdateDate = lastUpdateDate;
        this.transactionsId = transactionsId;
        this.currencyId = currencyId;
        this.type = type;
    }
}
