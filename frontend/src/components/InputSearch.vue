<script setup>
const props = defineProps(['inputPlaceholder', 'inputId'])
</script>
<template>
  <div class="flex">
    <input
      type="text"
      class="w-full bg-bp_neutral-800 border border-bp_neutral-700 rounded-l-lg py-2 px-4 placeholder:text-bp_neutral-500"
      :placeholder="props.inputPlaceholder"
      :id="props.inputId"
    />
    <button class="bg-bp_primary-300 text-bp_primary-50 rounded-r-lg py-2 px-4">
      <v-icon name="md-search" scale="1.5" />
    </button>
  </div>
</template>
