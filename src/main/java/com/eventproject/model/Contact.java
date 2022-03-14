package com.eventproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idContact;

    @Email(message = "provide correct email format")
    @NotBlank(message = "You should write the correct email")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "Provide a subject")
    @Column(nullable = false)
    private String subject;

    @NotBlank(message = "You should write the message")
    @Column(nullable = false, length = 1000)
    private String message;

}
