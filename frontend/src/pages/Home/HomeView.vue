<script setup>
import { useDark, useToggle } from '@vueuse/core'

const isDark = useDark()
const toggleDark = useToggle(isDark)
console.log(isDark.value)
</script>

<template>
  <main class="bg-white text-black rounded-md dark:bg-black dark:text-white h-screen">
    Home
    <div class="flex">
      <span class="text-black dark:text-white mr-2"> {{ isDark ? 'Dark ' : 'Light ' }} Mode </span>
      <button
        @click="toggleDark()"
        class="flex items-center justify-between py-1 px-1 bg-white text-black border border-black dark:bg-black dark:text-white dark:border-white rounded-md"
      >
        <v-icon
          :name="isDark ? 'md-darkmode-outlined' : 'md-lightmode-outlined'"
          class="mr-2 ml-2"
          scale="1.1"
        ></v-icon>
      </button>
    </div>
  </main>
</template>
