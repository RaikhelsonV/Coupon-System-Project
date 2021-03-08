package com.jb.MyProject.service;

import com.jb.MyProject.entity.Coupon;
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

import java.util.List;
import java.util.Optional;

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

    @Override
    public Coupon removeCouponFromCustomerShoppingCard(long couponId) throws NoSuchCouponException {
        Coupon coupon = getCoupon(couponId);
        List<Coupon> coupons = couponRepository.findAllByCustomerId(customerId);
        Optional<Customer> optCustomer = customerRepository.findById(customerId);
        if (coupons.contains(coupon) && optCustomer.isPresent()) {
            Customer customer = optCustomer.get();
            coupon.getCustomers().remove(customer); //delete customer from customer's list of specified coupon
            couponRepository.save(coupon);
            coupons.remove(getCoupon(couponId)); //delete coupon from list coupons that belongs to customer
            customer.setCoupons(coupons);
            customerRepository.save(customer);
            System.out.println("Coupon was removed from shopping card successfully!");
            return coupon;
        }
        String message = "No such coupon to release.";
        throw new NoSuchCouponException(message);
    }

    public Coupon getCoupon(long couponId) throws NoSuchCouponException {
        Optional<Coupon> optionalCoupon = couponRepository.findById(couponId);
        if (optionalCoupon.isPresent()) {
            return optionalCoupon.get();
        }
        throw new NoSuchCouponException(String.format("There is no coupon with this id:%d", couponId));
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
            coupon.setAmount(coupon.getAmount() - 1); /*-1 coupon to the coupon's repository*/
            couponRepository.save(coupon);
            customerRepository.save(customer);    /*saving new owner of the coupon to the customer's repository*/
            return coupon;

        }
        throw new NoSuchCouponException(String.format("Couldn't find coupon with such id #%d.", couponId));
    }

    @Override
    public Coupon addAmountCouponsInShoppingBag(long id, int amountShoppingBag) throws NoSuchCouponException {
        Optional<Coupon> optionalCoupon = couponRepository.findById(id);
        if (!optionalCoupon.isPresent()) {
            throw new NoSuchCouponException(String.format("There is no coupon with such id: " + id));
        }
        Coupon coupon = optionalCoupon.get();
        coupon.setAmount(coupon.getAmount() - amountShoppingBag);
        couponRepository.save(coupon);
        return coupon;
    }

    @Override
    public List<Coupon> getAllCustomerCoupons() {
        return couponRepository.findAllByCustomerId(customerId);
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
    public long getAmountOfPurchasesCouponsInShoppingCard() {
        return Long.parseLong(null);
    }


}
