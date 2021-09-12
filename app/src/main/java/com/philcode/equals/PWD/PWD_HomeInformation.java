package com.philcode.equals.PWD;

public class PWD_HomeInformation {

    public String postImage;
    public String postContentTitle;
    public String postDescription;

    public PWD_HomeInformation (){

    }

    public PWD_HomeInformation (String postImage,String postContentTitle, String postDescription){
        this.postImage = postImage;
        this.postContentTitle = postContentTitle;
        this.postDescription = postDescription;
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
}
