package com.example.isaretdil;

public class Kelime {
    private String gorunenAd;   // Türkçe (büyük harfli)
    private String ingilizce;   // İngilizce (küçük, gri)
    private int videoId;

    public Kelime(String gorunenAd, String ingilizce, int videoId) {
        this.gorunenAd = gorunenAd;
        this.ingilizce = ingilizce;
        this.videoId   = videoId;
    }

    public String getGorunenAd() { return gorunenAd; }
    public String getIngilizce() { return ingilizce; }
    public int getVideoId()      { return videoId; }
}