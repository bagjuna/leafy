package com.devwiki.leafy.dto.plant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class PlantDetailDto {


	private Long plantId;
	private String plantName;
	private String plantType;
	private String plantDesc;
	private String imageUrl;
	private float temperatureLow;
	private float temperatureHigh;
	private float humidityLow;
	private float humidityHigh;
	private int wateringInterval;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;


}
