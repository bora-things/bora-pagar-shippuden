<script setup>
import NavBar from '@/components/NavBar.vue';
import SimboraChat from '@/components/chat/SimboraChat.vue';
import { RouterView } from 'vue-router';
</script>

<template>
  <div class="min-h-screen h-full bg-bp_neutral-900 text-bp_neutral-50">
    <NavBar />
    <SimboraChat />
    <RouterView />
  </div>
</template>
