<script setup>
import CustomButton from '@/components/CustomButton.vue';
import BaseLayout from '@/components/BaseLayout.vue';
import DAOService from '@/services/DAOService';
import { auth } from '@/firebase';
import { createUserWithEmailAndPassword } from 'firebase/auth';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const daoUser = new DAOService('user');
const daoShop = new DAOService('shop');

const router = useRouter();
const email = ref("");
const password = ref("");
const confirmPassword = ref("");
const showPassword = ref(false);
const userType = ref("individual");
const cpf = ref("");
const name = ref("");
const cnpj = ref("");
const businessName = ref("");

const register = async () => {
    if (password.value !== confirmPassword.value) {
        alert("As senhas não coincidem. Por favor, verifique.");
        return;
    }

    try {
        // Cria o usuário no Firebase Authentication
        const userCredential = await createUserWithEmailAndPassword(auth, email.value, password.value);
        const { uid, email: createdEmail } = userCredential.user;

        // Determina o DAO e os dados adicionais com base no tipo de usuário
        const dao = userType.value === "individual" ? daoUser : daoShop;
        const additionalData =
            userType.value === "individual"
                ? { name: name.value, cpf: cpf.value }
                : { businessName: businessName.value, cnpj: cnpj.value };

        // Insere na tabela associada ao DAO
        await dao.insert({
            uid, // ID retornado pelo Firebase
            email: createdEmail, // E-mail retornado pelo Firebase
            userType: userType.value,
            password: password.value, // Senha fornecida no registro
            ...additionalData,
        });

        alert("Cadastro realizado com sucesso!");
        router.push('/');
    } catch (error) {
        console.error(error);
        alert(error.message);
    }
};

const toggleShowPassword = () => {
    showPassword.value = !showPassword.value;
};

// Formatador de CPF
const formatCPF = () => {
    let value = cpf.value.replace(/\D/g, "");
    if (value.length > 11) value = value.slice(0, 11);
    cpf.value = value.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, "$1.$2.$3-$4");
};

// Formatador de CNPJ
const formatCNPJ = () => {
    let value = cnpj.value.replace(/\D/g, "");
    if (value.length > 14) value = value.slice(0, 14);
    cnpj.value = value.replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/, "$1.$2.$3/$4-$5");
};
</script>

<template>
    <BaseLayout>
        <div>
            <div class="container">
                <div class="card register-card">
                    <div class="row g-0">
                        <div class="col-md-6 col-lg-6 d-flex">
                            <div class="card-body p-4 p-lg-5">
                                <form @submit.prevent="register">
                                    <div class="mb-2 pb-1">
                                        <h1 class="h1 fw-bold register-title">RateMaster</h1>
                                    </div>
                                    <h5 class="fw-normal pb-3 register-header">Criar uma nova conta</h5>

                                    <div class="mb-3">
                                        <label class="form-label">Tipo de Conta</label>
                                        <select v-model="userType" class="form-select">
                                            <option value="individual">Pessoa Física</option>
                                            <option value="business">Pessoa Jurídica</option>
                                        </select>
                                    </div>

                                    <div class="form-outline mb-2">
                                        <input type="email" class="form-control form-control-lg"
                                            placeholder="Digite seu email" v-model="email" required />
                                    </div>
                                    <div class="form-outline mb-2 position-relative">
                                        <input :type="showPassword ? 'text' : 'password'"
                                            class="form-control form-control-lg" placeholder="Digite sua senha"
                                            v-model="password" required />
                                        <i :class="showPassword ? 'bi bi-eye-slash' : 'bi bi-eye'"
                                            class="toggle-password" @click="toggleShowPassword"></i>
                                    </div>
                                    <div class="form-outline mb-2 position-relative">
                                        <input :type="showPassword ? 'text' : 'password'"
                                            class="form-control form-control-lg" placeholder="Confirme sua senha"
                                            v-model="confirmPassword" required />
                                        <i :class="showPassword ? 'bi bi-eye-slash' : 'bi bi-eye'"
                                            class="toggle-password" @click="toggleShowPassword"></i>
                                    </div>

                                    <!-- Campos específicos para Pessoa Física -->
                                    <div v-if="userType === 'individual'">
                                        <div class="form-outline mb-2">
                                            <input type="text" class="form-control form-control-lg" placeholder="Digite seu nome"
                                                v-model="name" required />
                                        </div>
                                        <div class="form-outline mb-2">
                                            <input type="text" class="form-control form-control-lg" placeholder="Digite seu CPF"
                                                v-model="cpf" @input="formatCPF" required />
                                        </div>
                                    </div>

                                    <!-- Campos específicos para Pessoa Jurídica -->
                                    <div v-if="userType === 'business'">
                                        <div class="form-outline mb-2">
                                            <input type="text" class="form-control form-control-lg"
                                                placeholder="Nome da Empresa" v-model="businessName" required />
                                        </div>
                                        <div class="form-outline mb-2">
                                            <input type="text" class="form-control form-control-lg"
                                                placeholder="Digite seu CNPJ" v-model="cnpj" @input="formatCNPJ" required />
                                        </div>
                                    </div>

                                    <div class="d-flex justify-content-between m-3">
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox" id="termsCheck" required>
                                            <label class="form-check-label" for="termsCheck">
                                                Eu concordo com o
                                                <RouterLink to="/terms">Termos de uso</RouterLink> e
                                                <RouterLink to="/privacy">Política de privacidade.</RouterLink>
                                            </label>
                                        </div>
                                    </div>

                                    <CustomButton class="button">Cadastrar-se</CustomButton>
                                    <p class="mb-3 mt-3 pb-lg-2 has-account">
                                        Já possui uma conta? <RouterLink to="/login">Entrar</RouterLink>
                                    </p>
                                </form>
                            </div>
                        </div>
                        <div class="col-md-6 col-lg-6 d-none d-md-block">
                            <img src="../assets/img/evaluation-feedback-customer-smiley-response.jpg"
                                alt="register form" class="img-fluid register-image rounded-end" />
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

.register-card {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.register-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.register-header {
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

.has-account {
    font-size: 14px;
    text-align: center;
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
</style>
