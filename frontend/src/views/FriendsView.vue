<script setup>
import NavBar from '@/components/NavBar.vue'
import ListItemFriend from '@/components/ListItemFriend.vue'
import { ref } from 'vue'
import BpButton from '@/components/BpButton.vue'
import BpPagination from '@/components/BpPagination.vue'
import InputSearch from '@/components/InputSearch.vue'

const allFriends = ref([
  {
    name: 'Danillo',
    period: '5',
    degree: 'História'
  },
  {
    name: 'Alisson',
    period: '7',
    degree: 'TI'
  },
  {
    name: 'CR7 Jales',
    period: '4',
    degree: 'TI'
  },
  {
    name: 'Iñaki Marlon Lourenço',
    period: '7',
    degree: 'Geografia'
  },
  {
    name: 'Ían Gabriel',
    period: '5',
    degree: 'TI'
  },
  {
    name: 'Parabéns',
    period: '5',
    degree: 'TI'
  },
  {
    name: 'Mary Peace',
    period: '4',
    degree: 'TI'
  },
  {
    name: 'Tiago',
    period: '6',
    degree: 'TI'
  },
  {
    name: 'Natálya',
    period: '4',
    degree: 'TI'
  },
  {
    name: 'Dudu',
    period: '2',
    degree: 'Ciência da Computação'
  },
  {
    name: 'NãoExiste',
    period: '-1',
    degree: 'Biblioteconomia'
  }
])

const renderedFriends = ref(allFriends.value)

function handlePaginate(indexFrom, indexTo) {
  renderedFriends.value = allFriends.value.slice(indexFrom, indexTo + 1)
}
</script>

<template>
  <div class="h-full w-full">
    <NavBar />

    <main class="h-full container mx-auto p-6 xl:max-w-7xl">
      <header class="border-b border-bp_neutral-700 pb-4">
        <h1 class="title-h1">amigos</h1>
      </header>
      <div class="container-search-friends my-6">
        <div class="flex items-center flex-wrap gap-6 justify-between">
          <InputSearch
            class="w-full lg:w-3/5"
            input-placeholder="Nome do amigo"
            input-id="friend-search"
          />
          <div class="flex items-center gap-8">
            <div class="flex items-center space-x-2">
              <select class="card-options text-lg">
                <option value="">Todos</option>
                <option value="same-course">Do meu curso</option>
                <option value="favorites">Favoritos</option>
              </select>
            </div>

            <BpButton size="small">
              <v-icon name="md-personaddalt1-outlined"></v-icon>
              <span class="text-sm">Adicionar Amigo</span>
            </BpButton>
          </div>
        </div>

        <div class="grid grid-cols-1 gap-4 mt-10 lg:grid-cols-2">
            <ListItemFriend
              v-for="(friend, index) in renderedFriends"
              v-bind:key="index"
              :nome="friend.name"
              :curso="friend.degree"
              :periodo="friend.period"
            />
        </div>
      </div>
      <div class="flex justify-center mt-12 pb-7">
        <BpPagination
          :paginateHandler="handlePaginate"
          :items-per-page="2"
          :array-size="allFriends.length"
        />
      </div>
    </main>
  </div>
</template>
