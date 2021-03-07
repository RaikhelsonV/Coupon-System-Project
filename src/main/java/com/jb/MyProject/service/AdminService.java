package com.jb.MyProject.service;

import com.jb.MyProject.entity.Company;
import com.jb.MyProject.entity.Coupon;
import com.jb.MyProject.entity.Customer;
import com.jb.MyProject.entity.User;
import com.jb.MyProject.exceptions.*;

import java.util.List;

public interface AdminService {

    List<Customer> getAllCustomers();

    List<User> getAllUsers();

    List<Company> getAllCompanies();

    List<Coupon> getAllCoupons();

    Customer removeCustomer(long customerId) throws NoSuchCustomerException;

    Customer removeUserCustomer(long customerId) throws NoSuchCustomerException;

    Company removeCompany(long companyId) throws NoSuchCompanyException;

    Company removeUserCompany(long companyId);

    Company createCompany(String email, String password, String companyName) throws NoSuchUserException, UnknownRoleException;

    Company updateCompany(long id, Company company) throws NoSuchCompanyException;


    Company getCompany(long companyId) throws NoSuchCompanyException;

    Customer addCustomer(Customer customer);

    Customer updateCustomer(long id, Customer customer) throws NoSuchCustomerException;

    Customer getCustomer(long customerId) throws NoSuchCustomerException;

    Coupon updateCoupon(long id, Coupon coupon) throws NoSuchCouponException;

    Coupon removeCoupon(long couponId) throws NoSuchCouponException;

    Coupon getCoupon(long couponId) throws NoSuchCouponException;

}

