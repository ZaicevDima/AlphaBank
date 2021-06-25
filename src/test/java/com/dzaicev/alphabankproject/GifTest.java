package com.dzaicev.alphabankproject;

import com.dzaicev.alphabankproject.data.Gif;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GifTest {
    private final Gif gif = new Gif("mp4", "webp", "url", "rich", 10, 20);

    @Test
    public void getCurrencyValueTest() {
        assertEquals("mp4", gif.getMp4());
        assertEquals("webp", gif.getWebp());
        assertEquals("url", gif.getUrl());
        assertEquals("rich", gif.getType());
        assertEquals(10, gif.getWidth());
        assertEquals(20, gif.getHeight());
    }
}
