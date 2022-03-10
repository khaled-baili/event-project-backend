package com.eventproject.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class ForgotPasswordDto {

    @Email
    @NotEmpty
    private String Email;
}
