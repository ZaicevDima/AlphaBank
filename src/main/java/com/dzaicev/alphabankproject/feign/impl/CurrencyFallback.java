package com.dzaicev.alphabankproject.feign.impl;

import com.dzaicev.alphabankproject.feign.CurrencyClient;
import org.springframework.http.ResponseEntity;

public class CurrencyFallback implements CurrencyClient {
    @Override
    public ResponseEntity<String> getCurrencyFromDateInfo(String yesterday, String currencyId, String base, String symbol) {
        return null;
    }
}
