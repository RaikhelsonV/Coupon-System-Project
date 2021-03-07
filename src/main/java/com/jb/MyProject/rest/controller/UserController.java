package com.jb.MyProject.rest.controller;

import com.jb.MyProject.entity.Company;
import com.jb.MyProject.entity.Coupon;
import com.jb.MyProject.entity.Token;
import com.jb.MyProject.entity.User;
import com.jb.MyProject.exceptions.NoSuchCompanyException;
import com.jb.MyProject.exceptions.NoSuchCouponException;
import com.jb.MyProject.exceptions.NoSuchUserException;
import com.jb.MyProject.exceptions.UnknownRoleException;
import com.jb.MyProject.service.CompanyService;
import com.jb.MyProject.service.CustomerService;
import com.jb.MyProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/")
public class UserController {
    private UserService userService;
    private ApplicationContext applicationContext;

    @Autowired
    public UserController(UserService userService, ApplicationContext applicationContext) {
        this.userService = userService;
        this.applicationContext = applicationContext;
    }

    @PostMapping("reg/{email}/{password}/{role}")
    public ResponseEntity<Token> registration(@PathVariable String email,
                                              @PathVariable String password,
                                              @PathVariable int role) throws UnknownRoleException, NoSuchUserException {
        userService.createUser(email, password, role);
        LoginController loginController = applicationContext.getBean(LoginController.class);
        return loginController.login(email, password);
    }

    @GetMapping("user/{email}/{password}")
    public ResponseEntity<User> getUserByEmailAndPassword(@PathVariable String email,
                                                          @PathVariable String password) throws NoSuchUserException {
        User user = userService.getUserByEmailAndPassword(email, password);
        return ResponseEntity.ok(user);
    }

    @PostMapping("update-user/{email}/{password}/{newEmail}/{newPassword}")
    public ResponseEntity<String> updateEmail(
            @PathVariable String email,
            @PathVariable String password,
            @PathVariable String newEmail,
            @PathVariable String newPassword) throws NoSuchUserException {
        userService.updateUser(email, password, newEmail, newPassword);
        String ok = "The email has been changed successfully";
        return ResponseEntity.ok(ok);
    }

    @PostMapping("changePassword/{email}/{password}/{newPassword}")
    public ResponseEntity<String> changePassword(@PathVariable String email,
                                                 @PathVariable String password,
                                                 @PathVariable String newPassword) throws NoSuchUserException {
        userService.updateUserPassword(email, password, newPassword);
        String ok = "The password has been changed successfully";
        return ResponseEntity.ok(ok);
    }


    @GetMapping("coupon-id/{couponId}")
    public ResponseEntity<Coupon> getCoupon(@PathVariable long couponId) throws NoSuchCouponException {
        CustomerService service = applicationContext.getBean(CustomerService.class);
        Coupon coupon = service.getCoupon(couponId);
        return ResponseEntity.ok(coupon);
    }

    @GetMapping("coupons-category/{category}")
    public ResponseEntity<List<Coupon>> getAllCouponsByCategory(@PathVariable String category) {
        CustomerService service = applicationContext.getBean(CustomerService.class);
        List<Coupon> allCouponsByCategory = service.getAllCouponsByCategory(category);
        return ResponseEntity.ok(allCouponsByCategory);
    }

    @GetMapping("user/coupons")
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        UserService service = applicationContext.getBean(UserService.class);
        List<Coupon> coupons = service.getAllCoupons();
        return ResponseEntity.ok(coupons);
    }


    @GetMapping("user/companies")
    public ResponseEntity<List<Company>> getAllCompanies() {
        UserService service = applicationContext.getBean(UserService.class);
        List<Company> companies = service.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("company-id/{companyId}")
    public ResponseEntity<Company> getCompanyById(@PathVariable long companyId) throws NoSuchCompanyException {
        CompanyService service = applicationContext.getBean(CompanyService.class);
        Company company = service.getCompanyById(companyId);
        return ResponseEntity.ok(company);
    }

    @GetMapping("company-id-coupons/{companyId}")
    public ResponseEntity<List<Coupon>> getAllCompanyCoupons(@PathVariable long companyId) {
        CompanyService service = applicationContext.getBean(CompanyService.class);
        List<Coupon> coupons = service.getAllCouponsByCompanyId(companyId);
        return ResponseEntity.ok(coupons);
    }


    /*  @GetMapping("/getCompany/{name}")
      public ResponseEntity<Company> getCompany(@PathVariable String name) throws NoSuchCompanyException {
          CompanyService service = applicationContext.getBean(CompanyService.class);
          Company company = service.getCompanyByName(name);
          return ResponseEntity.ok(company);
      }*/


}
