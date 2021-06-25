package com.dzaicev.alphabankproject.controllers;

import com.dzaicev.alphabankproject.data.Currency;
import com.dzaicev.alphabankproject.data.CurrencyPair;
import com.dzaicev.alphabankproject.data.Gif;
import com.dzaicev.alphabankproject.exceptions.IncorrectCurrencyException;
import com.dzaicev.alphabankproject.feign.GifClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class GifController {

    Logger log = Logger.getLogger(GifController.class.getName());

    @Value("${gif.id}")
    private String gifId;

    @Value("${gif.rich}")
    private String gifRich;

    @Value("${gif.broke}")
    private String gifBroke;

    private final GifClient gifClient;
    private final CurrencyController currencyController;

    @Autowired
    public GifController(GifClient gifClient, CurrencyController currencyController) {
        this.gifClient = gifClient;
        this.currencyController = currencyController;
    }

    public Gif getGif(String base) throws IncorrectCurrencyException {
        CurrencyPair currenciesFromTwoDays = currencyController.getCurrencyPairFromTwoDays(base);
        Currency todayCurrency = currenciesFromTwoDays.today;
        Currency yesterdayCurrency = currenciesFromTwoDays.yesterday;

        if (todayCurrency.getCurrencyValue() > yesterdayCurrency.getCurrencyValue()) {
            return getGifFromQuery(gifRich);
        } else {
            return getGifFromQuery(gifBroke);
        }
    }

    private Gif getGifFromQuery(String query) {
        final Random rand = new Random();

        try {
            JSONArray jsonGifs = new JSONObject(gifClient.getGif(gifId, query).getBody()).getJSONArray("data");
            int gifIndex = rand.nextInt(jsonGifs.length());

            JSONObject originalJSONGif = jsonGifs.getJSONObject(gifIndex).getJSONObject("images").getJSONObject("original");

            return new Gif(originalJSONGif.getString("mp4"), originalJSONGif.getString("webp"),
                    originalJSONGif.getString("url"), query, originalJSONGif.getInt("width"),
                    originalJSONGif.getInt("height"));
        } catch (JSONException e) {
            log.log(Level.INFO, "incorrect searching", e);
            return null;
        }
    }

}
