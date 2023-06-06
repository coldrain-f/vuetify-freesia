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
          hide-details
          v-model="selectedVocabulary"
          :items="allVocabularyList"
        />
      </v-col>
      <v-col cols="6">
        <v-select
          label="Unit"
          variant="underlined"
          v-show="selectedCategory === 'Word'"
          density="compact"
          hide-details
          v-model="selectedUnit"
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
import { vocabularyService } from "@/service/vocabularyService";
import { utils } from "@/common/utils";
import { storeToRefs } from "pinia";
import { unitService } from "@/service/unitService";

const adminHomeStore = useAdminHomeStore();

const {
  categories,
  selectedCategory,
  allVocabularyList,
  selectedVocabulary,
  allUnitList,
  selectedUnit,
} = storeToRefs(adminHomeStore);

const handleCategoryChange = async (c) => {
  if (c === "Vocabulary") return;

  allVocabularyList.value = await vocabularyService.getAllVocabularyList();

  if (utils.isEmptyArray(allVocabularyList.value)) {
    selectedVocabulary.value = "No data available";
    return;
  }

  const firstVocabulary = allVocabularyList.value[0];
  selectedVocabulary.value = firstVocabulary;

  allUnitList.value = await unitService.searchUnitResponsePage(
    firstVocabulary.id,
    {
      page: 0,
      size: await unitService.searchUnitResponsePage(firstVocabulary.id)
        .totalPages,
    }
  ).content;

  if (utils.isEmptyArray(allUnitList.value)) {
    selectedUnit.value = "No data available";
    return;
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
