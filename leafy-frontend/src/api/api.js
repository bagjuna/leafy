
// src/api/api.js
import axios from 'axios';
import { useAuthStore } from '@/store/auth'; // Pinia Store 가져오기
import router from '../router/router.js';

const api = axios.create({
    baseURL: import.meta.env.BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// 요청 인터셉터 (토큰 싣기)
api.interceptors.request.use(
    (config) => {
        const accessToken = localStorage.getItem('accessToken');
        if (accessToken) {
            config.headers.Authorization = `Bearer ${accessToken}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// 응답 인터셉터 (에러 처리 핵심)
api.interceptors.response.use(
    (response) => response,
    async (error) => {
        // 백엔드에서 401(인증 실패) 에러가 왔을 때
        if (error.response && error.response.status === 401) {
            console.warn("세션이 만료되었거나 유효하지 않습니다. 로그아웃 처리합니다.");

            // 1. Pinia Store를 가져와서 로그아웃 액션 실행
            const authStore = useAuthStore();
            authStore.logoutUser();

            // 2. 로그인 페이지로 강제 이동
            // (router가 setup 되지 않았을 경우를 대비해 window.location 사용 가능)
            if (router) {
                router.push('/login');
            } else {
                window.location.href = '/login';
            }
        }
        return Promise.reject(error);
    }
);

export default api;