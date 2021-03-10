package com.jb.MyProject.service;

import com.jb.MyProject.entity.Coupon;
import com.jb.MyProject.entity.CouponShoppingCart;
import com.jb.MyProject.entity.Customer;
import com.jb.MyProject.exceptions.AlreadyPurchaseCouponException;
import com.jb.MyProject.exceptions.NoSuchCouponException;
import com.jb.MyProject.exceptions.NoSuchCustomerException;
import com.jb.MyProject.repository.CouponRepository;
import com.jb.MyProject.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CustomerServiceImpl implements CustomerService {

    private long customerId;
    private CustomerRepository customerRepository;
    private CouponRepository couponRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, CouponRepository couponRepository) {
        this.customerRepository = customerRepository;
        this.couponRepository = couponRepository;
    }

    @Override
    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    @Override
    public Customer getCustomer() throws NoSuchCustomerException {
        Customer customer = new Customer();
        customer.setId(customerId);
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            return optionalCustomer.get();
        }
        throw new NoSuchCustomerException(String.format("There is no customer with this id:%d", customerId));
    }

    @Override
    public Customer getCustomerById(long customerId) throws NoSuchCustomerException {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            return optionalCustomer.get();
        }
        throw new NoSuchCustomerException(String.format("There is no customer with this id:%d", customerId));
    }

    @Override
    public Customer getCustomerByLastName(String customerLastName) {
        return customerRepository.findByLastName(customerLastName);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer updateCustomer(Customer customer) throws NoSuchCustomerException {
        Optional<Customer> optionalCustomer = customerRepository.findById(customer.getId());
        if (optionalCustomer.isPresent()) {
            Customer existCustomer = optionalCustomer.get();
            existCustomer.setFirstName(customer.getFirstName());
            existCustomer.setLastName(customer.getLastName());
            return customerRepository.save(existCustomer);
        }
        throw new NoSuchCustomerException(String.format("There is no customer with this id:%d", customer.getId()));
    }

    @Override
    public Customer removeCustomerById(long customerId) throws NoSuchCustomerException {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            customerRepository.delete(customer);
            return customer;
        }
        throw new NoSuchCustomerException(String.format("There is no customer with this id:%d", customerId));
    }



    public Coupon getCoupon(long couponId) throws NoSuchCouponException {
        Optional<Coupon> optionalCoupon = couponRepository.findById(couponId);
        if (optionalCoupon.isPresent()) {
            return optionalCoupon.get();
        }
        throw new NoSuchCouponException(String.format("There is no coupon with this id:%d", couponId));
    }

    @Override
    public Set getAllCustomerCoupons() {
        return new HashSet(couponRepository.findAllByCustomerId(customerId));
    }

    @Override
    public Coupon purchaseCoupon(long couponId) throws NoSuchCouponException, AlreadyPurchaseCouponException {
        Optional<Coupon> optCoupon = couponRepository.findById(couponId);
        Optional<Customer> optCustomer = customerRepository.findById(this.customerId);
        if (optCoupon.isPresent() && optCustomer.isPresent()) {
            Coupon coupon = optCoupon.get();
            Customer customer = optCustomer.get();

//            for (Coupon c : customer.getCoupons()) {
//                if (coupon.getId() == c.getId()) {
//                    throw new AlreadyPurchaseCouponException(String.format(
//                            "You already have coupon with id#%d", couponId));
//                }
//            }

            customer.add(coupon);         /*  add is adding as well customer to coupon's List of Customers*/
            coupon.setAmount(coupon.getAmount() - 1);/*-1 coupon to the coupon's repository*/
            addAmountPurchasedCoupon(customer, couponId);
            couponRepository.save(coupon);
            customerRepository.save(customer);    /*saving new owner of the coupon to the customer's repository*/
            return coupon;

        }
        throw new NoSuchCouponException(String.format("There is no coupon with this id:%d", couponId));
    }
    @Override
    public Coupon removeCouponFromCustomerShoppingCard(long couponId) throws NoSuchCouponException {
        Coupon coupon = getCoupon(couponId);
        List<Coupon> coupons = couponRepository.findAllByCustomerId(customerId);
        Optional<Customer> optCustomer = customerRepository.findById(customerId);
        if (coupons.contains(coupon) && optCustomer.isPresent()) {
            Customer customer = optCustomer.get();
            coupon.getCustomers().remove(customer);
            coupon.setAmount(coupon.getAmount() + 1);
            decreaseAmountPurchasedCoupon(customer, couponId);
            couponRepository.save(coupon);
            coupons.remove(getCoupon(couponId)); //delete coupon from list coupons that belongs to customer
            customer.setCoupons(coupons);
            customerRepository.save(customer);
            System.out.println("Coupon was removed from shopping card successfully!");
            return coupon;
        }
        throw new NoSuchCouponException(String.format("There is no coupon with this id:%d", couponId));
    }

    private void addAmountPurchasedCoupon(Customer customer, long couponId) {
        Map<Long, Long> amountPurchasedCoupons = customer.getAmountPurchasedCoupons();
        if (!amountPurchasedCoupons.containsKey(couponId)) {
            amountPurchasedCoupons.put(couponId, (long) 1);
        } else {
            amountPurchasedCoupons.put(couponId, amountPurchasedCoupons.get(couponId) + 1);
        }
    }
    private void decreaseAmountPurchasedCoupon(Customer customer, long couponId) {
        Map<Long, Long> amountPurchasedCoupons = customer.getAmountPurchasedCoupons();
        if (!amountPurchasedCoupons.containsKey(couponId)) {
            amountPurchasedCoupons.put(couponId, (long) 1);
        } else {
            amountPurchasedCoupons.put(couponId, amountPurchasedCoupons.get(couponId) -1);
        }
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
