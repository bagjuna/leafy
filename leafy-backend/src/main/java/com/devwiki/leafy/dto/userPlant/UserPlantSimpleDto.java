package com.devwiki.leafy.dto.userPlant;

import com.devwiki.leafy.dto.plant.PlantSimpleDto;
import com.devwiki.leafy.dto.user.UserResponseDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserPlantSimpleDto {
    private Long userPlantId;
    // 안나오게 함
    // @JsonIgnore
    // private UserResponseDto user;
    private PlantSimpleDto plant;
    private String plantNickname;
    private Boolean waterRequired;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
