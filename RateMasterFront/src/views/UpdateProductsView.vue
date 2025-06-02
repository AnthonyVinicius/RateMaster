<script setup>
import CustomButton from '@/components/CustomButton.vue';
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import GenericDAO from '@/services/GenericDAO';
import { authState } from '@/services/AuthService';
import { computed } from 'vue';

const idUser = computed(() => authState.userId);

const route = useRoute();
const router = useRouter();

const alertMessage = ref(null);
const alertType = ref('success');
const showAlert = ref(false);
const priceInput = ref('');
const daoProducts = new GenericDAO('product');
const daoBrands = new GenericDAO('brand');
const daoCategories = new GenericDAO('category');

const brands = ref([]);
const categories = ref([]);
const product = ref({
  name: '',
  description: '',
  price: 0,
  brandModel: '',
  userId: '',
  categoryModel: '',
  image: '',
  type: ''
});

const loadBrands = async () => {
  brands.value = await daoBrands.getAll();
};

const loadCategories = async () => {
  categories.value = await daoCategories.getAll();
};

const triggerAlert = (message, type = 'success') => {
  alertMessage.value = message;
  alertType.value = type;
  showAlert.value = true;
  setTimeout(() => (showAlert.value = false), 3000);
};

const fetchProduct = async () => {
  const productId = route.params.id;

  if (!productId) {
    triggerAlert('Produto não encontrado!', 'warning');
    router.push({ name: 'listProducts' });
    return;
  }

  try {
    const fetchedProduct = await daoProducts.getById(productId);
    product.value = { ...fetchedProduct };
    console.log(fetchedProduct)
  } catch (error) {
    console.error(error);
    triggerAlert('Erro ao carregar o produto.', 'danger');
    router.push({ name: 'listProducts' });
  }
};


  const submit = async () => {
  const name = product.value.name.trim();
  const description = product.value.description.trim();
  const price = product.value.price;
  const image = product.value.image.trim();
  const brandModel = product.value.brandModel;
  const userId = idUser.value;
  const categoryModel = product.value.categoryModel;
  const type = product.value.type;

  const productData = {
    name,
    description,
    price,
    image,
    brandModel,
    userId,
    categoryModel,
    type
  };

  try {
    await daoProducts.update(product.value.id, productData);
    triggerAlert('Produto atualizado com sucesso!', 'success');
    router.push("/myProfile");
  } catch (error) {
    console.error(error);
    triggerAlert('Erro ao atualizar o produto.', 'danger');
    console.log(productData)
  }
};

const formatPrice = (event) => {
  let value = event.target.value.replace(/[^\d]/g, '');

  if (value === '') {
    priceInput.value = '';
    product.value.price = 0;
    return;
  }

  let numericValue = parseFloat(value) / 100;

  priceInput.value = numericValue.toLocaleString('pt-BR', {
    style: 'currency',
    currency: 'BRL',
    minimumFractionDigits: 2
  });

  product.value.price = numericValue;
};

onMounted(() => {
  loadBrands();
  loadCategories();
  fetchProduct();
});
</script>

