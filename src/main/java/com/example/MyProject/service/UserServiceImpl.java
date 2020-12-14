package com.example.MyProject.service;

import com.example.MyProject.entity.Company;
import com.example.MyProject.entity.Customer;
import com.example.MyProject.entity.User;
import com.example.MyProject.exceptions.NoSuchUserException;
import com.example.MyProject.exceptions.UnknownRoleException;
import com.example.MyProject.repository.CompanyRepository;
import com.example.MyProject.repository.CustomerRepository;
import com.example.MyProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepo;
    private ApplicationContext context;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, ApplicationContext context) {
        this.userRepo = userRepository;
        this.context = context;
    }
    @Override
    public User getUserByEmailAndPassword(String email, String password) throws NoSuchUserException {
        Optional<User> optUser = userRepo.findByEmailAndPassword(email, password);
        if (!optUser.isPresent()) {
            throw new NoSuchUserException(String.format("User with such email %s is not exist!", email));
        }
        return optUser.get();
    }
    @Override
    public User createUser(String email, String password, int role) throws NoSuchUserException, UnknownRoleException {
        Optional<User> optUser = userRepo.findByEmailAndPassword(email, password);
        if (optUser.isPresent()) {
            throw new NoSuchUserException(String.format("User with such email %s is already exist.", email));
        }
        User user = new User(email, password, role);

        if(user.getClient() instanceof Customer){
            CustomerRepository customerRepository = this.context.getBean(CustomerRepository.class);
            customerRepository.save((Customer)user.getClient());

        } else{
            CompanyRepository companyRepository = this.context.getBean(CompanyRepository.class);
            companyRepository.save((Company)user.getClient());
        }
        userRepo.save(user);
        return user;
    }

    @Override
    public User updateUser(String email, String password, String newEmail, String newPassword) throws NoSuchUserException {
        return null;
    }


    @Override
    public void updateUsersEmail(String email, String password, String newEmail) throws NoSuchUserException {
        User user = getUserByEmailAndPassword(email, password);
        user.setEmail(newEmail);
        userRepo.save(user);
    }
    @Override
    public void updateUsersPassword(String email, String password, String newPassword) throws NoSuchUserException {
        User user = getUserByEmailAndPassword(email, password);
        user.setPassword(newPassword);
        userRepo.save(user);
    }



}
