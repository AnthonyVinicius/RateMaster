import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/HomePageView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', name: 'homepage', component: Home },
    { path: '/login', name: 'login', component: () => import('../views/LoginView.vue') },
    { path: '/registerUser', name: 'registerUser', component: () => import('../views/RegisterUserView.vue') },
    { path: '/reviews', name: 'reviews', component: () => import('../views/ProductReviewsView.vue') },
    { path: '/registerProduct', name: 'registerProduct', component: () => import('../views/RegisterProductView.vue') },
    { path: '/brand', name: 'brands', component: () => import('../views/BrandView.vue') },
    { path: '/updateProducts/:id', name: 'updateProducts', component: () => import('../views/UpdateProductsView.vue') },
    { path: '/product/:id', name: 'productDetail', component: () => import('../views/ProductDetailsView.vue') },
    { path: '/myProfile', name: 'myProfile', component: () => import('../views/MyProfileView.vue')},
  ],
})

export default router
