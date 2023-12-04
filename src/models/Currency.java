package models;

import java.util.Objects;

public class Currency {
    private String name;
    private String symbole;

    @Override
    public String toString() {
        return "Currency{" +
                "name='" + name + '\'' +
                ", symbole='" + symbole + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return Objects.equals(name, currency.name) && Objects.equals(symbole, currency.symbole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, symbole);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbole() {
        return symbole;
    }

    public void setSymbole(String symbole) {
        this.symbole = symbole;
    }

    public Currency(String name, String symbole) {
        this.name = name;
        this.symbole = symbole;
    }
}
