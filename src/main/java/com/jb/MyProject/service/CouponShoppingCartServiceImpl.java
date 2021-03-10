package com.jb.MyProject.service;

import com.jb.MyProject.entity.Coupon;
import com.jb.MyProject.entity.CouponShoppingCart;
import com.jb.MyProject.entity.Customer;
import com.jb.MyProject.repository.CouponRepository;
import com.jb.MyProject.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class CouponShoppingCartServiceImpl implements CouponShoppingCartService {

    private long customerId;
    private CustomerRepository customerRepository;
    private CouponRepository couponRepository;

    @Autowired
    public CouponShoppingCartServiceImpl(CustomerRepository customerRepository, CouponRepository couponRepository) {
        this.customerRepository = customerRepository;
        this.couponRepository = couponRepository;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    @Override
    public long getTotalPriceOfPurchasesCouponsInShoppingCart() {
        List<Coupon> coupons = couponRepository.findAllByCustomerId(customerId);
        long sum = 0;
        for (int i = 0; i < coupons.size(); i++) {
            sum += (long) (coupons.get(i).getPrice());
        }
        return sum;
    }

    @Override
    public Set<CouponShoppingCart> formationTableCouponsInShoppingCart() {
        HashSet<Coupon> coupons = new HashSet(couponRepository.findAllByCustomerId(customerId));
        Set<CouponShoppingCart> couponsShoppingCart = new HashSet<>();
        for (Coupon coupon : coupons) {
            for (Map.Entry<Long, Long> entry : getAmountPurchasedCoupons().entrySet()) {
                if (coupon.getId() == entry.getKey()) {
                    Long amount = getAmountPurchasedCoupons().get(entry.getKey());
                    couponsShoppingCart.add(createCouponInShoppingCart(
                            coupon,
                            amount,
                            calculatePricePurchasedCoupon(coupon.getPrice(), amount)));
                }
            }
        }
        return couponsShoppingCart;

    }

    private CouponShoppingCart createCouponInShoppingCart(Coupon coupon, double amountPurchasedCoupons, double pricePurchasedCoupon) {
        CouponShoppingCart couponShoppingCart = new CouponShoppingCart();
        couponShoppingCart.setId(coupon.getId());
        couponShoppingCart.setTitle(coupon.getTitle());
        couponShoppingCart.setDescription(coupon.getDescription());
        couponShoppingCart.setCategory(coupon.getCategory());
        couponShoppingCart.setImageURL(coupon.getImageURL());
        couponShoppingCart.setAmountPurchasedCoupons(amountPurchasedCoupons);
        couponShoppingCart.setPricePurchasedCoupons(pricePurchasedCoupon);
        return couponShoppingCart;
    }

    private Map<Long, Long> getAmountPurchasedCoupons() {
        Optional<Customer> optCustomer = customerRepository.findById(this.customerId);
        Customer customer = optCustomer.get();
        return customer.getAmountPurchasedCoupons();
    }

    private double calculatePricePurchasedCoupon(double price, Long amountPurchasedCoupons) {
        return price * amountPurchasedCoupons;
    }

}
