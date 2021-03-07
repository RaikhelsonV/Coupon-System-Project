package com.jb.MyProject.service;

import com.jb.MyProject.entity.Company;
import com.jb.MyProject.entity.Coupon;
import com.jb.MyProject.entity.Customer;
import com.jb.MyProject.exceptions.NoSuchCompanyException;
import com.jb.MyProject.exceptions.NoSuchCouponException;
import com.jb.MyProject.repository.CompanyRepository;
import com.jb.MyProject.repository.CouponRepository;
import com.jb.MyProject.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CompanyServiceImpl implements CompanyService {

    private long companyId;
    private CompanyRepository companyRepository;
    private CouponRepository couponRepository;
    private ApplicationContext context;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, CouponRepository couponRepository, ApplicationContext context) {
        this.companyRepository = companyRepository;
        this.couponRepository = couponRepository;
        this.context = context;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    @Override
    public Company getCompany() {
        Optional<Company> optionalCompany = companyRepository.findById(companyId);
        return optionalCompany.get();
    }

    @Override
    public List<Coupon> getAllCompanyCoupons() {
        List<Coupon> coupons = couponRepository.findAllByCompanyId(companyId);
        return coupons;
    }

    @Override
    public Company getCompanyById(long companyId) throws NoSuchCompanyException {
        Optional<Company> optionalCompany = companyRepository.findById(companyId);
        if (optionalCompany.isPresent()) {
            return optionalCompany.get();
        }
        throw new NoSuchCompanyException(String.format("There is no company with this id:%d", companyId));
    }

    @Override
    public Company getCompanyByName(String companyName) throws NoSuchCompanyException {
        Optional<Company> optionalCompany = companyRepository.findById(this.companyId);
        if (optionalCompany.isPresent()) {
            return optionalCompany.get();
        }
        throw new NoSuchCompanyException(String.format("There is no company with this id:%d", this.companyId));
    }


    @Override
    public List<Coupon> getAllCouponsByCompanyId(long companyId) {
        return couponRepository.findAllByCompanyId(companyId);
    }

    @Override
    public Company updateCompany(long id, Company company) throws NoSuchCompanyException {
        Optional<Company> optionalCompany = companyRepository.findById(id);
        if (optionalCompany.isPresent()) {
            Company existCompany = optionalCompany.get();
            existCompany.setName(company.getName());
            existCompany.setImageURL(company.getImageURL());
            return companyRepository.save(existCompany);
        }
        throw new NoSuchCompanyException(String.format("There is no customer with this id:%d", id));
    }

    @Override
    @Transactional
    public Coupon createCoupon(Coupon coupon) throws NoSuchCompanyException {
        Optional<Company> optionalCompany = companyRepository.findById(companyId);
        if (!optionalCompany.isPresent()) {
            throw new NoSuchCompanyException(String.format("There is no company with such id:%d " + optionalCompany.get().getId()));
        }
        Company company = optionalCompany.get();
        if (company.getCoupons() != null) {
            for (Coupon existCoupon : company.getCoupons()) {
                if (coupon.similarCoupon(existCoupon)) {
                    existCoupon.setAmount(existCoupon.getAmount() + coupon.getAmount());
                    existCoupon.setCompanyId(companyId);
                    return couponRepository.save(existCoupon);
                }
            }
        }
        coupon.setCompanyId(companyId);
        company.add(coupon);
        return couponRepository.save(coupon);
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

    /*generalInfo*/
    @Override
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAllByCompanyId(companyId);
    }

    @Override
    public List<Coupon> getAllCouponsByCategory(String category) {
        return couponRepository.findAllByCategory(category);
    }

    @Override
    public List<Coupon> getAllCouponsByEndDate(LocalDate endDate) {
        List<Coupon> coupons = new ArrayList<>();
        for (Coupon coupon : couponRepository.findAllByCompanyId(companyId)) {
            if (coupon.getEndDate().equals(endDate)) {
                coupons.add(coupon);
            }
        }
        return coupons;
    }

    @Override
    public List<Coupon> getAllCouponsByStartDate(LocalDate startDate) {
        List<Coupon> coupons = new ArrayList<>();
        for (Coupon coupon : couponRepository.findAllByCompanyId(companyId)) {
            if (coupon.getStartDate().equals(startDate)) {
                coupons.add(coupon);
            }
        }
        return coupons;
    }

    @Override
    public List<Coupon> getAllCouponsByPriceLessThan(double price) {
        return couponRepository.findAllByPriceLessThan(price);
    }

    @Override
    public List<Coupon> getAllCouponsByTittle(String title) {
        return couponRepository.findAllByTitle(title);
    }

    @Override
    public List<Coupon> getAllCouponsByDescriptionLike(String description) {
        return couponRepository.findAllByDescription(description);
    }


}

