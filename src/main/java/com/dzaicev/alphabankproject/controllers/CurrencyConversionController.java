package com.dzaicev.alphabankproject.controllers;

import com.dzaicev.alphabankproject.data.Currency;
import com.dzaicev.alphabankproject.data.CurrencyPair;
import com.dzaicev.alphabankproject.data.Gif;
import com.dzaicev.alphabankproject.feign.CurrencyClient;
import com.dzaicev.alphabankproject.feign.GifClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONPointerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.net.MalformedURLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@RestController
public class CurrencyConversionController {

    @Value("${currency.id}")
    private String currencyId;

    @Value("${currency.symbol}")
    private String currencySymbol;

    @Value("${gif.id}")
    private String gifId;

    @Value("${gif.rich}")
    private String gifRich;

    @Value("${gif.broke}")
    private String gifBroke;

    private final CurrencyClient currencyClient;
    private final GifClient gifClient;

    @Autowired
    public CurrencyConversionController(CurrencyClient currencyClient, GifClient gifClient) {
        this.currencyClient = currencyClient;
        this.gifClient = gifClient;
    }

    @GetMapping("currency/{base}")
    @ResponseBody
    String drawGif(@PathVariable String base) throws MalformedURLException {
        Gif gifUrl = getGifURL(base);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("gif.html");
        modelAndView.addObject("gifUrl", gifUrl);
        return "<html>\n" + "<header><meta charset=\"utf-8\">\n" + "</header>\n" +
                "<body>\n <h1>" + gifUrl.getType() + "</h1> \n <p><img src="+ gifUrl.getUrl() + " width=" + gifUrl.getWidth() +
                " height=" + gifUrl.getHeight() + " alt=\"\"></p>" + "</body>\n" + "</html>";
    }

    Gif getGifURL(String base) throws MalformedURLException {
        CurrencyPair currenciesFromTwoDays = getCurrencyPairFromTwoDays(base);
        Currency todayCurrency = currenciesFromTwoDays.today;
        Currency yesterdayCurrency = currenciesFromTwoDays.yesterday;

        System.out.println(todayCurrency.getCurrencyValue());
        System.out.println(yesterdayCurrency.getCurrencyValue());
        if (todayCurrency.getCurrencyValue() > yesterdayCurrency.getCurrencyValue()) {
            return getGifFromQuery(gifRich);
        } else {
            return getGifFromQuery(gifBroke);
        }
    }

    private CurrencyPair getCurrencyPairFromTwoDays(String base) {
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

    private Currency getCurrencyFromDate(String base, String date) {
        JSONObject jsonObject = new JSONObject(
                currencyClient.getCurrencyFromDateInfo(date, currencyId, base, currencySymbol).getBody());

        try {
            return new Currency(jsonObject.getJSONObject("rates").getDouble(currencySymbol));
        } catch (JSONPointerException e) {
            date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(ZonedDateTime.now().minusDays(1));
            jsonObject = new JSONObject(
                    currencyClient.getCurrencyFromDateInfo(date, currencyId, base, currencySymbol).getBody());

            return new Currency(jsonObject.getJSONObject("rates").getDouble(currencySymbol));
        }
    }

    private Gif getGifFromQuery(String query) {
        final Random rand = new Random();

        JSONArray jsonGifs = new JSONObject(gifClient.getGif(gifId, query).getBody()).getJSONArray("data");
        int gifIndex = rand.nextInt(jsonGifs.length());

        JSONObject originalJSONGif = jsonGifs.getJSONObject(gifIndex).getJSONObject("images").getJSONObject("original");

        return new Gif(originalJSONGif.getString("mp4"), originalJSONGif.getString("webp"),
                originalJSONGif.getString("url"), query, originalJSONGif.getInt("width"),
                originalJSONGif.getInt("height"));
    }

}
