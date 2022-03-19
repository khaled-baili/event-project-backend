package com.eventproject.model.actorModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

}
