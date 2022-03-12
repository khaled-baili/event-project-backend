package com.eventproject.dto;



import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateCred {

    @NotBlank(message = "Please enter your first name")
    private String firstname;

    @NotBlank(message = "Please enter your last name")
    private String lastname;

    @NotNull(message = "Please provide a date")
    private String birthdate;

    @NotBlank(message = "Please enter your tel number")
    private String telnumber;

    private String occupation;
    private String entrepriseRole;
    private String employeeType;
}
