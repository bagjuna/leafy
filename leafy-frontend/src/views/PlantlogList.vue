<template>
  <v-container class="main-container">
    <v-btn
        block
        color="#556B2F"
        size="large"
        class="add-log-btn mb-6"
        @click="openAddDialog"
    >
      일기 추가
    </v-btn>

    <div v-if="isLoading" class="text-center my-4">데이터 로딩 중...</div>
    <div v-else-if="error" class="text-center error--text">데이터 로드 실패: {{ error.message }}</div>

    <div v-else class="log-list">
      <v-card
          v-for="log in logs"
          :key="log.plantLogId"
          class="log-card mb-5 pa-4"
          variant="outlined"
      >
        <div class="card-header d-flex justify-end">
          <v-icon v-if="log.watered" color="indigo darken-2" class="mr-2">mdi-water</v-icon>
          <v-icon color="red lighten-1" @click.stop="deleteLog(log.plantLogId)" style="cursor: pointer;">
            mdi-delete
          </v-icon>
        </div>

        <div class="card-body text-center" @click="openPlantDetailModal(log.userPlant.plant.plantId)">
          <h3 class="plant-name-title mb-2">
            {{ log.userPlant.plantNickname }}
            <span class="plant-species">({{ log.userPlant.plant ? log.userPlant.plant.plantName : '?' }})</span>
          </h3>
          <p class="log-note">{{ log.note }}</p>
        </div>

        <div class="card-footer text-right mt-3">
          <span class="date-text">[ {{ formatDate(log.createdAt) }} ]</span>
        </div>
      </v-card>
    </div>

    <v-dialog v-model="showAddDialog" max-width="500px">
      <v-card class="pa-4 rounded-lg">
        <v-card-title class="text-center font-weight-bold mb-2">
          내 식물 리스트
        </v-card-title>

        <v-card-text>
          <v-select
              v-model="newLog.userPlantId"
              :items="userPlants"
              item-title="plantNickname"
              item-value="userPlantId"
              label="식물 선택"
              variant="filled"
              background-color="grey lighten-4"
              class="mb-2"
          ></v-select>

          <v-text-field
              v-model="newLog.note"
              label="Note"
              variant="filled"
              background-color="grey lighten-4"
          ></v-text-field>

          <div class="d-flex align-center mt-2">
            <v-switch
                v-model="newLog.watered"
                color="grey darken-1"
                hide-details
            ></v-switch>
            <span class="ml-2">물을 주었음</span>
          </div>
        </v-card-text>

        <v-card-actions class="justify-end">
          <v-btn color="blue darken-1" variant="text" @click="closeAddDialog">CANCEL</v-btn>
          <v-btn color="blue darken-1" variant="text" @click="submitLog">ADD</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <PlantDetailModal
        :deleteButton="false"
        v-model:isOpen="showPlantDetailModal"
        :plantId="selectedPlantId"
        @removed-plant="fetchMyPlants"
    />
  </v-container>
</template>

<script setup>
import {ref, reactive} from 'vue';
import {usePlantData} from '@/composables/usePlantData';
import PlantDetailModal from '@/components/modals/PlantDetailModal.vue';
import api from '@/api/api'; // API 호출을 위해 import
import {useAuthStore} from '@/store/auth'; // userId 가져오기 위해 필요

// Composable 사용
const {logs, userPlants, isLoading, error, fetchMyPlants, fetchRecentLogs} = usePlantData();
const authStore = useAuthStore();


// --- 상태 관리 ---
const showPlantDetailModal = ref(false);
const selectedPlantId = ref(null);
const showAddDialog = ref(false);

// 새 일기 데이터
const newLog = reactive({
  userPlantId: null,
  note: '',
  watered: false
});

// --- 함수 정의 ---

// 1. 식물 상세 모달 열기
const openPlantDetailModal = (plantId) => {
  selectedPlantId.value = plantId;
  showPlantDetailModal.value = true;
};

// 2. 일기 추가 모달 열기/닫기
const openAddDialog = () => {
  // 모달 열 때 입력값 초기화
  newLog.userPlantId = null;
  newLog.note = '';
  newLog.watered = false;
  showAddDialog.value = true;
};

const closeAddDialog = () => {
  showAddDialog.value = false;
};


// 일기 저장 함수
const submitLog = async () => {
  if (!newLog.userPlantId) {
    alert("식물을 선택해주세요!");
    return;
  }

  try {
    await api.post('/api/plant-logs', {
      userPlant: { userPlantId: newLog.userPlantId },
      note: newLog.note,
      watered: newLog.watered,
    });

    // ✅ [핵심] 저장이 완료되면 목록을 다시 불러옵니다.
    await fetchRecentLogs();

    closeAddDialog();
    // 입력창 초기화
    newLog.note = '';
    newLog.watered = false;
    newLog.userPlantId = null;

  } catch (err) {
    console.error("일기 저장 실패:", err);
    alert("일기 저장 중 오류가 발생했습니다.");
  }
};

// 일기 삭제 함수
const deleteLog = async (logId) => {
  if (!confirm("정말 이 일기를 삭제하시겠습니까?")) return;

  try {
    await api.delete(`/api/plant-logs/${logId}`);

    // ✅ [핵심] 삭제가 완료되면 목록을 다시 불러옵니다.
    await fetchRecentLogs();

  } catch (err) {
    console.error("일기 삭제 실패:", err);
  }
};


// 5. 날짜 포맷팅 함수 (예: [ 2023, 3, 24 ])
const formatDate = (dateArray) => {
  if (!dateArray) return '';
  // 배열 [2025, 11, 19, ...] 형태로 온다고 가정
  if (Array.isArray(dateArray)) {
    return `${dateArray[0]}, ${dateArray[1]}, ${dateArray[2]}`;
  }
  // 문자열이면 Date 객체로 변환
  const d = new Date(dateArray);
  return `${d.getFullYear()}, ${d.getMonth() + 1}, ${d.getDate()}`;
};
</script>

<style scoped>
.main-container {
  max-width: 800px;
  margin: 0 auto;
  padding-top: 20px;
}

/* 버튼 스타일 */
.add-log-btn {
  color: white !important;
  font-weight: bold;
  font-size: 1rem;
  border-radius: 5px;
}

/* 카드 스타일 */
.log-card {
  border-radius: 12px;
  border: 1px solid #e0e0e0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05) !important;
  transition: transform 0.2s;
}

.log-card:hover {
  transform: translateY(-2px);
}

.plant-name-title {
  color: #556B2F; /* 녹색 */
  font-weight: 700;
  font-size: 1.1rem;
}

.plant-species {
  color: #556B2F;
  font-weight: 400;
}

.log-note {
  font-size: 1rem;
  font-weight: 600;
  color: #333;
  margin-top: 10px;
}

.date-text {
  color: #757575;
  font-size: 0.85rem;
}

/* 커서 스타일 */
.card-body {
  cursor: pointer;
}
</style>
