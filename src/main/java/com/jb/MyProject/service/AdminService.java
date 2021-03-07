package com.example.MyProject.service;


import com.example.MyProject.entity.Company;
import com.example.MyProject.entity.Coupon;
import com.example.MyProject.entity.Customer;
import com.example.MyProject.exceptions.*;

import java.util.List;

public interface AdminService {
    List<Company> getAllComp();
    Company createCompany(String email, String password, String name) throws NoSuchUserException, UnknownRoleException;
    Company updateCompany(long id, Company name) throws NoSuchCompanyException;
    Company removeCompany(long company_id) throws NoSuchCompanyException;
         Company getCompany(long company_id) throws NoSuchCompanyException;

    List<Customer> getAllCustomers();
    Customer addCustomer(Customer customer);
    Customer updateCustomer(long id, Customer customer) throws NoSuchCustomerException;
    Customer removeCustomer(long customer_id) throws NoSuchCustomerException;
        Customer getCustomer(long customer_id) throws NoSuchCustomerException;

    List<Coupon> getAllCoup();
    Coupon updateCoupon(long id, Coupon coupon) throws NoSuchCouponException;
    Coupon removeCoupon(long coupon_id) throws NoSuchCouponException;
        Coupon getCoupon(long coupon_id) throws NoSuchCouponException;


        /*general info*/

    List<Company> getAllCompanies();
    List<Coupon> getAllCoupons();



}

