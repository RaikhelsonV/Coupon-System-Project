package com.jb.MyProject.repository;

import com.jb.MyProject.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByLastName(String lastName);


}
