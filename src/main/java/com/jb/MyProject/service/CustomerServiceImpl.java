package com.example.MyProject.service;

import com.example.MyProject.entity.Coupon;
import com.example.MyProject.entity.Customer;
import com.example.MyProject.exceptions.AlreadyPurchaseCouponException;
import com.example.MyProject.exceptions.NoSuchCouponException;
import com.example.MyProject.exceptions.NoSuchCustomerException;
import com.example.MyProject.repository.CouponRepository;
import com.example.MyProject.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CustomerServiceImpl implements CustomerService {

    private long customer_id;
    private CustomerRepository customerRepository;
    private CouponRepository couponRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, CouponRepository couponRepository) {
        this.customerRepository = customerRepository;
        this.couponRepository = couponRepository;
    }

    @Override
    public void setCustomer_id(long customer_id) {
        this.customer_id = customer_id;
    }

    @Override
    public Customer getCustomer() {
        Customer customer = new Customer();
        customer.setId(customer_id);
        return customer;
    }

    @Override
    public Customer getCustomerById(long customer_id) throws NoSuchCustomerException {
        Optional<Customer> optionalCustomer = customerRepository.findById(customer_id);
        if (optionalCustomer.isPresent()) {
            return optionalCustomer.get();
        }
        throw new NoSuchCustomerException(String.format("There is no customer with this id:%d", customer_id));
    }

    @Override
    public Customer getCustomerByLastName(String customerLastName) {
        return customerRepository.findByLastName(customerLastName);
    }

    @Override
    public Customer updateCustomer(long id, Customer customer) throws NoSuchCustomerException {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isPresent()) {
            Customer existCustomer = optionalCustomer.get();
            existCustomer.setFirstName(customer.getFirstName());
            existCustomer.setLastName(customer.getLastName());
            return customerRepository.save(existCustomer);
        }
        throw new NoSuchCustomerException(String.format("There is no customer with this id:%d", id));
    }
    @Override
    public Coupon toUseCoupon(long coupon_id) throws NoSuchCouponException {
        Coupon coupon = getCoupon(coupon_id);
        List<Coupon> coupons = couponRepository.findAllByCustomerId(customer_id);
        Optional<Customer> optCustomer = customerRepository.findById(customer_id);
        if (coupons.contains(coupon) && optCustomer.isPresent()) {
            Customer customer = optCustomer.get();
            coupon.getCustomers().remove(customer); //delete customer from customer's list of specified coupon
            couponRepository.save(coupon);
            coupons.remove(getCoupon(coupon_id)); //delete coupon from list coupons that belongs to customer
            customer.setCoupons(coupons);
            customerRepository.save(customer);
            System.out.println("Coupon was released successfully!");
            return coupon;
        }
        String message = "No such coupon to release.";
        throw new NoSuchCouponException(message);
    }

    @Override
    public Coupon purchaseCoupon(long coupon_id) throws NoSuchCouponException, AlreadyPurchaseCouponException {
        Optional<Coupon> optCoupon = couponRepository.findById(coupon_id);
        Optional<Customer> optCustomer = customerRepository.findById(this.customer_id);
        Coupon coupon;
        Customer customer;

        if (optCoupon.isPresent() && optCustomer.isPresent()) {
            coupon = optCoupon.get();
            customer = optCustomer.get();

            for (Coupon c : customer.getCoupons()) {
                if (coupon.getId() == c.getId()) {
                    throw new AlreadyPurchaseCouponException(String.format(
                            "You already have coupon with id#%d", coupon_id));
                }
            }

            customer.add(coupon);         /*  add is adding as well customer to coupon's List of Customers*/
            coupon.setAmount(coupon.getAmount()-1); /*-1 coupon to the coupon's repository*/
            couponRepository.save(coupon);
            customerRepository.save(customer);    /*saving new owner of the coupon to the customer's repository*/
            return coupon;

        }
        throw new NoSuchCouponException(String.format("Couldn't find coupon with such id #%d.", coupon_id));
    }

    @Override
    public List<Coupon> getAllCustomerCoupons() {
        return couponRepository.findAllByCustomerId(customer_id);
    }

    /*general info*/

    @Override
    public Coupon getCoupon(long coupon_id) throws NoSuchCouponException {
        Optional<Coupon> optionalCoupon = couponRepository.findById(coupon_id);
        if (optionalCoupon.isPresent()) {
            return optionalCoupon.get();
        }
        throw new NoSuchCouponException(String.format("There is no coupon with this id:%d",coupon_id));
    }
    @Override
    public List<Coupon> getAllCouponsByCategory(String category) {
        return couponRepository.findAllByCategory(category); }
    @Override
    public List<Coupon> getAllCouponsByPriceLessThan(double price) {
        return couponRepository.findAllByPriceLessThan(price); }

    @Override
    public List<Coupon> getAllByPriceIsGreaterThan(double price) {
        return couponRepository.findAllByPriceIsGreaterThan(price);
    }

    @Override
    public List<Coupon> getAllCouponsByTittle(String title) {
        return couponRepository.findAllByTitle(title); }
    @Override
    public List<Coupon> getAllCouponsByDescriptionLike(String description) {
        return couponRepository.findAllByDescription(description); }


}
