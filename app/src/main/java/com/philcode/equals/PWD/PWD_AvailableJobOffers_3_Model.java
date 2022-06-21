package com.philcode.equals.PWD;

public class PWD_AvailableJobOffers_3_Model {
    public String imageURL;
    public String jobTitle;
    public String companyName;
    public String postDate;
    public String postJobId;

    public PWD_AvailableJobOffers_3_Model() {
    }

    public PWD_AvailableJobOffers_3_Model(String imageURL, String jobTitle, String companyName, String postDate, String postJobId) {
        this.imageURL = imageURL;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.postDate = postDate;
        this.postJobId = postJobId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getPostJobId() {
        return postJobId;
    }

    public void setPostJobId(String postJobId) {
        this.postJobId = postJobId;
    }
}
