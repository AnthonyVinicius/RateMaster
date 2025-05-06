<script setup>
import ReviewHomePage from '@/components/ReviewHomePage.vue';
import UserReview from '@/components/UserReview.vue';
</script>

<template>
    <div class="container-fluid p-0">
        <img class="header-img shadow-sm" src="../assets/img/Home.png" alt="Picture">
    </div>

    <div class="container-fluid  white-comment-container">
        <div class="text-box text-center">
            <h1 class="display-4 text-color">Bem Vindo à RateMaster</h1>
            <p class="lead text-color pb-3">Descubra os melhores produtos e compartilhe suas opniões.</p>
        </div>

        <div class="container text-center p-3">
            <div class="row g-4">
                <div class="col">
                    <ReviewHomePage>
                        <template #title>
                            Soluções personalizadas desenvolvidas para atender às suas necessidades específicas.
                        </template>
                        <template #comment>
                            Oferecemos uma gama de produtos que simplificam a avaliação de desempenho.
                        </template>
                    </ReviewHomePage>
                </div>
                <div class="col">
                    <ReviewHomePage>
                        <template #title>
                            Nossos serviços garantem qualidade e eficiência em cada projeto.
                        </template>
                        <template #comment>
                            Estamos comprometidos em entregar resultados que superem suas expectativas.
                        </template>
                    </ReviewHomePage>
                </div>
                <div class="col">
                    <ReviewHomePage>
                        <template #title>
                            A tecnologia que transforma a maneira como você avalia produtos.
                        </template>
                        <template #comment>
                            Inovação e conveniência são os pilares das nossas soluções.
                        </template>
                    </ReviewHomePage>
                </div>
            </div>
        </div>
    </div>


    <div class="container-fluid colored-comment-container">
        <div class="text-box text-center p-5">
            <h1 class="display-4 text-white text-start pt-5">Avalie com confiança.</h1>
            <p class="lead text-white text-start pb-5">Descubra avaliações detalhadas e imparciais para ajudá-lo
                a tomar decisões de compra informadas.</p>
        </div>
    </div>

    <div class="container">
        <div class="container text-center white-comment-container">
            <div class="row g-3 pt-5 ">
                <div class="col">
                    <UserReview>
                        <template #comment>
                            Eu simplesmente amei este produto! Qualidade é excepcional e superou minhas expectativas.
                            Vou recomendá-lo aos meus amigos e familiares.
                        </template>
                        <template #src>
                            <img class="review-logo rounded-pill" src="/user1.jpeg" alt="review-logo">
                        </template>
                        <template #user>
                            Carol
                        </template>
                    </UserReview>
                </div>
                <div class="col">
                    <UserReview>
                        <template #comment>
                            O produto é razoável pelo preço, mas não atendeu totalmente às minhas expectativas.
                            Funciona, mas há espaço para melhorias.
                        </template>
                        <template #src>
                            <img class="review-logo rounded-pill" src="/user2.jpeg" alt="review-logo">
                        </template>
                        <template #user>
                            Maria
                        </template>
                    </UserReview>
                </div>
                <div class="col">
                    <UserReview>
                        <template #comment>
                            O atendimento ao cliente foi fantástico! Foram rápidos em responder, amigáveis e
                            resolveram meu problema em pouco tempo. Experiência A+!
                        </template>
                        <template #src>
                            <img class="review-logo rounded-pill" src="/user3.jpeg" alt="review-logo">
                        </template>
                        <template #user>
                            Carlos
                        </template>
                    </UserReview>
                </div>
            </div>
        </div>
    </div>
    <div class="container-fluid colored-comment-container">
        <div class="row">
            <div class="col p-5 d-flex justify-content-between align-items-center">
                <div>
                    <h1 class="display-4 text-white text-start">Entre em contato conosco.</h1>
                    <p class="lead text-white text-start">Descubra como nossa plataforma pode ajudá-lo.</p>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.colored-comment-container {
    padding-top: 100px;
    padding-bottom: 100px;
    background: linear-gradient(135deg, #1E2A38, #34495E);
}

.white-comment-container {
    padding-top: 100px;
    padding-bottom: 100px;
}

.button {
    width: 100%;
}

button:hover {
    transform: scale(1.01);
}

.header-img {
    width: 100%;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.review-logo {
    width: 100px;

}
</style>
