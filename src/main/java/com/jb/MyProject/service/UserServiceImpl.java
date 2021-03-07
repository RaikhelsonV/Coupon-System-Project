package com.jb.MyProject.service;

import com.jb.MyProject.entity.Company;
import com.jb.MyProject.entity.Coupon;
import com.jb.MyProject.entity.Customer;
import com.jb.MyProject.entity.User;
import com.jb.MyProject.exceptions.NoSuchUserException;
import com.jb.MyProject.exceptions.UnknownRoleException;
import com.jb.MyProject.repository.CompanyRepository;
import com.jb.MyProject.repository.CouponRepository;
import com.jb.MyProject.repository.CustomerRepository;
import com.jb.MyProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private long customerId;
    private UserRepository userRepository;
    private CouponRepository couponRepository;
    private CompanyRepository companyRepository;
    private CustomerRepository customerRepository;
    private ApplicationContext context;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, CouponRepository couponRepository, CompanyRepository companyRepository, CustomerRepository customerRepository, ApplicationContext context) {
        this.userRepository = userRepository;
        this.couponRepository = couponRepository;
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
        this.context = context;
    }

    @Override
    public User getUserByEmailAndPassword(String email, String password) throws NoSuchUserException {
        Optional<User> optionalUser = userRepository.findByEmailAndPassword(email, password);
        if (!optionalUser.isPresent()) {
            throw new NoSuchUserException(String.format("User with such email %s is not exist!", email));
        }
        return optionalUser.get();
    }

    @Override
    public void updateUser(String email, String password, String newEmail, String newPassword) throws NoSuchUserException {
        User user = getUserByEmailAndPassword(email, password);
        user.setEmail(newEmail);
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    @Override
    public User createUser(String email, String password, int role) throws NoSuchUserException, UnknownRoleException {
        Optional<User> optUser = userRepository.findByEmailAndPassword(email, password);
        if (optUser.isPresent()) {
            throw new NoSuchUserException(String.format("User with such email %s is already exist.", email));
        }
        User user = new User(email, password, role);

        if (user.getClient() instanceof Customer) {
            CustomerRepository customerRepository = this.context.getBean(CustomerRepository.class);
            customerRepository.save((Customer) user.getClient());

        } else {
            CompanyRepository companyRepository = this.context.getBean(CompanyRepository.class);
            companyRepository.save((Company) user.getClient());
        }
        userRepository.save(user);
        return user;
    }

    @Override
    public void updateUserPassword(String email, String password, String newPassword) throws NoSuchUserException {
//        User user = getUserByEmailAndPassword(email, password);
//        user.setPassword(newPassword);
//        userRepository.save(user);
    }

    ///////////////////////////////
    @Override
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }


}
