package com.devwiki.leafy.service.plant;


import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.devwiki.leafy.dto.plant.PlantDetailDto;
import com.devwiki.leafy.repository.plant.PlantRepository;
import com.devwiki.leafy.repository.user.UserRepository;

@SpringBootTest
class PlantServiceTest {

	@Autowired
	private PlantService plantService;

	@Autowired
	private PlantRepository plantRepository;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void setUp() {
		plantRepository.deleteAll();
		userRepository.deleteAll();
	}

	@Test
	@DisplayName("식물 등록")
	void test1() {
		// given
		PlantDetailDto dto = PlantDetailDto.builder()
			.plantName("몬스테라")
			.plantType("관엽식물")
			.plantDesc("공기정화식물")
			.imageUrl("https://example.com/monstera.jpg")
			.temperatureLow(20.0f)
			.temperatureHigh(30.0f)
			.humidityLow(40.0f)
			.humidityHigh(60.0f)
			.wateringInterval(7)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
		// when
		plantService.addPlant(dto);
		// then
		assertEquals(1, plantRepository.count());
		assertEquals("몬스테라", plantRepository.findAll().get(0).getPlantName());
	}
}
