<script setup>
import CustomButton from '@/components/CustomButton.vue';
import DAOService from '@/services/DAOService';
import { ref, onMounted, inject } from 'vue';
import { useRouter } from 'vue-router';


const router = useRouter();

const daoProducts = new DAOService('products');
const daoBrands = new DAOService('brands');
const daoUser = new DAOService('user');
const daoShop = new DAOService('shop');
const products = ref([]);
const brandMap = ref({});
const alertMessage = ref(null);
const alertType = ref('success');
const showAlert = ref(false);

const userData = inject('userData');

const viewType = ref('columns');

const on_off = ref(false);
const newUserName = ref('');
const editedUserName = ref('');

const viewTypeColumns = () => {
  viewType.value = 'columns';
};

const viewTypeList = () => {
  viewType.value = 'list';
};
const editUserName = () => {
  on_off.value = true;
};

const cancelEditUserName = () => {
  on_off.value = false;
  newUserName.value = null;
};
const triggerAlert = (message, type = 'success') => {
    alertMessage.value = message;
    alertType.value = type;
    showAlert.value = true;
    setTimeout(() => (showAlert.value = false), 3000);
};

const updateUserName = async () => {
  editedUserName.value = newUserName.value.trim();
  on_off.value = false;
  try {
    if (userData.value.userType === 'individual') {
      await daoUser.update(userData.value.id, { name: editedUserName.value });
    }
    if (userData.value.userType === 'business') {
      await daoShop.update(userData.value.id, { name: editedUserName.value });
    }

    userData.value.name = editedUserName.value;

    newUserName.value = null;
  } catch (error) {
    console.error("Erro ao atualizar o nome:", error);
  }
};


const loadBrands = async () => {
  try {
    const brands = await daoBrands.getAll();
    brandMap.value = brands.reduce((map, brand) => {
      map[brand.id] = brand.name;
      return map;
    }, {});
  } catch (error) {
    console.error("Erro ao carregar marcas:", error);
  }
};

const showAll = async () => {
  try {
    await loadBrands();
    products.value = await daoProducts.search('idShop', userData.value.id);

    products.value = products.value.map(product => {
      product.brandName = brandMap.value[product.brand] || 'Sem Marca';
      return product
    });
  } catch (error) {
    console.log('erro filtrado', products.value)
    console.error('Erro ao carregar produtos:', error);
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

const goToUpdate = (productId) => {
  router.push({ name: 'updateProducts', params: { id: productId } });
};

const goToDetails = (productId) => {
  router.push({ name: 'productDetail', params: { id: productId } });
};


onMounted(() => {
  showAll();
});

</script>

<template>
  <div class="text-white d-flex flex-row profile-background position-relative">
    <CustomButton class="position-absolute top-0 end-0 m-3 me-4" @click="editUserName"><i
        class="bi bi-pencil-square"></i>
    </CustomButton>
    <div class="ms-5 mt-5 d-flex flex-column">
      <img src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-profiles/avatar-1.webp" alt="Profile Logo"
        class="img-fluid img-thumbnail mt-4 mb-2 rounded-pill profile-logo">
    </div>
    <div class="ms-3 ">
      <div class="hstack gap-2">
        <h5 class="profile-text" v-if="!on_off">{{ userData.name }}</h5>
        <input v-if="on_off" v-model="newUserName" type="text" class="form-control" />
        <div class="hstack gap-2" v-if="on_off">
          <CustomButton class="button me-1 form-control" @click="updateUserName">
            Salvar
          </CustomButton>
          <CustomButton class="button form-control" @click="cancelEditUserName">
            Cancelar
          </CustomButton>
        </div>
      </div>

      <h5>{{ userData.email }}</h5>


    </div>
  </div>
  <div v-if="showAlert" :class="`alert alert-${alertType} alert-dismissible fade show custom-alert m-3`" role="alert">
    <i v-if="alertType === 'success'" class="bi bi-check-circle-fill"></i>
    <i v-if="alertType === 'warning'" class="bi bi-exclamation-triangle-fill"></i>
    <i v-if="alertType === 'danger'" class="bi bi-x-circle-fill"></i>
    {{ alertMessage }}
    <button type="button" class="btn-close" @click="showAlert = false"></button>
  </div>

  <div class="container-fluid pb-3 bg-white" v-if="userData?.userType !== 'individual'">
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
        <div class="product-card" v-for="product in products" :key="product.id">
          <div class="card rounded-3 h-100 text-truncate">
            <div class="content-card" @click="goToDetails(product.id)">
              <div class="d-flex justify-content-center align-items-center img-container">
                <img class="img-fluid product-img" :src="product.image" :alt="product.name">
              </div>
              <div class="card-body">
                <div class="hstack">
                  <h4 class="fw-bold text-truncate">{{ product.name }}</h4>
                </div>
                <p class="card-text text-truncate">Marca: <strong>{{ product.brandName }}</strong></p>
                <p class="card-text text-truncate">{{ product.description }}</p>
                <p class="price text-truncate">{{ product.price }}</p>
              </div>
            </div>
            <div class="card-actions">
              <CustomButton @click="deleteProduct(product.id)" class="action-button">
                <i class="bi bi-trash-fill"></i>
              </CustomButton>
              <CustomButton @click="goToUpdate(product.id)" class="action-button">
                <i class="bi bi-pencil-square"></i>
              </CustomButton>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="mt-3 pt-5 text-body" v-if="viewType == 'list'">
      <div>
        <div class="card-body p-4">
          <div class="table-responsive">
            <table class="table table-striped table-bordered">
              <thead>
                <tr>
                  <th scope="col">Imagem</th>
                  <th scope="col">Nome</th>
                  <th scope="col">
                    <span class="m-3">Marca</span>
                  </th>
                  <th scope="col">Preço</th>
                  <th scope="col">Tipo</th>
                  <th scope="col">Descrição</th>
                  <th scope="col">Ações</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="product in products" :key="product.id">
                  <td>
                    <div class="d-flex justify-content-center align-items-center img-container-list">
                      <img class="img-fluid rounded-2 product-img-list" :src="product.image" :alt="product.name">
                    </div>
                  </td>
                  <td class="card-text">{{ product.name }}</td>
                  <td class="card-text">{{ product.brandName }}</td>
                  <td class="price">{{ product.price }}</td>
                  <td class="card-text">{{ product.type }}</td>
                  <td class="card-text">{{ product.description }}</td>
                  <td>
                    <CustomButton @click="deleteProduct(product.id)" type="button" class=" ms-2 me-2">
                      <i class="bi bi-trash-fill"></i>
                    </CustomButton>
                    <CustomButton @click="goToUpdate(product.id)" type="button" class="ms-2 me-2">
                      <i class="bi bi-pencil-square"></i>
                    </CustomButton>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.profile-text {
  margin-top: 100px;
  font-size: 2rem;
  font-weight: 600;
}

.form-control {
  margin-top: 100px;
}

.profile-background {
  background-color: #000;
  height: 200px;
}

.profile-logo {
  width: 150px;
  z-index: 1;
}

.product-img {
  position: relative;
  width: 100%;
  height: 100%;
  object-fit: cover;
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
  max-width: 100%;
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
</style>