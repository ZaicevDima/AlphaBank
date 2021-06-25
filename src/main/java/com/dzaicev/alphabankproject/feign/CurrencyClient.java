package com.dzaicev.alphabankproject.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "currency", url = "${currency.client.url}")
public interface CurrencyClient {
    @RequestMapping(value = "/{date}.json?app_id={currencyId}&base={base}&symbols={symbol}", method = RequestMethod.GET)
    ResponseEntity<String> getCurrencyFromDateInfo(@PathVariable String date, @PathVariable String currencyId, @PathVariable String base, @PathVariable String symbol);
}
