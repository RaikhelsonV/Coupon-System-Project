package com.example.MyProject.service;

import com.example.MyProject.entity.Coupon;
import com.example.MyProject.entity.Customer;
import com.example.MyProject.exceptions.AlreadyPurchaseCouponException;
import com.example.MyProject.exceptions.NoSuchCouponException;
import com.example.MyProject.exceptions.NoSuchCustomerException;

import java.util.List;

public interface CustomerService {
    void setCustomer_id(long customer_id);
    Customer getCustomer();
    Customer getCustomerById(long customer_id) throws NoSuchCustomerException;
    Customer getCustomerByLastName(String customerLastName);
    Customer updateCustomer(long customer_id, Customer customer) throws NoSuchCustomerException;

    Coupon toUseCoupon(long coupon_id) throws NoSuchCouponException;
    Coupon purchaseCoupon(long coupon_id) throws NoSuchCouponException, AlreadyPurchaseCouponException;

    List<Coupon> getAllCustomerCoupons();

    /*general*/
    List<Coupon> getAllCouponsByCategory(String category);
    List<Coupon> getAllCouponsByPriceLessThan(double price);
    List<Coupon> getAllByPriceIsGreaterThan(double price);
    List<Coupon> getAllCouponsByTittle(String title);
    List<Coupon> getAllCouponsByDescriptionLike(String description);
    Coupon getCoupon(long coupon_id) throws NoSuchCouponException;


}
