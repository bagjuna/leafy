<template>
  <v-dialog v-model="isDialogOpen" max-width="600px">
    <v-card v-if="userPlant" class="pa-4">
      <img
          :src="userPlant.plant.imageUrl"
          alt="Plant Image"
          class="plant-image"
          v-if="userPlant.plant && userPlant.plant.imageUrl"
      />

      <v-card-title class="text-center">
        <div class="plant-nickname">{{ userPlant.plantNickname }}</div>
        <div class="plant-name text-grey">({{ userPlant.plant.plantName }})</div>
      </v-card-title>

      <img :src="userPlant.imageUrl" alt="Plant Image" class="plant-image" v-if="userPlant.imageUrl" />

      <v-card-text>
        <div class="plant-desc">{{ userPlant.plant.plantDesc }}</div>

        <v-divider class="my-3"></v-divider>

        <v-row>
          <v-col cols="6">
            <span class="info-label">키운 날짜:</span>
            {{ calculateDays(userPlant.createdAt) }}일째
          </v-col>
          <v-col cols="6">
            <span class="info-label">물 주기:</span>
            {{ userPlant.plant.wateringInterval }}일에 한 번
          </v-col>
          <v-col cols="12" class="mt-2 text-center" v-if="userPlant.waterRequired">
            <v-chip color="blue" text-color="white">💧 물 줄 시간입니다!</v-chip>
          </v-col>
        </v-row>
      </v-card-text>

      <v-card-actions class="justify-center">
        <v-btn color="grey" variant="text" @click="close">닫기</v-btn>
        <v-btn color="error" variant="text" @click="removeUserPlant">
          내 식물에서 삭제
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, watch, computed } from 'vue';
import api from '@/api/api.js';

const props = defineProps({
  isOpen: Boolean,
  userPlantId: Number, // plantId가 아니라 userPlantId를 받음
});

const emit = defineEmits(['update:isOpen', 'removed-plant']);

// 모달 상태 동기화
const isDialogOpen = computed({
  get: () => props.isOpen,
  set: (val) => emit('update:isOpen', val)
});

const userPlant = ref(null);

// 내 식물 상세 조회 API 호출
const fetchUserPlantDetail = async () => {
  if (!props.userPlantId) return;
  try {
    // API 엔드포인트 변경: /api/user-plants/{id}
    const response = await api.get(`/api/user-plants/${props.userPlantId}`);
    userPlant.value = response.data;
  } catch (error) {
    console.error("내 식물 정보 로딩 실패:", error);
  }
  console.log(userPlant.value);
};

// 내 식물 삭제
const removeUserPlant = async () => {
  if (!confirm(`정말 '${userPlant.value.plantNickname}'을(를) 삭제하시겠습니까?\n관련된 일기도 모두 삭제됩니다.`)) return;

  try {
    await api.delete(`/api/user-plants/${props.userPlantId}`);
    emit('removed-plant'); // 목록 갱신 요청
    close();
  } catch (error) {
    console.error("삭제 실패:", error);
  }
};

const close = () => {
  isDialogOpen.value = false;
};

// D-Day 계산기
const calculateDays = (dateData) => {
  let start;

  // 1. 배열 형태인 경우 [Year, Month, Day, ...]
  if (Array.isArray(dateData)) {
    // 주의: 자바스크립트의 Month는 0부터 시작하므로 (월 - 1) 해줘야 합니다.
    start = new Date(dateData[0], dateData[1] - 1, dateData[2]);
  }
  // 2. 문자열 형태인 경우 ("2025-12-01T...")
  else if (typeof dateData === 'string') {
    start = new Date(dateData);
  }
  // 3. 그 외 (null 등)
  else {
    return 0; // 혹은 '-' 등 기본값 리턴
  }

  const now = new Date();
  // 시간 차이를 밀리초 단위로 계산
  const diff = now - start;

  // 밀리초 -> 일(Day) 변환 (올림 처리하여 1일차부터 시작)
  return Math.floor(diff / (1000 * 60 * 60 * 24)) + 1;
};

// 모달 열릴 때 데이터 조회
watch(() => props.isOpen, (newVal) => {
  if (newVal && props.userPlantId) {
    fetchUserPlantDetail();
  }
});
</script>

<style scoped>
.plant-image {
  width: 100%;
  max-height: 300px;
  object-fit: cover;
  border-radius: 8px;
  margin-bottom: 1rem;
}
.plant-nickname {
  font-size: 1.8rem;
  font-weight: bold;
  color: #556B2F;
}
.plant-name {
  font-size: 1.2rem;
  font-weight: normal;
}
.info-label {
  font-weight: bold;
  color: #555;
  margin-right: 5px;
}
</style>