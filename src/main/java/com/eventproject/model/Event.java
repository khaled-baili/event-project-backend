package com.eventproject.model;


import com.eventproject.enumType.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idEvent;

    @Column(nullable = false)
    @NotBlank(message = "You should provide a title for event")
    private String title;

    @Column(length = 10000)
    @NotBlank(message = "You should provide a description for your event")
    private String description;

    @Column(nullable = false)
    @NotNull(message = "Provide a date for your event")
    private Date eventDate;

    @Column(nullable = false)
    @NotNull(message = "Provide the event duration")
    private LocalTime duration;

    @OneToOne(fetch = FetchType.EAGER)
    private ImageModel imageModel;

    @CreationTimestamp
    private LocalDateTime createdDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    private boolean isPaid = false;

    private RequestStatus eventValidated = RequestStatus.WAITING;

}
