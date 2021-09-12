package com.philcode.equals.PWD;

public class PWD_UserInformation {

        public String email;
        public String typeStatus;
        public String firstName, lastName;
        public String address;
        public String city;


        public String contact;


        public String pwdIdCardNum;


        public PWD_UserInformation() {

        }

        public PWD_UserInformation(String email, String typeStatus, String firstName, String lastName, String address, String city, String contact, String pwdIdCardNum) {
            this.email = email;
            this.typeStatus = typeStatus;
            this.firstName = firstName;
            this.lastName = lastName;
            this.address = address;
            this.city = city;
            this.contact = contact;

            this.pwdIdCardNum = pwdIdCardNum;

        }

    public String getPwdIdCardNum() {
        return pwdIdCardNum;
    }

    public void setPwdIdCardNum(String pwdIdCardNum) {
        this.pwdIdCardNum = pwdIdCardNum;
    }

    }
