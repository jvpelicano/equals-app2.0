package com.philcode.equals.PWD;

import java.util.ArrayList;

public class PWD_AvailableJobs_Model {
    public String imageURL, displayPostTitle, displayCompanyName, displayPostDate, postJobID, typeOfDisabilityMore;


    public PWD_AvailableJobs_Model() {

    }

    public PWD_AvailableJobs_Model(String imageURL, String displayPostTitle, String displayCompanyName, String displayPostDate, String postJobID) {
        this.imageURL = imageURL;
        this.displayPostTitle = displayPostTitle;
        this.displayCompanyName = displayCompanyName;
        this.displayPostDate = displayPostDate;
        this.postJobID = postJobID;

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
