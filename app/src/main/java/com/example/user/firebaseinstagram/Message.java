package com.example.user.firebaseinstagram;

public class Message {

    String mesajText;
    String gonderici;
    String alici;
    String zaman;

    public Message() {
    }

    public Message(String mesajText, String gonderici,String alici,String zaman) {
        this.mesajText = mesajText;
        this.gonderici = gonderici;
        this.alici = alici;
        this.zaman = zaman;
    }

    public String getMesajText() {
        return mesajText;
    }

    public void setMesajText(String mesajText) {
        this.mesajText = mesajText;
    }

    public String getGonderici() {
        return gonderici;
    }

    public void setGonderici(String gonderici) {
        this.gonderici = gonderici;
    }

    public String getAlici() {
        return alici;
    }

    public void setAlici(String alici) {
        this.alici = alici;
    }

    public String getzaman() {
        return zaman;
    }

    public void setzaman(String zaman) {
        this.zaman = zaman;
    }

}
