package com.philcode.equals;

import android.widget.CheckBox;

import java.util.ArrayList;

public class EMP_PWD_Information {

    public String email;
    public String typeStatus;
    public String firstName, lastName;
    public String address;
    public String pwdIdCardNum;
    public String contact;
    public String city;
    public String pwdProfilePic;

    public String educationalAttainment;
    public String skill;
    public String workExperience;
    public String key;
   // public String totalYears;



    public EMP_PWD_Information() {

    }

    public EMP_PWD_Information(String email, String typeStatus, String firstName, String lastName, String address, String city,
                               String pwdProfilePic, String key, String contact) {
        this.email = email;
        this.typeStatus = typeStatus;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.contact = contact;
        this.pwdIdCardNum = pwdIdCardNum;

        this.key = key;
        this.pwdProfilePic = pwdProfilePic;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTypeStatus() {
        return typeStatus;
    }

    public void setTypeStatus(String typeStatus) {
        this.typeStatus = typeStatus;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }




    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPwdIdCardNum() {
        return pwdIdCardNum;
    }

    public void setPwdIdCardNum(String pwdIdCardNum) {
        this.pwdIdCardNum = pwdIdCardNum;
    }


    public String getPwdProfilePic() {
        return pwdProfilePic;
    }

    public void setPwdProfilePic(String pwdProfilePic) {
        this.pwdProfilePic = pwdProfilePic;
    }


    public String getEducationalAttainment() {
        return educationalAttainment;
    }

    public void setEducationalAttainment(String educationalAttainment) {
        this.educationalAttainment = educationalAttainment;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(String workExperience) {
        this.workExperience = workExperience;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
