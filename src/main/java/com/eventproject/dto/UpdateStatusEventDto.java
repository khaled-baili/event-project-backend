package com.eventproject.dto;

import com.eventproject.enumType.RequestStatus;
import lombok.Data;

@Data
public class UpdateStatusEventDto {
    private RequestStatus eventValidated ;
}
