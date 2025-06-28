<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import GenericDAO from '@/services/GenericDAO';
import ProductDAO from '@/services/ProductDAO';

const route = useRoute();
const router = useRouter();
const daoUser = new GenericDAO('user');
const daoProducts = new ProductDAO();

const user = ref(null);
const products = ref([]);
const loading = ref(true);

const currentPage = ref(1);
const pageSize = 30;

const totalPages = computed(() => Math.ceil(products.value.length / pageSize));
const paginatedProducts = computed(() => {
  const start = (currentPage.value - 1) * pageSize;
  return products.value.slice(start, start + pageSize);
});

const fetchData = async () => {
    try {
        user.value = await daoUser.getById(route.params.id);
        const allProducts = await daoProducts.getAll();
        products.value = allProducts.filter(p => p.userModel?.id === route.params.id);
    } catch (error) {
        console.error('Erro ao carregar loja:', error);
    } finally {
        loading.value = false;
    }
};

const goToDetails = (productId) => {
    router.push({ name: 'productDetail', params: { id: productId } });
};

const goToPage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page;
    window.scrollTo(0, 0); // opcional: volta ao topo da página ao trocar
  }
};

onMounted(() => {
    fetchData();
});
</script>

<template>
    <div>
        <div v-if="loading" class="text-center p-5">
            Carregando loja...
        </div>

        <div v-else-if="user" :style="{ backgroundColor: user.backGroundColor || '#000000' }"
            class="text-white d-flex flex-row profile-background position-relative">
            <div class="ms-5 mt-5 d-flex flex-column profile-logo-container">
                <img :src="user.image || 'https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-profiles/avatar-1.webp'"
                    alt="Logo da loja" class="img-fluid img-thumbnail mb-2 rounded-pill profile-logo" />
            </div>

            <div class="ms-4 user-info d-flex">
                <div class="d-flex flex-column justify-content-center">
                    <h1 class="profile-text">{{ user.name }}</h1>
                    <p class="email-text">{{ user.email }}</p>
                    <p v-if="user.type" class="email-text">Tipo: {{ user.type }}</p>
                </div>
            </div>
        </div>

        <div v-else class="p-3">
            <p>Loja não encontrada.</p>
        </div>

        <div class="container mt-5">
            <button type="button" class="btn pb-3 back-button" @click="router.push('/reviews')">
                ← Voltar para os produtos
            </button>
            <h3>Produtos da loja</h3>

            <div v-if="products.length === 0" class="text-muted">
                Nenhum produto encontrado para esta loja.
            </div>

            <div class="row row-cols-1 row-cols-md-6 g-4 mt-3 mb-3">
                <div v-for="product in paginatedProducts" :key="product.id" class="product-card"
                    @click="goToDetails(product.id)" role="button" tabindex="0" @keyup.enter="goToDetails(product.id)">
                    <div class="card rounded-3 h-100 text-truncate cursor-pointer">
                        <div class="d-flex justify-content-center align-items-center img-container">
                            <img class="img-fluid product-img" :src="product.image" :alt="product.name" />
                        </div>
                        <div class="card-body">
                            <h5 class="fw-bold text-truncate">{{ product.name }}</h5>
                            <p class="card-text text-truncate">{{ product.description }}</p>
                            <p class="price">R$ {{ product.price }}</p>
                            <p class="card-text text-truncate">Marca: {{ product.brandModel?.name || 'Sem Marca' }}</p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Paginação -->
            <nav v-if="totalPages > 1" aria-label="Paginação">
              <ul class="pagination justify-content-center">
                <li class="page-item" :class="{ disabled: currentPage === 1 }">
                  <button class="page-link" @click="goToPage(currentPage - 1)" aria-label="Página anterior">
                    &laquo;
                  </button>
                </li>

                <li v-for="page in totalPages" :key="page" class="page-item" :class="{ active: page === currentPage }">
                  <button class="page-link" @click="goToPage(page)">{{ page }}</button>
                </li>

                <li class="page-item" :class="{ disabled: currentPage === totalPages }">
                  <button class="page-link" @click="goToPage(currentPage + 1)" aria-label="Próxima página">
                    &raquo;
                  </button>
                </li>
              </ul>
            </nav>
        </div>
    </div>
</template>

<style scoped>
.profile-text {
    font-size: 2.5rem;
    font-weight: 700;
}

.profile-background {
    height: 200px;
}

.profile-logo-container {
    position: relative;
    width: 150px;
}

.profile-logo {
    width: 150px;
    height: 150px;
    border-radius: 50%;
    object-fit: cover;
}

.user-info {
    margin-left: 2rem;
}

.email-text {
    font-size: 1rem;
    opacity: 0.8;
}

.product-img {
    width: 100%;
    height: 150px;
    object-fit: contain;
}

.product-card {
    cursor: pointer;
}

.price {
    font-weight: 600;
    color: #28a745;
    font-size: 1.1rem;
}

.img-container {
    width: 100%;
    height: 150px;
    overflow: hidden;
}

.back-button {
  background: none;
  border: none;
  color: #666;
  font-size: 1.1rem;
  padding: 0;
  transition: color 0.3s ease;
}

.back-button:hover {
  color: #333;
}
</style>
