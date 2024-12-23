import { createRouter, createWebHistory } from 'vue-router'
import ConsultSubjectsView from '../pages/ConsultSubjects/ConsultSubjectsView.vue'
import DashboardView from '../pages/Dashboard/DashboardView.vue'
import FriendsView from '../pages/Friends/FriendsView.vue'
import HomeView from '../pages/Home/HomeView.vue'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/disciplinas',
      name: 'subjects',
      component: ConsultSubjectsView
    },
    {
      path: '/dashboard',
      name: 'dashboard',
      component: DashboardView
    },
    {
      path: '/amigos',
      name: 'friends',
      component: FriendsView
    }
  ]
})

export default router
