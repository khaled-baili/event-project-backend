package com.eventproject.dto;

import com.eventproject.model.actorModel.User;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CompanyDto {

    @Email
    @NotNull
    @Column(nullable = false, unique = true)
    private String companyEmail;

    @NotBlank(message = "provide your company name")
    @Size(min = 2, message = "the company name should have more then two character")
    @Column(nullable = false, unique = true)
    private String companyName;


    @NotBlank(message = "Provide company telephone number")
    private String companyTel;
    private String companyFax;

    private String description;

    @NotBlank(message = "provide a country")
    @Column(nullable = false)
    private String companyCountry;

    @Column(length = 30,nullable = false)
    @NotBlank(message = "provide a company region")
    private String companyRegion;

    @Column(length = 30,nullable = false)
    @NotBlank(message = "provide a company city")
    private String companyCity;

    @Column(length = 100,nullable = false)
    @NotBlank(message = "provide a street address")
    private String companyStreet;

    @Column(length = 10,nullable = false)
    @NotBlank(message = "provide a company poste code")
    private String companyPostCode;

    @OneToOne(fetch = FetchType.EAGER)
    private User user;
}
