// src/composables/usePlantData.js
import { reactive, ref, onMounted, toRefs } from 'vue';
import api from '@/api/api';
import { useAuthStore } from '@/store/auth';

export function usePlantData() {
    const authStore = useAuthStore();

    const state = reactive({
        logs: [],
        userPlants: [],
        isLoading: ref(true),
        error: ref(null),
    });

    // 1. 최근 로그 조회 함수
    const fetchRecentLogs = async () => {
        try {
            // userId는 백엔드에서 토큰으로 처리하거나, 필요하다면 경로에 추가
            const response = await api.get(`/api/plant-logs/recent/user`);
            state.logs = response.data;
        } catch (error) {
            state.error = error;
            console.error('로그 조회 실패:', error);
        }
    };

    // 2. 식물 목록 조회 함수
    const fetchMyPlants = async () => {
        try {
            const response = await api.get(`/api/user-plants/user`);
            state.userPlants = response.data;
        } catch (error) {
            state.error = error;
            console.error('식물 목록 조회 실패:', error);
        }
    };

    onMounted(async () => {
        state.isLoading = true;
        const userId = authStore.user?.userId;

        if (userId) {
            await Promise.all([
                fetchRecentLogs(),
                fetchMyPlants()
            ]);
        } else {
            console.error("로그인 정보가 없습니다.");
        }
        state.isLoading = false;
    });

    return {
        ...toRefs(state),
        fetchMyPlants,   // 기존에 있던 것
        fetchRecentLogs
    };
}
