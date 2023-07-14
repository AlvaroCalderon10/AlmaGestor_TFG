package com.example.almagestor.DTOs;

public class ClientDTO {
    String name;
    String nif;
    String phone;
    String street;

    public ClientDTO(String name, String nif, String phone,String street) {
        this.name = name;
        this.nif = nif;
        this.phone = phone;
        this.street=street;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
