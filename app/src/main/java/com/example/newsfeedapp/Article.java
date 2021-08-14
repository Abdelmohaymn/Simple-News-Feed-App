package com.example.newsfeedapp;

public class Article {
    private String section;
    private String date;
    private String title;
    private String webUrl;
    private String author;
    private String imageUrl;

    public Article(String sec,String mDate,String mTitle,String url,String mAuthor,String imgUrl){
        section = sec;
        date = mDate;
        title = mTitle;
        webUrl = url;
        author = mAuthor;
        imageUrl = imgUrl;
    }

    public String getSection(){
        return section;
    }

    public String getDate(){
        return date;
    }

    public String getTitle(){
        return title;
    }

    public String getWebUrl(){
        return webUrl;
    }

    public String getAuthor(){
        return author;
    }

    public String getImageUrl(){
        return imageUrl;
    }

}
