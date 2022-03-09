package com.eventproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
@Entity
@DiscriminatorValue("PO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductOwner extends User{
    private String entrepriseRole;
}
