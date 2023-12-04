package models;

import java.util.Objects;

public class Account {
    private String name;
    private int currencyId;

    public Account(String name, int currencyId) {
        this.name = name;
        this.currencyId = currencyId;
    }

    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", currencyId=" + currencyId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return currencyId == account.currencyId && Objects.equals(name, account.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, currencyId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }
}
