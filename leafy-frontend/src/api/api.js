
// src/api/api.js
import axios from 'axios';
import { useAuthStore } from '@/store/auth'; // Pinia Store 가져오기
import router from '../router/router.js';


const BASE_URL = '/api'

const api = axios.create({
    baseURL: '/api',
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

// 응답 인터셉터
api.interceptors.response.use(
    (response) => response,
    async (error) => {
        // 원래 요청 정보 저장
        const originalRequest = error.config;

        // 1. 401 에러가 발생했고, 아직 재시도를 안 한 요청이라면
        if (error.response && error.response.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true; // 재시도 플래그 설정 (무한 루프 방지)

            try {
                // 2. 리프레시 토큰으로 AccessToken 재발급 요청
                // (백엔드 /reissue 스펙에 맞춰 수정 필요: 보통 헤더나 바디에 refreshToken을 실어 보냄)
                const refreshToken = localStorage.getItem('refreshToken');

                // ⚠️ 주의: 여기서 api.post를 쓰면 안됨 (인터셉터 또 탐). axios.post 사용

                const baseURL = 'http://localhost:8080';

                const response = await axios.post(`${BASE_URL}/users/reissue`, {
                    refreshToken: refreshToken
                });


                // 3. 재발급 성공 시: 새로운 토큰 저장
                const newAccessToken = response.data.accessToken; // 백엔드 응답 필드명 확인 필요
                localStorage.setItem('accessToken', newAccessToken);

                // (선택) 리프레시 토큰도 갱신된다면 같이 저장
                if(response.data.refreshToken) {
                    localStorage.setItem('refreshToken', response.data.refreshToken);
                }

                // 4. 실패했던 원래 요청의 헤더를 새 토큰으로 교체하고 다시 시도
                originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
                return api(originalRequest);

            } catch (reissueError) {
                // 5. 재발급조차 실패함 (리프레시 토큰도 만료됨) -> 진짜 로그아웃
                console.warn("리프레시 토큰도 만료되었습니다. 로그아웃 처리합니다.");

                const authStore = useAuthStore();
                authStore.logoutUser(); // 스토어 초기화 (토큰 삭제 등)

                if (router) router.push('/login');
                else window.location.href = '/login';

                return Promise.reject(reissueError);
            }
        }

        // 401 이외의 에러거나, 이미 재시도했는데도 에러난 경우
        return Promise.reject(error);
    }
);

export default api;