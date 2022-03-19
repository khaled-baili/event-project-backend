package com.eventproject.service;

import com.eventproject.model.Company;
import com.eventproject.repository.CompanyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    @Autowired
    CompanyRepo companyRepo;

    public List<Company> getAllCompany() {
        return companyRepo.findAll();
    }

    public Company save(Company company) {
        return companyRepo.save(company);
    }

    public Company getCompanyById(long idCompany) {
        return companyRepo.findCompanyByIdCompany(idCompany);
    }

    public void deleteCompany(Company company) {
        companyRepo.delete(company);
    }

    public void deleteAllCompanies() {
        companyRepo.deleteAll();
    }

    public boolean companyExist(String name) {
        Company company = companyRepo.findCompanyByCompanyName(name);
        if (company == null) return false;
        else return true;
    }
}
