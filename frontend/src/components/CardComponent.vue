<script setup>

const props = defineProps(['function', 'name'])

</script>
<template>
    <div class="grid place-items-center relative mb-36 mt-4">
        <div class="relative bg-bp_neutral-500 w-48 h-48 rounded-full z-10"></div>
        <div class="absolute top-1/2 bg-bp_neutral-800 rounded-b-lg w-52 h-52 z-0">
            <div class="pt-28 grid place-items-center space-y-2">
                <span class="text-sm text-bp_neutral-400">{{ props.function }}</span>
                <span class="text-xl font-bold">{{ props.name }}</span>
            </div>
        </div>
    </div>
    
</template>