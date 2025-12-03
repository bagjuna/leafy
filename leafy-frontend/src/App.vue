<template>
  <div id="app">
    <header class="app-header" v-if="authStore.isAuthenticated">
      <div class="brand">LEAFY</div>

      <div class="welcome-message">
        <p class="welcome-text">
          안녕하세요, <span class="user-name">{{ authStore.user?.name }}</span>님!
        </p>
        <p class="description">오늘도 즐거운 식물 관리하세요!!</p>
      </div>

      <button class="logout-btn" @click="handleLogout">로그아웃</button>
    </header>

    <div class="brand text-center" v-else>로그인 화면</div>

    <NavBar v-if="authStore.isAuthenticated"></NavBar>

    <div class="router-view-wrapper">
      <router-view></router-view>
    </div>

    <footer class="footer" v-if="authStore.isAuthenticated">
      <p>&copy; 2025 Leafy. All rights reserved.</p>
    </footer>

    <BasicPopup
        :message="authStore.popup.message"
        :status="authStore.popup.status"
        :visible="authStore.popup.visible"
        @close="closePopup"
    />
  </div>
</template>

<script setup>
import {onMounted, watch} from 'vue';
import {useRouter, useRoute} from 'vue-router';
import {useAuthStore} from '@/store/auth';
import BasicPopup from "@/components/BasicPopup.vue";
import NavBar from '@/components/NavBar.vue';

const authStore = useAuthStore();
const router = useRouter();
const route = useRoute();

const closePopup = () => {
  authStore.popup.visible = false;
};

// 로그아웃 핸들러
const handleLogout = () => {
  authStore.logoutUser(); // 스토어와 로컬스토리지 비우기
  router.push('/login');  // 로그인 페이지로 이동
};

const checkLoginStatus = () => {
  const publicPages = ['/login', '/signup'];
  const authRequired = !publicPages.includes(route.path);

  if (authRequired && !authStore.isAuthenticated) {
    authStore.showPopup({
      message: "로그인이 필요한 서비스입니다.",
      status: "warning"
    });
    router.push('/login');
  }
};

onMounted(() => {
  checkLoginStatus();
});

watch(
    () => route.path,
    () => {
      checkLoginStatus();
    }
);
</script>


/* ... ===============================. */
<style scoped>


/* 헤더 스타일 추가 */
.app-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  background-color: white;
}

.brand {
  font-size: 2rem;
  font-weight: 900;
  color: #556B2F;
}

/* 로그아웃 버튼 스타일 */
.logout-btn {
  background-color: transparent;
  color: #666;
  border: 1px solid #ccc;
  padding: 5px 10px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: all 0.2s;
}


.logout-btn:hover {
  background-color: #f8f9fa;
  color: #333;
  border-color: #999;
}

/* 기존 스타일 유지 (text-center 클래스 활용을 위해 일부 수정) */
.text-center {
  text-align: center;
  margin-top: 20px;
}


#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.welcome-message {
  margin-top: 20px;
  margin-bottom: 10px;
}

.welcome-text {
  font-size: 1.2rem;
  font-weight: bold;
  color: #333;
}

.user-name {
  color: #42b983;
}

.description {
  font-size: 0.9rem;
  color: #666;
}

.brand {
  font-size: 2rem;
  font-weight: bold;
  color: #556B2F;
  margin-bottom: 10px;
}

.router-view-wrapper {
  flex: 1;
}

.footer {
  background-color: #f8f9fa;
  padding: 20px;
  text-align: center;
  color: #6c757d;
  margin-top: auto;
}
</style>
