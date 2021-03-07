package com.jb.MyProject.util;

import com.jb.MyProject.entity.CustomerErrorResponse;
import com.jb.MyProject.exceptions.InvalidLoginException;
import com.jb.MyProject.rest.controller.CustomerController;
import org.hibernate.type.AssociationType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(assignableTypes = {AssociationType.class, CustomerController.class})
public class CustomerControllerAdvice {

    @ExceptionHandler(InvalidLoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public CustomerErrorResponse handleUnauthorized(InvalidLoginException ex) {
        return CustomerErrorResponse.of(HttpStatus.UNAUTHORIZED, "Unauthorized");
    }
}
