<script setup>
import CustomButton from '@/components/CustomButton.vue';
import { ref, onMounted } from 'vue';
import { useRouter } from "vue-router";
import GenericDAO from '@/services/GenericDAO';
import { authState } from '@/services/AuthService';

const daoCategories = new GenericDAO('category');
const router = useRouter();
const categories = ref([]);
const newCategory = ref('');
const editingCategoryId = ref(null);
const editedCategoryName = ref('');
const alertMessage = ref(null);
const alertType = ref('success');
const showAlert = ref(false);

const auth = authState.isLogged;


const triggerAlert = (message, type = 'success') => {
    alertMessage.value = message;
    alertType.value = type;
    showAlert.value = true;
    setTimeout(() => (showAlert.value = false), 3000);
};


const showAllCategories = async () => {
    categories.value = await daoCategories.getAll();
};

const addCategory = async () => {
    const trimmedCategory = newCategory.value.trim();
    if (!trimmedCategory) {
        triggerAlert('Por favor, insira um nome válido para a categoria.', 'warning');
        return;
    }
    try {
        await daoCategories.insert({ name: trimmedCategory });
        await showAllCategories();
        newCategory.value = '';
        triggerAlert('Categoria adicionada com sucesso!', 'success');
    } catch (error) {
        console.error(error);
        triggerAlert('Erro ao adicionar a categoria.', 'danger');
    }
};

const deleteCategory = async (id) => {
    if (confirm('Tem certeza de que deseja remover esta categoria?')) {
        try {
            await daoCategories.delete(id);
            categories.value = categories.value.filter((category) => category.id !== id);
            triggerAlert('Categoria removida com sucesso!', 'success');
        } catch (error) {
            console.error(error);
            triggerAlert('Erro ao remover a categoria.', 'danger');
        }
    }
};

const editCategory = (category) => {
    editingCategoryId.value = category.id;
    editedCategoryName.value = category.name;
};

const cancelEdit = () => {
    editingCategoryId.value = null;
    editedCategoryName.value = '';
};

const updateCategory = async () => {
    const trimmedCategory = editedCategoryName.value.trim();
    if (!trimmedCategory) {
        triggerAlert('Por favor, insira um nome válido para a categoria.', 'warning');
        return;
    }

    try {
        await daoCategories.update(editingCategoryId.value, { name: trimmedCategory });

        const index = categories.value.findIndex((category) => category.id === editingCategoryId.value);
        if (index !== -1) {
            categories.value[index].name = trimmedCategory;
        }

        editingCategoryId.value = null;
        editedCategoryName.value = '';
        triggerAlert('Categoria atualizada com sucesso!', 'success');
    } catch (error) {
        console.error(error);
        triggerAlert('Erro ao atualizar a categoria.', 'danger');
    }

};
onMounted(async () => {
    if (auth) {
        showAllCategories();
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
            <h1 class="header ms-auto me-auto"><i class="bi bi-bookmarks-fill"></i> Categorias</h1>
        </div>
        <button type="button" class="btn pb-3 back-button" @click="router.push('/myProfile')">
            ← Voltar para os produtos
        </button>

        <div class="d-flex align-items-center mb-4">
            <input v-model="newCategory" type="text" class="form-control form-control-sm w-auto"
                placeholder="Nova Categoria" />
            <CustomButton class="btn btn-sm ms-2" @click="addCategory">Adicionar</CustomButton>
        </div>

        <table class="table table-striped table-bordered">
            <thead>
                <tr>
                    <th scope="col">Nome</th>
                    <th scope="col">Ações</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="category in categories" :key="category.id">
                    <td>
                        <div v-if="editingCategoryId === category.id">
                            <input v-model="editedCategoryName" type="text" class="form-control form-control-sm" />
                        </div>
                        <div v-else>
                            {{ category.name }}
                        </div>
                    </td>
                    <td>
                        <div v-if="editingCategoryId === category.id">
                            <CustomButton @click="updateCategory" type="button" class="btn ms-2 me-2">
                                Salvar
                            </CustomButton>
                            <CustomButton @click="cancelEdit" type="button" class="btn ms-2 me-2">
                                Cancelar
                            </CustomButton>
                        </div>
                        <div v-else>
                            <CustomButton @click="() => editCategory(category)" type="button" class="btn ms-2 me-2">
                                <i class="bi bi-pencil-square"></i>
                            </CustomButton>
                            <CustomButton @click="deleteCategory(category.id)" type="button" class="btn ms-2 me-2">
                                <i class="bi bi-trash-fill"></i>
                            </CustomButton>
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
