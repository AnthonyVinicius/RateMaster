<template>
  <div class="container-fluid d-flex flex-column align-items-center box rounded-4 mb-5 shadow-lg">
    <p class="lead text-white pt-4 text-resize">
      <slot name="comment">

      </slot>
    </p>
    <slot name="src">
      
    </slot>
    <p class="lead text-white">
      <slot name="user">

      </slot >
    </p>
  </div>
</template>

<style scoped>
.box {
  background: linear-gradient(135deg, #1E2A38, #34495E);
  min-height: 350px; 
  overflow: hidden;
  text-align: center;
  }
  img{
    width: 100px;
    border-radius: 70px;
  }
</style>
