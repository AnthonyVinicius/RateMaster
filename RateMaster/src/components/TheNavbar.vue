<script setup>
import { inject, ref } from 'vue';
import { onAuthStateChanged, signOut } from 'firebase/auth';
import { auth } from '@/firebase.js';
import CustomButton from './CustomButton.vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const isLogged = ref(false);

const userData = inject('userData');

  onAuthStateChanged(auth, (user) => {
  if (user) {
    isLogged.value = true;
  } else {
    isLogged.value = false;
  }
});


const handleSignOut = async () => {
    try {
        await signOut(auth);
        router.push("/");
    } catch (error) {
        console.error("Erro ao sair:", error);
    }
};
</script>

<template>
  <nav class="navbar navbar-expand-lg">
    <div class="container-fluid">
      <RouterLink to="/">
        <img src="../assets/img/Icon.png" alt="Logo" class="logo" />
      </RouterLink>
      <h1 class="fw-medium fs-3 text-white">RateMaster</h1>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarTogglerDemo03"
        aria-controls="navbarTogglerDemo03" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarTogglerDemo03">
        <ul class="navbar-nav d-flex gap-3 me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <RouterLink class="nav-link" to="/">
              <i class="bi bi-house-fill p-2"></i>Início
            </RouterLink>
          </li>
          <li class="nav-item">
            <RouterLink class="nav-link" to="/reviews">
              <i class="bi bi-star-fill p-2"></i>Avaliações
            </RouterLink>
          </li>
          <li class="nav-item" v-if="!isLogged">
            <RouterLink class="nav-link" to="/login">
              <i class="bi bi-box-arrow-in-right p-2"></i>Login
            </RouterLink>
          </li>
          <li class="nav-item" v-if="isLogged && userData?.userType !== 'individual'">
            <RouterLink class="nav-link" to="/registerProduct">
              <i class="bi bi-plus-square-fill p-2"></i>Registrar Produto
            </RouterLink>
          </li>
        </ul>

        <div v-if="!isLogged">
          <RouterLink to="/registerUser">
            <CustomButton class="button m-2">Cadastrar-se</CustomButton>
          </RouterLink>
          <RouterLink to="/login">
            <CustomButton class="button m-2">Entrar</CustomButton>
          </RouterLink>
        </div>

        <div v-if="isLogged">
          <div class="dropdown">
            <button class="btn btn-link dropdown-toggle profile-link" type="button" id="profileDropdown"
              data-bs-toggle="dropdown" aria-expanded="false" aria-label="Profile options">
              <i class="bi bi-person-circle profile-icon"></i>
            </button>
            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="profileDropdown">
              <RouterLink to="/MyProfile" class="dropdown-item">Perfil</RouterLink>
              <li @click="handleSignOut" class="dropdown-item">Sair</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>


<style scoped>
.navbar {
  background-color: #2C3E50;
  padding: 10px 20px;
}

.nav-link {
  color: white;
  font-size: 16px;
  padding: 8px 12px;
  margin: 5px;
  transition: all 0.3s ease;
}

.nav-link:hover {
  background-color: #34495e;
  color: #fff;
  border-radius: 5px;
  transform: scale(1.1);
}

.logo {
  width: 50px;
  margin-left: 20px;
  margin-right: 20px;
}

.profile-icon {
  font-size: 50px;
  color: white;
  transition: color 0.3s ease;
}

.profile-icon:hover {
  color: #1e90ff;
}

.dropdown-menu {
  max-width: 200px;
  overflow-wrap: break-word;
  right: 0;
}

.dropdown-item {
  color: #333;
  padding: 8px 15px;
  transition: background-color 0.2s ease;
}

.dropdown-item:hover {
  background-color: #f1f1f1;
}

.navbar-toggler {
  background-color: #E74C3C;
  border: none;
  padding: 10px;
  border-radius: 5px;
}

.navbar-toggler:hover {
  background-color: #E74C3C;
}

</style>
