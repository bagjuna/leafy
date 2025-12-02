<template>
  <v-dialog v-model="isDialogOpen" max-width="600px">
    <v-card v-if="plantDetail" class="pa-4">

      <img :src="plantDetail.imageUrl" alt="Plant Image" class="plant-image" v-if="plantDetail.imageUrl" />

      <v-card-title class="text-center">
        <div class="plant-name">{{ plantDetail.plantName }}</div>
        <div class="plant-type text-grey">{{ plantDetail.plantType }}</div>
      </v-card-title>

      <v-card-text>
        <div class="plant-desc">{{ plantDetail.plantDesc }}</div>

        <v-divider class="my-3"></v-divider>

        <v-row>
          <v-col cols="6">
            <span class="plant-info-label">온도:</span>
            {{ plantDetail.temperatureLow }} ~ {{ plantDetail.temperatureHigh }}°C
          </v-col>
          <v-col cols="6">
            <span class="plant-info-label">습도:</span>
            {{ plantDetail.humidityLow }} ~ {{ plantDetail.humidityHigh }}%
          </v-col>
          <v-col cols="12" class="mt-2">
            <span class="plant-info-label">물주기:</span>
            {{ plantDetail.wateringInterval }}일에 한 번
          </v-col>
        </v-row>
      </v-card-text>

      <v-card-actions class="justify-center button-group">
        <v-btn color="grey" variant="text" @click="close">닫기</v-btn>

        <v-btn v-if="deleteButton" color="error" variant="text" @click="removePlant">
          삭제
        </v-btn>

        <v-btn
            v-if="!deleteButton"
            color="#556B2F"
            class="text-white"
            variant="flat"
            @click="openNicknameDialog"
        >
          내 식물로 추가
        </v-btn>
      </v-card-actions>
    </v-card>

    <v-dialog v-model="nicknameDialogOpen" max-width="400px">
      <v-card>
        <v-card-title>식물 애칭 정하기</v-card-title>
        <v-card-text>
          <v-text-field
              v-model="plantNickname"
              label="애칭을 입력해주세요"
              placeholder="예: 초록이"
              autofocus
              @keyup.enter="submitMyPlant"
          ></v-text-field>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="grey" @click="nicknameDialogOpen = false">취소</v-btn>
          <v-btn color="#556B2F" text @click="submitMyPlant">확인</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

  </v-dialog>
</template>

<script setup>
import { ref, watch, computed } from 'vue';
import api from '@/api/api.js';
import { useAuthStore } from '@/store/auth'; // Ensure this path is correct

// 1. Props Definition
const props = defineProps({
  isOpen: {
    type: Boolean,
    required: true,
  },
  plantId: {
    type: [Number, null], // Allow null initially
    required: true,
  },
  deleteButton: {
    type: Boolean,
    default: false,
  }
});

// 2. Emits Definition
const emit = defineEmits(['update:isOpen', 'removed-plant']);

// 3. State
// Sync internal state with prop using a computed setter/getter is often cleaner,
// but using a watcher + ref is also fine for dialogs.
const isDialogOpen = computed({
  get: () => props.isOpen,
  set: (val) => emit('update:isOpen', val)
});

const plantDetail = ref(null);
const nicknameDialogOpen = ref(false);
const plantNickname = ref('');

const authStore = useAuthStore();

// 4. Methods
const close = () => {
  isDialogOpen.value = false;
};

const fetchPlantDetail = async () => {
  if (!props.plantId) return; // Guard clause

  try {
    const response = await api.get(`/plants/${props.plantId}`);
    plantDetail.value = response.data;
  } catch (error) {
    console.error("Failed to fetch plant detail:", error);
  }
};

const removePlant = async () => {
  if (!confirm("정말 이 식물을 삭제하시겠습니까?")) return;

  try {
    await api.delete(`/plants/${props.plantId}`);
    emit('removed-plant'); // Notify parent to refresh list
    close();
  } catch (error) {
    console.error("Failed to remove plant:", error);
  }
};

const openNicknameDialog = () => {
  nicknameDialogOpen.value = true;
};

const submitMyPlant = async () => {
  if (!plantNickname.value.trim()) {
    alert("애칭을 입력해주세요!");
    return;
  }

  try {
    await api.post(`/user-plants`, {
      user: {
        userId: authStore.user?.userId
      },
      plant: {
        plantId: props.plantId
      },
      plantNickname: plantNickname.value,
    });

    // Optional: Notify success
    alert("내 식물로 추가되었습니다!");

    // Close nickname dialog and main dialog
    nicknameDialogOpen.value = false;
    plantNickname.value = '';
    close();

  } catch (error) {
    console.error("Failed to add user plant:", error);
    alert("식물 추가에 실패했습니다.");
  }
};

// 5. Watchers
// Fetch data when dialog opens
watch(() => props.isOpen, (newVal) => {
  if (newVal && props.plantId) {
    fetchPlantDetail();
  }
});
</script>

<style scoped>
/* Button text color fix */
.text-white {
  color: white !important;
}

.button-group {
  margin: 1rem;
}

.plant-image {
  width: 100%;
  max-height: 300px;
  object-fit: cover; /* Ensures image covers area nicely */
  border-radius: 8px;
  margin-bottom: 1rem;
  display: block;
}

.plant-name {
  font-size: 1.8rem;
  font-weight: bold;
  color: #333;
}

.plant-type {
  font-size: 1.1rem;
  font-weight: normal;
  margin-bottom: 0.5rem;
}

.plant-desc {
  font-size: 1rem;
  line-height: 1.6;
  color: #555;
  margin-bottom: 1.5rem;
}

.plant-info-label {
  font-weight: bold;
  color: #556B2F; /* Brand color */
  margin-right: 5px;
}
</style>
