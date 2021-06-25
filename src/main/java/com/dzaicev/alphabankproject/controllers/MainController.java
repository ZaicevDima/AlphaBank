package com.dzaicev.alphabankproject.controllers;

import com.dzaicev.alphabankproject.data.Gif;
import com.dzaicev.alphabankproject.exceptions.IncorrectCurrencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class MainController {
    GifController gifController;

    @Autowired
    public MainController(GifController gifController) {
        this.gifController = gifController;
    }

    @GetMapping("{base}")
    @ResponseBody
    public String drawGif(@PathVariable String base) {
        try {
            Gif gif = gifController.getGif(base);
            return "<html>\n" + "<header><meta charset=\"utf-8\">\n" + "</header>\n" +
                    "<body>\n <h1>" + gif.getType() + "</h1> \n <p><img src=" + gif.getUrl() + " width=" + gif.getWidth() +
                    " height=" + gif.getHeight() + " alt=\"\"></p>" + "</body>\n" + "</html>";
        } catch (IncorrectCurrencyException e) {
            Logger logger = Logger.getLogger(MainController.class.getName());
            logger.log(Level.WARNING, e.getMessage());

            return "<html>\n" + "<header><meta charset=\"utf-8\">\n" + "</header>\n" +
                    "<body>\n <h1> This base currency code not exist </h1> \n </body>\n" + "</html>";
        }
    }
}
