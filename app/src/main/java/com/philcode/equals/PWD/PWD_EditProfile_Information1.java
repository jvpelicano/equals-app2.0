package com.philcode.equals.PWD;

public class PWD_EditProfile_Information1 {
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

    public String firstName, lastName;
    public String address;
    public String city;
    public String contact;

    public PWD_EditProfile_Information1 (){
    }
    public PWD_EditProfile_Information1(String firstName, String lastName, String address, String city, String contact) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.contact = contact;


    }


}