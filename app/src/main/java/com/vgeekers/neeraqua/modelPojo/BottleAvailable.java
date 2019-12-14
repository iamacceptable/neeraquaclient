package com.vgeekers.neeraqua.modelPojo;

public class BottleAvailable {
    private String bottleImage = "";
    private String bottleName = "";
    private String bottleDescription = "";
    private String bottleReviews = "";
    private String bottlePrice = "";
    private int bottleQty = 1;

    public String getBottleImage() {
        return bottleImage;
    }

    public void setBottleImage(String bottleImage) {
        this.bottleImage = bottleImage;
    }

    public String getBottleName() {
        return bottleName;
    }

    public void setBottleName(String bottleName) {
        this.bottleName = bottleName;
    }

    public String getBottleDescription() {
        return bottleDescription;
    }

    public void setBottleDescription(String bottleDescription) {
        this.bottleDescription = bottleDescription;
    }

    public String getBottleReviews() {
        return bottleReviews;
    }

    public void setBottleReviews(String bottleReviews) {
        this.bottleReviews = bottleReviews;
    }

    public String getBottlePrice() {
        return bottlePrice;
    }

    public void setBottlePrice(String bottlePrice) {
        this.bottlePrice = bottlePrice;
    }

    public int getBottleQty() {
        return bottleQty;
    }

    public void setBottleQty(int bottleQty) {
        this.bottleQty = bottleQty;
    }

    public BottleAvailable(String bottleImage, String bottleName, String bottleDescription, String bottleReviews, String bottlePrice, int bottleQty) {
        this.bottleImage = bottleImage;
        this.bottleName = bottleName;
        this.bottleDescription = bottleDescription;
        this.bottleReviews = bottleReviews;
        this.bottlePrice = bottlePrice;
        this.bottleQty = bottleQty;
    }
}
