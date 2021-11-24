package com.philcode.equals.EMP;

public class EMP_ManageJobs_Information {
    public String imageURL, displayPostTitle, displayCompanyName, displayPostDate, postJobID, typeOfDisabilityMore, permission;


    public EMP_ManageJobs_Information(){

    }

    public EMP_ManageJobs_Information(String imageURL, String displayPostTitle, String displayCompanyName, String displayPostDate, String postJobID, String permission) {
        this.imageURL = imageURL;
        this.displayPostTitle = displayPostTitle;
        this.displayCompanyName = displayCompanyName;
        this.displayPostDate = displayPostDate;
        this.postJobID = postJobID;
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getPostJobID() {
        return postJobID;
    }

    public void setPostJobID(String postJobID) {
        this.postJobID = postJobID;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDisplayPostTitle() {
        return displayPostTitle;
    }

    public void setDisplayPostTitle(String displayPostTitle) {
        this.displayPostTitle = displayPostTitle;
    }

    public String getDisplayCompanyName() {
        return displayCompanyName;
    }

    public void setDisplayCompanyName(String displayCompanyName) {
        this.displayCompanyName = displayCompanyName;
    }

    public String getDisplayPostDate() {
        return displayPostDate;
    }

    public void setDisplayPostDate(String displayPostDate) {
        this.displayPostDate = displayPostDate;
    }
}
