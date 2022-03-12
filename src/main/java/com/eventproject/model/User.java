package com.eventproject.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "USER_TYPE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(nullable = false, unique = true)
    @Email
    @NotBlank
    private String email;

    @Column(nullable = false)
    private String password;


    @Column(nullable = false)
    @NotBlank
    private String firstname;

    @Column(nullable = false)
    @NotEmpty
    private String lastname;

    @Column(nullable = false)
    private String birthdate;

    @Column(nullable = false)
    @NotEmpty
    private String telnumber;

    @Column(length = 64)
    private String verificationCode;
    private boolean enabled;
    private int accountStatus;
    @Column(nullable = true,length = 36)
    private String resetToken;
    @OneToOne(fetch = FetchType.EAGER)
    private Role role;
}
