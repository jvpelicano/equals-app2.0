package com.philcode.equals.EMP;

public class EmployeeInformation {

    public String email;
    public String password;
    public String typeStatus;
    public String firstname;
    public String lastname;
    public String fullname;
    public String companybg;
    public String contact;

    public String empValidID;
    public String companyaddress;
    public String companycity;

    public EmployeeInformation(){

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

    public String getCompanybg() {
        return companybg;
    }

    public void setCompanybg(String companybg) {
        this.companybg = companybg;
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
    public String getCompanyaddress() {
        return companyaddress;
    }

    public void setCompanyaddress(String companyaddress) {
        this.companyaddress = companyaddress;
    }

    public String getCompanycity() {
        return companycity;
    }

    public void setCompanycity(String companycity) {
        this.companycity = companycity;
    }

    public EmployeeInformation(String email, String password, String typeStatus, String firstname, String lastname, String fullname, String companybg,
                               String contact, String empValidID, String companyaddress, String companycity) {

        this.email=email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.typeStatus=typeStatus;
        this.fullname = fullname;
        this.companybg = companybg;
        this.contact = contact;
        this.empValidID = empValidID;
        this.companyaddress = companyaddress;
        this.companycity = companycity;
    }

}