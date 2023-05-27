<template>
  <v-container>
    <v-select
      label="Language"
      variant="underlined"
      v-model="adminHomeStore.selectedAdminCategory"
      :items="adminHomeStore.adminCategories"
      chips
      hide-details
    />
  </v-container>
  <VocabularyAdmin
    v-if="adminHomeStore.selectedAdminCategory === 'Vocabulary'"
  />
</template>

<script setup>
import VocabularyAdmin from "./VocabularyAdmin.vue";
import { useAdminHomeStore } from "@/stores/adminHome";

const adminHomeStore = useAdminHomeStore();
</script>

<!-- 공통 스타일로 분리 예정  -->
<style>
@import url("https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@500&display=swap");

.noto-sans {
  font-family: "Noto Sans KR", sans-serif;
}
</style>
