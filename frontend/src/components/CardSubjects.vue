<script setup>
import { computed } from 'vue'
import BpButton from './BpButton.vue'

const props = defineProps(['subject']);
const { code, workload, name, departament, course, difficulty } = props.subject;

const difficultyClass = computed(() => {
  switch (difficulty) {
    case 'ALTA':
      return 'bg-bp_danger'
    case 'MEDIA':
      return 'bg-bp_yellow'
    case 'BAIXA':
      return 'bg-bp_sucess'
    default:
      return 'bg-bp_neutral-200'
  }
})
</script>

<template>
  <div class="text-bp_neutral-50 w-full lg:h-24 rounded-lg bg-bp_neutral-800">
    <div class="flex flex-col md:flex-row md:justify-start md:items-center space-x-6 pr-4">
      <div class="flex flex-col items-center pl-8">
        <p class="text-2xl font-semibold">{{ code }}</p>
        <p class="text-xl">{{ workload }}h</p>
      </div>
      <div class="hidden md:block w-0.5 h-24 bg-bp_neutral-700"></div>
      <div class="flex justify-between items-center w-full gap-4">
        <div class="">
          <div class="flex items-center space-x-2 lg:space-x-4">
            <p class="text-base sm:text-lg font-semibold">{{ name }}</p>
            <div :class="difficultyClass" class="w-2 h-2 sm:w-5 sm:h-5 rounded-full"></div>
          </div>
          <div class="flex items-center space-x-2 lg:space-x-4">
            <p class="text-2xl sm:text-base">{{ departament }}</p>
            <p>|</p>
            <p class="text-base">{{ course }}</p>
          </div>
        </div>
        <BpButton size="small" type="outlined"> Detalhes </BpButton>
      </div>
    </div>
  </div>
</template>
