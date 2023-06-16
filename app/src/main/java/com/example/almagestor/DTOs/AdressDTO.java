package com.example.almagestor.DTOs;

public class AdressDTO {
    String Street;
    String info_Street;
    String codePostal;

    public AdressDTO(String street, String info_Street, String codePostal) {
        Street = street;
        this.info_Street = info_Street;
        this.codePostal = codePostal;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getInfo_Street() {
        return info_Street;
    }

    public void setInfo_Street(String info_Street) {
        this.info_Street = info_Street;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }
}
