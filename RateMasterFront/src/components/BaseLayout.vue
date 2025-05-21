<script setup>
import TheNavbar from './TheNavbar.vue';
import TheFooter from './TheFooter.vue';
import { configure, defineRule } from 'vee-validate';
import { required, max, between, min } from '@vee-validate/rules';

defineRule('required', required);
defineRule('max', max);
defineRule('min', min);
defineRule('between', between);
defineRule('email', value =>{
  const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
  return emailRegex.test(value) || 'O campo deve ser um email válido.';
})

configure({
  generateMessage: (ctx) => {
    const messages = {
      required: `Este campo é obrigatório.`,
      max: `Este campo deve ter no máximo ${ctx.rule.params[0]} caracteres`,
      min: `Este campo deve ter no mínimo ${ctx.rule.params[0]} caracteres`,
      email: `O campo ${ctx.field} deve ser um email válido`
    };
    return messages[ctx.rule.name || `O campo ${ctx.field} é inválido`]
  }
});

</script>

<template>
    <div class="page-container">
        <TheNavbar />
        <main class="content">
            <slot></slot>
        </main>
        <TheFooter />
    </div>
</template>

<style scoped></style>
