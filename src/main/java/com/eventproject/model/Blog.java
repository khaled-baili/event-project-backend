package com.eventproject.model;

import com.eventproject.enumType.ImageFor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long blogId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 10000)
    private String description;

    @OneToOne(fetch = FetchType.EAGER)
    private ImageModel blogPic;

    @CreationTimestamp
    private LocalDateTime createdDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;
}
