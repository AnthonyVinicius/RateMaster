<script setup>
import CustomButton from '@/components/CustomButton.vue';
import GenericDAO from '@/services/GenericDAO';
import { Form, Field, ErrorMessage } from 'vee-validate';
import { ref, onMounted } from 'vue';
import { useRouter } from "vue-router";
import { authState } from '@/services/AuthService';
import { computed } from 'vue';

const router = useRouter();
const daoProducts = new GenericDAO('product');
const daoBrands = new GenericDAO('brand');
const daoCategories = new GenericDAO('category');

const idUser = computed(() => authState.userId);


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

const priceInput = ref('');

const brands = ref([]);
const categories = ref([]);

const alertMessage = ref(null);
const alertType = ref('success');
const showAlert = ref(false);

const triggerAlert = (message, type = 'success') => {
  alertMessage.value = message;
  alertType.value = type;
  showAlert.value = true;
  setTimeout(() => (showAlert.value = false), 3000);
};

const loadBrands = async () => {
  brands.value = await daoBrands.getAll();
};

const loadCategories = async () => {
  categories.value = await daoCategories.getAll();
};

onMounted(() => {
  if (!authState.isLogged){
    router.push('/login');
    return;
  }

  loadBrands();
  loadCategories();
});

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

  await daoProducts.insert(productData);
  triggerAlert('Produto registrado com sucesso!', 'success');

  product.value = {
    name: '',
    description: '',
    price: 0,
    brandModel: '',
    shopModel: '',
    categoryModel: '',
    image: '',
    type: ''
  };
  priceInput.value = '';
};
</script>


<template>
  <div class="bg-light min-vh-100 py-5">
    <div class="container">
      <div v-if="showAlert" :class="`alert alert-${alertType} alert-dismissible fade show custom-alert`" role="alert">
        <i v-if="alertType === 'success'" class="bi bi-check-circle-fill"></i>
        <i v-if="alertType === 'warning'" class="bi bi-exclamation-triangle-fill"></i>
        <i v-if="alertType === 'danger'" class="bi bi-x-circle-fill"></i>
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
                <label for="imageUrl">Image URL</label>
              </div>
            </div>
          </div>
        </div>

        <div class="col-12 col-lg-7">
          <div class="card border-0 shadow-sm">
            <div class="card-body p-4">
              <h5 class="card-title mb-4">Registrar Produto</h5>
              <Form @submit="submit">
                <div class="form-floating mb-3">
                  <Field id="productName" name="productName" type="text" rules="required|min:5|max:100"
                    v-slot="{ field, errors, meta }">
                    <input v-model="product.name" v-bind="field" :class="{
                      'form-control': true,
                      'is-valid': meta.touched && !errors.length,
                      'is-invalid': meta.touched && errors.length
                    }" placeholder="Enter product name" />
                  </Field>
                  <ErrorMessage name="productName" class="errorMessage" />
                  <label for="productName">Nome do Produto</label>
                </div>

                <div class="form-floating mb-3">
                  <Field id="description" name="description" type="text" rules="required|min:5|max:300"
                    v-slot="{ field, errors, meta }">
                    <textarea v-model="product.description" v-bind="field" :class="{
                      'form-control': true,
                      'is-valid': meta.touched && !errors.length,
                      'is-invalid': meta.touched && errors.length
                    }" placeholder="Enter product description" style="height: 100px"></textarea>
                  </Field>
                  <ErrorMessage name="description" class="errorMessage" />
                  <label for="description">Descrição</label>
                </div>

                <div class="row g-3 mb-3">
                  <div class="col-md-6">
                    <input v-model="priceInput" type="text" class="form-control p-3" placeholder="R$ 0,00" required
                      @input="formatPrice" />
                  </div>

                  <div class="col-md-6">
                    <div class="form-floating">
                      <Field v-model="product.type" name="type" id="type" rules="required"
                        v-slot="{ field, errors, meta }">
                        <select v-bind="field" :class="{
                          'form-control': true,
                          'is-valid': meta.touched && !errors.length,
                          'is-invalid': meta.touched && errors.length
                        }">
                          <option value="">Selecione o tipo</option>
                          <option value="Product">Produto</option>
                          <option value="Service">Serviço</option>
                        </select>
                        <label for="type">Tipo</label>
                      </Field>
                      <ErrorMessage name="type" class="errorMessage" />
                    </div>
                  </div>
                </div>

                <div class="mb-4">
                  <div class="d-flex gap-2">
                    <div class="form-floating flex-grow-1">
                      <Field v-model="product.brandModel" name="brand" id="brand" rules="required"
                        v-slot="{ field, errors, meta }">
                        <select v-bind="field" :class="{
                          'form-control': true,
                          'is-valid': meta.touched && !errors.length,
                          'is-invalid': meta.touched && errors.length
                        }">
                          <option value="">Selecione a marca</option>
                          <option v-for="brand in brands" :key="brand.id" :value="brand.id">
                            {{ brand.name }}
                          </option>
                        </select>
                      </Field>
                      <ErrorMessage name="brand" class="errorMessage" />
                      <label for="brand">Marca</label>
                    </div>
                    <RouterLink to="/brand" class="d-flex align-items-center">
                      <CustomButton>
                        <i class="bi bi-plus-lg"></i>
                      </CustomButton>
                    </RouterLink>
                  </div>
                </div>

                <div class="mb-4">
                  <div class="d-flex gap-2">
                    <div class="form-floating flex-grow-1">
                      <Field v-model="product.categoryModel" name="category" id="category" rules="required"
                        v-slot="{ field, errors, meta }">
                        <select v-bind="field" :class="{
                          'form-control': true,
                          'is-valid': meta.touched && !errors.length,
                          'is-invalid': meta.touched && errors.length
                        }">
                          <option value="">Selecione a categoria</option>
                          <option v-for="category in categories" :key="category.id" :value="category.id">
                            {{ category.name }}
                          </option>
                        </select>
                      </Field>
                      <ErrorMessage name="category" class="errorMessage" />
                      <label for="category">Categoria</label>
                    </div>
                    <RouterLink to="/category" class="d-flex align-items-center">
                      <CustomButton>
                        <i class="bi bi-plus-lg"></i>
                      </CustomButton>
                    </RouterLink>
                  </div>
                </div>

                <CustomButton class="button container-fluid py-3">
                  Registrar Produto
                </CustomButton>
              </Form>
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