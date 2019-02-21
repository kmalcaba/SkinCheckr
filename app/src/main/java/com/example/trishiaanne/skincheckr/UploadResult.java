package com.example.trishiaanne.skincheckr;

public class UploadResult {
    private int disease;
    private String imageURL;
    private String date;
    private String userId;

<<<<<<< HEAD
    public UploadResult() {

    }

    public UploadResult(int d, String url, String date) {
=======
    public UploadResult(String d, String url, String date, String userId) {
>>>>>>> ce44be6a07288c736e2d1711f4f62ba8875a6c7f
        this.disease = d;
        this.imageURL = url;
        this.date = date;
        this.userId = userId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
