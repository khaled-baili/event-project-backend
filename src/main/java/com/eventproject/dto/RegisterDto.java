package com.eventproject.dto;

import com.eventproject.model.Role;
import lombok.Data;

import java.util.Date;

@Data
public class RegisterDto {
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private Date birthdate;
    private String telnumber;
    private String occupation;
    private String entrepriseRole;
    private String employeeType;
    private String role;
}
