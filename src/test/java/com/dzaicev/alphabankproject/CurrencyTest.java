package com.dzaicev.alphabankproject;

import com.dzaicev.alphabankproject.data.Currency;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CurrencyTest {
    private final Currency currency = new Currency(10);

    @Test
    public void getCurrencyValueTest() {
        assertEquals(10, currency.getCurrencyValue());
    }
}
