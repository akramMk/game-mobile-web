<script setup>
import Login from './components/Login.vue'
import HelloWorld from './components/HelloWorld.vue'
import {ref} from "vue";

const logged = ref(false);
let loginMsg = "";
function handleLoginSuccess() {
  logged.value = true;
  loginMsg = "Bienvenue";
  console.log(loginMsg)
}

function logout() {
  logged.value = false;
  loginMsg = "";
  console.log(loginMsg)
}
</script>

<template class="template">
  <HelloWorld msg="Veuillez vous connecter" v-if="!logged"/>
  <HelloWorld msg="Bienvenue" v-else/>
  <Login v-if="!logged" @loginSuccess="handleLoginSuccess" :message=loginMsg />
  <button @click="logout" v-else>Logout</button>
</template>


<style scoped>
.template {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}
</style>
