package com.example.almagestor.DTOs;

public class UserDTO {

    private Integer groupeid;
    private String codePDV;
    private String Password;

    private String company_name;

    public UserDTO(Integer groupeid, String codePDV, String password, String company_name) {
        this.groupeid = groupeid;
        this.codePDV = codePDV;
        Password = password;
        this.company_name = company_name;
    }


    public Integer getGroupeid() {
        return groupeid;
    }

    public void setGroupeid(Integer groupeid) {
        this.groupeid = groupeid;
    }

    public String getCodePDV() {
        return codePDV;
    }

    public void setCodePDV(String codePDV) {
        this.codePDV = codePDV;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }



}
