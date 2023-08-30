package com.example.almagestor.DTOs;

public class ShopDTO {
    String shop_name;
    String shop_pdv;
    String shop_street;
    String shop_info_street;
    String codepostal;
    String phone;
    String email;
    String nif;

    public ShopDTO(String shop_name, String shop_pdv, String shop_street, String shop_info_street,String codepostal, String phone, String email,String nif) {
        this.shop_name = shop_name;
        this.shop_pdv = "PDV: "+shop_pdv;
        this.shop_street = shop_street;
        this.shop_info_street = shop_info_street;
        this.phone = phone;
        this.codepostal=codepostal;
        this.email = email;
        this.nif=nif;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_pdv() {
        return shop_pdv;
    }

    public void setShop_pdv(String shop_pdv) {
        this.shop_pdv = "PDV: "+shop_pdv;
    }

    public String getShop_street() {
        return shop_street;
    }

    public void setShop_street(String shop_street) {
        this.shop_street = shop_street;
    }

    public String getShop_info_street() {
        return shop_info_street;
    }

    public void setShop_info_street(String shop_info_street) {
        this.shop_info_street = shop_info_street;
    }

    public String getCodepostal() {
        return codepostal;
    }

    public void setCodepostal(String codepostal) {
        this.codepostal = codepostal;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public boolean someNull(){
        if(codepostal==null){
            return true;
        }else if (nif==null){
            return true;
        }else if(shop_info_street == null){
            return true;
        } else if (shop_street==null) {
            return true;
        }
        return false;
    }

}
