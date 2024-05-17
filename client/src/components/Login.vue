<template>
  <h2>{{ message }}</h2>

  <label for="login">Login :&nbsp;</label>
  <input type="text" name="login" id="login" />
  <br />
  <label for="password">Password :&nbsp;</label>
  <input type="password" name="password" id="password" />
  <br />
  <button @click="loginEvent">Send</button>
{{ errorMessage }}
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      errorMessage: "", // Message d'erreur de login
    };
  },
  props: {
    message: String,
  },
  methods: {
    async loginEvent() {
      console.log("Login cliqué.");
      const login = document.getElementById("login").value;
      const password = document.getElementById("password").value;

      const response = await fetch(`https://192.168.75.14/api/users/login?login=${login}&password=${password}`, {
        method: 'POST',
      });
      // getToken
      const token = response.headers.get("Authorization");
      //console.log(token);
      if (token) {
        console.log("Login réussi.");
        this.errorMessage = "";
        this.$emit('loginSuccess');
      } else {
        // modifier la valeur du message
        console.log("Login échoué.");
        this.errorMessage = "Login échoué. Veuillez réessayer.";
      }
    },
    emits: ['loginSuccess', 'authenticate']
  }
};
</script>

<style scoped>
input,
input[type="submit"],
select {
  color: grey;
  border: 1px solid;
}
</style>
