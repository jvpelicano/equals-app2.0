package com.philcode.equals.EMP;

public class Emp_PostJob_Information {
    public String imageURL;
    public String postTitle;
    public String postDescription;
    public String postLocation;

    public String permission;
    public String companyName;
    public String uid;
    public String expDate;
    public String postDate;
    public String city;
    public String educationalAttainment;
    public String workExperience;

    public String skill;


    public String educationalAttainmentRequirement;
    public Emp_PostJob_Information() {

    }



    public Emp_PostJob_Information(String imageURL, String postTitle, String postDescription,
                                   String postLocation, String permission,
                                   String companyName, String uid, String expDate, String postDate, String city, String educationalAttainment, String workExperience,
                                   String skill, String educationalAttainmentRequirement) {

        this.imageURL = imageURL;
        this.postTitle = postTitle;
        this.postDescription = postDescription;

        this.postLocation = postLocation;

        this.permission = permission;
        this.companyName = companyName;
        this.uid = uid;
        this.expDate = expDate;
        this.postDate = postDate;
        this.city = city;
        this.educationalAttainment = educationalAttainment;
        this.workExperience = workExperience;


        this.skill = skill;


        this.educationalAttainmentRequirement = educationalAttainmentRequirement;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostLocation() {
        return postLocation;
    }

    public void setPostLocation(String postLocation) {
        this.postLocation = postLocation;
    }


    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getCity() {
        return city;
    }

    public String getEducationalAttainment() {
        return educationalAttainment;
    }

    public void setEducationalAttainment(String educationalAttainment) {
        this.educationalAttainment = educationalAttainment;
    }

    public String getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(String workExperience) {
        this.workExperience = workExperience;
    }


    public void setCity(String city) {
        this.city = city;
    }


    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }


    public String getEducationalAttainmentRequirement() {
        return educationalAttainmentRequirement;
    }

    public void setEducationalAttainmentRequirement(String educationalAttainmentRequirement) {
        this.educationalAttainmentRequirement = educationalAttainmentRequirement;
    }
}