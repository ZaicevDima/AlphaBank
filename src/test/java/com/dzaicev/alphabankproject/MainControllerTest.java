package com.dzaicev.alphabankproject;

import com.dzaicev.alphabankproject.controllers.GifController;
import com.dzaicev.alphabankproject.controllers.MainController;
import com.dzaicev.alphabankproject.data.Gif;
import com.dzaicev.alphabankproject.exceptions.IncorrectCurrencyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MainControllerTest {
    private final GifController gifController = mock(GifController.class);
    private final MainController mainController = spy(new MainController(gifController));
    private final Gif gif = new Gif("mp4", "webp", "url", "rich", 10, 20);

    @BeforeEach
    public void before() throws IncorrectCurrencyException {
        when(gifController.getGif("USD")).thenReturn(gif);
        when(gifController.getGif("USD1")).thenThrow(IncorrectCurrencyException.class);
    }

    @Test
    public void getHTMLGifTest() {
        String result = mainController.drawGif("USD");

        assertEquals("<html>\n" + "<header><meta charset=\"utf-8\">\n" + "</header>\n" +
                "<body>\n <h1>" + gif.getType() + "</h1> \n <p><img src=" + gif.getUrl() + " width=" + gif.getWidth() +
                " height=" + gif.getHeight() + " alt=\"\"></p>" + "</body>\n" + "</html>", result);
    }

    @Test
    public void getHTMLGifWithIncorrectCurrencyCodeTest() {
        String result = mainController.drawGif("USD1");

        assertEquals("<html>\n" + "<header><meta charset=\"utf-8\">\n" + "</header>\n" +
                "<body>\n <h1> This base currency code not exist </h1> \n </body>\n" + "</html>", result);
    }
}
