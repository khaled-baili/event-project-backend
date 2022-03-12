package com.eventproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
@Entity
@DiscriminatorValue("S")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Sponsor extends User {
    private String employeeType;
    private int sponsorValidation;

}
