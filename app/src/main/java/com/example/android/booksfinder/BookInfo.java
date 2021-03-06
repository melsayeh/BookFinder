package com.example.android.booksfinder;

import android.graphics.Bitmap;

import java.util.ArrayList;

/* The BookInfo class is used to create an object type that contains information about the
books that will be parsed from Google Books API
Information include book title,publisher,cover image URL,description, preview link, retail price
currency code, and cover image.
* */
public class BookInfo extends Object {
    private String bookTitle;
    private ArrayList<String> authors;
    private String publisher;
    private String bookCoverImageUrl;
    private String publishedDate;
    private String bookDescription;
    private String previewLinkUrl;
    private String retailPrice;
    private String currencyCode;
    private Bitmap coverImage;

    public BookInfo(String bookTitle, ArrayList<String> authors, String publisher, String bookCoverImageUrl, String publishedDate, String bookDescription, String previewLinkUrl, String retailPrice, String currencyCode, Bitmap coverImage) {
        this.bookTitle = bookTitle;
        this.authors = authors;
        this.publisher = publisher;
        this.bookCoverImageUrl = bookCoverImageUrl;
        this.publishedDate = publishedDate;
        this.bookDescription = bookDescription;
        this.previewLinkUrl = previewLinkUrl;
        this.retailPrice = retailPrice;
        this.currencyCode = currencyCode;
        this.coverImage = coverImage;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<String> authors) {
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

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Bitmap getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(Bitmap coverImage) {
        this.coverImage = coverImage;
    }
}
