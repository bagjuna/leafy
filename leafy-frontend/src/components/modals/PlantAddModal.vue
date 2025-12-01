<template>
  <v-dialog v-model="dialogVisible" persistent max-width="600px">
    <v-card>
      <v-card-title class="text-center">
        <h3 style="margin-top: 20px;">식물 추가</h3>
      </v-card-title>
      <v-card-text>
        <v-container>
          <v-form ref="formRef" @submit.prevent="addPlant">
            <v-row>
              <v-col cols="6">
                <v-text-field
                    v-model="plantName"
                    label="식물명"
                    :rules="[rules.required]"
                    required
                ></v-text-field>
              </v-col>
              <v-col cols="6">
                <v-text-field
                    v-model="plantType"
                    label="식물 타입"
                    :rules="[rules.required]"
                    required
                ></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-textarea
                    v-model="plantDesc"
                    label="식물 상세 설명"
                    :rules="[rules.required]"
                    required
                ></v-textarea>
              </v-col>
              <v-col cols="12">
                <v-text-field
                    v-model="imageUrl"
                    label="이미지 URL"
                    :rules="[rules.required]"
                    required
                ></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-range-slider
                    v-model="temperatureRange"
                    :min="10" :max="40" step="0.1"
                    label="온도 범위" thumb-label tick-labels
                ></v-range-slider>
              </v-col>
              <v-col cols="12">
                <v-range-slider
                    v-model="humidityRange"
                    :min="30" :max="60" step="1"
                    label="습도 범위" thumb-label tick-labels
                ></v-range-slider>
              </v-col>
              <v-col cols="12">
                <v-text-field
                    v-model="wateringInterval"
                    label="물 주기(일)"
                    type="number"
                    :rules="[rules.required, rules.number]"
                    required
                ></v-text-field>
              </v-col>
            </v-row>
          </v-form>
        </v-container>
      </v-card-text>

      <v-row align="center" justify="center" class="mb-4">
        <v-col cols="5">
          <v-btn block size="large" color="#999999" @click="close" >취소</v-btn>
        </v-col>
        <v-col cols="5">
          <v-btn block size="large" color="#556B2F" @click="addPlant" >추가</v-btn>
        </v-col>
      </v-row>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, computed } from 'vue';
import api from '@/api/api.js';

const props = defineProps({
  isOpen: {
    type: Boolean,
    required: true,
  },
});

const emit = defineEmits(['update:isOpen', 'added-plant']);

// 1. 모달 상태 동기화 (Computed 사용 - 가장 깔끔한 방법)
// props가 바뀌면 dialogVisible이 바뀌고,
// 다이얼로그가 닫히면(set) emit을 날려서 부모에게 알림
const dialogVisible = computed({
  get: () => props.isOpen,
  set: (val) => emit('update:isOpen', val)
});

const formRef = ref(null);

// 2. 유효성 검사 규칙 정의
const rules = {
  required: value => !!value || '필수 입력 항목입니다.',
  number: value => !isNaN(parseFloat(value)) && isFinite(value) || '숫자만 입력 가능합니다.'
};

// 데이터
const plantName = ref("");
const plantType = ref("");
const plantDesc = ref("");
const imageUrl = ref("");
const temperatureRange = ref([18.0, 30.0]);
const humidityRange = ref([40.0, 50.0]);
const wateringInterval = ref(7);

// 3. 입력 폼 초기화 함수
const resetForm = () => {
  plantName.value = "";
  plantType.value = "";
  plantDesc.value = "";
  imageUrl.value = "";
  temperatureRange.value = [18.0, 30.0];
  humidityRange.value = [40.0, 50.0];
  wateringInterval.value = 7;
  // 유효성 검사 에러 메시지도 초기화
  if (formRef.value) formRef.value.resetValidation();
};

const close = () => {
  resetForm(); // 닫을 때 초기화
  dialogVisible.value = false; // computed의 setter 호출 -> emit 발생
};

const addPlant = async () => {
  // Vuetify 3의 validate는 Promise를 반환합니다.
  const { valid } = await formRef.value.validate();

  if (valid) {
    try {
      await api.post("/api/plants", {
        plantName: plantName.value,
        plantType: plantType.value,
        plantDesc: plantDesc.value,
        imageUrl: imageUrl.value,
        temperatureLow: temperatureRange.value[0],
        temperatureHigh: temperatureRange.value[1],
        humidityLow: humidityRange.value[0],
        humidityHigh: humidityRange.value[1],
        wateringInterval: wateringInterval.value,
      });

      emit("added-plant"); // 목록 갱신 요청
      close(); // 모달 닫기 (+ 초기화)
    } catch (error) {
      console.error(error);
      alert("식물 추가에 실패했습니다.");
    }
  }
};
</script>

<style scoped>
.v-btn {
  color: white;
}
</style>
