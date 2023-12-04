package models;

import java.util.Objects;

public class Currency {
    private String name;
    private String symbol;

    @Override
    public String toString() {
        return "Currency{" +
                "name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return Objects.equals(name, currency.name) && Objects.equals(symbol, currency.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, symbol);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbole() {
        return symbol;
    }

    public void setSymbole(String symbole) {
        this.symbol = symbol;
    }

    public Currency(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }
}
