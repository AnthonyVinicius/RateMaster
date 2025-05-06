`<template>
  <div class=" d-flex justify-content-center bg-light">
    <div class="text-center mt-5">
      <div class="mb-4 me-3"><i class="bi bi-slash-circle text-danger block"></i></div>
      
      <div class="bg-white p-5 rounded-3 shadow-lg">
        <h1 class="display-6 fw-bold mb-3">Acesso Negado</h1>
        <p class="text-muted mb-4">Você não tem permissão para acessar esta página.</p>
        
        <a href="/" class="btn btn-danger btn-lg px-4">
          <i class="bi bi-house-door me-2"></i>
          Voltar para a página inicial
        </a>
      </div>
    </div>
  </div>
</template>

<style scoped>
.block{
  font-size: 56pt;
}

.btn-danger {
  transition: all 0.3s ease;
}

.btn-danger:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(220, 53, 69, 0.2);
}



</style>