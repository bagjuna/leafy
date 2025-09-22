package com.devwiki.leafy.dto.plant;

import com.devwiki.leafy.model.plant.Plant;
import org.springframework.stereotype.Component;

@Component
public class PlantMapper {

    public static PlantDetailDto toDetailDto(Plant plant) {
        return PlantDetailDto.builder()
            .plantId(plant.getPlantId())
            .plantName(plant.getPlantName())
            .plantType(plant.getPlantType())
            .plantDesc(plant.getPlantDesc())
            .imageUrl(plant.getImageUrl())
            .temperatureLow(plant.getTemperatureLow())
            .temperatureHigh(plant.getTemperatureHigh())
            .humidityLow(plant.getHumidityLow())
            .humidityHigh(plant.getHumidityHigh())
            .wateringInterval(plant.getWateringInterval())
            .createdAt(plant.getCreatedAt())
            .updatedAt(plant.getUpdatedAt())
            .build();
    }


    public static PlantSimpleDto toSimpleDto(Plant plant) {
        PlantSimpleDto plantSimpleDto = new PlantSimpleDto();
        plantSimpleDto.setPlantId(plant.getPlantId());
        plantSimpleDto.setPlantName(plant.getPlantName());
        plantSimpleDto.setPlantType(plant.getPlantType());
        return plantSimpleDto;
    }
}
