package com.dzaicev.alphabankproject.data;

public class CurrencyPair {
    public Currency today;
    public Currency yesterday;

    public CurrencyPair(Currency today, Currency yesterday) {
        this.today = today;
        this.yesterday = yesterday;
    }
}