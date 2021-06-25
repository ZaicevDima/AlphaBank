package com.dzaicev.alphabankproject.controllers;

import com.dzaicev.alphabankproject.data.Currency;
import com.dzaicev.alphabankproject.data.CurrencyPair;
import com.dzaicev.alphabankproject.exceptions.IncorrectCurrencyException;
import com.dzaicev.alphabankproject.exceptions.IncorrectDateException;
import com.dzaicev.alphabankproject.feign.CurrencyClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CurrencyController {

    @Value("${currency.id}")
    private String currencyId;

    @Value("${currency.symbol}")
    private String currencySymbol;

    private final CurrencyClient currencyClient;

    @Autowired
    public CurrencyController(CurrencyClient currencyClient) {
        this.currencyClient = currencyClient;
    }

    public CurrencyPair getCurrencyPairFromTwoDays(String base) throws IncorrectCurrencyException {
        String yesterday = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(ZonedDateTime.now().minusDays(1));
        String today = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(ZonedDateTime.now());

        Currency todayCurrency = getCurrencyFromDate(base, today);
        Currency yesterdayCurrency = getCurrencyFromDate(base, yesterday);

        if (todayCurrency.getCurrencyValue() == yesterdayCurrency.getCurrencyValue()) {
            yesterday = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(ZonedDateTime.now().minusDays(2));
            yesterdayCurrency = getCurrencyFromDate(base, yesterday);
        }

        return new CurrencyPair(todayCurrency, yesterdayCurrency);
    }

    private Currency getCurrencyFromDate(String base, String date) throws IncorrectCurrencyException {
        JSONObject jsonObject = new JSONObject(
                currencyClient.getCurrencyFromDateInfo(date, currencyId, base, currencySymbol).getBody());
        try {

            if (jsonObject.has("error")) {
                System.out.println("aaaa");
                System.out.println(jsonObject.getInt("status"));
                switch (jsonObject.getInt("status")) {
                    case (400): {
                        throw new IncorrectDateException("This date not exist");
                    }
                    case (403): {
                        System.out.println("kek1");
                        throw new IncorrectCurrencyException("This base currency code not exist");
                    }
                }
            }
            return new Currency(jsonObject.getJSONObject("rates").getDouble(currencySymbol));
        } catch (IncorrectDateException e) {
            date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(ZonedDateTime.now().minusDays(1));
            jsonObject = new JSONObject(
                    currencyClient.getCurrencyFromDateInfo(date, currencyId, base, currencySymbol).getBody());

            return new Currency(jsonObject.getJSONObject("rates").getDouble(currencySymbol));
        }
    }
}
