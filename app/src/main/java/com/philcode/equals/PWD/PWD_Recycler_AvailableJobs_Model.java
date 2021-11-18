package com.philcode.equals.PWD;

public class PWD_Recycler_AvailableJobs_Model {
    String imageURL, displayPostTitle, displayCompanyName, displayPostDate;

    public PWD_Recycler_AvailableJobs_Model() {
    }

    public PWD_Recycler_AvailableJobs_Model(String imageURL, String displayPostTitle, String displayCompanyName, String displayPostDate) {
        this.imageURL = imageURL;
        this.displayPostTitle = displayPostTitle;
        this.displayCompanyName = displayCompanyName;
        this.displayPostDate = displayPostDate;
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
