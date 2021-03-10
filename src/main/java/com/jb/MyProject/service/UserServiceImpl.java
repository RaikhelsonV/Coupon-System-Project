package com.jb.MyProject.service;

import com.jb.MyProject.entity.Company;
import com.jb.MyProject.entity.Customer;
import com.jb.MyProject.entity.User;
import com.jb.MyProject.exceptions.NoSuchUserException;
import com.jb.MyProject.exceptions.UnknownRoleException;
import com.jb.MyProject.repository.CompanyRepository;
import com.jb.MyProject.repository.CustomerRepository;
import com.jb.MyProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private CustomerRepository customerRepository;
    private CompanyRepository companyRepository;
    private ApplicationContext context;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, CustomerRepository customerRepository, CompanyRepository companyRepository, ApplicationContext context) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.companyRepository = companyRepository;
        this.context = context;
    }

    @Override
    public User getUserByEmailAndPassword(String email, String password) throws NoSuchUserException {
        Optional<User> optionalUser = userRepository.findByEmailAndPassword(email, password);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new NoSuchUserException(String.format("User with such email %s is not exist!", email));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(String email, String password, int role) throws NoSuchUserException, UnknownRoleException {
        Optional<User> optionalUser = userRepository.findByEmailAndPassword(email, password);
        if (optionalUser.isPresent()) {
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
    public void updateUser(String email, String password, String newEmail, String newPassword) throws NoSuchUserException {
        User user = getUserByEmailAndPassword(email, password);
        user.setEmail(newEmail);
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    @Override
    public void updateUserPassword(String email, String password, String newPassword) throws NoSuchUserException {
//        User user = getUserByEmailAndPassword(email, password);
//        user.setPassword(newPassword);
//        userRepository.save(user);
    }

    @Override
    public void removeUserCompany(long clientId) {
        List<User> users = userRepository.findAll();
        for (int i = 1; i < users.size(); i++) {
            User user = users.get(i);
            long id = user.getClient().getId();
            if (id == clientId) {
                long userId = user.getId();
                Optional<User> optionalUser = userRepository.findById(userId);
                User relevantUser = optionalUser.get();
                Optional<Company> optionalCompany = companyRepository.findById(clientId);
                Company company = optionalCompany.get();
                companyRepository.delete(company);
                System.out.println("company was deleted successfully!");
                userRepository.delete(relevantUser);
                System.out.println("user was deleted successfully!");
                break;
            }
        }
    }

    @Override
    public void removeUserCustomer(long clientId) {
        List<User> users = userRepository.findAll();
        for (int i = 1; i < users.size(); i++) {
            User user = users.get(i);
            long id = user.getClient().getId();
            if (id == clientId) {
                long userId = user.getId();
                Optional<User> optionalUser = userRepository.findById(userId);
                User relevantUser = optionalUser.get();
                Optional<Customer> optionalCustomer = customerRepository.findById(clientId);
                Customer customer = optionalCustomer.get();
                customerRepository.delete(customer);
                System.out.println("customer was deleted successfully!");
                userRepository.delete(relevantUser);
                System.out.println("user was deleted successfully!");
                break;
            }
        }
    }

}
