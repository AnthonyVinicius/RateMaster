<script setup>
import CustomButton from '@/components/CustomButton.vue';
import BaseLayout from '@/components/BaseLayout.vue';
import { auth } from '@/firebase.js';
import { GoogleAuthProvider, signInWithPopup, signInWithEmailAndPassword } from "firebase/auth";
import DAOService from "@/services/DAOService";
import { ref } from "vue";
import { useRouter } from "vue-router";

const daoUser = new DAOService("user");
const daoShop = new DAOService("shop");

const router = useRouter();
const email = ref("");
const password = ref("");
const errMsg = ref("");
const showPassword = ref(false);
const userData = ref(null); // Dados do usuário logado

const login = async () => {
  errMsg.value = "";

  try {
    // Realiza o login
    const { user } = await signInWithEmailAndPassword(auth, email.value, password.value);

    // Busca os dados do usuário nos DAOs
    const userFromDB = await fetchUserData(user.uid);

    if (userFromDB) {
      alert("Usuário logado com sucesso!");
      console.log("Dados do usuário:", userFromDB);

      // Redireciona para a página inicial
      router.push("/");
    } else {
      errMsg.value = "Erro ao carregar dados do usuário. Entre em contato com o suporte.";
    }
  } catch (error) {
    // Tratamento de erros de autenticação
    switch (error.code) {
      case "auth/invalid-email":
        errMsg.value = "E-mail inválido.";
        break;
      case "auth/user-not-found":
        errMsg.value = "Nenhuma conta com este e-mail foi encontrada.";
        break;
      case "auth/wrong-password":
        errMsg.value = "Senha inválida.";
        break;
      default:
        errMsg.value = "Erro ao tentar login. Tente novamente.";
    }
  }
};

const fetchUserData = async (uid) => {
  // Tenta buscar nos DAOs `user` e `shop`
  try {
    let user = await daoUser.get(uid);
    if (user) {
      userData.value = { ...user, userType: "user" }; // Define tipo como `user`
      return userData.value;
    }

    let shop = await daoShop.get(uid);
    if (shop) {
      userData.value = { ...shop, userType: "shop" }; // Define tipo como `shop`
      return userData.value;
    }

    return null; // Caso não encontre em nenhum DAO
  } catch (error) {
    console.error("Erro ao buscar dados do usuário:", error);
    return null;
  }
};

const loginWithGoogle = async () => {
  const provider = new GoogleAuthProvider();

  try {
    // Realiza o login com Google
    const { user } = await signInWithPopup(auth, provider);

    // Busca os dados do usuário nos DAOs
    const userFromDB = await fetchUserData(user.uid);

    if (userFromDB) {
      alert("Login com Google realizado com sucesso!");
      console.log("Dados do usuário:", userFromDB);

      // Redireciona para a página inicial
      router.push("/");
    } else {
      errMsg.value = "Erro ao carregar dados do usuário. Entre em contato com o suporte.";
    }
  } catch (error) {
    console.error("Erro na autenticação com Google:", error);
    errMsg.value = "Erro ao tentar autenticar com o Google.";
  }
};

const toggleShowPassword = () => {
  showPassword.value = !showPassword.value;
};
</script>

<template>
  <BaseLayout>
    <div class="container">
      <div class="card login-card">
        <div class="row g-0">
          <div class="col-md-6 col-lg-6 d-none d-md-block">
            <img src="../assets/img/Login.jpg" alt="login form" class="img-fluid login-image rounded-start" />
          </div>
          <div class="col-md-6 col-lg-6 d-flex">
            <div class="card-body p-4 p-lg-5">
              <form @submit.prevent="login">
                <div class="mb-2 pb-1">
                  <h1 class="h1 fw-bold login-title">RateMaster</h1>
                </div>
                <h5 class="fw-normal pb-3 login-header">Faça login na sua conta.</h5>
                <div>
                  <div class="form-outline mb-1">
                    <input type="email" class="form-control form-control-lg" placeholder="Digite seu email" v-model="email" required />
                  </div>
                  <div class="form-outline mb-2 position-relative">
                    <input :type="showPassword ? 'text' : 'password'" class="form-control form-control-lg" placeholder="Digite sua senha" v-model="password" required />
                    <i :class="showPassword ? 'bi bi-eye-slash' : 'bi bi-eye'" class="toggle-password" @click="toggleShowPassword"></i>
                  </div>
                  <div class="d-flex justify-content-between m-3">
                    <RouterLink to="/forgot-password">Esqueci a senha</RouterLink>
                  </div>
                </div>
                <CustomButton @click="login" class="button">Entrar</CustomButton>
                <p v-if="errMsg" class="text-danger text-center">{{ errMsg }}</p>
                <CustomButton @click="loginWithGoogle" class="google-button">
                  <i class="bi bi-google me-2"></i> Entrar com Google
                </CustomButton>
                <p class="mb-3 mt-3 pb-lg-2 no-account">Não possui uma conta? <RouterLink to="/registerUser">Cadastre-se aqui.</RouterLink></p>
                <RouterLink to="/terms" class="small text-muted">Termos de uso</RouterLink>
                <RouterLink to="/privacy" class="small text-muted">Política de Privacidade</RouterLink>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </BaseLayout>
</template>

<style scoped>
.container {
  padding: 100px;
}

.login-card {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.login-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.login-header {
  letter-spacing: 1px;
  color: #333;
}

a {
  color: #E74C3C;
  text-decoration: none;
}

a:hover {
  color: #1e90ff;
  text-decoration: underline;
}

.button {
  width: 100%;
}

button:hover {
  transform: scale(1.01);
}

.toggle-password {
  position: absolute;
  top: 50%;
  right: 10px;
  transform: translateY(-50%);
  cursor: pointer;
  font-size: 1.2rem;
  color: #aaa;
}

.toggle-password:hover {
  color: #000;
}

.google-button {
  background-color: #4285F4;
  color: white;
  border: none;
  width: 100%;
  padding: 10px 0;
  font-size: 1rem;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  margin-top: 1rem;
}

.google-button:hover {
  background-color: #357ae8;
}
</style>
