package com.dzaicev.alphabankproject;

import com.dzaicev.alphabankproject.controllers.CurrencyController;
import com.dzaicev.alphabankproject.data.CurrencyPair;
import com.dzaicev.alphabankproject.exceptions.IncorrectCurrencyException;
import com.dzaicev.alphabankproject.feign.CurrencyClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.powermock.api.mockito.PowerMockito.*;

@PrepareForTest(fullyQualifiedNames = "com.dzaicev.alphabankproject.controllers.CurrencyController")
@RunWith(PowerMockRunner.class)
public class CurrencyControllerTest {
    private final CurrencyClient currencyClient = mock(CurrencyClient.class);
    private final CurrencyController currencyController = spy(new CurrencyController(currencyClient));

    String today = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(ZonedDateTime.now());
    String yesterday = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(ZonedDateTime.now().minusDays(1));
    String beforeYesterday = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(ZonedDateTime.now().minusDays(2));

    ResponseEntity<String> responseEntityJsonToday = new ResponseEntity<>("{\n" +
            "    \"disclaimer\": \"Usage subject to terms: https://openexchangerates.org/terms\",\n" +
            "    \"license\": \"https://openexchangerates.org/license\",\n" +
            "    \"timestamp\": 1624629600,\n" +
            "    \"base\": \"USD\",\n" +
            "    \"rates\": {\n" +
            "        \"RUB\": 72.171\n" +
            "    }\n" +
            "}", HttpStatus.OK);

    ResponseEntity<String> responseEntityJsonYesterday = new ResponseEntity<>("{\n" +
            "    \"disclaimer\": \"Usage subject to terms: https://openexchangerates.org/terms\",\n" +
            "    \"license\": \"https://openexchangerates.org/license\",\n" +
            "    \"timestamp\": 1624625999,\n" +
            "    \"base\": \"USD\",\n" +
            "    \"rates\": {\n" +
            "        \"RUB\": 72.0995\n" +
            "    }\n" +
            "}", HttpStatus.OK);

    ResponseEntity<String> responseEntityJsonTomorrow = new ResponseEntity<>("{\n" +
            "    \"error\": true,\n" +
            "    \"status\": 400,\n" +
            "    \"message\": \"not_available\",\n" +
            "    \"description\": \"Historical rates for the requested date are not available - please try a different date, or contact support@openexchangerates.org.\"\n" +
            "}", HttpStatus.BAD_REQUEST);

    ResponseEntity<String> responseEntityJsonBeforeYesterday = new ResponseEntity<>("{\n" +
            "    \"disclaimer\": \"Usage subject to terms: https://openexchangerates.org/terms\",\n" +
            "    \"license\": \"https://openexchangerates.org/license\",\n" +
            "    \"timestamp\": 1624492798,\n" +
            "    \"base\": \"USD\",\n" +
            "    \"rates\": {\n" +
            "        \"RUB\": 72.6658\n" +
            "    }\n" +
            "}", HttpStatus.OK);

    ResponseEntity<String> responseEntityJsonIncorrect = new ResponseEntity<>("{\n" +
            "    \"error\": true,\n" +
            "    \"status\": 403,\n" +
            "    \"message\": \"not_allowed\",\n" +
            "    \"description\": \"Changing the API `base` currency is available for Developer, Enterprise and Unlimited plan clients. Please upgrade, or contact support@openexchangerates.org with any questions.\"\n" +
            "}", HttpStatus.FORBIDDEN);

    @BeforeEach
    public void before() {

        when(currencyClient.getCurrencyFromDateInfo(yesterday, null, "USD", "RUB")).thenReturn(responseEntityJsonYesterday);

        when(currencyClient.getCurrencyFromDateInfo(today, null, "USD1", "RUB")).thenReturn(responseEntityJsonIncorrect);
        when(currencyClient.getCurrencyFromDateInfo(yesterday, null, "USD1", "RUB")).thenReturn(responseEntityJsonIncorrect);
        when(currencyClient.getCurrencyFromDateInfo(beforeYesterday, null, "USD1", "RUB")).thenReturn(responseEntityJsonIncorrect);
    }

    @Test
    public void getCurrencyPairFromTwoDaysTest() throws IncorrectCurrencyException {
        ReflectionTestUtils.setField(currencyController, "currencySymbol", "RUB");
        when(currencyClient.getCurrencyFromDateInfo(today, null, "USD", "RUB")).thenReturn(responseEntityJsonToday);

        CurrencyPair pair = currencyController.getCurrencyPairFromTwoDays("USD");

        assertEquals(72.171, pair.today.getCurrencyValue());
        assertEquals(72.0995, pair.yesterday.getCurrencyValue());
    }

    @Test
    public void getCurrencyPairFromTwoDaysWithIncorrectBaseTest() {
        ReflectionTestUtils.setField(currencyController, "currencySymbol", "RUB");

        assertThrows(IncorrectCurrencyException.class, () -> {currencyController.getCurrencyPairFromTwoDays("USD1");});
    }

    @Test
    public void getIncorrectDateCurrencyPairFromTwoDays() throws IncorrectCurrencyException {
        ReflectionTestUtils.setField(currencyController, "currencySymbol", "RUB");
        when(currencyClient.getCurrencyFromDateInfo(today, null, "USD", "RUB")).thenReturn(responseEntityJsonTomorrow);
        when(currencyClient.getCurrencyFromDateInfo(beforeYesterday, null, "USD", "RUB")).thenReturn(responseEntityJsonBeforeYesterday);

        CurrencyPair pair = currencyController.getCurrencyPairFromTwoDays("USD");

        assertEquals(72.0995, pair.today.getCurrencyValue());
        assertEquals(72.6658, pair.yesterday.getCurrencyValue());
    }
}
