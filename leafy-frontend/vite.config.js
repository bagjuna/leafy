import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import vuetify from 'vite-plugin-vuetify'; // Vuetify 플러그인
import path from 'path'; // Node.js 'path' 모듈

export default defineConfig({
    plugins: [
        vue(),
        vuetify({
            autoImport: true, // Vuetify 컴포넌트 자동 임포트
        }),
    ],
    // 'src' 폴더를 '@' 별칭으로 사용하기 위한 설정
    resolve: {
        alias: {
            '@': path.resolve(__dirname, './src'),
        },
    },

    server: {
        host: '0.0.0.0', // 외부 기기에서 접근 가능하도록 설정

        port: 5173,
        proxy: {
            '/api': {
                target: 'http://localhost:8080',
            }

        }
    },
});