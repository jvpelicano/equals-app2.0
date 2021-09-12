package com.philcode.equals.EMP;



public class EmployeeInformation2 {

    public String typeStatus;
    public String fullname;
    public String address;
    public String contact;

    public String empValidID;

    public EmployeeInformation2(){

    }

    public String getTypeStatus() {
        return typeStatus;
    }

    public void setTypeStatus(String typeStatus) {
        this.typeStatus = typeStatus;
    }



    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmpValidID() {
        return empValidID;
    }

    public void setEmpValidID(String empValidID) {
        this.empValidID = empValidID;
    }

    public EmployeeInformation2(String typeStatus, String fullname, String address,
                               String contact, String empValidID) {


        this.typeStatus=typeStatus;
        this.fullname = fullname;
        this.address = address;
        this.contact = contact;
        this.empValidID = empValidID;
    }

}
