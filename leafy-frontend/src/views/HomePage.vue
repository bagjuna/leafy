<template>
  <v-container class="home-container">
    <h3 class="section-title">나의 식물</h3>

    <div v-if="isLoading" class="text-center my-4">로딩 중...</div>

    <div v-else class="plant-list">
      <div
          v-for="userPlant in userPlants"
          :key="userPlant.userPlantId"
          class="plant-item"
          @click="openUserPlantDetailModal(userPlant.userPlantId)"
      >
        <v-icon v-if="userPlant.waterRequired" color="red darken-4" large class="status-icon">
          mdi-water-alert-outline
        </v-icon>
        <v-icon v-else color="blue darken-4" large class="status-icon">
          mdi-water-check-outline
        </v-icon>

        <div class="plant-details">
          <h5 class="plant-name">
            {{ userPlant.plantNickname }}
            <span class="plant-species" v-if="userPlant.plant">
              ({{ userPlant.plant.plantName }})
            </span>
          </h5>
          <p class="plant-type">
            {{ userPlant.plant ? userPlant.plant.plantType : '알 수 없는 식물' }}
          </p>
        </div>
      </div>
    </div>

    <h3 class="section-title mt-5">최근 일기</h3>

    <div v-if="isLoading" class="text-center my-4">로딩 중...</div>

    <div v-else class="log-container">
      <v-card
          v-for="log in logs"
          :key="log.plantLogId"
          class="log-card"
          variant="outlined"
      >
        <div class="log-header">
          <v-icon v-if="log.watered" color="indigo darken-4" large>mdi-water-check</v-icon>
        </div>

        <div class="log-body">
          <v-card-text class="log-title">
            <div
                v-if="log.userPlant"
                class="clickable-title"
                @click="openUserPlantDetailModal(log.userPlant.userPlantId)"
            >
              <h5 class="plant-name-text">
                {{ log.userPlant.plantNickname }}
                <span class="small-text" v-if="log.userPlant.plant">
                  ({{ log.userPlant.plant.plantName }})
                </span>
              </h5>
            </div>

            <div v-else class="text-grey">
              (삭제된 식물)
            </div>

            <div class="log-note mt-2">{{ log.note }}</div>
          </v-card-text>
        </div>

        <div class="log-footer">
          <v-card-text class="date-text">{{ formatDate(log.createdAt) }}</v-card-text>
        </div>
      </v-card>
    </div>

    <UserPlantDetailModal
        v-model:isOpen="showDetailModal"
        :userPlantId="selectedUserPlantId"
        @removed-plant="onPlantRemoved"
    />
  </v-container>
</template>

<script setup>
import { ref } from 'vue';
import { usePlantData } from '@/composables/usePlantData';
// ✅ UserPlantDetailModal로 교체
import UserPlantDetailModal from '@/components/modals/UserPlantDetailModal.vue';

// Composable 사용 (데이터 로딩 로직 재사용)
const { logs, userPlants, isLoading, fetchRecentLogs, fetchMyPlants } = usePlantData();

// 상태 관리
const showDetailModal = ref(false);
const selectedUserPlantId = ref(null);

// 모달 열기 함수
const openUserPlantDetailModal = (userPlantId) => {
  selectedUserPlantId.value = userPlantId;
  showDetailModal.value = true;
};

// 식물 삭제 후 데이터 갱신
const onPlantRemoved = async () => {
  await Promise.all([
    fetchRecentLogs(),
    fetchMyPlants()
  ]);
};

// 날짜 포맷팅 함수
const formatDate = (dateArray) => {
  if (!dateArray) return '';
  if (Array.isArray(dateArray)) {
    return `${dateArray[0]}. ${dateArray[1]}. ${dateArray[2]}`;
  }
  return dateArray;
};
</script>

<style scoped>
.home-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.section-title {
  margin-top: 15px;
  margin-bottom: 15px;
  font-weight: bold;
  color: #333;
}

/* 나의 식물 스타일 */
.plant-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
}

.plant-item {
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 280px;
  height: 150px;
  border-radius: 12px;
  border: 1px solid #e0e0e0;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  padding: 20px;
  cursor: pointer;
  transition: transform 0.2s;
  background-color: white;
}

.plant-item:hover {
  transform: translateY(-3px);
}

.status-icon {
  position: absolute;
  top: 15px;
  right: 15px;
}

.plant-details {
  text-align: center;
}

.plant-name {
  color: #556B2F;
  font-weight: bold;
  font-size: 1.1rem;
}

.plant-species {
  font-size: 0.9rem;
  font-weight: normal;
}

.plant-type {
  font-size: 0.9rem;
  color: #757575;
  margin-top: 5px;
}

/* 최근 일기 스타일 */
.log-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
}

.log-card {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 280px;
  min-height: 180px; /* 높이 고정 */
  border-radius: 12px;
  border-color: #e0e0e0;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  padding: 15px;
}

.log-header {
  height: 30px;
  display: flex;
  justify-content: flex-end;
}

.log-body {
  flex-grow: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
}

.clickable-title {
  cursor: pointer;
}

.plant-name-text {
  color: #556B2F;
  font-weight: bold;
  font-size: 1.1rem;
}

.small-text {
  font-size: 0.9rem;
  font-weight: normal;
}

.log-note {
  margin-top: 8px;
  font-size: 0.95rem;
  color: #333;
  word-break: break-all;
}

.log-footer {
  text-align: right;
}

.date-text {
  color: #999;
  font-size: 0.8rem;
  padding: 0;
}
</style>