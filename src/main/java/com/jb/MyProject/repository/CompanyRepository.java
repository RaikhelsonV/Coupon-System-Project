package com.jb.MyProject.repository;

import com.jb.MyProject.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company findCompanyByName(String name);

    Company findCompanyById(long id);

}
