package com.example.android.booksfinder;

import android.graphics.Bitmap;

public class BookInfo extends Object {
    private String bookTitle;
    private String authors[];
    private String publisher;
    private String bookCoverImageUrl;
    private String publishedDate;
    private String bookDescription;
    private String previewLinkUrl;
    private double retailPrice;
    private String currencyCode;
    private Bitmap bookCoverImage;

    public BookInfo(String bookTitle, String[] authors, String publisher, String bookCoverImageUrl, String publishedDate, String bookDescription, String previewLinkUrl, double retailPrice, String currencyCode, Bitmap bookCoverImage) {
        this.bookTitle = bookTitle;
        this.authors = authors;
        this.publisher = publisher;
        this.bookCoverImageUrl = bookCoverImageUrl;
        this.publishedDate = publishedDate;
        this.bookDescription = bookDescription;
        this.previewLinkUrl = previewLinkUrl;
        this.retailPrice = retailPrice;
        this.currencyCode = currencyCode;
        this.bookCoverImage = bookCoverImage;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String[] getAuthors() {
        return authors;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getBookCoverImageUrl() {
        return bookCoverImageUrl;
    }

    public void setBookCoverImageUrl(String bookCoverImageUrl) {
        this.bookCoverImageUrl = bookCoverImageUrl;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public String getPreviewLinkUrl() {
        return previewLinkUrl;
    }

    public void setPreviewLinkUrl(String previewLinkUrl) {
        this.previewLinkUrl = previewLinkUrl;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Bitmap getBookCoverImage() {
        return bookCoverImage;
    }

    public void setBookCoverImage(Bitmap bookCoverImage) {
        this.bookCoverImage = bookCoverImage;
    }
}
