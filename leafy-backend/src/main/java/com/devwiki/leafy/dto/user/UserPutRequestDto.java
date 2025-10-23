package com.devwiki.leafy.dto.user;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserPutRequestDto {

    @NotNull
    private String name;
    @NotNull
    private String password;
}
