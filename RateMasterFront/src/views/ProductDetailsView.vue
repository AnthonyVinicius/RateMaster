<script setup>
import { ref, onMounted, computed } from "vue";
import { useRouter, useRoute } from "vue-router";
import CustomButton from "@/components/CustomButton.vue";
import { Form, Field, ErrorMessage } from "vee-validate";
import GenericDAO from "@/services/GenericDAO";
import { authState } from "@/services/AuthService";

const router = useRouter();
const route = useRoute();

const averageRating = ref(0);

const daoProducts = new GenericDAO("product");
const daoReviews = new GenericDAO("review");
const daoResponse = new GenericDAO("response");

const userID = computed(() => authState.userId);
const userName = computed(() => authState.userName);
const userType = computed(() => authState.userRole);

const userData = ref(null);
const product = ref(null);
const reviews = ref([]);
const newReview = ref({ rating: "", comment: "" });
const responseStates = ref({});

const alertMessage = ref(null);
const alertType = ref("success");
const showAlert = ref(false);

const formatDate = (date) => {
  return new Date(date).toLocaleString("pt-BR", {
    day: "2-digit",
    month: "2-digit",
    year: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
  });
};

const triggerAlert = (message, type = "success") => {
  alertMessage.value = message;
  alertType.value = type;
  showAlert.value = true;
  setTimeout(() => (showAlert.value = false), 3000);
};

const fetchProductDetails = async () => {
  try {
    const productId = route.params.id;
    product.value = await daoProducts.getById(productId);
    userData.value = product.value.userModel.id;

    const allReviews = await daoReviews.getAll();
    const allResponses = await daoResponse.getAll(); // <-- carrega todas as respostas
    

    const reviewIdsFromProduct = product.value.reviewModels.map(
      (review) => review.id
    );

    // Filtra os reviews do produto
    reviews.value = allReviews.filter((review) =>
      reviewIdsFromProduct.includes(review.id)
    );

    // Agrupa respostas por reviewId
    const groupedResponses = {};
    allResponses.forEach((resp) => {
      if (!groupedResponses[resp.reviewId]) {
        groupedResponses[resp.reviewId] = [];
      }
      groupedResponses[resp.reviewId].push(resp);
    });

    // Atribui as respostas a cada review
    reviews.value.forEach((review) => {
      review.responses = groupedResponses[review.id] || [];
      responseStates.value[review.id] = { comment: "" };
    });

    // Média das avaliações
    if (reviews.value.length > 0) {
      averageRating.value = (
        reviews.value.reduce((sum, review) => sum + review.rating, 0) /
        reviews.value.length
      ).toFixed(1);
    } else {
      averageRating.value = 0;
    }
  } catch (error) {
    console.error("Erro ao carregar os detalhes do produto:", error);
  }
};

const submitReview = async () => {
  const review = {
    productId: product.value.id,
    userId: userID.value,
    name: userName.value,
    rating: Number(newReview.value.rating),
    comment: newReview.value.comment,
    createdAt: new Date().toISOString(),
  };

  try {
    await daoReviews.insert(review);
    reviews.value.unshift(review);

    averageRating.value = (
      reviews.value.reduce((sum, review) => sum + review.rating, 0) /
      reviews.value.length
    ).toFixed(1);

    newReview.value.comment = "";
    newReview.value.rating = "";

    triggerAlert("Avaliação enviada com sucesso!", "success");
  } catch (error) {
    console.log(userData.value);
    console.error("Erro ao enviar avaliação:", error);
    triggerAlert(
      "Ocorreu um erro ao enviar sua avaliação. Tente novamente.",
      "danger"
    );
  }
};

const submitResponse = async (review, values, { resetForm }) => {
  try {
    const response = {
      userId: userID.value,
      name: userName.value,
      comment: responseStates.value[review.id]?.comment || "",
      createdAt: new Date().toISOString(),
      reviewId: review.id
    };

    console.log("Enviando resposta:", response);
    await daoResponse.insert(response);
    console.log("Resposta enviada com sucesso");
    if (!review.responses) review.responses = [];
    review.responses.push(response);
    responseStates.value[review.id].comment = "";
    resetForm();
  } catch (error) {
    console.error("Erro ao enviar resposta:", error);
  }
};

onMounted(() => {
  fetchProductDetails();

});
</script>

