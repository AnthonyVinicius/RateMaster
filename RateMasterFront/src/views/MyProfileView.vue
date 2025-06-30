<script setup>
import CustomButton from '@/components/CustomButton.vue';
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { authState } from '@/services/AuthService';
import ProductDAO from '@/services/ProductDAO';
import UserDAO from '@/services/UserDAO';

const router = useRouter();
const daoProducts = new ProductDAO();
const userDAO = new UserDAO();

const userData = ref({
  name: '',
  email: '',
  type: '',
  image: '',
  backGroundColor: '#000000',
});

const products = ref([]);
const alertMessage = ref(null);
const alertType = ref('success');
const showAlert = ref(false);
const viewType = ref('columns');
const isEditingUserName = ref(false);
const newUserName = ref('');
const isEditingProfileImage = ref(false);
const newProfileImageUrl = ref('');

// PAGINAÇÃO
const currentPage = ref(1);
const pageSize = 12; // quantidade por página
const totalPages = computed(() => Math.ceil(products.value.length / pageSize));
const paginatedProducts = computed(() => {
  const start = (currentPage.value - 1) * pageSize;
  return products.value.slice(start, start + pageSize);
});
const goToPage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page;
  }
};

const editProfileImage = () => { newProfileImageUrl.value = userData.value.image || ''; isEditingProfileImage.value = true; };
const cancelEditProfileImage = () => { isEditingProfileImage.value = false; newProfileImageUrl.value = ''; };
const updateProfileImage = async () => {
  const updatedImage = (newProfileImageUrl.value || '').trim();
  if (!updatedImage) return;
  isEditingProfileImage.value = false;
  try {
    await userDAO.update(authState.userId, { image: updatedImage });
    userData.value.image = updatedImage;
    triggerAlert('Imagem de perfil atualizada com sucesso!');
  } catch (error) {
    console.error("Erro ao atualizar a imagem:", error);
    triggerAlert('Erro ao atualizar a imagem.', 'danger');
  }
};

const loadData = async () => {
  try {
    const data = await userDAO.getUserData();
    userData.value = {
      name: data.name || '',
      email: data.email || '',
      type: data.role || '',
      image: data.image || '',
      backGroundColor: data.backGroundColor || '#000000',
    };
  } catch (error) {
    console.error('Erro ao carregar dados:', error);
    triggerAlert('Erro ao carregar dados.', 'danger');
  }
};

const triggerAlert = (message, type = 'success') => {
  alertMessage.value = message;
  alertType.value = type;
  showAlert.value = true;
  setTimeout(() => (showAlert.value = false), 3000);
};

const viewTypeColumns = () => { viewType.value = 'columns'; };
const viewTypeList = () => { viewType.value = 'list'; };

const editUserName = () => { newUserName.value = userData.value.name || ''; isEditingUserName.value = true; };
const cancelEditUserName = () => { isEditingUserName.value = false; newUserName.value = ''; };
const updateUserName = async () => {
  const updatedName = (newUserName.value || '').trim();
  if (!updatedName) return;
  isEditingUserName.value = false;
  try {
    await userDAO.update(authState.userId, { name: updatedName });
    userData.value.name = updatedName;
    authState.userName = updatedName;
    newUserName.value = '';
    triggerAlert('Nome atualizado com sucesso!');
  } catch (error) {
    console.error("Erro ao atualizar o nome:", error);
    triggerAlert('Erro ao atualizar o nome.', 'danger');
  }
};

const updateBackgroundColor = async () => {
  try {
    await userDAO.update(authState.userId, { backGroundColor: userData.value.backGroundColor });
    triggerAlert('Cor de fundo atualizada com sucesso!', 'success');
  } catch (error) {
    triggerAlert('Erro ao atualizar a cor de fundo.', 'danger');
  }
};

const deleteProduct = async (id) => {
  if (confirm('Tem certeza de que deseja remover este Produto?')) {
    try {
      await daoProducts.delete(id);
      products.value = products.value.filter(product => product.id !== id);
      triggerAlert('Produto removido com sucesso!', 'success');
    } catch (error) {
      console.error(error);
      triggerAlert('Erro ao remover produto.', 'danger');
    }
  }
};

