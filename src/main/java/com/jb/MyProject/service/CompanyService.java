package com.jb.MyProject.service;

import com.jb.MyProject.entity.Company;
import com.jb.MyProject.entity.Coupon;
import com.jb.MyProject.exceptions.InvalidUpdateCouponException;
import com.jb.MyProject.exceptions.NoSuchCompanyException;
import com.jb.MyProject.exceptions.NoSuchCouponException;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

public interface CompanyService {
    void setCompanyId(long companyId);

    Company updateCompany(long id, Company company) throws NoSuchCompanyException;

    Coupon createCoupon(Coupon coupon) throws NoSuchCompanyException;

    Coupon updateCoupon(Coupon coupon) throws InvalidUpdateCouponException, NoSuchCouponException;

    @Transactional
    void deleteCouponById(long couponId) throws NoSuchCouponException, NoSuchCompanyException;

    List<Coupon> getAllCompanyCoupons();

    List<Coupon> getAllCouponsByCompanyId(long companyId);

    /*general info*/
    List<Coupon> getAllCoupons();

    Company getCompany() throws NoSuchCompanyException;

    Company getCompanyById(long companyId) throws NoSuchCompanyException;

    Company getCompanyByName(String companyName) throws NoSuchCompanyException;

    List<Coupon> getAllCouponsByCategory(String category);

    List<Coupon> getAllCouponsByStartDate(LocalDate startDate);

    List<Coupon> getAllCouponsByEndDate(LocalDate endDate);

    List<Coupon> getAllCouponsByPriceLessThan(double price);

    List<Coupon> getAllCouponsByTittle(String title);

    List<Coupon> getAllCouponsByDescriptionLike(String description);


}
