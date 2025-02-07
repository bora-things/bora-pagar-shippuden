<script setup>
import BpButton from './BpButton.vue'
import { ref, computed, watch } from 'vue'

const props = defineProps(['current', 'itemsPerPage', 'paginateHandler', 'arraySize'])

const pagesNumber = Math.ceil(props.arraySize / props.itemsPerPage)
const pages = computed(() => {
  return Array.from({ length: pagesNumber }, (_, index) => index)
})
const currentPageIndex = ref(0)

const secondToLastIndex = pages.value.length - 2

const shownIndexInitial = ref(0)
const showIndexFinal = ref(5)
const shown5Indexes = computed(() =>
  pages.value.slice(shownIndexInitial.value, showIndexFinal.value)
)

watch(
  currentPageIndex,
  () => {
    function getInterval(pageIndex, itemsPerPage) {
      let from = pageIndex * itemsPerPage
      let to = from + itemsPerPage - 1

      return [from, to]
    }

    function indexesHandler() {
      if (
        currentPageIndex.value >= shown5Indexes.value.at(4) &&
        currentPageIndex.value <= secondToLastIndex
      ) {
        shownIndexInitial.value += 1
        showIndexFinal.value += 1
      } else if (
        currentPageIndex.value > 0 &&
        currentPageIndex.value === shown5Indexes.value.at(0)
      ) {
        shownIndexInitial.value -= 1
        showIndexFinal.value -= 1
      }
    }

    indexesHandler()

    const [indexFrom, indexTo] = getInterval(currentPageIndex.value, props.itemsPerPage)
    props.paginateHandler(indexFrom, indexTo)
  },
  { immediate: true }
)

function next() {
  if (currentPageIndex.value <= pagesNumber - 1) {
    currentPageIndex.value += 1
  }
}
function previous() {
  if (currentPageIndex.value >= 0) {
    currentPageIndex.value -= 1
  }
}

function handlePageClick(clickedPage) {
  currentPageIndex.value = clickedPage
}
</script>

<template>
  <div class="w-full flex flex-col items-center justify-center">
    <nav class="flex flex-nowrap bg-bp_neutral-900 text-bp_neutral-50">
      <BpButton
        :disabled="currentPageIndex === 0"
        type="text"
        class="text-white mr-2 disabled:cursor-default"
        @click="previous"
        ><v-icon name="md-arrowbackios-outlined" scale="1.1"
      /></BpButton>
      <ul class="flex flex-nowrap">
        <li v-for="index in shown5Indexes" :key="`pg_${index}`">
          <BpButton
            type="text"
            :class="[
              {
                'text-2xl transition-all duration-500 pb-2': currentPageIndex === index
              },
              currentPageIndex === index ? 'text-bp_primary-200' : 'text-white'
            ]"
            @click="handlePageClick(index)"
          >
            {{ index + 1 }}
          </BpButton>
        </li>
      </ul>
      <BpButton
        :disabled="currentPageIndex === pagesNumber - 1"
        type="text"
        class="text-white ml-2 disabled:cursor-default"
        @click="next"
        ><v-icon name="md-arrowforwardios-outlined" scale="1.1"
      /></BpButton>
    </nav>
    <p class="text-sm mt-2 text-bp_neutral-500">{{ currentPageIndex + 1 }} de {{ pages.length }}</p>
  </div>
</template>
