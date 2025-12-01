<template>

  <div class="container">
    <div class="plant-list">
      <div
          v-for="plant in plants"
          :key="plant.plantId"
          class="plant-item"
          @click="openPlantDetailModal(plant.plantId)"
      >
        <img :src="plant.imageUrl" :alt="plant.plantName" class="plant-image">
        <div class="plant-details">
          <h2 class="plant-name">{{ plant.plantName }}</h2>
          <p class="plant-type">{{ plant.plantType }}</p>
          <p class="plant-desc">{{ plant.plantDesc }}</p>
        </div>
      </div>
    </div>

    <button class="add-plant-button" @click="openPlantAddModal">식물 추가</button>

    <PlantAddModal
        v-model:isOpen="showPlantAddModal"
        @added-plant="fetchPlants"
    />

    <PlantDetailModal
        :deleteButton="true"
        v-model:isOpen="showPlantDetailModal"
        :plantId="selectedPlantId"
        @removed-plant="fetchPlants"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import api from '@/api/api';
import PlantAddModal from '@/components/modals/PlantAddModal.vue';
import PlantDetailModal from '@/components/modals/PlantDetailModal.vue';

// 1. 상태 정의 (Reactive State)
const plants = ref([]);
const showPlantAddModal = ref(false);
const showPlantDetailModal = ref(false);
const selectedPlantId = ref(null);

// 2. 데이터 조회 함수
const fetchPlants = async () => {
  try {
    const response = await api.get('/api/plants');
    plants.value = response.data;
  } catch (error) {
    console.error("식물 목록 로딩 실패:", error);
  }
};

// 3. 모달 제어 함수
const openPlantAddModal = () => {
  showPlantAddModal.value = true;
};

const openPlantDetailModal = (plantId) => {
  selectedPlantId.value = plantId;
  showPlantDetailModal.value = true;
};

// 4. 라이프사이클 훅 (마운트 시 실행)
onMounted(() => {
  fetchPlants();
});
</script>

<style scoped>
.container {
  max-width: 1200px;
  margin: 40px auto;
  padding: 0 20px; /* 모바일 대응을 위해 좌우 패딩 추가 */
}

.plant-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  grid-gap: 2rem;
}

.plant-item {
  border: 1px solid #eee;
  border-radius: 12px; /* 둥글기 조정 */
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s; /* 호버 효과 추가 */
  background-color: white;
}

.plant-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0,0,0,0.1);
}

.plant-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.plant-details {
  padding: 1.2rem;
}

.plant-name {
  font-size: 1.4rem;
  font-weight: bold;
  margin-bottom: 0.5rem;
  color: #333;
}

.plant-type {
  font-size: 1rem;
  color: #556B2F; /* 브랜드 컬러 적용 */
  font-weight: 600;
  margin-bottom: 0.5rem;
}

.plant-desc {
  font-size: 0.95rem;
  color: #666;
  line-height: 1.4;

  /* 긴 설명 줄임 처리 */
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.add-plant-button {
  display: block;
  margin: 3rem auto; /* 위아래 여백 */
  padding: 1rem 2rem;
  font-size: 1.1rem;
  font-weight: bold;
  color: white;
  background-color: #556B2F; /* 일관된 녹색 */
  border: none;
  border-radius: 8px;
  cursor: pointer;
  width: 100%;
  max-width: 400px; /* 너무 넓어지지 않게 제한 */
  box-shadow: 0 4px 6px rgba(0,0,0,0.1);
  transition: background-color 0.2s;
}

.add-plant-button:hover {
  background-color: #445725;
}
</style>
