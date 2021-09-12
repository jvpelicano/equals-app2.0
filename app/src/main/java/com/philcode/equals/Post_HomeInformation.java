package com.philcode.equals;

public class Post_HomeInformation {

    public String postImage;
    public String postContentTitle;
    public String postDescription;
    public String formattedDate;

    public Post_HomeInformation(){

    }

    public Post_HomeInformation(String postImage, String postContentTitle, String postDescription, String formattedDate){
        this.postImage = postImage;
        this.postContentTitle = postContentTitle;
        this.postDescription = postDescription;
        this.formattedDate = formattedDate;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostContentTitle() {
        return postContentTitle;
    }

    public void setPostContentTitle(String postContentTitle) {
        this.postContentTitle = postContentTitle;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }
    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }
}

