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

    List<Customer> getAllCustomers();

    Customer getCustomerByLastName(String customerLastName);

    Customer updateCustomer(Customer customer) throws NoSuchCustomerException;

    Customer removeCustomerById(long customerId) throws NoSuchCustomerException;

    Coupon purchaseCoupon(long couponId) throws NoSuchCouponException, AlreadyPurchaseCouponException;

    List<Coupon> getAllCustomerCoupons();

    long getTotalPriceOfPurchasesCouponsInShoppingCart();

    Coupon addAmountCouponsInShoppingBag(long id, int amount) throws NoSuchCouponException;

    long getAmountOfPurchasesCouponsInShoppingCard();

    Coupon removeCouponFromCustomerShoppingCard(long couponId) throws NoSuchCouponException;

}
