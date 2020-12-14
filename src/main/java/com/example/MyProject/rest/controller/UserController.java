package com.example.MyProject.rest.controller;

import com.example.MyProject.entity.Company;
import com.example.MyProject.entity.Coupon;
import com.example.MyProject.entity.Token;
import com.example.MyProject.entity.User;
import com.example.MyProject.exceptions.*;
import com.example.MyProject.service.AdminService;
import com.example.MyProject.service.CompanyService;
import com.example.MyProject.service.CustomerService;
import com.example.MyProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class UserController {
    private UserService service;
    private ApplicationContext applicationContext;

    @Autowired
    public UserController(UserService service, ApplicationContext applicationContext ) {
        this.service = service;
        this.applicationContext = applicationContext;
    }

    @GetMapping("/getAllCoupons")
    public ResponseEntity<List<Coupon>> getAllCoupons(){
        AdminService service = applicationContext.getBean(AdminService.class);
        List<Coupon> coupons = service.getAllCoupons();
        return  ResponseEntity.ok(coupons);
    }
    @GetMapping("/getAllCompanies")
    public ResponseEntity<List<Company>> getAllCompanies ()  {
        AdminService service = applicationContext.getBean(AdminService.class);
        List<Company> companies = service.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

  /*  @GetMapping("/getCompany/{name}")
    public ResponseEntity<Company> getCompany(@PathVariable String name) throws NoSuchCompanyException {
        CompanyService service = applicationContext.getBean(CompanyService.class);
        Company company = service.getCompanyByName(name);
        return ResponseEntity.ok(company);
    }*/
    @GetMapping("/getCompanyById/{company_id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable long company_id) throws NoSuchCompanyException {
        CompanyService service = applicationContext.getBean(CompanyService.class);
        Company company = service.getCompanyById(company_id);
        return ResponseEntity.ok(company);
    }

    @GetMapping("/getCouponById/{coupon_id}")
    public ResponseEntity<Coupon> getCoupon(@PathVariable long coupon_id) throws NoSuchCouponException {
        CustomerService service = applicationContext.getBean(CustomerService.class);
        Coupon coupon = service.getCoupon(coupon_id);
        return ResponseEntity.ok(coupon);
    }
    @GetMapping("/getAllCouponsByCategory/{category}")
    public ResponseEntity<List<Coupon>> getAllCouponsByCategory (@PathVariable String category)  {
        CustomerService service = applicationContext.getBean(CustomerService.class);
        List<Coupon> allCouponsByCategory = service.getAllCouponsByCategory(category);
        return ResponseEntity.ok(allCouponsByCategory);
    }
    @GetMapping("comCoup/{id}")
    public ResponseEntity<List<Coupon>> getAllCompanyCoupons (@PathVariable long id){

        CompanyService service = applicationContext.getBean(CompanyService.class);
        List<Coupon> allCompanyCouponsR = service.getAllCompanyCouponsR(id);
        return ResponseEntity.ok(allCompanyCouponsR);
    }




    @GetMapping("/users/{email}/{password}")
    public ResponseEntity<User> login(@PathVariable String email, @PathVariable String password) throws NoSuchUserException {
        User user = service.getUserByEmailAndPassword(email, password);
        if (user == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(user);
    }
    @PostMapping("/reg/{email}/{password}/{role}")
    public ResponseEntity<Token> registration(@PathVariable String email,
                                              @PathVariable String password,
                                              @PathVariable int role) throws UnknownRoleException, NoSuchUserException {
        service.createUser(email, password, role);
        LoginController loginController = applicationContext.getBean(LoginController.class);
        return loginController.login(email, password);
    }
//    @PostMapping("/reg/{email}/{password}/{role}/{customerFirstName}/{customerLastName}")
//    public ResponseEntity<Token> registrationCus(@PathVariable String email,
//                                              @PathVariable String password,
//                                              @PathVariable int role,
//                                                 @PathVariable String customerFirstName,
//                                                 @PathVariable String customerLastName) throws UnknownRoleException, NoSuchUserException {
//        service.createUserCus(email, password, role, customerFirstName, customerLastName);
//        LoginController loginController = applicationContext.getBean(LoginController.class);
//        return loginController.login(email, password);
//    }


    @PostMapping("/changeEmail/{email}/{password}/{newEmail}")
    public ResponseEntity<String> updateEmail(
            @PathVariable String email,
            @PathVariable String password,
            @PathVariable String newEmail) throws NoSuchUserException {
        service.updateUsersEmail(email, password, newEmail);
        String ok = "The email has been changed successfully";
        return ResponseEntity.ok(ok);
    }

    @PostMapping("/changePassword/{email}/{password}/{newPassword}")
    public ResponseEntity<String> changePassword(@PathVariable String email,
                                                 @PathVariable String password,
                                                 @PathVariable String newPassword) throws NoSuchUserException {
        service.updateUsersPassword(email, password, newPassword);
        String ok = "The password has been changed successfully";
        return ResponseEntity.ok(ok);
    }
}