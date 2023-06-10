<template>
  <v-container>
    <v-select
      label="Category"
      variant="underlined"
      v-model="selectedCategory"
      :items="categories"
      :hide-details="selectedCategory === 'Vocabulary'"
      chips
      @update:model-value="handleCategoryChange"
    />
    <v-row>
      <v-col :cols="6">
        <!-- :hint="`${selectedVocabulary.title}, ${selectedVocabulary.value}`" -->
        <v-select
          label="Voca"
          variant="underlined"
          v-show="selectedCategory === 'Unit' || selectedCategory === 'Word'"
          density="compact"
          v-model="selectedVocabulary"
          :items="vocabularyItems"
          item-title="title"
          item-value="value"
          hide-details
          return-object
          persistent-hint
        />
      </v-col>
      <v-col cols="6">
        <!-- :hint="`${selectedUnit.subject}, ${selectedUnit.value}`" -->
        <v-select
          label="Unit"
          variant="underlined"
          v-show="selectedCategory === 'Word'"
          density="compact"
          v-model="selectedUnit"
          :items="unitItems"
          item-title="subject"
          item-value="value"
          hide-details
          return-object
          persistent-hint
        />
      </v-col>
    </v-row>
  </v-container>
  <VocabularyAdmin v-if="selectedCategory === 'Vocabulary'" />
  <UnitAdmin v-if="selectedCategory === 'Unit'" />
  <WordAdmin v-if="selectedCategory === 'Word'" />
</template>

<script setup>
import VocabularyAdmin from "./VocabularyAdmin.vue";
import UnitAdmin from "./UnitAdmin.vue";
import WordAdmin from "./WordAdmin.vue";
import { useAdminHomeStore } from "@/stores/adminHome";
import { onMounted } from "vue";
import { storeToRefs } from "pinia";

// Bug 1. 새로운 단어장 추가 후 Unit 카테고리로 이동하면 기존 테이블 데이터 유지되는 버그
// Bug 2. 새로운 단어장 추가 후 Word 카테고리로 이동 → 다른 단어장 선택 → 새로 추가한 단어장 → Unit이 이전 단어장으로 유지되는 버그

const adminHomeStore = useAdminHomeStore();

const {
  categories,
  selectedCategory,
  selectedVocabulary,
  selectedUnit,
  vocabularyItems,
  unitItems,
} = storeToRefs(adminHomeStore);

// 단어장, 단위의 데이터가 변화가 생겼을 수 있으므로 카테고리가 변경될 때마다 매번 초기화
const handleCategoryChange = async () => {
  await adminHomeStore.initialize();
};

onMounted(async () => {
  // 첫 AdminHome 진입 시 초기화
  await adminHomeStore.initialize();
});
</script>

<!-- 공통 스타일로 분리 예정  -->
<style>
@import url("https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@500&display=swap");

.noto-sans {
  font-family: "Noto Sans KR", sans-serif;
}
</style>
