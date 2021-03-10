package com.jb.MyProject.entity;

import com.jb.MyProject.exceptions.AlreadyPurchaseCouponException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "customer")
public class Customer extends Client {
    private String firstName;
    private String lastName;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "customer_coupon",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "coupon_id"))
    private List<Coupon> coupons;

    @ElementCollection
    private Map<Long,Long> amountPurchasedCoupons;

    public Customer() {
        this.coupons = new ArrayList<>();
        this.amountPurchasedCoupons = new HashMap<>();
    }

    public Customer(long id) {
        setId(id);
    }

    public static Customer empty() {
        return new Customer(NO_ID);
    }

    public void add(Coupon coupon) throws AlreadyPurchaseCouponException {
        coupon.addCustomer(this); /*"this" is reference for our coupon-"guy"*/
        coupons.add(coupon);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }

    public Map<Long, Long> getAmountPurchasedCoupons() {
        return amountPurchasedCoupons;
    }

    public void setAmountPurchasedCoupons(Map<Long, Long> amountPurchasedCoupons) {
        this.amountPurchasedCoupons = amountPurchasedCoupons;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", coupons=" + coupons +
                ", amountPurchasedCoupons=" + amountPurchasedCoupons +
                '}';
    }
}
