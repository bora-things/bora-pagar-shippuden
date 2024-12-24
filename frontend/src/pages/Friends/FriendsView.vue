<script setup>
import BpButton from '@/components/BpButton.vue'
import BpPagination from '@/components/BpPagination.vue'
import ListItemFriend from '@/components/ListItemFriend.vue'
import { onMounted, ref } from 'vue'

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
const currentPagination = ref(0)
function updateCurrentPagination(newValue) {
  currentPagination.value = newValue
  console.log(newValue * 10)
}
onMounted(() => {
  console.log(currentPagination)
})
</script>

<template>
  <div class="min-h-screen bg-bp_neutral-900 text-bp_neutral-50">
    <main class="pl-16 pr-16">
      <div class="container-search-friends ml-[60px] mr-[60px]">
        <h2 class="text-3xl font-bold border-b border-bp_neutral-800 pb-6 mb-6">amigos</h2>
        <div class="flex items-center justify-between">
          <div class="flex items-center">
            <input
              type="text"
              name=""
              class="h-[60px] w-[510px] rounded-l-[10px] bg-bp_neutral-800 text-white border-[1px] border-solid border-bp_neutral-600 hover:bg-gray-700 indent-[5px]"
              placeholder=""
            />
            <button
              class="h-[60px] w-[60px] rounded-r-[10px] bg-bp_primary-400 text-white font-bold flex justify-center items-center"
            >
              <v-icon name="md-search-round" scale="1.5"></v-icon>
            </button>
            <select
              name="filter"
              class="h-[60px] w-[158px] py-[18px] px-4 bg-bp_neutral-800 text-white font-bold ml-4 rounded-[10px] border-[1px] border-bp_neutral-600 border-solid"
            >
              <option value="">Todos</option>
              <option value="same-course">Do meu curso</option>
              <option value="favorites">Favoritos</option>
            </select>
          </div>

          <BpButton>
            <v-icon class="mr-2" name="md-personaddalt1-outlined" scale="1.5"></v-icon>
            Adicionar Amigo
          </BpButton>
        </div>

        <div class="grid grid-cols-1 gap-4 mt-10 lg:grid-cols-2">
          <ListItemFriend
            v-for="(friend, index) in allFriends"
            v-bind:key="index"
            :nome="friend.name"
            :curso="friend.degree"
            :periodo="friend.period"
          />
        </div>
      </div>
      <div class="flex justify-center mt-12 pb-7">
        <BpPagination
          :current="currentPagination"
          @changeCurrentValue="updateCurrentPagination"
          :count="10"
        />
      </div>
    </main>
  </div>
</template>

<style scoped></style>
