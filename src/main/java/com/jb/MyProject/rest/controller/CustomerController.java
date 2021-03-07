package com.example.MyProject.rest.controller;

import com.example.MyProject.entity.Coupon;
import com.example.MyProject.entity.Customer;
import com.example.MyProject.exceptions.AlreadyPurchaseCouponException;
import com.example.MyProject.exceptions.NoSuchCouponException;
import com.example.MyProject.exceptions.NoSuchCustomerException;
import com.example.MyProject.rest.ClientSession;
import com.example.MyProject.service.CustomerService;
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
public class CustomerController {

    private Map<String, ClientSession> tokensMap;

    @Autowired
    public CustomerController(@Qualifier("tokens") Map <String, ClientSession> tokensMap)  {
        this.tokensMap = tokensMap;
    }

    public ClientSession getSession(String token){
        return tokensMap.get(token);
    }

    @GetMapping("/getCustomer/{token}")
    public ResponseEntity<Customer> getCustomer(@PathVariable String token){
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerService service = session.getCustomerService();
        Customer customer = service.getCustomer();
        return  ResponseEntity.ok(customer);
    }
    @GetMapping("/getCustomerById/{token}/{customer_id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String token,
            @PathVariable long customer_id) throws NoSuchCustomerException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerService service = session.getCustomerService();
        Customer customer = service.getCustomerById(customer_id);
        return ResponseEntity.ok(customer);
    }


    @GetMapping("/customerCoup/{token}")
    public ResponseEntity<List<Coupon>> getAllCustomerCoupons (@PathVariable String token){
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerService service = session.getCustomerService();
        List<Coupon> allCustomerCoupons = service.getAllCustomerCoupons();
        return ResponseEntity.ok(allCustomerCoupons);
    }
    @GetMapping("/description/{token}/{description}")
    public ResponseEntity<List<Coupon>> getAllCouponsByDescriptionLike (@PathVariable String token,
                                                                        @PathVariable String description){
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerService service = session.getCustomerService();
        List<Coupon> allCouponsByDescriptionLike = service.getAllCouponsByDescriptionLike(description);
        return ResponseEntity.ok(allCouponsByDescriptionLike);
    }
    @GetMapping("/title/{token}/{title}")
    public ResponseEntity<List<Coupon>> getAllCouponsByTittle (@PathVariable String token,
                                                               @PathVariable String title)  {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerService service = session.getCustomerService();
        List<Coupon> allCouponsByTittle = service.getAllCouponsByTittle(title);
        return ResponseEntity.ok(allCouponsByTittle);
    }

    @GetMapping("/priceLessThan/{token}/{price}")
    public ResponseEntity<List<Coupon>> getAllCouponsByPriceLessThan (@PathVariable String token,
                                                                      @PathVariable double price ){
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerService service = session.getCustomerService();
        List<Coupon> coupons = service.getAllCouponsByPriceLessThan(price);
        return ResponseEntity.ok(coupons);
    }
    @GetMapping("/priceGreaterThan/{token}/{price}")
    public ResponseEntity<List<Coupon>>  getAllByPriceIsGreaterThan (@PathVariable String token,
                                                                      @PathVariable double price ){
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerService service = session.getCustomerService();
        List<Coupon> coupons = service.getAllByPriceIsGreaterThan(price);
        return ResponseEntity.ok(coupons);
    }
     @PutMapping("/customer/{token}/updateCustomer/{customer_id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable String token,
                                                   @PathVariable long customer_id,
                                                   @RequestBody Customer customer) throws NoSuchCustomerException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerService service = session.getCustomerService();
        service.getCustomer();
        service.updateCustomer(customer_id, customer);
        return ResponseEntity.ok(customer);
    }


    @PostMapping("/customer/{token}/useCoupon/{coupon_id}")
    public ResponseEntity<Coupon> releaseCoupon(
            @PathVariable String token,
            @PathVariable long coupon_id) throws NoSuchCouponException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerService service = session.getCustomerService();
        Coupon coupon = service.toUseCoupon(coupon_id);
        return ResponseEntity.ok(coupon);
    }
    @PostMapping("/customer/{token}/addCoupon/{coupon_id}")
    public ResponseEntity<Coupon> purchaseCoupon(
            @PathVariable String token,
            @PathVariable long coupon_id) throws NoSuchCouponException, AlreadyPurchaseCouponException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CustomerService service = session.getCustomerService();
        Coupon coupon = service.purchaseCoupon(coupon_id);
        return ResponseEntity.ok(coupon);
    }
}
