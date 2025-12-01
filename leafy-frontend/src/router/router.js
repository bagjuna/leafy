import { createRouter, createWebHistory } from 'vue-router'
import store from '@/store';

const routes = [
  {
    path: '/',
    name: 'HomePage',
    component: () => import('@/views/HomePage.vue'),
    meta: { },
  },
  {
    path: '/plants',
    name: 'PlantList',
    component: () => import('@/views/PlantList.vue'),
    meta: {  },
  },
  {
    path: '/plants/add',
    name: 'PlantAdd',
    component: () => import('@/components/modals/PlantAddModal.vue'),
    meta: {

    },
  },
  {
    path: '/plantlogs',
    name: 'PlantlogList',
    component: () => import('@/views/PlantlogList.vue'),
    meta: {  },
  },
  {
    path: '/setting',
    name: 'UserCard',
    component: () => import('@/views/UserCard.vue'),
    meta: { },
  },
  {
    path: '/edituser',
    name: 'UserEdit',
    component: () => import('@/views/UserEdit.vue'),
    meta: { },
  },
  {
    path: '/login',
    name: 'UserLogin',
    component: () => import('@/views/UserLogin.vue'),
  },
  {
    path: '/signup',
    name: 'UserSignUp',
    component: () => import('@/views/UserSignUp.vue'),
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})



export default router
