package com.dzaicev.alphabankproject.data;

public class Gif {
    private final String mp4;
    private final String webp;
    private final String url;
    private final String type;
    private final int width;
    private final int height;

    public Gif(String mp4, String webp, String url, String type, int width, int height) {
        this.mp4 = mp4;
        this.webp = webp;
        this.url = url;
        this.type = type;
        this.width = width;
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public String getMp4() {
        return mp4;
    }

    public String getWebp() {
        return webp;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getType() {
        return type;
    }
}
