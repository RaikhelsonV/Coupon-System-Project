package com.jb.MyProject.rest.controller;

import com.jb.MyProject.entity.*;
import com.jb.MyProject.exceptions.NoSuchCompanyException;
import com.jb.MyProject.exceptions.NoSuchCouponException;
import com.jb.MyProject.exceptions.NoSuchCustomerException;
import com.jb.MyProject.exceptions.SystemMalfunctionException;
import com.jb.MyProject.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:4200"}, allowCredentials = "true")
@RequestMapping("/api/")
public class AdminController {
    private Map<String, ClientSession> tokensMap;

    @Autowired
    public AdminController(@Qualifier("tokens") Map<String, ClientSession> tokensMap) {
        this.tokensMap = tokensMap;
    }

    public ClientSession getSession(String token) {
        return tokensMap.get(token);
    }

    @GetMapping("admin/{token}/customers")
    public ResponseEntity<List<Customer>> getAllCustomers(@PathVariable String token) {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        List<Customer> customers = service.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("admin/{token}/users")
    public ResponseEntity<List<User>> getAllUsers(@PathVariable String token) {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        List<User> users = service.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("admin/{token}/companies")
    public ResponseEntity<List<Company>> getAllCompanies(@PathVariable String token) {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        List<Company> companies = service.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("admin/{token}/coupons")
    public ResponseEntity<List<Coupon>> getAllCoupons(@PathVariable String token) {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        List<Coupon> coupons = service.getAllCoupons();
        return ResponseEntity.ok(coupons);
    }

    @DeleteMapping("admin/{token}/delete-customer/{customerId}")
    public ResponseEntity<String> deleteCustomerById(@PathVariable String token, @PathVariable long customerId) throws NoSuchCustomerException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        Customer customer = service.getCustomer(customerId);
        service.removeCustomer(customerId);
        String msg = "The customer was deleted successfully!";
        return ResponseEntity.ok(msg);
    }

    @DeleteMapping("admin/{token}/delete-user-customer/{customerId}")
    public ResponseEntity<String> deleteUserCustomerById(@PathVariable String token, @PathVariable long customerId) throws NoSuchCustomerException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        service.removeUserCustomer(customerId);
        String msg = "The user was deleted successfully!";
        return ResponseEntity.ok(msg);
    }

    @DeleteMapping("admin/{token}/delete-company/{companyId}")
    public ResponseEntity<Company> deleteCompanyById(@PathVariable String token, @PathVariable long companyId) throws NoSuchCompanyException, SystemMalfunctionException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        Company company = service.getCompany(companyId);
        service.removeCompany(companyId);
        return ResponseEntity.ok(company);
    }

    @DeleteMapping("admin/{token}/delete-user-company/{companyId}")
    public ResponseEntity<String> deleteUserCompanyById(@PathVariable String token, @PathVariable long companyId) throws NoSuchCompanyException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        service.removeUserCompany(companyId);
        String msg = "The user was deleted successfully!";
        return ResponseEntity.ok(msg);
    }

    @PutMapping("admin/{token}/update-coupon/{couponId}")
    public ResponseEntity<Coupon> updateCoupon(
            @PathVariable String token,
            @PathVariable long couponId,
            @RequestBody Coupon coupon) throws NoSuchCouponException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        Coupon updateCoupon = service.updateCoupon(couponId, coupon);

        return ResponseEntity.ok(updateCoupon);
    }

    @PutMapping("admin/{token}/update-customer/{customerId}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable String token,
                                                   @PathVariable long customerId,
                                                   @RequestBody Customer customer) throws NoSuchCustomerException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        service.updateCustomer(customerId, customer);

        return ResponseEntity.ok(customer);
    }

    @PutMapping("admin/{token}/update-company/{companyId}")
    public ResponseEntity<Company> updateCompany(@PathVariable String token,
                                                 @PathVariable long companyId,
                                                 @RequestBody Company company) throws NoSuchCompanyException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        service.updateCompany(companyId, company);

        return ResponseEntity.ok(company);
    }

    @DeleteMapping("admin/{token}/delete-coupon/{couponId}")
    public ResponseEntity<Coupon> deleteCouponById(@PathVariable String token, @PathVariable long couponId) throws NoSuchCouponException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        Coupon coupon = service.getCoupon(couponId);
        service.removeCoupon(couponId);
        return ResponseEntity.ok(coupon);
    }


}

