package com.example.almagestor.DTOs;

public class UserDTO {

    private String groupeid;
    private String codePDV;
    private String Password;
    private String company_name;

    public UserDTO(String groupeid, String codePDV, String company_name,String password) {
        this.groupeid = groupeid;
        this.codePDV = codePDV;
        Password = password;
        this.company_name = company_name;
    }


    public String getGroupeid() {
        return groupeid;
    }

    public void setGroupeid(String groupeid) {
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
    public boolean isEmpty(){
        if(getCodePDV()==""&&getCompany_name()==""&& getGroupeid()==""&& getPassword()==""){
            return true;
        }
        return false;
    }


}
