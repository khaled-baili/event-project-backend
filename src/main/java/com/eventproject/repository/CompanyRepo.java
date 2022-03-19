package com.eventproject.repository;

import com.eventproject.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepo extends JpaRepository<Company, Long> {
    Company findCompanyByIdCompany(long idCompany);
    Company findCompanyByCompanyName(String name);
}
