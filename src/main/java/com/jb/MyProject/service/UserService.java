package com.jb.MyProject.service;

import com.jb.MyProject.entity.Company;
import com.jb.MyProject.entity.Coupon;
import com.jb.MyProject.entity.User;
import com.jb.MyProject.exceptions.NoSuchUserException;
import com.jb.MyProject.exceptions.UnknownRoleException;

import java.util.List;

public interface UserService {
    List<Company> getAllCompanies();

    List<Coupon> getAllCoupons();

    User getUserByEmailAndPassword(String email, String password) throws NoSuchUserException;

    void updateUser(String email, String password, String newEmail, String newPassword) throws NoSuchUserException;

    void updateUserPassword(String email, String password, String newPassword) throws NoSuchUserException;

    User createUser(String email, String password, int role) throws NoSuchUserException, UnknownRoleException;


//    User createUserCus(String email, String password, int role, String customerFirstName, String customerLastName) throws NoSuchUserException, UnknownRoleException;
}
