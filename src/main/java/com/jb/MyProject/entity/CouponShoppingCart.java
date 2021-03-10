package com.jb.MyProject.entity;

public class CouponShoppingCart {
    private long id;
    private String title;
    private String description;
    private String category;
    private double price;
    private String imageURL;
    private long amountPurchasedCoupons;
    private long pricePurchasedCoupons;

    public CouponShoppingCart() {
    }

    public CouponShoppingCart(long id, String title, String description, String category, double price, String imageURL, long amountPurchasedCoupons, long pricePurchasedCoupons) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.price = price;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public long getAmountPurchasedCoupons() {
        return amountPurchasedCoupons;
    }

    public void setAmountPurchasedCoupons(long amountPurchasedCoupons) {
        this.amountPurchasedCoupons = amountPurchasedCoupons;
    }

    public long getPricePurchasedCoupons() {
        return pricePurchasedCoupons;
    }

    public void setPricePurchasedCoupons(long pricePurchasedCoupons) {
        this.pricePurchasedCoupons = pricePurchasedCoupons;
    }

    @Override
    public String toString() {
        return "CouponShoppingCart{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", imageURL='" + imageURL + '\'' +
                ", amountPurchasedCoupons=" + amountPurchasedCoupons +
                ", pricePurchasedCoupons=" + pricePurchasedCoupons +
                '}';
    }
}
