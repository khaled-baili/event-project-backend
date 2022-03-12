package com.eventproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("V")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Visitor extends User {
    private String occupation;
}
