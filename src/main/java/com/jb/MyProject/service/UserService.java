package com.jb.MyProject.service;

import com.jb.MyProject.entity.User;
import com.jb.MyProject.exceptions.NoSuchCustomerException;
import com.jb.MyProject.exceptions.NoSuchUserException;
import com.jb.MyProject.exceptions.UnknownRoleException;

import java.util.List;

public interface UserService {

    User getUserByEmailAndPassword(String email, String password) throws NoSuchUserException;

    List<User> getAllUsers();

    User createUser(String email, String password, int role) throws NoSuchUserException, UnknownRoleException;

    void updateUser(String email, String password, String newEmail, String newPassword) throws NoSuchUserException;

    void updateUserPassword(String email, String password, String newPassword) throws NoSuchUserException;

    void removeUserCustomer(long customerId) throws NoSuchCustomerException;

    void removeUserCompany(long companyId);
//    User createUserCus(String email, String password, int role, String customerFirstName, String customerLastName) throws NoSuchUserException, UnknownRoleException;
}
