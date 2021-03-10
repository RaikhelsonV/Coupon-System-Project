package com.jb.MyProject.service;

import com.jb.MyProject.entity.Company;
import com.jb.MyProject.entity.Coupon;
import com.jb.MyProject.exceptions.NoSuchCompanyException;

import java.time.LocalDate;
import java.util.List;

public interface CompanyService {

    void setCompanyId(long companyId);

    Company getCompany() throws NoSuchCompanyException;

    Company getCompanyById(long companyId) throws NoSuchCompanyException;

    Company getCompanyByName(String companyName) throws NoSuchCompanyException;

    List<Company> getAllCompanies();

    Company updateCompany(Company company) throws NoSuchCompanyException;

    Company removeCompanyById(long companyId) throws NoSuchCompanyException;

    Coupon createCoupon(Coupon coupon) throws NoSuchCompanyException;

    List<Coupon> getAllCompanyCoupons();

    List<Coupon> getAllCouponsByCompanyId(long companyId);

    List<Coupon> getAllCouponsByStartDateAndByCompanyId(LocalDate startDate);

    List<Coupon> getAllCouponsByEndDateAndByCompanyId(LocalDate endDate);




}
