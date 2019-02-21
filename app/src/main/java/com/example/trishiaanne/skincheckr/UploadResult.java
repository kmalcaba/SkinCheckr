package com.example.trishiaanne.skincheckr;

public class UploadResult {
    private int disease;
    private String imageURL;
    private String date;

    public UploadResult() {

    }

    public UploadResult(int d, String url, String date) {
        this.disease = d;
        this.imageURL = url;
        this.date = date;
    }

    public int getDisease() {
        return disease;
    }

    public void setDisease(int disease) {
        this.disease = disease;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
