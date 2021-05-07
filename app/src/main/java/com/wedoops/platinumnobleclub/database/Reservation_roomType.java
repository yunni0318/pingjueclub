package com.wedoops.platinumnobleclub.database;

import com.orm.SugarRecord;

public class Reservation_roomType extends SugarRecord {

    String ProductGUID;
    String ProductName;
    String ProductCatagory;
    String Category;
    String ProductDescription;
    String ProductImage;
    int EstimateParticipant;

    public Reservation_roomType(String productGUID, String productName, String productCatagory, String category, String productDescription, String productImage, int estimateParticipant) {
        ProductGUID = productGUID;
        ProductName = productName;
        ProductCatagory = productCatagory;
        Category = category;
        ProductDescription = productDescription;
        ProductImage = productImage;
        EstimateParticipant = estimateParticipant;
    }

    public Reservation_roomType() {
    }

    public String getProductGUID() {
        return ProductGUID;
    }

    public void setProductGUID(String productGUID) {
        ProductGUID = productGUID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductCatagory() {
        return ProductCatagory;
    }

    public void setProductCatagory(String productCatagory) {
        ProductCatagory = productCatagory;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getProductDescription() {
        return ProductDescription;
    }

    public void setProductDescription(String productDescription) {
        ProductDescription = productDescription;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    public int getEstimateParticipant() {
        return EstimateParticipant;
    }

    public void setEstimateParticipant(int estimateParticipant) {
        EstimateParticipant = estimateParticipant;
    }
}
