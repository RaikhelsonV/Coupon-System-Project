package com.example.MyProject.service;

import com.example.MyProject.entity.Company;
import com.example.MyProject.entity.Coupon;
import com.example.MyProject.entity.Customer;
import com.example.MyProject.exceptions.InvalidUpdateCouponException;
import com.example.MyProject.exceptions.NoSuchCompanyException;
import com.example.MyProject.exceptions.NoSuchCouponException;
import com.example.MyProject.repository.CompanyRepository;
import com.example.MyProject.repository.CouponRepository;
import com.example.MyProject.repository.CustomerRepository;
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

    private long company_id;
    private CompanyRepository companyRepository;
    private CouponRepository couponRepository;
    private ApplicationContext context;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, CouponRepository couponRepository, ApplicationContext context) {
        this.companyRepository = companyRepository;
        this.couponRepository = couponRepository;
        this.context = context;
    }

    public void setCompany_id(long company_id) {
        this.company_id = company_id;
    }

    /*
        @Override
        public void updateCompany(String name) {
            Optional<Company> optional = companyRepository.findById(company_id);
            Company company = optional.get();
            company.setName(name);
            companyRepository.save(company);
        }*/
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
    public Coupon createCoupon(Coupon coupon) {
        Optional<Company> optionalCompany = companyRepository.findById(company_id);
        if (!optionalCompany.isPresent()) {
        }
        Company company = optionalCompany.get();
        if (company.getCoupons() != null) {
            for (Coupon existCoupon : company.getCoupons()) {
                if (coupon.similarCoupon(existCoupon)) {
                    existCoupon.setAmount(existCoupon.getAmount() + coupon.getAmount());
                    existCoupon.setCompanyId(company_id);
                    return couponRepository.save(existCoupon);
                }
            }
        }
        coupon.setCompanyId(company_id);
        company.add(coupon);
        return couponRepository.save(coupon);
    }

    @Override
    public void deleteCouponById(long coupon_id) throws NoSuchCouponException {
        Optional<Coupon> optionalCoupon = couponRepository.findById(coupon_id);
        if (optionalCoupon.isPresent()) {
            Coupon coupon = optionalCoupon.get();

            CustomerRepository customerRepository = context.getBean(CustomerRepository.class);
            for (Customer customer : customerRepository.findAll()) {
                customer.getCoupons().remove(coupon);
            }

            Company company = coupon.getCompany();
            List<Coupon> coupons = company.getCoupons();
            coupons.remove(coupon);
            company.setCoupons(coupons);
            couponRepository.deleteById(coupon_id);
            System.out.println("Coupon was deleted successfully!");
        } else throw new NoSuchCouponException(String.format("Coupon does not exist with id%d", coupon_id));
    }

    @Override
    public List<Coupon> getAllCompanyCoupons() {
        return couponRepository.findAllByCompanyId(company_id);
    }

    @Override
    public List<Coupon> getAllCompanyCouponsR(long id) {
        return couponRepository.findAllByCompanyId(id);
    }

    @Override
    public Coupon updateCoupon(Coupon coupon) throws InvalidUpdateCouponException {
        Optional<Coupon> optionalCoupon = couponRepository.findById(coupon.getId());
        Optional<Company> optionalCompany = companyRepository.findById(company_id);
        if (optionalCoupon.isPresent()) {
            Coupon couponUp = optionalCoupon.get();
            if (couponUp.getTitle().equals(coupon.getTitle()) && coupon.getAmount() >= 0) {
                coupon.setCompany(optionalCompany.get());
                return couponRepository.save(coupon);
            }
        }
        throw new InvalidUpdateCouponException("Unable to update this coupon!");
    }

    /*generalInfo*/
    @Override
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAllByCompanyId(company_id);
    }


    @Override
    public Company getCompany() {
        Optional<Company> optionalCompany = companyRepository.findById(company_id);
        return optionalCompany.get();
    }
//        Company company = new Company();
//        company.setId(company_id);
//        return  company;
//        }

    @Override
    public Company getCompanyById(long company_id) throws NoSuchCompanyException {
        Optional<Company> optionalCompany = companyRepository.findById(company_id);
        if (optionalCompany.isPresent()) {
            return optionalCompany.get();
        }
        throw new NoSuchCompanyException(String.format("There is no company with this id:%d", company_id));
    }

    @Override
    public Company getCompanyByName(String company_name) throws NoSuchCompanyException {
        Optional<Company> optionalCompany = companyRepository.findById(this.company_id);
        if (optionalCompany.isPresent()) {
            return optionalCompany.get();
        }
        throw new NoSuchCompanyException(String.format("There is no company with this id:%d", this.company_id));
    }


    @Override
    public List<Coupon> getAllCouponsByCategory(String category) {
        return couponRepository.findAllByCategory(category);
    }

    @Override
    public List<Coupon> getAllCouponsByEndDate(LocalDate endDate) {
        List<Coupon> coupons = new ArrayList<>();
        for (Coupon coupon : couponRepository.findAllByCompanyId(company_id)) {
            if (coupon.getEndDate().equals(endDate)) {
                coupons.add(coupon);
            }
        }
        return coupons;
    }

    @Override
    public List<Coupon> getAllCouponsByStartDate(LocalDate startDate) {
        List<Coupon> coupons = new ArrayList<>();
        for (Coupon coupon : couponRepository.findAllByCompanyId(company_id)) {
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

