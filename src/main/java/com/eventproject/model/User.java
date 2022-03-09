package com.eventproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "USER_TYPE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private Date birthdate;
    private String telnumber;
    @Column(length = 64)
    private String verificationCode;
    private int accountverfication;
    private int accountStatus;
    @OneToOne(fetch = FetchType.EAGER)
    private Role role;
}
