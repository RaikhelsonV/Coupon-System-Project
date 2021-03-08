package com.jb.MyProject.rest.controller;

import com.jb.MyProject.entity.ClientSession;
import com.jb.MyProject.entity.Company;
import com.jb.MyProject.entity.Coupon;
import com.jb.MyProject.exceptions.InvalidUpdateCouponException;
import com.jb.MyProject.exceptions.NoSuchCompanyException;
import com.jb.MyProject.exceptions.NoSuchCouponException;
import com.jb.MyProject.exceptions.WrongInputDataException;
import com.jb.MyProject.service.CompanyService;
import com.jb.MyProject.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/")
public class CompanyController {
    private Map<String, ClientSession> tokensMap;

    @Autowired
    public CompanyController(@Qualifier("tokens") Map<String, ClientSession> tokensMap) {
        this.tokensMap = tokensMap;
    }

    public ClientSession getSession(String token) {
        return tokensMap.get(token);
    }

    @GetMapping("company/{token}")
    public ResponseEntity<Company> getCompany(@PathVariable String token) throws NoSuchCompanyException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CompanyService service = session.getCompanyService();
        Company company = service.getCompany();
        return ResponseEntity.ok(company);
    }

    @GetMapping("company-coupons/{token}")
    public ResponseEntity<List<Coupon>> getAllCompanyCoupons(@PathVariable String token) {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CompanyService service = session.getCompanyService();
        List<Coupon> allCompanyCoupons = service.getAllCompanyCoupons();
        return ResponseEntity.ok(allCompanyCoupons);
    }

    @PutMapping("{token}/update-company/{companyId}")
    public ResponseEntity<Company> updateCompany(@PathVariable String token,
                                                 @PathVariable long companyId,
                                                 @RequestBody Company company) throws NoSuchCompanyException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CompanyService service = session.getCompanyService();
        service.updateCompany(companyId, company);
        return ResponseEntity.ok(company);
    }

    @PutMapping("{token}/update-coupon")
    public ResponseEntity<Coupon> updateCoupon(@PathVariable String token, @RequestBody Coupon coupon) throws InvalidUpdateCouponException, NoSuchCouponException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CouponService service = session.getCouponService();
        Coupon updateCoupon = service.updateCoupon(coupon);
        if (updateCoupon != null) {
            return ResponseEntity.ok(updateCoupon);
        }
        throw new InvalidUpdateCouponException("Failed to update coupon.");
    }

    @PostMapping("{token}/add-coupon")
    public ResponseEntity<Coupon> createCoupon(
            @PathVariable String token,
            @RequestBody Coupon coupon) throws WrongInputDataException, NoSuchCompanyException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CompanyService companyService = session.getCompanyService();
        Coupon newCoupon = companyService.createCoupon(coupon);
        if (newCoupon != null) {
            companyService.createCoupon(newCoupon);
            return ResponseEntity.ok(newCoupon);
        }
        throw new WrongInputDataException("Couldn't create coupon.");
    }

    @DeleteMapping("{token}/delete-coupon/{couponId}")
    public ResponseEntity<List<Coupon>> deleteCouponById(
            @PathVariable String token,
            @PathVariable long couponId) throws NoSuchCouponException, NoSuchCompanyException {
        ClientSession session = getSession(token);
        if (session != null) {
            CouponService couponService = session.getCouponService();
            CompanyService companyService = session.getCompanyService();
            couponService.deleteCouponById(couponId);
            return ResponseEntity.ok(companyService.getAllCompanyCoupons());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
