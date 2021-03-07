package com.example.MyProject.rest.controller;


import com.example.MyProject.entity.Company;
import com.example.MyProject.entity.Coupon;
import com.example.MyProject.exceptions.InvalidUpdateCouponException;
import com.example.MyProject.exceptions.NoSuchCompanyException;
import com.example.MyProject.exceptions.NoSuchCouponException;
import com.example.MyProject.exceptions.WrongInputDataException;
import com.example.MyProject.rest.ClientSession;
import com.example.MyProject.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class CompanyController {
    private Map<String, ClientSession> tokensMap;

    @Autowired
    public CompanyController(@Qualifier("tokens") Map<String, ClientSession> tokensMap) {
        this.tokensMap = tokensMap;
    }

    public ClientSession getSession(String token){
        return tokensMap.get(token);
    }

    @GetMapping("/getCompany/{token}")
    public ResponseEntity<Company> getCompany(@PathVariable String token) throws NoSuchCompanyException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CompanyService service = session.getCompanyService();
        Company company = service.getCompany();
        return  ResponseEntity.ok(company);
    }


    @DeleteMapping("/company/{token}/deleteCoupon/{coupon_id}")
    public ResponseEntity<List<Coupon>> deleteCoupons(
             @PathVariable String token,
            @PathVariable long coupon_id) throws NoSuchCouponException, NoSuchCompanyException {
        ClientSession session = getSession(token);
        if (session != null) {
            CompanyService service = session.getCompanyService();
            service.deleteCouponById(coupon_id);
            return ResponseEntity.ok(service.getAllCoupons());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @GetMapping("/companyCoup/{token}")
    public ResponseEntity<List<Coupon>> getAllCompanyCoupons (@PathVariable String token){
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CompanyService service = session.getCompanyService();
        List<Coupon> allCompanyCoupons = service.getAllCompanyCoupons();
        return ResponseEntity.ok(allCompanyCoupons);
    }


    @PostMapping("/company/{token}/createCoupon")
    public ResponseEntity<Coupon> createCoupon(
            @PathVariable String token,
            @RequestBody Coupon coupon) throws WrongInputDataException, NoSuchCouponException, InvalidUpdateCouponException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CompanyService companyService = session.getCompanyService();
        Coupon newCoupon = companyService.createCoupon(coupon);
        if (newCoupon != null){
            companyService.updateCoupon(newCoupon);
            return  ResponseEntity.ok(newCoupon);
        }
        throw new WrongInputDataException("Couldn't create coupon.");
    }

    @PutMapping("/company/{token}/updateCoupon")
    public ResponseEntity<Coupon> updateCoupon(@PathVariable String token, @RequestBody Coupon coupon) throws InvalidUpdateCouponException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CompanyService service = session.getCompanyService();
        Coupon updateCoupon = service.updateCoupon(coupon);
        if (updateCoupon != null) {
            return ResponseEntity.ok(updateCoupon);
        }
        throw new InvalidUpdateCouponException("Failed to update coupon.");
    }
/*
    @PutMapping("/company/{token}/updateCompany")
    public void updateCompany(@PathVariable String token, @RequestParam String name) throws NoSuchCompanyException {
        ClientSession session = getSession(token);
        CompanyService service = session.getCompanyService();
        service.updateCompany(name);
    }
    */




    @PutMapping("/company/{token}/updateCompany/{company_id}")
    public ResponseEntity<Company> updateCompany (@PathVariable String token,
                                                   @PathVariable long company_id,
                                                   @RequestBody Company company) throws NoSuchCompanyException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CompanyService service = session.getCompanyService();
        service.updateCompany(company_id, company);
        return ResponseEntity.ok(company);
    }






//
//    ClientSession session = getSession(token);
//        if (session == null) {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//    }
//    CompanyService service = session.getCompanyService();
//    Company company = service.getCompany(company_name);
//        company.setName(name);
//        service.updateCompany(company);
//        return ResponseEntity.ok(company);


}