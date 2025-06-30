<script setup>
import CustomButton from '@/components/CustomButton.vue';
import { ref, onMounted } from 'vue';
import { useRouter } from "vue-router";
import GenericDAO from '@/services/GenericDAO';
import { authState } from '@/services/AuthService';
import { computed } from 'vue';

const daoBrands = new GenericDAO('brand');
const router = useRouter();
const brands = ref([]);
const newBrand = ref('');
const editingBrandId = ref(null);
const editedBrandName = ref('');
const alertMessage = ref(null);
const alertType = ref('success');
const showAlert = ref(false);
const auth = computed(() => authState.isLogged);
const idUser = computed(() => authState.userId);



const triggerAlert = (message, type = 'success') => {
    alertMessage.value = message;
    alertType.value = type;
    showAlert.value = true;
    setTimeout(() => (showAlert.value = false), 3000);
};


const showAllBrands = async () => {
    try {
        brands.value = await daoBrands.getAll();
    } catch (error) {
        console.error(error);
        triggerAlert('Erro ao carregar marcas.', 'danger');
    }
};

const addBrand = async () => {
    const trimmedBrand = newBrand.value.trim();
    if (!trimmedBrand) {
        triggerAlert('Por favor, insira um nome válido para a marca.', 'warning');
        return;
    }
    try {
        await daoBrands.insert({ name: trimmedBrand,  userId: idUser.value});
        await showAllBrands();
        newBrand.value = '';
        triggerAlert('Marca adicionada com sucesso!', 'success');
    } catch (error) {
        console.error(error);
        triggerAlert('Erro ao adicionar a marca.', 'danger');
    }
};

const deleteBrand = async (id) => {
    if (confirm('Tem certeza de que deseja remover esta marca?')) {
        try {
            await daoBrands.delete(id);
            brands.value = brands.value.filter((brand) => brand.id !== id);
            triggerAlert('Marca removida com sucesso!', 'success');
        } catch (error) {
            console.error(error);
            triggerAlert('Erro ao remover a marca.', 'danger');
        }
    }
};

const editBrand = (brand) => {
    editingBrandId.value = brand.id;
    editedBrandName.value = brand.name;
};

const cancelEdit = () => {
    editingBrandId.value = null;
    editedBrandName.value = '';
};

const updateBrand = async () => {
    const trimmedName = editedBrandName.value.trim();
    if (!trimmedName) {
        triggerAlert('Por favor, insira um nome válido para a marca.', 'warning');
        return;
    }

    try {
        await daoBrands.update(editingBrandId.value, { name: trimmedName });

        const index = brands.value.findIndex((brand) => brand.id === editingBrandId.value);
        if (index !== -1) {
            brands.value[index].name = trimmedName;
        }

        editingBrandId.value = null;
        editedBrandName.value = '';
        triggerAlert('Marca atualizada com sucesso!', 'success');
    } catch (error) {
        console.error(error);
        triggerAlert('Erro ao atualizar a marca.', 'danger');
    }

};
onMounted(async () => {
    if (auth.value) {
        showAllBrands();
    } else {
        alert("Usuário não logado");
        router.push("/login");
    }

});
</script>

<template>
    <div class="content container-fluid mt-5">
        <div v-if="showAlert" :class="`alert alert-${alertType} alert-dismissible fade show custom-alert`" role="alert">
            <i v-if="alertType === 'success'" class="bi bi-check-circle-fill"></i>
            <i v-if="alertType === 'warning'" class="bi bi-exclamation-triangle-fill"></i>
            <i v-if="alertType === 'danger'" class="bi bi-x-circle-fill"></i>
            {{ alertMessage }}
            <button type="button" class="btn-close" @click="showAlert = false"></button>
        </div>
        <div class="d-flex ">
            <h1 class="header ms-auto me-auto"><i class="bi bi-award-fill"></i> Marcas</h1>
        </div>
        <button type="button" class="btn pb-3 back-button" @click="router.push('/myProfile')">
            ← Voltar para os produtos
        </button>

        <div class="d-flex align-items-center mb-4">
            <input v-model="newBrand" type="text" class="form-control form-control-sm w-auto"
                placeholder="Nova marca" />
            <CustomButton class="btn btn-sm ms-2" @click="addBrand">Adicionar</CustomButton>
        </div>

        <table class="table table-striped table-bordered">
            <thead>
                <tr>
                    <th scope="col">Nome</th>
                    <th scope="col">Ações</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="brand in brands" :key="brand.id">
                    <td>
                        <div v-if="editingBrandId === brand.id">
                            <input v-model="editedBrandName" type="text" class="form-control form-control-sm" />
                        </div>
                        <div v-else>
                            {{ brand.name }}
                        </div>
                    </td>
                    <td>
                        <div v-if="editingBrandId === brand.id">
                            <CustomButton @click="updateBrand" type="button" class="btn ms-2 me-2">
                                Salvar
                            </CustomButton>
                            <CustomButton @click="cancelEdit" type="button" class="btn ms-2 me-2">
                                Cancelar
                            </CustomButton>
                        </div>
                        <div v-else>
                            <div v-if="brand.userModel?.id === idUser">
                                <CustomButton @click="editBrand(brand)" type="button" class="btn ms-2 me-2">
                                    <i class="bi bi-pencil-square"></i>
                                </CustomButton>
                                <CustomButton @click="deleteBrand(brand.id)" type="button" class="btn ms-2 me-2">
                                    <i class="bi bi-trash-fill"></i>
                                </CustomButton>
                            </div>
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<style scoped>
.header {
    font-size: 2.5rem;
    color: #1a1a1a;
    font-weight: 700;
}

.custom-alert {
    display: flex;
    align-items: center;
    gap: 10px;
    animation: fadeIn 0.5s;
    margin-bottom: 20px;
    font-size: 1rem;
}

.back-button {
    background: none;
    border: none;
    color: #666;
    font-size: 1.1rem;
    padding: 0;
    transition: color 0.3s ease;
}

@keyframes fadeIn {
    from {
        opacity: 0;
    }

    to {
        opacity: 1;
    }
}
</style>
