import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

// 'auth'는 스토어의 고유 ID입니다.
export const useAuthStore = defineStore('auth', () => {

    // ==============================
    // 1. State (ref로 선언)
    // ==============================

    // 새로고침 시 로컬스토리지에서 복구
    const storedUser = localStorage.getItem('user');
    const user = ref(storedUser ? JSON.parse(storedUser) : null);

    const popup = ref({
        message: "",
        status: "",
        visible: false,
        showingSecond: 3000,
    });

    // ==============================
    // 2. Getters (computed로 선언)
    // ==============================
    const isAuthenticated = computed(() => !!user.value);

    // ==============================
    // 3. Actions (일반 함수로 선언)
    // ==============================

    // 로그인
    const loginUser = (payload) => {
        const { user: userData, accessToken, refreshToken } = payload;

        // Pinia는 state를 직접 수정합니다 (mutations 불필요)
        user.value = userData;

        // 로컬스토리지 저장
        localStorage.setItem('user', JSON.stringify(userData));
        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('refreshToken', refreshToken);
    };

    // 로그아웃
    const logoutUser = () => {
        user.value = null;
        localStorage.removeItem('user');
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        // 필요 시 router.push('/login') 추가
    };

    // 팝업 표시
    const showPopup = (payload) => {
        popup.value = {
            message: payload.message,
            status: payload.status,
            showingSecond: payload.showingSecond || 3000,
            visible: true
        };

        setTimeout(() => {
            popup.value.visible = false;
        }, popup.value.showingSecond);
    };

    // 외부에서 쓸 변수와 함수들을 반환
    return {
        user,
        popup,
        isAuthenticated,
        loginUser,
        logoutUser,
        showPopup
    };
});
