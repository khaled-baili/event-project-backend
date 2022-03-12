package com.eventproject.dto;

import com.eventproject.utility.passwordValid.ValidPassword;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class ResetPasswordDto {
    @ValidPassword
    private String paasswordReset;
}
