<template>
  <div class="login-container">
    <h1>LEAFY</h1>

    <form @submit.prevent="handleLogin">
      <div class="form-group">
        <input type="text" id="userEmail" v-model="email" placeholder="이메일을 입력하세요." required />
      </div>
      <div class="form-group">
        <input type="password" id="userPassword" v-model="password" placeholder="비밀번호를 입력하세요." required />
      </div>
      <button type="submit">로그인</button>
    </form>

    <!--회원 가입하러 가기    -->
    <div style="text-align: center; margin-top: 1rem;">
      <span>계정이 없으신가요? </span>
      <router-link to="/signup" style="color: #556B2F; font-weight: bold;">회원가입</router-link>
    </div>
  </div>
</template>


<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import api from '@/api/api';
import { useAuthStore } from '@/store/auth'; // 1. 스토어 import

const router = useRouter();
const authStore = useAuthStore(); // 2. 스토어 인스턴스 생성

const email = ref('');
const password = ref('');

const handleLogin = async () => {
  try {
    const response = await api.post('/users/login', {
      email: email.value,
      password: password.value
    });

    // 응답 구조 예시: { accessToken: "...", refreshToken: "...", user: { ... } }
    console.log('서버 응답:', response.data);
    const { accessToken, refreshToken, user } = response.data
    console.log('로그인 성공:', user);
    console.log('accessToken:', accessToken);
    console.log('refreshToken:', refreshToken);
    // 3. Pinia 액션 호출 (함수 쓰듯이 바로 호출!)
    authStore.loginUser({
      user,
      accessToken,
      refreshToken
    });

    router.push('/');

  } catch (error) {
    console.error(error);
    // 팝업 띄우기
    authStore.showPopup({
      message: '로그인 실패! 정보를 확인해주세요.',
      status: 'error'
    });
  }
};
</script>

<style scoped>
.login-container {
  max-width: 300px; /* increased width */
  margin: 0 auto;
  height: 90vh;
  display: flex;
  justify-content: center;
  align-content: stretch;
  flex-direction: column;
}
h1 {
  font-weight: 900;
  font-size: 3rem; /* made the text bolder */
  margin: 2rem;
  margin-top: 1rem; /* moved the container 50px up */
  color: #556B2F;
}
form {
  display: flex;
  flex-direction: column;
}
.form-group {
  margin-bottom: 1rem;
  text-align: left;
}
label {
  display: block;
  font-weight: bold;
  margin-bottom: 0.3rem;
}
input[type="text"],
input[type="password"] {
  padding: 0.6rem;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  width: 100%;
  box-sizing: border-box; 
}
button[type="submit"] {
  background-color: #8FBC8F;
  border: none;
  color: white;
  padding: 0.7rem;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 1.2rem;
  border-radius: 4px;
  cursor: pointer;
  width: 100%; /* adjusted width */
  box-sizing: border-box; 
}
button[type="submit"]:hover {
  background-color: #556B2F;
}
</style>
