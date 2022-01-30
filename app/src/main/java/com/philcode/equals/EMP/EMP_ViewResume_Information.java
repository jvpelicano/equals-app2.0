package com.philcode.equals.EMP;

public class EMP_ViewResume_Information {
    private String firstName;
    private String lastName;
    private String email;
    private String contact;

    private String userID;
    private String resumeFile;


    public EMP_ViewResume_Information(){

    }

    public EMP_ViewResume_Information(String firstName, String lastName,String email, String contact, String userId, String resumeFile){
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
        this.contact=contact;
        this.userID =userId;
        this.resumeFile=resumeFile;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getResumeFile() {
        return resumeFile;
    }

    public void setResumeFile(String resumeFile) {
        this.resumeFile = resumeFile;
    }




}
