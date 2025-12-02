<template>
  <div class="user-plants-select">
    <v-select
        v-model="selectedUserPlantId"
        :items="userPlantList"
        item-title="displayName"
        item-value="userPlantId"
        label="식물 선택"
        variant="filled"
        background-color="grey lighten-4"
        hide-details
        @update:model-value="onSelectChange"
    ></v-select>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import api from '@/api/api';

// Emits 정의: 부모에게 선택된 ID를 알려줌
const emit = defineEmits(['update:modelValue']);

// Props 정의: v-model 지원을 위해 modelValue 사용
const props = defineProps({
  modelValue: {
    type: Number,
    default: null
  }
});

const userPlantList = ref([]);
const selectedUserPlantId = ref(props.modelValue);

// 식물 목록 조회
const fetchUserPlants = async () => {
  try {
    // userId는 토큰에 있으므로 URL 파라미터 불필요
    const response = await api.get(`/api/user-plants/user`);

    userPlantList.value = response.data
        .filter(plant => plant.plant !== null)
        .map(plant => ({
          ...plant,
          // 드롭다운에 표시될 이름 (예: "초록이 (몬스테라)")
          displayName: `${plant.plantNickname} (${plant.plant.plantName})`
        }));
  } catch (error) {
    console.error("내 식물 목록 로딩 실패:", error);
  }
};

// 선택 변경 시 부모에게 알림
const onSelectChange = (newValue) => {
  emit('update:modelValue', newValue);
};

onMounted(() => {
  fetchUserPlants();
});
</script>

<style scoped>
.user-plants-select {
  width: 100%;
}
.user-plants {
  padding: 1rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-bottom: 1rem;
}

h3 {
  margin-top: 0;
  margin-bottom: 1rem;
}
</style>
