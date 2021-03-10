package com.jb.MyProject.service;

import com.jb.MyProject.entity.Company;
import com.jb.MyProject.entity.Coupon;
import com.jb.MyProject.exceptions.NoSuchCompanyException;
import com.jb.MyProject.repository.CompanyRepository;
import com.jb.MyProject.repository.CouponRepository;
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
        throw new NoSuchCompanyException(String.format("There is no company with this name:%s", companyName));
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public List<Coupon> getAllCompanyCoupons() {
        return couponRepository.findAllByCompanyId(companyId);
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
        throw new NoSuchCompanyException(String.format("There is no company with this id:%d", id));
    }

    @Override
    public Company removeCompanyById(long companyId) throws NoSuchCompanyException {
        Optional<Company> optionalCompany = companyRepository.findById(companyId);
        if (optionalCompany.isPresent()) {
            Company company = optionalCompany.get();
            companyRepository.delete(company);
            return company;
        }
        throw new NoSuchCompanyException(String.format("There is no company with this id:%d", companyId));
    }

    @Override
    @Transactional
    public Coupon createCoupon(Coupon coupon) throws NoSuchCompanyException {
        Optional<Company> optionalCompany = companyRepository.findById(companyId);
        if (optionalCompany.isPresent()) {
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
        throw new NoSuchCompanyException(String.format("There is no company with such id:%d " + optionalCompany.get().getId()));
      }

    @Override
    public List<Coupon> getAllCouponsByEndDateAndByCompanyId(LocalDate endDate) {
        List<Coupon> coupons = new ArrayList<>();
        for (Coupon coupon : couponRepository.findAllByCompanyId(companyId)) {
            if (coupon.getEndDate().equals(endDate)) {
                coupons.add(coupon);
            }
        }
        return coupons;
    }

    @Override
    public List<Coupon> getAllCouponsByStartDateAndByCompanyId(LocalDate startDate) {
        List<Coupon> coupons = new ArrayList<>();
        for (Coupon coupon : couponRepository.findAllByCompanyId(companyId)) {
            if (coupon.getStartDate().equals(startDate)) {
                coupons.add(coupon);
            }
        }
        return coupons;
    }
}

