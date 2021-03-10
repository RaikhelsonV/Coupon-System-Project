package com.jb.MyProject.entity;

public class CouponShoppingCart {
    private long id;
    private String title;
    private String description;
    private String category;
    private String imageURL;
    private double amountPurchasedCoupons;
    private double pricePurchasedCoupons;

    public CouponShoppingCart() {
    }

    public CouponShoppingCart(long id, String title, String description, String category, String imageURL, double amountPurchasedCoupons, double pricePurchasedCoupons) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.imageURL = imageURL;
        this.amountPurchasedCoupons = amountPurchasedCoupons;
        this.pricePurchasedCoupons = pricePurchasedCoupons;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public double getAmountPurchasedCoupons() {
        return amountPurchasedCoupons;
    }

    public void setAmountPurchasedCoupons(double amountPurchasedCoupons) {
        this.amountPurchasedCoupons = amountPurchasedCoupons;
    }

    public double getPricePurchasedCoupons() {
        return pricePurchasedCoupons;
    }

    public void setPricePurchasedCoupons(double pricePurchasedCoupons) {
        this.pricePurchasedCoupons = pricePurchasedCoupons;
    }

    @Override
    public String toString() {
        return "CouponShoppingCart{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", amountPurchasedCoupons=" + amountPurchasedCoupons +
                ", pricePurchasedCoupons=" + pricePurchasedCoupons +
                '}';
    }
}