<template>
  <div class="bg-light min-vh-100 py-5">
    <div class="container">
      <div v-if="showAlert" :class="`alert alert-${alertType} alert-dismissible fade show`" role="alert">
        <i v-if="alertType === 'success'" class="bi bi-check-circle-fill me-2"></i>
        <i v-if="alertType === 'warning'" class="bi bi-exclamation-triangle-fill me-2"></i>
        <i v-if="alertType === 'danger'" class="bi bi-x-circle-fill me-2"></i>
        {{ alertMessage }}
        <button type="button" class="btn-close" @click="showAlert = false"></button>
      </div>

      <button type="button" class="btn pb-3 back-button" @click="router.push('/myProfile')">
        ← Voltar para os produtos
      </button>

      <div class="row g-4">
        <div class="col-12 col-lg-5">
          <div class="card border-0 shadow-sm">
            <div class="card-body p-4">
              <h5 class="card-title mb-4">Imagem do Produto</h5>
              <div class="image-preview rounded bg-light mb-3 d-flex align-items-center justify-content-center"
                :style="{ backgroundImage: product.image ? `url(${product.image})` : 'none' }">
                <div v-if="!product.image" class="text-center text-secondary">
                  <i class="bi bi-image fs-1"></i>
                  <p class="mt-2 mb-0">Pré-visualização</p>
                </div>
                <img v-else :src="product.image" alt="Product preview" class="img-fluid rounded" />
              </div>
              <div class="form-floating">
                <input v-model="product.image" type="text" class="form-control" id="imageUrl"
                  placeholder="Enter image URL" />
                <label for="imageUrl">URL da Imagem</label>
              </div>
            </div>
          </div>
        </div>

        <div class="col-12 col-lg-7">
          <div class="card border-0 shadow-sm">
            <div class="card-body p-4">
              <h5 class="card-title mb-4">Editar Produto</h5>
              <form @submit.prevent="submit">
                <div class="form-floating mb-3">
                <input v-model="product.name" type="text" class="form-control" id="productName" placeholder="Digite o nome do produto" required />
                <label for="productName">Nome do Produto</label>
              </div>

                <div class="form-floating mb-3">
                  <textarea v-model="product.description" class="form-control" id="description"
                    placeholder="Digite a descrição do produto" style="height: 100px" required></textarea>
                  <label for="description">Descrição</label>
                </div>

                <div class="row g-3 mb-3">
                  <div class="col-md-6">
                    <div class="form-floating">
                      <input v-model="product.price" type="text" class="form-control" placeholder="R$ 0,00" required
                        @input="formatPrice" />
                      <label>Preço</label>
                    </div>
                  </div>

                  <div class="col-md-6">
                    <div class="form-floating">
                      <select v-model="product.type" class="form-control">
                        <option value="">Selecione o tipo</option>
                        <option value="Produto">Produto</option>
                        <option value="Serviço">Serviço</option>
                      </select>
                      <label>Tipo</label>
                    </div>
                  </div>
                </div>

                <div class="mb-4">
                  <div class="d-flex gap-2">
                    <div class="form-floating flex-grow-1">
                      <select v-model="product.brandModel" class="form-control">
                        <option value="">Selecione a marca</option>
                        <option v-for="brand in brands" :key="brand.id" :value="brand.id">
                          {{ brand.name }}
                        </option>
                      </select>
                      <label>Marca</label>
                    </div>

                    <RouterLink to="/brand" class="d-flex align-items-center">
                      <CustomButton>
                        <i class="bi bi-plus-lg"></i>
                      </CustomButton>
                    </RouterLink>

                    <div class="form-floating flex-grow-1">
                      <select v-model="product.categoryModel" class="form-control">
                        <option value="">Selecione a categoria</option>
                        <option v-for="category in categories" :key="category.id" :value="category.id">
                          {{ category.name }}
                        </option>
                      </select>
                      <label>Categoria</label>
                    </div>
                    
                    
                    <RouterLink to="/category" class="d-flex align-items-center">
                      <CustomButton>
                        <i class="bi bi-plus-lg"></i>
                      </CustomButton>
                    </RouterLink>
                  </div>
                </div>

                <CustomButton class="button container-fluid py-3">
                  Salvar Alterações
                </CustomButton>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.image-preview {
  height: 300px;
  background-size: cover;
  background-position: center;
  overflow: hidden;
}

.image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.form-control:focus,
.form-select:focus {
  border-color: #86b7fe;
  box-shadow: 0 0 0 0.25rem rgba(13, 110, 253, 0.25);
}

.back-button {
  background: none;
  border: none;
  color: #666;
  font-size: 1.1rem;
  padding: 0;
  transition: color 0.3s ease;
}

.btn {
  transition: all 0.2s ease-in-out;
}

.btn:hover {
  transform: translateY(-1px);
}

@media (max-width: 991.98px) {
  .image-preview {
    height: 250px;
  }
}
</style>

<style scoped>
.card {
  margin-bottom: 20px;
}

.card-title {
  font-size: 1.25rem;
  font-weight: bold;
}

img {
  max-height: 200px;
  object-fit: cover;
  border-radius: 5px;
}

button:hover {
  transform: scale(1.01);
}
.custom-alert {
    display: flex;
    align-items: center;
    gap: 10px;
    animation: fadeIn 0.5s;
    margin-bottom: 20px;
    font-size: 1rem;
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
