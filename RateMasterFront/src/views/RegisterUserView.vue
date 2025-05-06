<script setup>
import CustomButton from '@/components/CustomButton.vue';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { Form, Field, ErrorMessage } from 'vee-validate';
import GenericDAO from '@/services/GenericDAO';

const daoShop = new GenericDAO('shop');
const daoUser = new GenericDAO('user');
const router = useRouter();
const user = ref("{}");
const name = ref("");
const email = ref("");
const password = ref("");
const confirmPassword = ref("");
const showPassword = ref(false);
const userType = ref("individual");

const createUser = () => {
  user.value = {
    name: name.value.trim(),
    email: email.value.trim(),
    password: password.value.trim(),
    userType: userType.value.trim()
  };
  if (password.value !== confirmPassword.value) {
    alert("As senhas não coincidem. Por favor, verifique.");
    return;
  }
  try {

    if (userType.value === "individual") {
      daoUser.insert(user.value);

    } else {
      daoShop.insert(user.value);

    };
    alert("Cadastro realizado com sucesso!");
    router.push('/');

  } catch (error) {
    console.error(error);
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
            <div class="col-lg-6">
              <div class="card-body p-4 p-lg-5">
                <div class="text-center mb-4">
                  <h2 class="h3 fw-bold mb-2">Criar Conta</h2>
                  <p class="text-muted">Junte-se ao RateMaster hoje</p>
                </div>

                <Form @submit="createUser">
                  <div class="mb-3">
                    <label class="form-label">Tipo de Conta</label>
                    <select v-model="userType" class="form-select">
                      <option value="individual">Pessoa Física</option>
                      <option value="business">Pessoa Jurídica</option>
                    </select>
                  </div>

                  <div class="mb-3">
                    <label class="form-label">Email</label>
                    <Field v-model="email" name="email" id="email" type="email" rules="required|email"
                      v-slot="{ field, errors, meta }">
                      <input v-bind="field" :class="{
                        'form-control': true,
                        'is-valid': meta.touched && errors.length,
                        'is-invalid': meta.touched && errors.length
                      }" placeholder="seu@email.com" />
                    </Field>
                    <ErrorMessage name="email" class="errorMessage" />
                  </div>

                  <div class="mb-3">
                    <label class="form-label">Senha</label>
                    <div class="input-group">
                      <Field v-model="password" name="password" id="password" rules="required"
                        v-slot="{ field, errors, meta }">
                        <input v-bind="field" :class="{
                          'form-control': true,
                          'is-valid': meta.touched && !errors.length,
                          'is-invalid': meta.touched && errors.length
                        }" :type="showPassword ? 'text' : 'password'" placeholder="Digite sua senha" />
                      </Field>

                      <button class="btn btn-outline-secondary" type="button" @click="toggleShowPassword">
                        <i :class="showPassword ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
                      </button>
                    </div>
                    <ErrorMessage name="password" class="errorMessage" />
                  </div>

                  <div class="mb-3">
                    <label class="form-label">Confirmar Senha</label>
                    <div class="input-group">
                      <Field v-model="confirmPassword" name="confirmPassword" id="confirmPassword" rules="required"
                        v-slot="{ field, errors, meta }">
                        <input v-bind="field" :class="{
                          'form-control': true,
                          'is-valid': meta.touched && !errors.length,
                          'is-invalid': meta.touched && errors.length
                        }" :type="showPassword ? 'text' : 'password'" placeholder="Confirme sua senha" />
                      </Field>
                      <button class="btn btn-outline-secondary" type="button" @click="toggleShowPassword">
                        <i :class="showPassword ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
                      </button>
                    </div>
                    <ErrorMessage name="confirmPassword" class="errorMessage" />
                  </div>

                  <div class="mb-3">
                    <label class="form-label">
                      {{ userType === 'individual' ? 'Nome Completo' : 'Nome da Empresa' }}
                    </label>
                    <Field v-model="name" name="name" id="name" rules="required|min:5|max:100"
                      v-slot="{ field, errors, meta }">
                      <input v-bind="field" :class="{
                        'form-control': true,
                        'is-valid': meta.touched && !errors.length,
                        'is-invalid': meta.touched && errors.length
                      }" :placeholder="userType === 'individual' ? 'Seu nome completo' : 'Nome da empresa'" />
                    </Field>
                    <ErrorMessage name="name" class="errorMessage" />
                  </div>

                  <div class="form-check mb-4">
                    <input class="form-check-input" type="checkbox" id="termsCheck" required />
                    <label class="form-check-label" for="termsCheck">
                      Eu concordo com os <a href="/terms">Termos de uso</a> e a
                      <a href="/privacy">Política de privacidade</a>
                    </label>
                  </div>

                  <div class="d-grid mb-3">
                    <CustomButton class="button btn-lg">Criar Conta</CustomButton>
                  </div>

                  <p class="text-center mb-0">
                    Já possui uma conta?
                    <RouterLink to="/login" class="text-decoration-none">Entrar</RouterLink>
                  </p>
                </Form>
              </div>
            </div>

            <div class="col-lg-6 d-none d-lg-block bg-light">
              <img src="../assets/img/evaluation-feedback-customer-smiley-response.jpg" alt="Register"
                class="w-100 h-100 object-fit-cover" />
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

.form-control,
.form-select {
  padding: 0.75rem 1rem;
}

.errorMessage {
  color: red;
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
