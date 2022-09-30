package com.eventproject.dto;
import com.eventproject.utility.passwordValid.ValidPassword;

import lombok.Data;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RegisterDto {

    @Email(message = "Please provide a valid email")
    @NotBlank(message = "provide an email")
    private String email;

    @ValidPassword
    private String password;

    @ValidPassword
    @NotBlank(message = "Please provide a valid email")
    private String confirmPassword;

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
    private String role;
}
