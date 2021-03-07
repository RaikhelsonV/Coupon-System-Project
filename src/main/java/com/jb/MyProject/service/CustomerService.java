package com.jb.MyProject.service;

import com.jb.MyProject.entity.Coupon;
import com.jb.MyProject.entity.Customer;
import com.jb.MyProject.exceptions.AlreadyPurchaseCouponException;
import com.jb.MyProject.exceptions.NoSuchCouponException;
import com.jb.MyProject.exceptions.NoSuchCustomerException;

import java.util.List;

public interface CustomerService {
    void setCustomerId(long customerId);

    Customer getCustomer() throws NoSuchCustomerException;

    Customer getCustomerById(long customerId) throws NoSuchCustomerException;

    Customer getCustomerByLastName(String customerLastName);

    Customer updateCustomer(Customer customer) throws NoSuchCustomerException;

    Coupon toUseCoupon(long couponId) throws NoSuchCouponException;

    Coupon purchaseCoupon(long couponId) throws NoSuchCouponException, AlreadyPurchaseCouponException;

    List<Coupon> getAllCustomerCoupons();

    long getAllCustomerCouponsTotalPrice();

    Coupon addAmountCouponsInShoppingBag(long id, int amount) throws NoSuchCouponException;

    /*general*/
    List<Coupon> getAllCouponsByCategory(String category);

    List<Coupon> getAllCouponsByPriceLessThan(double price);

    List<Coupon> getAllByPriceIsGreaterThan(double price);

    List<Coupon> getAllCouponsByTittle(String title);

    List<Coupon> getAllCouponsByDescriptionLike(String description);

    Coupon getCoupon(long couponId) throws NoSuchCouponException;


}
