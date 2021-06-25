package com.dzaicev.alphabankproject.feign.impl;

import com.dzaicev.alphabankproject.feign.GifClient;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class GifFallback implements GifClient {

    @Override
    public ResponseEntity<Map> getGif(String gifId, String query) {
        return null;
    }
}
