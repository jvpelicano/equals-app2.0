package com.philcode.equals.PWD;

public class PWD_AddWorkInformation {

    String position;
    String company_name;
    String skill;
    String dateStarted;
    String dateEnded;




    public PWD_AddWorkInformation (String position, String company_name, String skill, String dateStarted, String dateEnded){

        this.position = position;
        this.company_name = company_name;
        this.skill = skill;
        this.dateStarted = dateStarted;
        this.dateEnded = dateEnded;

    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(String dateStarted) {
        this.dateStarted = dateStarted;
    }

    public String getDateEnded() {
        return dateEnded;
    }

    public void setDateEnded(String dateEnded) {
        this.dateEnded = dateEnded;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public PWD_AddWorkInformation (){

    }
}