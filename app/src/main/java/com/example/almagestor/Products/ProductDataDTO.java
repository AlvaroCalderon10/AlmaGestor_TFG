package com.example.almagestor.Products;

import android.widget.Toast;

public class ProductDataDTO {
    String img;
    String nameProduct;
    String ean;
    int units;
    int price;
    public ProductDataDTO(String img, String nameProduct, String ean, int units, int price) {
        this.img = img;
        this.nameProduct = nameProduct;
        this.ean = ean;
        this.units = units;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    public ProductDataDTO create_validate_product(String name, String ean,int stock,int price){
        ProductDataDTO dto=new ProductDataDTO();
        if(name.isEmpty()||ean.isEmpty()){
            return null;
        }else{
            dto.setNameProduct(name);
            dto.setEan(ean);
            dto.setUnits(stock);
            dto.setPrice(price);
        }
        return dto;
    }
}
