package com.dzaicev.alphabankproject.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(value = "gif", url = "${gif.client.url}")
public interface GifClient {

    @RequestMapping(value = "?api_key={gifId}&q={query}", method = RequestMethod.GET)
    public ResponseEntity<Map> getGif(@PathVariable String gifId, @PathVariable String query);
}
