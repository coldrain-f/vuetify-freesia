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
        <v-select
          label="Voca"
          variant="underlined"
          v-show="selectedCategory === 'Unit' || selectedCategory === 'Word'"
          density="compact"
          v-model="selectedVocabulary"
          :items="allVocabularyList"
          :hint="`${selectedVocabulary.title}, ${selectedVocabulary.value}`"
          item-title="title"
          item-value="value"
          return-object
          persistent-hint
          single-line
        />
      </v-col>
      <v-col cols="6">
        <v-select
          label="Unit"
          variant="underlined"
          v-show="selectedCategory === 'Word'"
          density="compact"
          hide-details
          v-model="selectedUnitSubject"
          :items="allUnitList"
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

const adminHomeStore = useAdminHomeStore();

const {
  categories,
  selectedCategory,
  selectedVocabulary,
  allVocabularyList,
  allUnitList,
  selectedUnitSubject,
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
