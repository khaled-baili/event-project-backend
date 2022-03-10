package com.eventproject.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class ResetPasswordDto {
    @NotEmpty
    @Size(min = 8)
    private String paasswordReset;
}
