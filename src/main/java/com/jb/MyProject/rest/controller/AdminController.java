package com.example.MyProject.rest.controller;

import com.example.MyProject.entity.Company;
import com.example.MyProject.entity.Coupon;
import com.example.MyProject.entity.Customer;
import com.example.MyProject.exceptions.*;
import com.example.MyProject.rest.ClientSession;
import com.example.MyProject.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:4200"},allowCredentials = "true")
@RequestMapping("/api")
public class AdminController {
    private Map<String, ClientSession> tokensMap;

    @Autowired
    public AdminController(@Qualifier("tokens") Map<String, ClientSession> tokensMap) {
        this.tokensMap = tokensMap;
    }

    public ClientSession getSession(String token){
        return tokensMap.get(token);
    }
    @GetMapping("admin/{token}/getAllCustomers")
    public ResponseEntity<List<Customer>> getAllCustomers (@PathVariable String token) {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        List<Customer> customers = service.getAllCustomers();
        return ResponseEntity.ok(customers);
    }
    @GetMapping("admin/{token}/getAllComp")
    public ResponseEntity<List<Company>> getAllComp (@PathVariable String token) {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        List<Company> companies = service.getAllComp();
        return  ResponseEntity.ok(companies);
    }
    @GetMapping("admin/{token}/getAllCoup")
    public ResponseEntity<List<Coupon>> getAllCoup (@PathVariable String token) {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        List<Coupon> coupons = service.getAllCoup();
        return  ResponseEntity.ok(coupons);
    }
    @DeleteMapping("admin/{token}/deleteCustomer/{customer_id}")
    public ResponseEntity<String> deleteCustomer (@PathVariable String token, @PathVariable long customer_id) throws NoSuchCustomerException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        Customer customer = service.getCustomer(customer_id);
        service.removeCustomer(customer_id);
        String msg = "The customer was deleted successfully!";
        return ResponseEntity.ok(msg);
    }
    @DeleteMapping("admin/{token}/deleteCompany/{company_id}")
    public ResponseEntity<Company> deleteCompanyById(@PathVariable String token, @PathVariable long company_id) throws NoSuchCompanyException, SystemMalfunctionException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        Company company = service.getCompany(company_id);
        service.removeCompany(company_id);
        return ResponseEntity.ok(company);
    }
    @DeleteMapping("admin/{token}/deleteCoupon/{coupon_id}")
    public ResponseEntity<Coupon> deleteCouponById(@PathVariable String token, @PathVariable long coupon_id) throws NoSuchCouponException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        Coupon coupon = service.getCoupon(coupon_id);
        service.removeCoupon(coupon_id);
        return ResponseEntity.ok(coupon);
    }
    @PutMapping("admin/{token}/updateCoupon/{coupon_id}")
    public ResponseEntity<Coupon> updateCoupon(
            @PathVariable String token,
            @PathVariable long coupon_id,
            @RequestBody Coupon coupon) throws InvalidUpdateCouponException, NoSuchCouponException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        Coupon updateCoupon = service.updateCoupon(coupon_id, coupon);

        return ResponseEntity.ok(updateCoupon);
    }
    @PutMapping("admin/{token}/updateCustomer/{customer_id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable String token,
                                                   @PathVariable long customer_id,
                                                   @RequestBody Customer customer) throws NoSuchCustomerException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        service.updateCustomer(customer_id, customer);

        return ResponseEntity.ok(customer);
    }
    @PutMapping("admin/{token}/updateCompany/{company_id}")
    public ResponseEntity<Company> updateCompany  (@PathVariable String token,
                                                   @PathVariable long company_id,
                                                   @RequestBody Company company) throws NoSuchCompanyException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        service.updateCompany(company_id, company);

        return ResponseEntity.ok(company);
    }

}

