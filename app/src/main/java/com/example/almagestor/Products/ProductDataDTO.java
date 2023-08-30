package com.example.almagestor.Products;

import android.widget.Toast;

public class ProductDataDTO {
    String img;
    String nameProduct;
    String ean;
    int units;
    double price;
    public ProductDataDTO(String img, String nameProduct, String ean, int units, double price) {
        this.img = img;
        this.nameProduct = nameProduct;
        this.ean = ean;
        this.units = units;
        this.price=price;
    }
    public ProductDataDTO(String img, String nameProduct, String ean, double price) {
        this.img = img;
        this.nameProduct = nameProduct;
        this.ean = ean;
        this.price=price;
    }

    public ProductDataDTO() {

    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public ProductDataDTO create_validate_product(String img,String name, String ean,int stock,double price){
        ProductDataDTO dto=new ProductDataDTO();
        if(name.isEmpty()||ean.isEmpty()){
            return null;
        }else{
            if(img.isEmpty()){
                dto.img="1";
            }else{
                dto.img=img;
            }
            dto.setNameProduct(name);
            dto.setEan(ean);
            dto.setUnits(stock);
            dto.setPrice(price);
        }
        return dto;
    }
    public String print_Product(){
        return "Name:" +this.getNameProduct()+"Price: "+this.getPrice()+"EAN:"+this.getEan();
    }
}
