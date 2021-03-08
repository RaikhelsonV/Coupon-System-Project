package com.jb.MyProject.rest.controller;

import com.jb.MyProject.entity.ClientSession;
import com.jb.MyProject.entity.Coupon;
import com.jb.MyProject.entity.Customer;
import com.jb.MyProject.exceptions.AlreadyPurchaseCouponException;
import com.jb.MyProject.exceptions.InvalidUpdateCouponException;
import com.jb.MyProject.exceptions.NoSuchCouponException;
import com.jb.MyProject.exceptions.NoSuchCustomerException;
import com.jb.MyProject.service.CouponService;
import com.jb.MyProject.service.CustomerService;
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
public class CustomerController {

    private Map<String, ClientSession> tokensMap;

    @Autowired
    public CustomerController(@Qualifier("tokens") Map<String, ClientSession> tokensMap) {
        this.tokensMap = tokensMap;
    }

    public ClientSession getSession(String token) {
        return tokensMap.get(token);
    }

    @GetMapping("customer/{token}")
    public ResponseEntity<Customer> getCustomer(@PathVariable String token) throws NoSuchCustomerException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerService service = session.getCustomerService();
        Customer customer = service.getCustomer();
        return ResponseEntity.ok(customer);
    }

    @GetMapping("customer-id/{token}/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String token,
                                                    @PathVariable long customerId) throws NoSuchCustomerException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerService service = session.getCustomerService();
        Customer customer = service.getCustomerById(customerId);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("customer-coupons/{token}")
    public ResponseEntity<List<Coupon>> getAllCustomerCoupons(@PathVariable String token) {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerService service = session.getCustomerService();
        List<Coupon> allCustomerCoupons = service.getAllCustomerCoupons();
        return ResponseEntity.ok(allCustomerCoupons);
    }

    @GetMapping("customer-coupons-price/{token}")
    public ResponseEntity<Long> getAllCustomerCouponsTotalPrice(@PathVariable String token) {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerService service = session.getCustomerService();
        long totalPrice = service.getTotalPriceOfPurchasesCouponsInShoppingCart();
        return ResponseEntity.ok(totalPrice);
    }

    @GetMapping("customer/{token}/coupons-description/{description}")
    public ResponseEntity<List<Coupon>> getAllCouponsByDescriptionLike(@PathVariable String token,
                                                                       @PathVariable String description) {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CouponService service = session.getCouponService();
        List<Coupon> allCouponsByDescriptionLike = service.getAllCouponsByDescriptionLike(description);
        return ResponseEntity.ok(allCouponsByDescriptionLike);
    }

    @GetMapping("customer/{token}/coupons-title/{title}")
    public ResponseEntity<List<Coupon>> getAllCouponsByTitle(@PathVariable String token,
                                                             @PathVariable String title) {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CouponService service = session.getCouponService();
        List<Coupon> allCouponsByTittle = service.getAllCouponsByTittle(title);
        return ResponseEntity.ok(allCouponsByTittle);
    }

    @GetMapping("customer/{token}/coupons-price-less-than/{price}")
    public ResponseEntity<List<Coupon>> getAllCouponsByPriceLessThan(@PathVariable String token,
                                                                     @PathVariable double price) {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CouponService service = session.getCouponService();
        List<Coupon> coupons = service.getAllCouponsByPriceLessThan(price);
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("customer/{token}/coupons-price-greater-than/{price}")
    public ResponseEntity<List<Coupon>> getAllByPriceIsGreaterThan(@PathVariable String token,
                                                                   @PathVariable double price) {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CouponService service = session.getCouponService();
        List<Coupon> coupons = service.getAllByPriceIsGreaterThan(price);
        return ResponseEntity.ok(coupons);
    }

    @PutMapping("{token}/update-customer")
    public ResponseEntity<Customer> updateCustomer(@PathVariable String token,
                                                   @RequestBody Customer customer) throws NoSuchCustomerException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerService service = session.getCustomerService();
        service.getCustomer();
        service.updateCustomer(customer);
        return ResponseEntity.ok(customer);
    }


    @PostMapping("customer/{token}/release-coupon/{couponId}")
    public ResponseEntity<Coupon> releaseCoupon(
            @PathVariable String token,
            @PathVariable long couponId) throws NoSuchCouponException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerService service = session.getCustomerService();
        Coupon coupon = service.removeCouponFromCustomerShoppingCard(couponId);
        return ResponseEntity.ok(coupon);
    }

    @PostMapping("customer/{token}/purchase-coupon/{couponId}")
    public ResponseEntity<Coupon> purchaseCoupon(
            @PathVariable String token,
            @PathVariable long couponId) throws NoSuchCouponException, AlreadyPurchaseCouponException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CustomerService service = session.getCustomerService();
        Coupon coupon = service.purchaseCoupon(couponId);
        return ResponseEntity.ok(coupon);
    }

    @PutMapping("{token}/change-amount/{couponId}/{amount}")
    public ResponseEntity<Coupon> changeAmount(@PathVariable String token, @PathVariable long couponId,
                                               @PathVariable int amount) throws InvalidUpdateCouponException, NoSuchCouponException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerService service = session.getCustomerService();
        Coupon updateCoupon = service.addAmountCouponsInShoppingBag(couponId, amount);
        if (updateCoupon != null) {
            return ResponseEntity.ok(updateCoupon);
        }
        throw new InvalidUpdateCouponException("Failed to update coupon.");
    }

}
