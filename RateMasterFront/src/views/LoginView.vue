<script setup>
import CustomButton from '@/components/CustomButton.vue';
import { ref } from "vue";
import AuthService from '@/services/AuthService';
import { useRouter } from "vue-router";
import GenericDAO from '@/services/GenericDAO';
import { Form, Field, ErrorMessage } from 'vee-validate';

const errMsg = ref("");
const router = useRouter();
const email = ref("");
const password = ref("");
const showPassword = ref(false);
const daoUser = new GenericDAO('user');
const user = ref({});


const handleLogin = async () => {
  errMsg.value = '';
  user.value = {
    email: email.value.trim(),
    password: password.value.trim(),
  };

  try {
    const response = await daoUser.login(user.value);
    const token = response.token || (response.data && response.data.token);

    if (token) {
      AuthService.login(token);
      router.push('/reviews');
    } else {
      errMsg.value = 'Token não recebido da API.';
    }
  } catch (error) {
    console.error(error);
    errMsg.value = 'Erro no login: ' + (error.response?.data?.message || error.message || 'Erro desconhecido');
  }
};
const toggleShowPassword = () => {
  showPassword.value = !showPassword.value;
};
</script>

<template>
  <div class="container py-5">
    <div class="row justify-content-center">
      <div class="col-12 col-lg-10">
        <div class="card border-0 shadow-sm overflow-hidden">
          <div class="row g-0">

            <div class="col-lg-6 d-none d-lg-block bg-light">
              <img src="../assets/img/Login.jpg" alt="login" class="w-100 h-100 object-fit-cover" />
            </div>


            <div class="col-lg-6">
              <div class="card-body p-4 p-lg-5">
                <div class="text-center mb-4">
                  <h2 class="h3 fw-bold mb-2">Bem-vindo de volta</h2>
                  <p class="text-muted">Faça login na sua conta</p>
                </div>

                <Form @submit="handleLogin">

                  <div class="mb-3">
                    <label class="form-label">Email</label>
                    <Field v-model="email" name="email" id="email" rules="required|email"
                      v-slot="{ field, errors, meta }">
                      <input v-bind="field" :class="{
                        'form-control': true,
                        'is-valid': meta.touched && !errors.length,
                        'is-invalid': meta.touched && errors.length
                      }" placeholder="seu@email.com" />
                    </Field>
                    <ErrorMessage name="email" class="errorMessage" />
                  </div>


                  <div class="mb-4">
                    <label class="form-label">Senha</label>
                    <div class="input-group">
                      <Field v-model="password" name="password" id="password" rules="required"
                        v-slot="{ field, errors, meta }">
                        <input v-bind="field" :class="{
                          'form-control': true,
                          'is-valid': meta.touched && !errors.length,
                          'is-invalid': meta.touched && errors.length
                        }" :type="showPassword ? 'text' : 'password'" placeholder="Digite sua senha" />
                      </Field>
                      <button class="btn btn-outline-secondary" type="button" @click="toggleShowPassword">
                        <i :class="showPassword ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
                      </button>
                    </div>
                    <ErrorMessage name="password" class="errorMessage" />
                  </div>


                  <div v-if="errMsg" class="alert alert-danger text-center mb-3">
                    {{ errMsg }}
                  </div>


                  <div class="d-grid mb-3">
                    <CustomButton class="button btn-lg">Entrar</CustomButton>
                  </div>

                  <p class="text-center mb-3">
                    Não possui uma conta?
                    <RouterLink to="/registerUser" class="text-decoration-none">Cadastre-se aqui</RouterLink>
                  </p>

                  <div class="text-center">
                    <a href="#" class="small text-muted me-2" data-bs-toggle="modal" data-bs-target="#termsModal">Termos
                      de uso</a>
                    <a href="#" class="small text-muted" data-bs-toggle="modal" data-bs-target="#privacyModal">Política
                      de Privacidade</a>
                  </div>
                </Form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="modal fade" id="privacyModal" tabindex="-1" aria-labelledby="privacyModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-dialog-scrollable modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="privacyModalLabel">Política de Privacidade</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Fechar"></button>
          </div>
          <div class="modal-body">
            <h6>1. Introdução</h6>
            <p>Esta Política de Privacidade descreve como coletamos, usamos, armazenamos e protegemos as informações dos
              usuários e empresas cadastradas no nosso site.</p>

            <h6>2. Coleta de Dados</h6>
            <p>Coletamos informações fornecidas voluntariamente pelo usuário, como nome, email, senha, bem como dados de
              empresas e produtos cadastrados. Também podemos coletar dados de navegação através de cookies para
              melhorar sua experiência no site.</p>

            <h6>3. Uso das Informações</h6>
            <p>Utilizamos os dados para:</p>
            <ul>
              <li>Realizar o cadastro e autenticação de usuários.</li>
              <li>Permitir o cadastro e gestão de produtos por empresas.</li>
              <li>Permitir avaliações pelos usuários cadastrados.</li>
              <li>Melhorar nossos serviços e personalizar sua experiência.</li>
            </ul>

            <h6>4. Compartilhamento de Dados</h6>
            <p>Não vendemos ou compartilhamos seus dados pessoais com terceiros, exceto quando necessário para
              cumprimento legal ou ordem judicial.</p>

            <h6>5. Armazenamento e Segurança</h6>
            <p>Seus dados são armazenados em servidores seguros, com medidas técnicas e administrativas para proteger
              contra acesso não autorizado, uso indevido ou divulgação.</p>

            <h6>6. Direitos do Usuário</h6>
            <p>Você pode solicitar a qualquer momento:</p>
            <ul>
              <li>Acesso aos seus dados pessoais.</li>
              <li>Correção de informações incorretas.</li>
              <li>Exclusão dos seus dados, observadas as obrigações legais.</li>
            </ul>

            <h6>7. Cookies</h6>
            <p>Utilizamos cookies para melhorar a navegação, autenticar usuários e coletar dados de uso do site. Você
              pode desativar os cookies nas configurações do seu navegador, mas isso pode afetar algumas
              funcionalidades.</p>

            <h6>8. Alterações nesta Política</h6>
            <p>Esta Política de Privacidade pode ser atualizada periodicamente. Recomendamos que você a consulte
              regularmente para se manter informado sobre como protegemos seus dados.</p>

            <h6>9. Contato</h6>
            <p>Em caso de dúvidas sobre esta política, entre em contato conosco pelo email de suporte disponível no
              site.</p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fechar</button>
          </div>
        </div>
      </div>
    </div>

    <div class="modal fade" id="termsModal" tabindex="-1" aria-labelledby="termsModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-dialog-scrollable modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="termsModalLabel">Termos de Uso</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Fechar"></button>
          </div>
          <div class="modal-body">
            <h6>1. Objetivo</h6>
            <p>Esta Política de Uso estabelece as regras para utilização do site, garantindo um ambiente seguro,
              transparente e respeitoso para empresas que cadastram seus produtos e usuários que realizam avaliações.
            </p>

            <h6>2. Aceitação</h6>
            <p>Ao utilizar este site, você concorda integralmente com esta Política de Uso. Caso não concorde, por
              favor, não utilize nossos serviços.</p>

            <h6>3. Cadastro de Empresas e Produtos</h6>
            <p>Empresas devem fornecer informações verídicas sobre seus produtos. É proibido o cadastro de produtos
              ilegais, falsificados ou que infrinjam direitos de terceiros. Produtos cadastrados passam por análise e
              podem ser removidos caso estejam em desacordo com esta política ou legislação vigente.</p>

            <h6>4. Avaliação de Produtos pelos Usuários</h6>
            <p>Somente usuários cadastrados podem realizar avaliações. As avaliações devem ser baseadas em experiências
              reais, sendo proibido conteúdo ofensivo, discriminatório, difamatório ou que viole leis e direitos de
              terceiros. O site se reserva o direito de remover avaliações que não estejam de acordo com esta política.
            </p>

            <h6>5. Uso Adequado do Site</h6>
            <p>É proibido qualquer uso que possa comprometer a segurança, integridade ou funcionamento do site. Não é
              permitido tentar acessar dados ou informações de outros usuários sem autorização. O uso do site deve
              sempre respeitar as leis vigentes.</p>

            <h6>6. Responsabilidades</h6>
            <p><strong>Empresas:</strong> Garantir a veracidade das informações de seus produtos.</p>
            <p><strong>Usuários:</strong> Realizar avaliações de forma ética, honesta e responsável.</p>
            <p><strong>Administração do Site:</strong> Monitorar, moderar e garantir o cumprimento desta política.</p>

            <h6>7. Consequências de Descumprimento</h6>
            <p>O não cumprimento desta Política de Uso poderá resultar em suspensão ou exclusão da conta, remoção de
              conteúdo ou produtos cadastrados, além de possíveis medidas legais, caso aplicável.</p>

            <h6>8. Alterações</h6>
            <p>Esta Política de Uso pode ser atualizada periodicamente. Recomenda-se que seja consultada regularmente
              para conhecimento de eventuais alterações.</p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fechar</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.card {
  border-radius: 1rem;
}

.object-fit-cover {
  object-fit: cover;
}

a {
  color: #0d6efd;
}

a:hover {
  color: #0a58ca;
}

.form-control {
  padding: 0.75rem 1rem;
}

.input-group .btn {
  padding-left: 1rem;
  padding-right: 1rem;
}

@media (max-width: 991.98px) {
  .card-body {
    padding: 2rem !important;
  }
}
</style>