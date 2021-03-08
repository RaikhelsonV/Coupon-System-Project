package com.jb.MyProject.service;

import com.jb.MyProject.entity.Coupon;
import com.jb.MyProject.exceptions.InvalidUpdateCouponException;
import com.jb.MyProject.exceptions.NoSuchCompanyException;
import com.jb.MyProject.exceptions.NoSuchCouponException;

import javax.transaction.Transactional;
import java.util.List;

public interface CouponService {

    Coupon getCouponById(long couponId) throws NoSuchCouponException;

    List<Coupon> getAllCoupons();

    List<Coupon> getAllCouponsByPriceLessThan(double price);

    List<Coupon> getAllCouponsByTittle(String title);

    List<Coupon> getAllCouponsByDescriptionLike(String description);

    List<Coupon> getAllCouponsByCategory(String category);

    List<Coupon> getAllByPriceIsGreaterThan(double price);

    Coupon updateCoupon(Coupon coupon) throws InvalidUpdateCouponException, NoSuchCouponException;

    Coupon updateCouponById(long id, Coupon coupon) throws NoSuchCouponException;

    @Transactional
    void deleteCouponById(long couponId) throws NoSuchCouponException, NoSuchCompanyException;

    //////////////////
    Coupon removeCouponById(long couponId) throws NoSuchCouponException;
}
