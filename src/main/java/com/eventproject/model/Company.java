package com.eventproject.model;

import com.eventproject.enumType.RequestStatus;
import com.eventproject.model.actorModel.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idCompany;

    @Email
    @NotNull
    @Column(nullable = false)
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

    private RequestStatus sponsorValidation = RequestStatus.WAITING;

    @OneToOne(fetch = FetchType.EAGER)
    private User user;

    @CreationTimestamp
    private LocalDateTime createdDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;


}
