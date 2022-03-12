package com.eventproject.dto;

import com.eventproject.utility.passwordValid.ValidPassword;
import lombok.Data;

@Data
public class UpdatePasswordDto {

    private String email;
    private String oldPassword;
    @ValidPassword
    private String password;
    @ValidPassword
    private String confirmPassword;
}
