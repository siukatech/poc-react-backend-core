package com.siukatech.poc.react.backend.parent.business.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.TimeZone;


@Data
//@EqualsAndHashCode(callSuper = true)
public class UserViewDto extends UserDto {
    private LocalDateTime appDatetime;
    private LocalDateTime dbDatetime;
    private TimeZone timeZone;
}