const goToUpdate = (productId) => { router.push({ name: 'updateProducts', params: { id: productId } }); };
const goToDetails = (productId) => { router.push({ name: 'productDetail', params: { id: productId } }); };

onMounted(async () => {
  await loadData();
  try {
    if (userData.value.type !== 'INDIVIDUAL') {
      products.value = await daoProducts.getMyProducts();
    } else {
      products.value = await daoProducts.getProductsReviewedByUser(authState.userId);
    }
    products.value = products.value || [];
  } catch (error) {
    console.error('Erro ao carregar produtos:', error);
    products.value = [];
  }
});
</script>

<template>
  <div class="text-white d-flex flex-row profile-background position-relative"
    :style="{ backgroundColor: userData.backGroundColor }">

    <div class="position-absolute top-0 end-0 m-3 d-flex gap-2 align-items-center">
      <CustomButton @click="editUserName">
        <i class="bi bi-pencil-square"></i>
      </CustomButton>

      <CustomButton>
        <label class="m-0 d-flex align-items-center gap-1">
          <i class="bi bi-palette-fill"></i>
          <input type="color" v-model="userData.backGroundColor" class="color-picker-hidden"
            @change="updateBackgroundColor" />
        </label>
      </CustomButton>
    </div>

    <div class="ms-5 mt-5 d-flex flex-column profile-logo-container">
      <div class="profile-image-wrapper">
        <img
          :src="userData.image || 'https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-profiles/avatar-1.webp'"
          alt="Profile Logo" class="img-fluid img-thumbnail mb-2 rounded-pill profile-logo" />
        <div class="overlay flex-column gap-2 p-2">
          <div v-if="!isEditingProfileImage">
            <CustomButton @click="editProfileImage">
              <i class="bi bi-pencil-square"></i>
            </CustomButton>
          </div>
          <div v-else class="edit-image-container w-100">
            <input v-model="newProfileImageUrl" type="text" placeholder="URL da nova imagem"
              class="form-control form-control-sm rounded-2 mb-2" />
            <div class="d-flex gap-2 justify-content-center">
              <CustomButton @click="updateProfileImage" :disabled="!(newProfileImageUrl?.trim?.() ?? '')" size="sm">
                Salvar
              </CustomButton>
              <CustomButton @click="cancelEditProfileImage" variant="secondary" size="sm">Cancelar</CustomButton>
            </div>
          </div>
        </div>

      </div>
    </div>

    <div class="ms-4 user-info d-flex">
      <div class="d-flex flex-column justify-content-center">
        <h5 v-if="!isEditingUserName" class="profile-text">{{ userData.name }}</h5>

        <div class="hstack gap-2" v-if="isEditingUserName">
          <input v-if="isEditingUserName" v-model="newUserName" type="text" class="form-control input-name" />

          <CustomButton @click="updateUserName" :disabled="!(newUserName?.trim?.() ?? '')">
            Salvar
          </CustomButton>

          <CustomButton @click="cancelEditUserName">
            Cancelar
          </CustomButton>
        </div>

        <h5 class="email-text">{{ userData.email }}</h5>
      </div>
    </div>
  </div>

  <div v-if="showAlert" :class="`alert alert-${alertType} alert-dismissible fade show custom-alert m-3`" role="alert">
    <i v-if="alertType === 'success'" class="bi bi-check-circle-fill"></i>
    <i v-if="alertType === 'warning'" class="bi bi-exclamation-triangle-fill"></i>
    <i v-if="alertType === 'danger'" class="bi bi-x-circle-fill"></i>
    {{ alertMessage }}
    <button type="button" class="btn-close" @click="showAlert = false"></button>
  </div>

  <!-- Produtos avaliados (perfil individual) -->
  <div class="container mt-5" v-if="userData.type === 'INDIVIDUAL'">
    <h3>Produtos que você avaliou</h3>
    <div v-if="paginatedProducts.length">
      <div class="row row-cols-1 row-cols-md-4 g-4">
        <div class="product-card" v-for="product in paginatedProducts" :key="product.id">
          <div class="card rounded-3 h-100 text-truncate" @click="goToDetails(product.id)">
            <div class="d-flex justify-content-center align-items-center img-container">
              <img class="img-fluid product-img" :src="product.image" :alt="product.name">
            </div>
            <div class="card-body">
              <h5 class="fw-bold text-truncate">{{ product.name }}</h5>
              <p class="card-text text-truncate">{{ product.description }}</p>
              <p class="price text-truncate">R$ {{ product.price }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- paginação -->
      <nav v-if="totalPages > 1" aria-label="Paginação">
        <ul class="pagination justify-content-center">
          <li class="page-item" :class="{ disabled: currentPage === 1 }">
            <button class="page-link" @click="goToPage(currentPage - 1)" aria-label="Página anterior">&laquo;</button>
          </li>

          <li v-for="page in totalPages" :key="page" class="page-item" :class="{ active: page === currentPage }">
            <button class="page-link" @click="goToPage(page)">{{ page }}</button>
          </li>

          <li class="page-item" :class="{ disabled: currentPage === totalPages }">
            <button class="page-link" @click="goToPage(currentPage + 1)" aria-label="Próxima página">&raquo;</button>
          </li>
        </ul>
      </nav>
    </div>
    <p v-else>Você ainda não fez reviews em nenhum produto.</p>
  </div>

  <!-- Produtos cadastrados (loja) -->
  <div class="container-fluid pb-3 bg-white" v-if="userData.type !== 'INDIVIDUAL'">
    <div class="container-fluid d-flex">
      <div class="hstack gap-3 mt-5 me-auto">
        <RouterLink to="/brand">
          <CustomButton><i class="bi bi-plus-circle"></i> Nova Marca</CustomButton>
        </RouterLink>
        <RouterLink to="/category">
          <CustomButton><i class="bi bi-plus-circle"></i> Nova Categoria</CustomButton>
        </RouterLink>
        <RouterLink to="/registerProduct">
          <CustomButton><i class="bi bi-box-seam"></i> Novo produto</CustomButton>
        </RouterLink>
      </div>
      <div class="hstack gap-1 ms-auto">
        <CustomButton class="mt-3" @click="viewTypeColumns"><i class="bi bi-layout-three-columns"></i></CustomButton>
        <CustomButton class="mt-3" @click="viewTypeList"><i class="bi bi-list-nested"></i></CustomButton>
      </div>
    </div>

    <div class="container-fluid mt-5 mb-5 col-md-11" v-if="viewType === 'columns'">
      <div class="row row-cols-1 row-cols-md-6 g-4">
        <div class="product-card" v-for="product in paginatedProducts" :key="product.id">
          <div class="card rounded-3 h-100 text-truncate">
            <div class="content-card" @click="goToDetails(product.id)">
              <div class="d-flex justify-content-center align-items-center img-container">
                <img class="img-fluid product-img" :src="product.image" :alt="product.name">
              </div>
              <div class="card-body">
                <div class="hstack">
                  <h4 class="fw-bold text-truncate">{{ product.name }}</h4>
                </div>
                <p class="card-text text-truncate">Marca: <strong>{{ product.brandModel?.name || 'Sem Marca' }}</strong></p>
                <p class="card-text text-truncate">{{ product.description }}</p>
                <p class="price text-truncate">R$ {{ product.price }}</p>
              </div>
            </div>
            <div class="card-actions">
              <CustomButton @click.stop="deleteProduct(product.id)" class="action-button">
                <i class="bi bi-trash-fill"></i>
              </CustomButton>
              <CustomButton @click.stop="goToUpdate(product.id)" class="action-button">
                <i class="bi bi-pencil-square"></i>
              </CustomButton>
            </div>
          </div>
        </div>
      </div>

      <!-- paginação -->
      <nav v-if="totalPages > 1" aria-label="Paginação">
        <ul class="pagination justify-content-center">
          <li class="page-item" :class="{ disabled: currentPage === 1 }">
            <button class="page-link" @click="goToPage(currentPage - 1)" aria-label="Página anterior">&laquo;</button>
          </li>

          <li v-for="page in totalPages" :key="page" class="page-item" :class="{ active: page === currentPage }">
            <button class="page-link" @click="goToPage(page)">{{ page }}</button>
          </li>

          <li class="page-item" :class="{ disabled: currentPage === totalPages }">
            <button class="page-link" @click="goToPage(currentPage + 1)" aria-label="Próxima página">&raquo;</button>
          </li>
        </ul>
      </nav>
    </div>

    <div class="mt-3 pt-5 text-body" v-if="viewType == 'list'">
      <div class="card-body p-4">
        <div class="table-responsive">
          <table class="table table-striped table-bordered">
            <thead>
              <tr>
                <th>Imagem</th>
                <th>Nome</th>
                <th>Marca</th>
                <th>Preço</th>
                <th>Tipo</th>
                <th>Descrição</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="product in paginatedProducts" :key="product.id">
                <td>
                  <div class="d-flex justify-content-center align-items-center img-container-list">
                    <img class="img-fluid rounded-2 product-img-list" :src="product.image" :alt="product.name">
                  </div>
                </td>
                <td>{{ product.name }}</td>
                <td>{{ product.brandModel?.name || 'Sem Marca' }}</td>
                <td class="price">R$ {{ product.price }}</td>
                <td>{{ product.type }}</td>
                <td>{{ product.description }}</td>
                <td>
                  <CustomButton @click="deleteProduct(product.id)" class="ms-2 me-2">
                    <i class="bi bi-trash-fill"></i>
                  </CustomButton>
                  <CustomButton @click="goToUpdate(product.id)" class="ms-2 me-2">
                    <i class="bi bi-pencil-square"></i>
                  </CustomButton>
                </td>
              </tr>
            </tbody>
          </table>

          <!-- paginação -->
          <nav v-if="totalPages > 1" aria-label="Paginação">
            <ul class="pagination justify-content-center">
              <li class="page-item" :class="{ disabled: currentPage === 1 }">
                <button class="page-link" @click="goToPage(currentPage - 1)" aria-label="Página anterior">&laquo;</button>
              </li>

              <li v-for="page in totalPages" :key="page" class="page-item" :class="{ active: page === currentPage }">
                <button class="page-link" @click="goToPage(page)">{{ page }}</button>
              </li>

              <li class="page-item" :class="{ disabled: currentPage === totalPages }">
                <button class="page-link" @click="goToPage(currentPage + 1)" aria-label="Próxima página">&raquo;</button>
              </li>
            </ul>
          </nav>

        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.profile-text {
  margin-top: 60px;
  font-size: 2rem;
  font-weight: 600;
}

.profile-background {
  height: 200px;
}

.profile-logo-container {
  position: relative;
  width: 150px;
}

.profile-image-wrapper {
  position: relative;
  border-radius: 50%;
  overflow: hidden;
}

.profile-logo {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  transition: filter 0.3s ease, transform 0.3s ease, box-shadow 0.3s ease;
  object-fit: cover;
}

.profile-image-wrapper:hover .profile-logo {
  filter: blur(4px);
  transform: scale(1.05);
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 150px;
  height: 150px;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.5);
  color: white;
  display: flex;
  justify-content: center;
  align-items: center;
  opacity: 0;
  transition: opacity 0.3s ease;
  cursor: pointer;
}

.edit-image-container input {
  font-size: 0.8rem;
}

.overlay {
  flex-direction: column;
  padding: 10px;
  text-align: center;
}

.overlay input::placeholder {
  font-size: 0.75rem;
}

.profile-image-wrapper:hover .overlay {
  opacity: 1;
}

.pencil-icon {
  font-size: 2rem;
}

.product-img {
  position: relative;
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.img-container {
  width: 100%;
  height: 150px;
}

.img-container-list {
  height: 50px;
}

.product-img-list {
  max-width: 50px;
  max-height: 100%;
}

.card {
  transition: transform 0.3s, box-shadow 0.3s;
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.2);
}

.card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.rating {
  color: #f39c12;
}

.card-text {
  font-size: 0.9rem;
}

.price {
  color: #28a745;
  font-weight: bold;
  font-size: 1.1rem;
}

.card-actions {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  display: none;
  gap: 30px;
  z-index: 2;
}

.product-card:hover .card-actions {
  display: flex;
}

.product-card:hover .content-card {
  filter: blur(4px);
}

.color-picker-hidden {
  opacity: 0;
  position: absolute;
  cursor: pointer;
}
</style>