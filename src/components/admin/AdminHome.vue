<template>
  <v-container>
    <v-select
      label="Category"
      variant="underlined"
      v-model="adminHomeStore.selectedAdminCategory"
      :items="adminHomeStore.adminCategories"
      :hide-details="adminHomeStore.selectedAdminCategory === 'Vocabulary'"
      chips
    />
    <v-select
      label="Voca"
      variant="underlined"
      v-show="adminHomeStore.isSubCategory()"
      :hide-details="adminHomeStore.selectedAdminCategory === 'Unit'"
      model-value="단어가 읽기다 기본편"
      :items="['단어가 읽기다 기본편', '단어가 읽기다 실력편', 'JLPT N5']"
    />
    <v-select
      label="Unit"
      variant="underlined"
      v-show="adminHomeStore.selectedAdminCategory === 'Word'"
      hide-details
      model-value="Unit 01 - 일상1"
      :items="['Unit 01 - 일상1', 'Unit 02 - 학교생활1', 'Unit 03 - 학교생활2']"
    />
  </v-container>
  <VocabularyAdmin
    v-if="adminHomeStore.selectedAdminCategory === 'Vocabulary'"
  />
  <UnitAdmin v-if="adminHomeStore.selectedAdminCategory === 'Unit'" />
</template>

<script setup>
import VocabularyAdmin from "./VocabularyAdmin.vue";
import UnitAdmin from "./UnitAdmin.vue";
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
