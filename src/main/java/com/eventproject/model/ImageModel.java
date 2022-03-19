package com.eventproject.model;

import com.eventproject.enumType.ImageFor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String type;
    private ImageFor imageFor;

    @Column(length = 1000)
    private byte[] picByte;


}
