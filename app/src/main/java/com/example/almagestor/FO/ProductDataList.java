package com.example.almagestor.FO;

public class ProductDataList {
    String img;
    String nameProduct;
    String ean;
    String units;
    public ProductDataList(String img, String nameProduct, String ean, String units) {
        this.img = img;
        this.nameProduct = nameProduct;
        this.ean = ean;
        this.units = units;
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

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}
