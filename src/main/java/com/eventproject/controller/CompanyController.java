package com.eventproject.controller;

import com.eventproject.dto.CompanyDto;
import com.eventproject.dto.UpdateStatusCompanyDto;
import com.eventproject.model.Company;
import com.eventproject.model.actorModel.User;
import com.eventproject.service.CompanyService;
import com.eventproject.service.UserService;
import com.eventproject.utility.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/company-sponsor")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    @GetMapping("/get-all-companies")
    public ResponseEntity<List<Company>> getCompanies()
    {
        return ResponseEntity.ok().body(companyService.getAllCompany());
    }

    @PostMapping("/save-company")
    ResponseEntity<?> saveCompany(@Valid @RequestBody Company company, @RequestParam long idUser) {
        User user = userService.findUserById(idUser);
        if (user == null ) {
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.BAD_REQUEST,
                            "Not user for saving the company for the sponsor"),
                    HttpStatus.BAD_REQUEST);
        }
        if (company == null) {
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.BAD_REQUEST,
                            "You should provide information for saving company"),
                    HttpStatus.BAD_REQUEST);
        } else if (companyService.companyExist(company.getCompanyName())) {
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.BAD_REQUEST,
                            "Company already exist"),
                    HttpStatus.BAD_REQUEST);
        }
        else {
            company.setUser(user);
            companyService.save(company);
            return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED,"company Saved"),
                    HttpStatus.CREATED);
        }
    }

    @PutMapping("/update-company-status")
    ResponseEntity<?> updateCompany(@Valid @RequestBody UpdateStatusCompanyDto updateStatusCompanyDto,
                                    @RequestParam long idCompany) {
        Company company = companyService.getCompanyById(idCompany);
        if (company == null) {
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.BAD_REQUEST,"No company with the id provided"),
                    HttpStatus.BAD_REQUEST);
        } else {
            company.setSponsorValidation(updateStatusCompanyDto.getSponsorValidation());
            companyService.save(company);
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.OK,"Status updated successfully"),
                    HttpStatus.OK);
        }

    }

    @PutMapping("/update-company-details")
    ResponseEntity<?> updateCompanyDetails(@Valid @RequestBody CompanyDto companyDto,
                                           @RequestParam long idCompany) {
        Company oldCompany = companyService.getCompanyById(idCompany);
        if (oldCompany == null) {
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.BAD_REQUEST,
                            "No company with the id provided operation denied"),
                    HttpStatus.BAD_REQUEST);
        } else if (oldCompany.getCompanyName().equals(companyDto.getCompanyName())) {
            oldCompany.setCompanyCity(companyDto.getCompanyCity());
            oldCompany.setCompanyCountry(companyDto.getCompanyCountry());
            oldCompany.setCompanyFax(companyDto.getCompanyFax());
            oldCompany.setCompanyRegion(companyDto.getCompanyRegion());
            oldCompany.setCompanyStreet(companyDto.getCompanyStreet());
            oldCompany.setCompanyTel(companyDto.getCompanyTel());
            oldCompany.setCompanyPostCode(companyDto.getCompanyPostCode());
            oldCompany.setDescription(companyDto.getDescription());
            companyService.save(oldCompany);
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.OK,
                            "Company details updated successfully"),
                    HttpStatus.OK);
        }  else if (!oldCompany.getCompanyName().equals(companyDto.getCompanyName()) &&
                !companyService.companyExist(companyDto.getCompanyName())) {
            oldCompany.setCompanyCity(companyDto.getCompanyCity());
            oldCompany.setCompanyCountry(companyDto.getCompanyCountry());
            oldCompany.setCompanyFax(companyDto.getCompanyFax());
            oldCompany.setCompanyRegion(companyDto.getCompanyRegion());
            oldCompany.setCompanyStreet(companyDto.getCompanyStreet());
            oldCompany.setCompanyTel(companyDto.getCompanyTel());
            oldCompany.setCompanyPostCode(companyDto.getCompanyPostCode());
            oldCompany.setDescription(companyDto.getDescription());
            oldCompany.setCompanyName(companyDto.getCompanyName());
            companyService.save(oldCompany);
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.OK,
                            "Company details updated successfully"),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.BAD_REQUEST,
                            "Something go wrong"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete-company")
    ResponseEntity<?> deletecompany(@RequestParam Long idCompany) {
        Company company = companyService.getCompanyById(idCompany);
        if (company == null) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST,
                    "Provide valid id for deleting company"), HttpStatus.BAD_REQUEST);
        }
        else {
            companyService.deleteCompany(company);
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"company deleted successfully"),
                    HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete-all-companies")
    ResponseEntity<?> deleteAllcompanys() {
        companyService.deleteAllCompanies();
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"companys deleted successfully"),
                HttpStatus.OK);
    }
}
