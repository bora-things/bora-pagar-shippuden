<script setup>
import InputSearch from '@/components/InputSearch.vue'
import CardSubjects from '@/components/CardSubjects.vue'
import NavBar from '@/components/NavBar.vue'
</script>

<template>
  <div class="h-full w-full">
    <NavBar />
    <main class="h-full container mx-auto p-6 xl:max-w-7xl bg-bp_neutral-900 space-y-6">
      <p class="text-bp_neutral-50 text-3xl font-bold">consultar-disciplinas</p>
      <hr class="border-bp_neutral-700" />
      <div class="flex flex-col lg:flex-row lg:space-x-6">
        <InputSearch class="w-full lg:w-2/3 mb-4 lg:mb-0" />
        <div class="flex flex-wrap justify-end lg:w-1/2 space-x-4">
          <div class="flex items-center space-x-2">
            <select class="card-options text-lg">
              <option hidden value="">Departamento</option>
              <option value="imd">IMD</option>
              <option value="c&t">C&T</option>
              <option value="dimap">DIMAP</option>
            </select>
          </div>
          <div class="flex items-center space-x-2">
            <select class="card-options text-lg">
              <option hidden value="">Dificuldade</option>
              <option value="imd">Baixa</option>
              <option value="c&t">Média</option>
              <option value="dimap">Alta</option>
            </select>
          </div>
          <div class="flex items-center space-x-2">
            <select class="card-options text-lg">
              <option hidden value="">Horas</option>
              <option value="imd">30h</option>
              <option value="c&t">60h</option>
              <option value="dimap">90h</option>
            </select>
          </div>
        </div>
      </div>
      <div class="space-y-4">
        <CardSubjects
          code="IMD0126"
          workload="60"
          name="Matemática Elementar"
          dificulty="MEDIA"
          departament="Intituto Metrópole Digital"
          course="Tecnologia da Informação"
        />
        <CardSubjects
          code="IMD0127"
          workload="90"
          name="Fundamentos da Matemática Computacional"
          dificulty="ALTA"
          departament="Intituto Metrópole Digital"
          course="Tecnologia da Informação"
        />
        <CardSubjects
          code="IMD0128"
          workload="60"
          name="Pensamento Computacional"
          dificulty="BAIXA"
          departament="Intituto Metrópole Digital"
          course="Tecnologia da Informação"
        />
      </div>
    </main>
  </div>
</template>
