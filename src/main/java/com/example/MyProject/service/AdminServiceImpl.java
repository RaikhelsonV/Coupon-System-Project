package com.example.MyProject.service;

import com.example.MyProject.entity.Company;
import com.example.MyProject.entity.Coupon;
import com.example.MyProject.entity.Customer;
import com.example.MyProject.entity.User;
import com.example.MyProject.exceptions.*;
import com.example.MyProject.repository.CompanyRepository;
import com.example.MyProject.repository.CouponRepository;
import com.example.MyProject.repository.CustomerRepository;
import com.example.MyProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AdminServiceImpl implements AdminService {
    private UserRepository userRepository;
    private CustomerRepository customerRepository;
    private CompanyRepository companyRepository;
    private CouponRepository couponRepository;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository, CustomerRepository customerRepository, CompanyRepository companyRepository, CouponRepository couponRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.companyRepository = companyRepository;
        this.couponRepository = couponRepository;
    }



    @Override
    public Company createCompany(String email, String password, String name) throws NoSuchUserException, UnknownRoleException {
        Optional<User> optUser = userRepository.findByEmailAndPassword(email, password);
        if (optUser.isPresent()) {
            throw new NoSuchUserException(String.format("User with such email %s is already exist.", email));
        }
        User user = new User(email, password, 2);
        Company company = new Company();
        companyRepository.save((Company)user.getClient());
        companyRepository.save(company);
        return company;
    }

    @Override
    public Customer addCustomer(Customer customer) {
        return null;
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
        throw new NoSuchCompanyException(String.format("No company with such id#%d", id));
    }

    @Override
    public Company removeCompany(long company_id) throws NoSuchCompanyException {
        Optional<Company> optionalCompany = companyRepository.findById(company_id);
        if (optionalCompany.isPresent()) {
            Company company = optionalCompany.get();
            companyRepository.delete(company);
            return company;
        }
        throw new NoSuchCompanyException(String.format("There is no company with this id:%d", company_id));
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
    public Customer removeCustomer(long customer_id) throws NoSuchCustomerException {
        Optional<Customer> optionalCustomer = customerRepository.findById(customer_id);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            customerRepository.delete(customer);
            return customer;
        }
        throw new NoSuchCustomerException(String.format("There is no customer with this id:%d", customer_id));
    }

    @Override
    public Coupon updateCoupon(long id, Coupon coupon) throws NoSuchCouponException {
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
    public Coupon removeCoupon(long coupon_id) throws NoSuchCouponException {
        Optional<Coupon> optionalCoupon = couponRepository.findById(coupon_id);
        if (optionalCoupon.isPresent()) {
            Coupon coupon = optionalCoupon.get();
            couponRepository.delete(coupon);
            return coupon;
        }
        throw new NoSuchCouponException(String.format("There is no coupon with this id:%d", coupon_id));
    }


    @Override
    public Customer getCustomer(long customer_id) throws NoSuchCustomerException {
        Optional<Customer> optionalCustomer = customerRepository.findById(customer_id);
        if (optionalCustomer.isPresent()) {
            return optionalCustomer.get();
        }
        throw new NoSuchCustomerException(String.format("There is noc customer with this id:%d", customer_id));
    }

    @Override
    public List<Coupon> getAllCoup() {
        return couponRepository.findAll();
    }
    @Override
    public List<Company> getAllComp() {
        return companyRepository.findAll();
    }
    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    /*general info*/
    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    public Company getCompany(long company_id) throws NoSuchCompanyException {
        Optional<Company> optionalCompany = companyRepository.findById(company_id);
        if (optionalCompany.isPresent()) {
            return optionalCompany.get();
        }
        throw new NoSuchCompanyException(String.format("There is no company with this id:%d", company_id));
    }

    @Override
    public Coupon getCoupon(long coupon_id) throws NoSuchCouponException {
        Optional<Coupon> optionalCoupon = couponRepository.findById(coupon_id);
        if (optionalCoupon.isPresent()) {
            return optionalCoupon.get();
        }
        throw new NoSuchCouponException(String.format("There is no coupon with this id:%d", coupon_id));
    }
}




