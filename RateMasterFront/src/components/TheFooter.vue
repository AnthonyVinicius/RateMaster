<template>
    <footer class="container-fluid pt-5 pb-5 footer-container">
        <div class="d-flex flex-column align-items-center text-white gap-4">
            <div class="d-flex flex-row gap-2 ">
                <img class="logo" src="/src/assets/img/Icon.png" alt="Logo da Empresa">
                <h1 class="fw-bold">RateMaster</h1>
            </div>
            <div class="social-links d-flex flex-row gap-3">
                <a href="https://facebook.com" target="_blank">
                    <i class="fab fa-facebook"></i>
                </a>
                <a href="https://twitter.com" target="_blank">
                    <i class="fab fa-twitter"></i>
                </a>
                <a href="https://instagram.com" target="_blank">
                    <i class="fab fa-instagram"></i>
                </a>
            </div>
            <p class="text-lead">©{{ new Date().getFullYear() }} Ratemaster. Todos os direitos reservados.</p>
        </div>
    </footer>
</template>

<style scoped>
.footer-container {
    background: linear-gradient(135deg, #1E2A38, #34495E);
}

.logo {
    width: 50px;
}

.social-links {
    font-size: 1.5rem;
}

.social-links a {
    color: #FFF;
    transition: transform 0.3s ease, color 0.3s ease;
}

.social-links a:hover {
    transform: scale(1.2);
    color: #1AA7BD;
}
</style>
