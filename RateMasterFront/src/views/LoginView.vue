<script setup>
import CustomButton from '@/components/CustomButton.vue';
import { ref } from "vue";
import AuthService from '@/services/AuthService';
import { useRouter } from "vue-router";
import GenericDAO from '@/services/GenericDAO';
import { Form, Field, ErrorMessage } from 'vee-validate';

const errMsg = ref("");
const router = useRouter();
const email = ref("");
const password = ref("");
const showPassword = ref(false);
const daoUser = new GenericDAO('user');
const user = ref({});


const handleLogin = async () => {
  errMsg.value = '';
  user.value = {
    email: email.value.trim(),
    password: password.value.trim(),
  };

  try {
    const response = await daoUser.login(user.value);
    const token = response.token || (response.data && response.data.token);

    if (token) {
      AuthService.login(token);
      console.log('Token salvo e estado atualizado:', token);
      router.push('/');
    } else {
      errMsg.value = 'Token não recebido da API.';
    }
  } catch (error) {
    console.error(error);
    errMsg.value = 'Erro no login: ' + (error.response?.data?.message || error.message || 'Erro desconhecido');
  }
};
const toggleShowPassword = () => {
  showPassword.value = !showPassword.value;
};
</script>

<template>
  <div class="container py-5">
    <div class="row justify-content-center">
      <div class="col-12 col-lg-10">
        <div class="card border-0 shadow-sm overflow-hidden">
          <div class="row g-0">

            <div class="col-lg-6 d-none d-lg-block bg-light">
              <img src="../assets/img/Login.jpg" alt="login" class="w-100 h-100 object-fit-cover" />
            </div>


            <div class="col-lg-6">
              <div class="card-body p-4 p-lg-5">
                <div class="text-center mb-4">
                  <h2 class="h3 fw-bold mb-2">Bem-vindo de volta</h2>
                  <p class="text-muted">Faça login na sua conta</p>
                </div>

                <Form @submit="handleLogin">

                  <div class="mb-3">
                    <label class="form-label">Email</label>
                    <Field
                    v-model="email"
                    name="email"
                    id="email"
                    rules="required|email"
                    v-slot="{field, errors, meta}">
                      <input
                      v-bind="field"
                      :class="{
                        'form-control': true,
                        'is-valid': meta.touched && !errors.length,
                        'is-invalid': meta.touched && errors.length
                          }"                        
                      placeholder="seu@email.com" />
                    </Field>
                    <ErrorMessage name="email" class="errorMessage" />
                  </div>


                  <div class="mb-4">
                    <label class="form-label">Senha</label>
                    <div class="input-group">
                      <Field
                      v-model="password"
                      name="password"
                      id="password"
                      rules="required"
                      v-slot="{field, errors, meta}">
                      <input
                      v-bind="field"
                      :class="{
                        'form-control': true,
                        'is-valid': meta.touched && !errors.length,
                        'is-invalid': meta.touched && errors.length
                      }" 
                      :type="showPassword ? 'text' : 'password'" 
                      placeholder="Digite sua senha" />
                      </Field>
                      <button class="btn btn-outline-secondary" type="button" @click="toggleShowPassword">
                        <i :class="showPassword ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
                      </button>
                    </div>
                    <ErrorMessage name="password" class="errorMessage" />
                  </div>


                  <div v-if="errMsg" class="alert alert-danger text-center mb-3">
                    {{ errMsg }}
                  </div>


                  <div class="d-grid mb-3">
                    <CustomButton class="button btn-lg">Entrar</CustomButton>
                  </div>

                  <p class="text-center mb-3">
                    Não possui uma conta?
                    <RouterLink to="/registerUser" class="text-decoration-none">Cadastre-se aqui</RouterLink>
                  </p>

                  <div class="text-center">
                    <a href="/terms" class="small text-muted me-2">Termos de uso</a>
                    <a href="/privacy" class="small text-muted">Política de Privacidade</a>
                  </div>
                </Form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.card {
  border-radius: 1rem;
}

.object-fit-cover {
  object-fit: cover;
}

a {
  color: #0d6efd;
}

a:hover {
  color: #0a58ca;
}

.form-control {
  padding: 0.75rem 1rem;
}

.input-group .btn {
  padding-left: 1rem;
  padding-right: 1rem;
}

@media (max-width: 991.98px) {
  .card-body {
    padding: 2rem !important;
  }
}
</style>