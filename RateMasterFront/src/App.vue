<script setup>
import {RouterView} from 'vue-router'
import BaseLayout from '@/components/BaseLayout.vue';


</script>

<template>
  <BaseLayout>
  <div class="body">
    <link rel="shortcut icon" href="/RateMaster/src/assets/favicon.ico">
    <RouterView />
  </div>
  </BaseLayout>


</template>

