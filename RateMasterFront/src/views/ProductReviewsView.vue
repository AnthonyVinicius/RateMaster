<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import GenericDAO from '@/services/GenericDAO';

const daoProducts = new GenericDAO('product');
const daoReviews = new GenericDAO('review');
const daoShops = new GenericDAO('shop');
const router = useRouter();

const products = ref([]);
const filters = ref({
    price: [],
    rating: []
});
const reviews = ref([]);
const companies = ref([]);
const searchQuery = ref('');

const fetchProducts = async () => {
    try {
        products.value = await daoProducts.getAll();
        companies.value = await daoShops.getAll();
        reviews.value = await daoReviews.getAll();

        products.value.forEach(product => {
            const productReviews = product.reviewModels || [];

            const totalRating = productReviews.reduce((sum, review) => sum + review.rating, 0);

            if (productReviews.length > 0) {
                product.averageRating = (totalRating / productReviews.length).toFixed(1);
            } else {
                product.averageRating = 0;
            }

            const company = companies.value.find(company => company.id === product.shopModel.id);
            product.companyName = company ? company.name : 'Empresa desconhecida';

            if (productReviews.length > 0) {
                product.averageRating = (totalRating / productReviews.length).toFixed(1);
            } else {
                product.averageRating = 0;
            }
        });

    } catch (error) {
        console.error('Erro ao carregar produtos:', error);
    }
};

const filterProducts = computed(() => {
    return products.value.filter(product => {
        const matchSearch = !searchQuery.value ||
            product.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
            product.companyName.toLowerCase().includes(searchQuery.value.toLowerCase());

        const price = product.price;

        const equalPrice = filters.value.price.length === 0 || filters.value.price.some(priceRange => {
            if (priceRange === "0-50") return price >= 0 && price <= 50;
            if (priceRange === "51-100") return price >= 51 && price <= 100;
            if (priceRange === "101+") return price >= 101;
            return false;
        });

        const equalRating = filters.value.rating.length === 0 || filters.value.rating.some(rating => {
            return parseFloat(rating) === Math.floor(product.averageRating);
        });

        return equalPrice && equalRating && matchSearch;
    });
});



const goToDetails = (productId) => {
    router.push({ name: 'productDetail', params: { id: productId } });
};

onMounted(() => {
    fetchProducts();
});
</script>

<template>
    <div class="container-fluid pe-5 ps-5">
        <div class="d-flex flex-column  mt-4 mb-4">
                <h1 class="header ms-auto me-auto align-items-center ">Descubra nossos Produtos</h1>
                <div class="form-floating search-bar container search-container">
                    <input type="search" v-model="searchQuery" class="form-control" id="floatingInput" placeholder="Procurar produto..." />
                    <label class="ms-3" for="floatingInput"><i class="bi bi-search"></i> Procurar produto...</label>
                </div>
        </div>

        <div class="row d-flex mt-5">
            <section class="col-md-2">
                <aside class="bg-white p-4 pb-5 rounded-3 shadow-sm">
                    <div class="hstack mb-4">
                        <h6 class="filter-text m-0 p-0">Filtros</h6>
                        <button class="btn ms-auto clear-filters rounded-pill" @click="filters = { price: [], rating: [] }">
                            Clear All
                        </button>
                    </div>
                    <h6 class="filter-text">Preço</h6>
                    <div class="vstack mb-3 filter-section checkbox">
                        <label><input type="checkbox" v-model="filters.price" value="0-50"> R$0 - R$50</label>
                        <label><input type="checkbox" v-model="filters.price" value="51-100"> R$51 - R$100</label>
                        <label><input type="checkbox" v-model="filters.price" value="101+"> R$101+</label>
                    </div>

                    <div class="filter-section">
                        <div class="vstack">
                            <h3 class="filter-text">Nota</h3>
                            <label class="mb-2" v-for="n in 5" :key="n">
                                <input class="checkbox" type="checkbox" v-model="filters.rating" :value="6 - n">
                                <span class="star">
                                    <i class="bi bi-star-fill ms-1" v-for="s in (6 - n)" :key="s"></i>
                                    <i class="bi bi-star ms-1" v-for="s in (n - 1)" :key="s + 'empty'"></i>
                                </span>
                            </label>
                        </div>
                    </div>
                </aside>
            </section>

            <div class="mb-5 col-md-10">
                <div class="row row-cols-1 row-cols-md-5 g-4">
                    <div class="col" v-for="product in filterProducts" :key="product.id"
                        @click="goToDetails(product.id)">
                        <div class="card rounded-3 text-truncate">
                            <div class="d-flex justify-content-center align-items-center img-container">
                                <img class="img-fluid  product-img" :src="product.image" :alt="product.name">
                            </div>

                            <div class="card-body">
                                <div class="vstack gap-3">
                                    <div class="hstack">
                                        <h6 class="fw-bold text-truncate m-0">{{ product.name }}</h6>
                                        <div class="ms-auto me-2 star">
                                            <span class="star"><i class="bi bi-star-fill"></i></span>
                                            {{ product.averageRating }}
                                        </div>
                                    </div>
                                    <p class="card-text text-truncate">{{ product.description }}</p>
                                    <div class="hstack d-flex align-items-center">
                                        <p class="price m-0 text-truncate">R$ {{ product.price }}</p>
                                        <p class="card-text ms-auto   text-truncate"><strong></strong> {{
                                            product.companyName }}</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>


<style scoped>
.filter-section label {
    cursor: pointer;
}

.checkbox {
    color: #666;
}

.product-img {
    position: relative;
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.header {
    font-size: 2.5rem;
    color: #1a1a1a;
    font-weight: 700;
}

.search-container {
    max-width: 700px;
}

.search-container input {
    width: 100%;
    padding: 1rem 1rem 1rem 3rem;
    border: none;
    border-radius: 50px;
    background-color: white;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    font-size: 1rem;
    transition: all 0.3s ease;
}


.img-container {
    width: 100%;
    height: 150px;
}

.star {
    color: #ffd700;
    font-weight: 600;
}

.card {
    transition: transform 0.3s, box-shadow 0.3s;
    border: none;
    box-shadow: 0 5px 10px rgba(0, 0, 0, 0.2);
}
.clear-filters {
    color: #666;
    font-size: 0.9rem;
    cursor: pointer;
    transition: all 0.3s ease;
}

.clear-filters:hover {
    background-color: #f0f0f0;
}

.card:hover {
    transform: translateY(-10px);
    box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
}

.card-text {
    font-size: 0.9rem;
    color: #666;
}

.filter-text {
    font-size: 1rem;
    color: #1a1a1a;
    margin-bottom: 1rem;
    font-weight: 600;
}

.price {
    font-size: 1.2rem;
    font-weight: 700;
    color: #2ecc71;
}
</style>
