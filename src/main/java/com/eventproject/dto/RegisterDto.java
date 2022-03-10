package com.eventproject.dto;

import com.eventproject.model.Role;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
public class RegisterDto {
    @Email(message = "Please provide a valid email")
    @NotEmpty(message = "provide an email")
    private String email;

    @ValidPassword
    @NonNull
    private String password;

    @NotEmpty(message = "Please enter your first name")
    private String firstname;

    @NotEmpty(message = "Please enter your last name")
    private String lastname;

    @NotEmpty(message = "Please enter your birthdate")
    private Date birthdate;

    @NotEmpty(message = "Please enter your tel number")
    private String telnumber;

    private String occupation;
    private String entrepriseRole;
    private String employeeType;
    private String role;
}