<template>
  <div
    v-if="showAlert"
    :class="`alert alert-${alertType} alert-dismissible fade show custom-alert fixed-bottom m-3`"
    role="alert"
  >
    <i v-if="alertType === 'success'" class="bi bi-check-circle-fill"></i>
    <i
      v-if="alertType === 'warning'"
      class="bi bi-exclamation-triangle-fill"
    ></i>
    <i v-if="alertType === 'danger'" class="bi bi-x-circle-fill"></i>
    {{ alertMessage }}
    <button type="button" class="btn-close" @click="showAlert = false"></button>
  </div>
  <div class="container pt-5">
    <button
      type="button"
      class="btn pb-3 back-button"
      @click="router.push('/reviews')"
    >
      ← Voltar para os produtos
    </button>

    <div class="card p-0 rounded-4 shadow-sm" v-if="product">
      <div class="row g-0">
        <div
          class="col bg-light d-flex justify-content-center align-items-center p-5 rounded-start-3"
        >
          <img
            :src="product.image"
            class="img-fluid rounded-4"
            :alt="product.name"
            style="max-width: 400px"
          />
        </div>

        <div class="col bg-white rounded-end-4 p-4">
          <div class="d-flex flex-column gap-3">
            <div class="d-flex align-items-center">
              <h1 class="h2 text-truncate mb-0">{{ product.name }}</h1>
              <div class="ms-auto me-3">
                <div
                  class="badge bg-warning bg-opacity-25 text-warning p-2 rounded-4"
                >
                  <i class="bi bi-star-fill me-1"></i>
                  <span>{{ averageRating }}</span>
                </div>
              </div>
            </div>
            <p class="text-secondary text-truncate mb-0">
              {{ product.description }}
            </p>
            <p class="text-secondary text-truncate mb-0">
              <i class="bi bi-shop me-1"></i
              >{{ product.userModel?.name || "Empresa desconhecida" }}
            </p>
            <p class="fs-4 fw-bold text-success mb-0">R$ {{ product.price }}</p>
          </div>
        </div>
      </div>
    </div>

    <div v-if="userType == 'individual'" class="mt-5">
      <Form @submit="submitReview" class="card p-5 shadow-sm">
        <h3 class="fw-bold mb-3">Deixe sua avaliação</h3>
        <div class="mb-3">
          <label class="form-label" for="rating">Sua nota:</label>
          <div class="mb-4 star-rating">
            <input
              type="radio"
              id="sr-5"
              name="star-rating"
              value="5"
              v-model="newReview.rating"
            />
            <label for="sr-5"><i class="bi bi-star-fill"></i></label>
            <input
              type="radio"
              id="sr-4"
              name="star-rating"
              value="4"
              v-model="newReview.rating"
            />
            <label for="sr-4"><i class="bi bi-star-fill"></i></label>
            <input
              type="radio"
              id="sr-3"
              name="star-rating"
              value="3"
              v-model="newReview.rating"
            />
            <label for="sr-3"><i class="bi bi-star-fill"></i></label>
            <input
              type="radio"
              id="sr-2"
              name="star-rating"
              value="2"
              v-model="newReview.rating"
            />
            <label for="sr-2"><i class="bi bi-star-fill"></i></label>
            <input
              type="radio"
              id="sr-1"
              name="star-rating"
              value="1"
              v-model="newReview.rating"
            />
            <label for="sr-1"><i class="bi bi-star-fill"></i></label>
          </div>
        </div>

        <div class="mb-3">
          <label class="form-label" for="comment">Seu comentário:</label>
          <Field
            v-model="newReview.comment"
            name="comment"
            id="comment"
            rules="required|min:5|max:200"
            v-slot="{ field, errors, meta }"
          >
            <textarea
              v-bind="field"
              :class="{
                'form-control': true,
                'is-valid': meta.touched && !errors.length,
                'is-invalid': meta.touched && errors.length,
              }"
              rows="4"
              placeholder="Compartilhe sua experiência com o produto..."
            ></textarea>
          </Field>
          <ErrorMessage name="comment" class="invalid-feedback" />
        </div>
        <div>
          <CustomButton class="btn btn-primary shadow-sm"
            >Enviar Avaliação</CustomButton
          >
        </div>
      </Form>
    </div>

    <div class="mt-5 mb-5">
      <h2 class="mb-4 fw-bold">Avaliações dos Clientes</h2>
      <div v-if="reviews.length === 0" class="mb-5">
        Ainda não há avaliações para este produto.
      </div>
      <div v-else class="row g-4">
        <div v-for="review in reviews" :key="review.id" class="col-12">
          <div class="card shadow-sm p-4">
            <div class="d-flex flex-column gap-4">
              <div class="d-flex flex-column gap-2">
                <div class="d-flex align-items-center gap-1">
                  <div class="text-warning">
                    <i
                      class="bi bi-star-fill"
                      v-for="n in review.rating"
                      :key="n"
                    ></i>
                  </div>

                  <div class="ms-auto text-secondary">{{ review.name }}</div>
                </div>
                <div class="text-muted small">
                  <i class="bi bi-clock me-1"></i>
                  {{
                    review.createdAt
                      ? formatDate(review.createdAt)
                      : "Data não disponível"
                  }}
                </div>
              </div>
              <p class="mb-0">{{ review.comment }}</p>

              <div
                v-if="review.responses && review.responses.length > 0"
                class="bg-light rounded-3 p-4 mt-3"
              >
                <h5 class="fw-bold mb-4">Respostas da Loja</h5>
                <div
                  v-for="(response, index) in review.responses"
                  :key="index"
                  class="bg-white rounded-3 p-3 mb-3 shadow-sm"
                >
                  <div class="mb-3">
                    <div class="fw-semibold">
                      <i class="bi bi-shop me-2"></i>{{ response.name }}
                    </div>
                    <div class="text-muted small mt-1">
                      <i class="bi bi-clock me-1"></i>
                      {{
                        response.createdAt
                          ? formatDate(response.createdAt)
                          : "Data não disponível"
                      }}
                    </div>
                  </div>
                  <p class="mb-0 text-secondary">{{ response.comment }}</p>
                </div>
              </div>

              <div
                v-if="userType == 'business' && userData === userID"
                class="bg-light rounded-3 p-4 mt-3"
              >
                <Form
                  v-slot="{ resetForm }"
                  @submit="
                    (values) => submitResponse(review, values, { resetForm })
                  "
                >
                  <textarea
                    v-model="responseStates[review.id].comment"
                    class="form-control mb-3"
                    rows="4"
                    placeholder="Digite sua resposta como loja..."
                  ></textarea>

                  <button type="submit" class="btn btn-primary">
                    <i class="bi bi-reply me-2"></i>Responder como Loja
                  </button>
                </Form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
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

.star-rating {
  display: inline-flex;
  flex-direction: row-reverse;
  gap: 0.25rem;
}

.star-rating input {
  display: none;
}

.star-rating label {
  cursor: pointer;
  font-size: 1.5rem;
  color: #dee2e6;
  transition: color 0.2s ease;
}

.star-rating label:hover,
.star-rating label:hover ~ label,
.star-rating input:checked ~ label {
  color: #ffc107;
}
</style>
