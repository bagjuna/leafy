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
      <div v-if="logs.length === 0" class="text-center py-5 grey--text">
        작성된 일기가 없습니다. 첫 일기를 남겨보세요!
      </div>

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

        <div
            v-if="log.userPlant"
            class="card-body text-center"
            @click="openUserPlantDetailModal(log.userPlant.userPlantId)"
        >
          <h3 class="plant-name-title mb-2">
            {{ log.userPlant.plantNickname }}
            <span class="plant-species" v-if="log.userPlant.plant">
              ({{ log.userPlant.plant.plantName }})
            </span>
          </h3>
          <p class="log-note">{{ log.note }}</p>
        </div>

        <div v-else class="text-center text-grey">
          삭제된 식물의 일기입니다.
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
          일기 쓰기
        </v-card-title>

        <v-card-text>
          <UserPlants
              v-model="newLog.userPlantId"
              class="mb-3"
          />

          <v-text-field
              v-model="newLog.note"
              label="오늘의 기록"
              variant="filled"
              background-color="grey lighten-4"
              rows="3"
              auto-grow
          ></v-text-field>

          <div class="d-flex align-center mt-2">
            <v-switch
                v-model="newLog.watered"
                color="blue"
                hide-details
                inset
            ></v-switch>
            <span class="ml-2 font-weight-bold grey--text text--darken-2">물 주셨나요?</span>
          </div>
        </v-card-text>

        <v-card-actions class="justify-end">
          <v-btn color="grey darken-1" variant="text" @click="closeAddDialog">취소</v-btn>
          <v-btn color="#556B2F" variant="flat" class="text-white" @click="submitLog">등록</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <UserPlantDetailModal
        v-model:isOpen="showDetailModal"
        :userPlantId="selectedUserPlantId"
        @removed-plant="onPlantRemoved"
    />
  </v-container>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { usePlantData } from '@/composables/usePlantData';
import UserPlantDetailModal from '@/components/modals/UserPlantDetailModal.vue';
import UserPlants from '@/components/UserPlants.vue';
import api from '@/api/api';
import { useAuthStore } from '@/store/auth';

const { logs, isLoading, error, fetchRecentLogs, fetchMyPlants } = usePlantData();
const authStore = useAuthStore();

const showDetailModal = ref(false);
const selectedUserPlantId = ref(null);
const showAddDialog = ref(false);

const newLog = reactive({
  userPlantId: null,
  note: '',
  watered: false
});

const openUserPlantDetailModal = (userPlantId) => {
  selectedUserPlantId.value = userPlantId;
  showDetailModal.value = true;
};

const onPlantRemoved = async () => {
  await Promise.all([
    fetchRecentLogs(),
    fetchMyPlants()
  ]);
};

const openAddDialog = () => {
  newLog.userPlantId = null;
  newLog.note = '';
  newLog.watered = false;
  showAddDialog.value = true;
};

const closeAddDialog = () => {
  showAddDialog.value = false;
};

const submitLog = async () => {
  if (!newLog.userPlantId) {
    alert("어떤 식물의 일기인가요? 식물을 선택해주세요.");
    return;
  }

  try {
    await api.post('/api/plant-logs', {
      userPlant: { userPlantId: newLog.userPlantId },
      note: newLog.note,
      watered: newLog.watered,
    });
    await fetchRecentLogs();
    closeAddDialog();
  } catch (err) {
    console.error("일기 저장 실패:", err);
    alert("일기 저장 중 오류가 발생했습니다.");
  }
};

const deleteLog = async (logId) => {
  if (!confirm("정말 이 일기를 삭제하시겠습니까?")) return;

  try {
    await api.delete(`/api/plant-logs/${logId}`);
    await fetchRecentLogs();
  } catch (err) {
    console.error("일기 삭제 실패:", err);
  }
};

const formatDate = (dateArray) => {
  if (!dateArray) return '';
  if (Array.isArray(dateArray)) {
    return `${dateArray[0]}. ${dateArray[1]}. ${dateArray[2]}`;
  }
  const d = new Date(dateArray);
  return `${d.getFullYear()}. ${d.getMonth() + 1}. ${d.getDate()}`;
};
</script>

<style scoped>
.main-container {
  max-width: 800px;
  margin: 0 auto;
  padding-top: 20px;
  padding-left: 20px;
  padding-right: 20px;
}

.add-log-btn {
  color: white !important;
  font-weight: bold;
  font-size: 1rem;
  border-radius: 8px;
}

.log-card {
  border-radius: 12px;
  border: 1px solid #e0e0e0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05) !important;
  transition: transform 0.2s;
  background-color: white;
}

.log-card:hover {
  transform: translateY(-2px);
}

.plant-name-title {
  color: #556B2F;
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
  word-break: break-all;
}

.date-text {
  color: #757575;
  font-size: 0.85rem;
}

.card-body {
  cursor: pointer;
}
</style>