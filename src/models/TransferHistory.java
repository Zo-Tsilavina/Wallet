package models;

import java.sql.Timestamp;
import java.util.Objects;

public class TransferHistory {
    private int id;
    private int debtorTransactionId;
    private int creditorTransactionId;
    private Timestamp transferDate;

    public TransferHistory() {
    }

    public TransferHistory(int id, int debtorTransactionId, int creditorTransactionId, Timestamp transferDate) {
        this.id = id;
        this.debtorTransactionId = debtorTransactionId;
        this.creditorTransactionId = creditorTransactionId;
        this.transferDate = transferDate;
    }

    @Override
    public String toString() {
        return "TransferHistory{" +
                "id=" + id +
                ", debtorTransactionId=" + debtorTransactionId +
                ", creditorTransactionId=" + creditorTransactionId +
                ", transferDate=" + transferDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferHistory that = (TransferHistory) o;
        return id == that.id && debtorTransactionId == that.debtorTransactionId && creditorTransactionId == that.creditorTransactionId && Objects.equals(transferDate, that.transferDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, debtorTransactionId, creditorTransactionId, transferDate);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDebtorTransactionId() {
        return debtorTransactionId;
    }

    public void setDebtorTransactionId(int debtorTransactionId) {
        this.debtorTransactionId = debtorTransactionId;
    }

    public int getCreditorTransactionId() {
        return creditorTransactionId;
    }

    public void setCreditorTransactionId(int creditorTransactionId) {
        this.creditorTransactionId = creditorTransactionId;
    }

    public Timestamp getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Timestamp transferDate) {
        this.transferDate = transferDate;
    }
}
