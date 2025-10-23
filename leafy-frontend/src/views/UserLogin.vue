<template>
  <div class="login-container">
    <h1>LEAFY</h1>
    <form @submit.prevent="submitLogin">
      <div class="form-group">
        <input type="text" id="userEmail" v-model="userEmail" placeholder="이메일을 입력하세요." required />
      </div>
      <div class="form-group">
        <input type="password" id="userPassword" v-model="userPassword" placeholder="비밀번호를 입력하세요." required />
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

<script>
import api from '@/api/api';
import { mapState, mapActions } from "vuex";

export default {
  name: "UserLogin",
  data() {
    return {
      userEmail: '',
      userPassword: '',
    };
  },
  mounted() {
    if (this.isLoggedIn) {
      this.$router.replace({ name: 'HomePage' });
    }
  },
  computed: {
    isLoggedIn() {
      return !!this.$store.state.user;
    },
    computed: {
      ...mapState(["popup", "user"])
    },
  },
  methods: {
    ...mapActions(["showPopup"]),
    async submitLogin() {
      try {
        const response = await api.post('/api/users/login', {
          email: this.userEmail,
          password: this.userPassword,
        });
        this.$store.dispatch('loginUser', response.data);
        this.$store.dispatch("showPopup", {
          title: "로그인 성공",
          message: `${response.data.name}님, 환영합니다.`,
          status: "success",
          showingSecond: 1500
        });
        this.$router.push({ name: 'HomePage' }); 
      } catch (error) {
        this.$store.dispatch("showPopup", {
          title: "로그인 실패",
          message: "로그인에 실패하였습니다. 아이디와 비밀번호를 확인해주세요.",
          status: "error",
          showingSecond: 1500
        });
        console.error(error);
      }
    },
  },
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
