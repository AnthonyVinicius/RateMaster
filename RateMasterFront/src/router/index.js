import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/HomePageView.vue'
import { userData, initAuth } from "@/services/AuthService";

initAuth();

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', name: 'homepage', component: Home },
    { path: '/login', name: 'login', component: () => import('../views/LoginView.vue') },
    { path: '/reviews', name: 'reviews', component: () => import('../views/ProductReviewsView.vue') },
    { path: '/registerUser', name: 'registerUser', component: () => import('../views/RegisterUserView.vue') },

    { path: '/registerProduct', name: 'registerProduct', meta: { requireAuth: true, requireShop: true }, component: () => import('../views/RegisterProductView.vue') },
    { path: '/brand', name: 'brands', meta: { requireAuth: true, requireShop: true }, component: () => import('../views/BrandView.vue') },
    { path: '/category', name: 'categories', meta: {requireAuth: true, requireShop: true}, component: () => import('../views/CategoryView.vue')},
    { path: '/updateProducts/:id', name: 'updateProducts', meta: { requireAuth: true, requireShop: true }, component: () => import('../views/UpdateProductsView.vue') },

    { path: '/product/:id', name: 'productDetail', meta: { requireAuth: true }, component: () => import('../views/ProductDetailsView.vue') },
    { path: '/myProfile', name: 'myProfile', meta: { requireAuth: true }, component: () => import('../views/MyProfileView.vue') },
    { path: '/unauthorized', name: 'Unauthorized', component: () => import('../views/UnauthorizedView.vue') },
  ],
});


router.beforeEach((to, from, next) => {
  const requiresAuth = to.matched.some(record => record.meta.requireAuth);
  const requiresShop = to.matched.some(record => record.meta.requireShop);

  if (requiresAuth) {
    if (userData.value) {
      if (!requiresShop || (requiresShop && userData.value.userType === "business")) {
        next();
      } else {
        next('/unauthorized');
      }
    } else {
      next("/login");
    }
  } else {
    next();
  }
});

export default router;
