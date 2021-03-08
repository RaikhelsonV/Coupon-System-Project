package com.jb.MyProject.service;

import com.jb.MyProject.entity.Company;
import com.jb.MyProject.entity.Coupon;
import com.jb.MyProject.entity.Customer;
import com.jb.MyProject.exceptions.NoSuchCouponException;
import com.jb.MyProject.repository.CouponRepository;
import com.jb.MyProject.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CouponServiceImpl implements CouponService {
    private CouponRepository couponRepository;
    private ApplicationContext context;

    @Autowired
    public CouponServiceImpl(CouponRepository couponRepository, ApplicationContext context) {
        this.couponRepository = couponRepository;
        this.context = context;
    }

    @Override
    public Coupon getCouponById(long couponId) throws NoSuchCouponException {
        Optional<Coupon> optionalCoupon = couponRepository.findById(couponId);
        if (optionalCoupon.isPresent()) {
            return optionalCoupon.get();
        }
        throw new NoSuchCouponException(String.format("There is no coupon with this id:%d", couponId));
    }

    @Override
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    public List<Coupon> getAllCouponsByPriceLessThan(double price) {
        return couponRepository.findAllByPriceLessThan(price);
    }
    @Override
    public List<Coupon> getAllByPriceIsGreaterThan(double price) {
        return couponRepository.findAllByPriceIsGreaterThan(price);
    }

    @Override
    public List<Coupon> getAllCouponsByTittle(String title) {
        return couponRepository.findAllByTitle(title);
    }

    @Override
    public List<Coupon> getAllCouponsByDescriptionLike(String description) {
        return couponRepository.findAllByDescription(description);
    }
    @Override
    public List<Coupon> getAllCouponsByCategory(String category) {
        return couponRepository.findAllByCategory(category);
    }
    @Override
    public Coupon updateCoupon(Coupon coupon) throws NoSuchCouponException {
        Optional<Coupon> optionalCoupon = couponRepository.findById(coupon.getId());
        if (!optionalCoupon.isPresent()) {
            throw new NoSuchCouponException(String.format("There is no coupon with such id: " + coupon.getId()));
        }
        Coupon updatedCoupon = optionalCoupon.get();
        updatedCoupon.setTitle(coupon.getTitle());
        updatedCoupon.setStartDate(coupon.getStartDate());
        updatedCoupon.setEndDate(coupon.getEndDate());
        updatedCoupon.setCategory(coupon.getCategory());
        updatedCoupon.setAmount(coupon.getAmount());
        updatedCoupon.setDescription(coupon.getDescription());
        updatedCoupon.setPrice(coupon.getPrice());
        updatedCoupon.setImageURL(coupon.getImageURL());
        couponRepository.save(updatedCoupon);
        return updatedCoupon;
    }

    @Override
    public Coupon updateCouponById(long id, Coupon coupon) throws NoSuchCouponException {
        Optional<Coupon> optionalCoupon = couponRepository.findById(id);
        if (optionalCoupon.isPresent()) {
            Coupon existCoupon = optionalCoupon.get();
            existCoupon.setTitle(coupon.getTitle());
            existCoupon.setStartDate(coupon.getStartDate());
            existCoupon.setEndDate(coupon.getEndDate());
            existCoupon.setCategory(coupon.getCategory());
            existCoupon.setAmount((coupon.getAmount()));
            existCoupon.setPrice(coupon.getPrice());
            existCoupon.setDescription(coupon.getDescription());
            existCoupon.setImageURL(coupon.getImageURL());
            return couponRepository.save(existCoupon);
        }
        throw new NoSuchCouponException(String.format("There is not coupon with such%d", coupon.getId()));
    }


    @Override
    public void deleteCouponById(long couponId) throws NoSuchCouponException {
        Optional<Coupon> optionalCoupon = couponRepository.findById(couponId);
        if (optionalCoupon.isPresent()) {
            Coupon coupon = optionalCoupon.get();
            CustomerRepository customerRepository = context.getBean(CustomerRepository.class);
            for (Customer customer : customerRepository.findAll()) {
                if (customer.getCoupons().contains(coupon)) {
                    customer.getCoupons().remove(coupon);
                }
            }
            Company company = coupon.getCompany();
            List<Coupon> coupons = company.getCoupons();
            coupons.remove(coupon);
            company.setCoupons(coupons);
            couponRepository.deleteById(couponId);
            System.out.println("Coupon was deleted successfully!");
        } else throw new NoSuchCouponException(String.format("Coupon does not exist with id%d", couponId));
    }

    @Override
    public Coupon removeCouponById(long couponId) throws NoSuchCouponException {
        Optional<Coupon> optionalCoupon = couponRepository.findById(couponId);
        if (optionalCoupon.isPresent()) {
            Coupon coupon = optionalCoupon.get();
            couponRepository.delete(coupon);
            return coupon;
        }
        throw new NoSuchCouponException(String.format("There is no coupon with this id:%d", couponId));
    }
}
