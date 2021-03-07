package com.example.MyProject.service;

import com.example.MyProject.entity.User;
import com.example.MyProject.exceptions.NoSuchUserException;
import com.example.MyProject.exceptions.UnknownRoleException;

public interface UserService {
    User getUserByEmailAndPassword(String email, String password) throws NoSuchUserException;
    void updateUsersEmail(String email, String password, String newEmail) throws NoSuchUserException;
    void updateUsersPassword(String email, String password, String newPassword) throws NoSuchUserException;
    User createUser(String email, String password, int role) throws NoSuchUserException, UnknownRoleException;
    User updateUser(String email, String password, String newEmail , String newPassword) throws NoSuchUserException;

//    User createUserCus(String email, String password, int role, String customerFirstName, String customerLastName) throws NoSuchUserException, UnknownRoleException;
}
