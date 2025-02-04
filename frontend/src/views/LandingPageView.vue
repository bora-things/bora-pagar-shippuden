<script setup>
import BpButton from '@/components/BpButton.vue';
import CardComponent from '@/components/CardComponent.vue';
import SimboraImg from '@/components/SimboraImg.vue';
import { addIcons } from 'oh-vue-icons';
import { FaInstagram , FaDiscord } from 'oh-vue-icons/icons/fa';

// Adicionando os ícones do Font Awesome localmente
addIcons(FaInstagram, FaDiscord );
</script>


<template>
    <div>
       <header class="flex justify-between place-items-center py-6 px-10">
        <div class="flex place-items-center space-x-4">
            <SimboraImg/>
            <p class="font-bold text-3xl">BoraPagar</p>
        </div>
        <div class="space-x-6">
            <a class="nav-item" href="#">Motivação</a>
            <a class="nav-item" href="#">Sobre</a>
            <a class="nav-item" href="#">Criadores</a>
        </div>
        <div>
            <BpButton class="w-32" type="primary" size="medium">Avançar</BpButton>
        </div>
       </header>
<main class="space-y-20 h-full mx-auto">
  <div class="border-b border-bp_neutral-600 px-4 md:px-16 lg:px-40">
    <div class="space-y-8 py-20 md:py-32">
        <p class="text-6xl lg:text-8xl"><span class="text-bp_primary-300">Planeje</span> melhor o seu curso<span class="text-bp_primary-300">.</span></p>
        <p class="text-bp_neutral-400">Uma plataforma inovadora, criada por estudantes para estudantes, visamos mitigar dificuldades acadêmicas como indeferimentos, problemas de horário, organização e gerenciamento de curso, avaliação do corpo docente, trilhas de aprendizado personalizadas entre outros. Nosso objetivo é proporcionar uma experiência acadêmica mais eficiente e personalizada.</p>
    </div>
  </div>

  <div class="grid grid-cols-2 md:grid-cols-3 gap-x-4 md:gap-x-14 gap-y-10 text-bp_neutral-400 border-b border-bp_neutral-600 pb-20 px-6 md:px-20 lg:px-56">
    <div class="space-y-2">
        <v-icon class="text-bp_primary-400" name="md-inventory-outlined" scale="1.5"/>
        <p class="text-bp_neutral-50 font-bold">Organização</p>
        <p>Organize suas matérias de forma prática  e eficiente.  Ajudamos a tentar se organizar da melhor forma possível em relação a horários, fazer um semestre balanceado e escolher as matérias ideais de acordo com seu objetivo. </p>
    </div>
    <div class="space-y-2">
        <v-icon class="text-bp_primary-400" name="md-rule-sharp" scale="1.5"/>
        <p class="text-bp_neutral-50 font-bold">Progresso </p>
        <p>Ajudamos o aluno em seu progresso acadêmico, monitorando e apoiando seu desenvolvimento ao longo do curso. Fornecemos uma visão geral das matérias já cursadas e das que ainda faltam cursar. </p>
    </div>
    <div class="space-y-2">
        <v-icon class="text-bp_primary-400" name="md-managesearch" scale="1.5"/>
        <p class="text-bp_neutral-50 font-bold">Matricula</p>
        <p>Ajudamos os alunos a se organizar durante o período de matrícula, como escolher quais matérias cursar, ver quais amigos vão cursar essas matérias e montar um semestre balanceado de acordo com suas necessidades.</p>
    </div>
    <div class="space-y-2">
        <v-icon class="text-bp_primary-400" name="md-thumbupalt-sharp" scale="1.5"/>
        <p class="text-bp_neutral-50 font-bold">Avaliação e feedbacks</p>
        <p>A plataforma auxilia os alunos na avaliação do corpo docente e das matérias, permitindo que os estudantes forneçam feedback sobre seus professores, disciplinas e o ambiente de aprendizado. </p>
    </div>
    <div class="space-y-2">
        <v-icon class="text-bp_primary-400" name="md-description-outlined" scale="1.5"/>
        <p class="text-bp_neutral-50 font-bold">Relatórios</p>
        <p>Relatórios para o DAAL visando um melhor entendimento das necessidades dos alunos em relação às matérias, suporte e eventos. </p>
    </div>
    <div class="space-y-2">
        <v-icon class="text-bp_primary-400" name="md-smarttoy-outlined" scale="1.5"/>
        <p class="text-bp_neutral-50 font-bold">Chatbot Inteligente</p>
        <p>Imagina ter um sagui chamado Simbora sempre ao seu lado durante a graduação. O Simbora é uma IA que tá aqui pra ajudar você a navegar pelos desafios do curso, tipo escolher as matérias, organizar a grade, ou até planejar o que estudar em cada semestre. </p>
    </div>
  </div>
 
 
  <div class="px-4 md:px-16 lg:px-40 pb-20">
    <div>
      <p class="text-bp_neutral-400">Equipe Bora Pagar</p>
    </div>
    <div>
      <p class="text-4xl">Coordenador & Devops.</p>
        <div class="grid grid-cols-2 lg:grid-cols-6">
            <CardComponent function="Front-End Developer" name="Naty"/>
            <CardComponent function="Front-End Developer" name="Naty"/>
        </div>
    </div>
    <div>
        <p class="text-4xl">Front-End & UX/UI Designer.</p>
          <div class="grid grid-cols-2 lg:grid-cols-6">
              <CardComponent function="Front-End Developer" name="Naty"/>
              <CardComponent function="Front-End Developer" name="Naty"/>
              <CardComponent function="Front-End Developer" name="Naty"/>
              <CardComponent function="Front-End Developer" name="Naty"/>
              <CardComponent function="Front-End Developer" name="Naty"/>
              <CardComponent function="Front-End Developer" name="Naty"/>
            </div>
      </div>
      <div>
        <p class="text-4xl">Back-End & IA.</p>
        <div class="grid grid-cols-2 lg:grid-cols-6">
            <CardComponent function="Front-End Developer" name="Naty"/>
        </div>
      </div>
      <div>
        <p class="text-4xl">BI & Raspagem.</p>
        <div class="grid grid-cols-2 lg:grid-cols-6">
            <CardComponent function="Front-End Developer" name="Naty"/>
        </div>
      </div>
  </div>
    </main>


    <footer class="bg-bp_neutral-800">
        <div class="flex place-items-start justify-between px-4 md:px-16 lg:px-40 py-10">
            <div class="flex place-items-center space-x-4">
                <SimboraImg/>
                <p class="font-bold text-2xl">BoraPagar</p>
            </div>
           <div class="flex justify-end space-x-16 w-4/6">
            <div class="space-y-3">
                <p class="font-bold text-2xl">Contato</p>
                <p class="text-bp_neutral-400 text-sm">+55 21 9999-9999</p>
                <p class="text-bp_neutral-400 text-sm">contato@borapagar.com</p>
                <div class="flex space-x-2">
                    <button><v-icon name="fa-instagram" scale="1.5"/></button>
                    <button><v-icon name="fa-discord" scale="1.5"/> </button>
                </div>
               
               </div>
               <div class="space-y-3">
                <p class="font-bold text-2xl">Newsletter</p>
                <p class="text-bp_neutral-400 text-sm">Cadastro seu e-mail para receber nossas novidades.</p>
                <div class="relative flex items-center">
                    <v-icon name="md-email-outlined" scale="1.0" class="absolute text-bp_neutral-400" />
                    <input class="w-full bg-bp_neutral-800 border-b-2 border-bp_neutral-400 py-2 text-bp_neutral-50  placeholder:text-bp_neutral-400 text-sm indent-8" type="text" placeholder="Seu email aqui">
                  </div>
                <BpButton class="w-40" type="primary" size="small">Enviar</BpButton>
            </div>
           </div>
        </div>
        <div class="border-t border-bp_neutral-700">
        <div class="flex justify-between px-4 md:px-16 lg:px-40 py-4 text-xs text-bp_neutral-400">
            <div>
                <p class="">Bora Pagar © Alguns direitos reservados </p>
            </div>
            <div class="flex space-x-10">
                <p>Termos of Service</p>
                <p>Privacy Policy</p>
            </div>
        </div>
     
        </div>
      </footer>
    </div>
   
  </template>



