package com.example.MyProject.service;

import com.example.MyProject.entity.Company;
import com.example.MyProject.entity.Coupon;
import com.example.MyProject.exceptions.InvalidUpdateCouponException;
import com.example.MyProject.exceptions.NoSuchCompanyException;
import com.example.MyProject.exceptions.NoSuchCouponException;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

public interface CompanyService {
    void setCompany_id(long company_id);
   /* void updateCompany(String name);*/
    Company updateCompany(long company_id, Company company) throws NoSuchCompanyException;
    Coupon createCoupon(Coupon coupon);
    Coupon updateCoupon(Coupon coupon) throws InvalidUpdateCouponException;
    @Transactional
    void deleteCouponById(long coupon_id) throws NoSuchCouponException, NoSuchCompanyException;
    List<Coupon> getAllCompanyCoupons();
    List<Coupon> getAllCompanyCouponsR(long id);

    /*general info*/
    List<Coupon> getAllCoupons();

    Company getCompany() throws NoSuchCompanyException;
    Company getCompanyById(long company_id) throws NoSuchCompanyException;
    Company getCompanyByName(String company_name) throws NoSuchCompanyException;

    List<Coupon> getAllCouponsByCategory(String category);
    List<Coupon> getAllCouponsByStartDate(LocalDate startDate) ;
    List<Coupon> getAllCouponsByEndDate(LocalDate endDate);
    List<Coupon> getAllCouponsByPriceLessThan(double price);
    List<Coupon> getAllCouponsByTittle(String title);
    List<Coupon> getAllCouponsByDescriptionLike(String description);



}
