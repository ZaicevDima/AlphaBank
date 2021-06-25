package com.dzaicev.alphabankproject.data;

public class Currency {
    private final double currencyValue;

    public Currency(double currencyValue) {
        this.currencyValue = currencyValue;
    }

    public double getCurrencyValue() {
        return currencyValue;
    }
}
