<template>
  <v-container>
    <v-select
      label="Category"
      variant="underlined"
      v-model="adminHomeStore.selectedAdminCategory"
      :items="adminHomeStore.adminCategories"
      :hide-details="adminHomeStore.selectedAdminCategory === 'Vocabulary'"
      chips
      @update:model-value="handleCategoryChange"
    />
    <v-row>
      <v-col :cols="6">
        <v-select
          label="Voca"
          variant="underlined"
          v-show="adminHomeStore.isSubCategory()"
          density="compact"
          hide-details
          v-model="selectedVocabulary"
          :items="allVocabularyList"
        />
      </v-col>
      <v-col cols="6">
        <v-select
          label="Unit"
          variant="underlined"
          v-show="adminHomeStore.selectedAdminCategory === 'Word'"
          density="compact"
          hide-details
          model-value="Unit 01 - 일상1"
          :items="[
            'Unit 01 - 일상1',
            'Unit 02 - 학교생활1',
            'Unit 03 - 학교생활2',
          ]"
        />
      </v-col>
    </v-row>
  </v-container>
  <VocabularyAdmin
    v-if="adminHomeStore.selectedAdminCategory === 'Vocabulary'"
  />
  <UnitAdmin v-if="adminHomeStore.selectedAdminCategory === 'Unit'" />
  <WordAdmin v-if="adminHomeStore.selectedAdminCategory === 'Word'" />
</template>

<script setup>
import VocabularyAdmin from "./VocabularyAdmin.vue";
import UnitAdmin from "./UnitAdmin.vue";
import WordAdmin from "./WordAdmin.vue";
import { useAdminHomeStore } from "@/stores/adminHome";
import { onMounted, ref } from "vue";
import { vocabularyService } from "@/service/vocabularyService";
import { utils } from "@/common/utils";

const adminHomeStore = useAdminHomeStore();

const allVocabularyList = ref([]);
const selectedVocabulary = ref(null);

const handleCategoryChange = async (selectedCategory) => {
  if (selectedCategory === "Unit" || selectedCategory === "Word") {
    allVocabularyList.value = await vocabularyService.getAllVocabularyList();
    selectedVocabulary.value = utils.isEmptyArray(allVocabularyList.value)
      ? "No data available"
      : allVocabularyList.value[0];
  }
};

onMounted(async () => {});
</script>

<!-- 공통 스타일로 분리 예정  -->
<style>
@import url("https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@500&display=swap");

.noto-sans {
  font-family: "Noto Sans KR", sans-serif;
}
</style>
