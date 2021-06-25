package com.dzaicev.alphabankproject.feign.impl;

import com.dzaicev.alphabankproject.feign.GifClient;
import org.springframework.http.ResponseEntity;

public class GifFallback implements GifClient {
    @Override
    public ResponseEntity<String> getGif(String gifId, String query) {
        return null;
    }
}
