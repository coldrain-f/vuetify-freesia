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
    <v-row>
      <v-col :cols="6">
        <v-select
          label="Voca"
          variant="underlined"
          v-show="adminHomeStore.isSubCategory()"
          density="compact"
          hide-details
          v-model="selectedVocabulary"
          :items="vocabularyList"
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
import { onMounted, ref, watch } from "vue";
import { vocabularyService } from "@/service/vocabularyService";

const adminHomeStore = useAdminHomeStore();

const vocabularyList = ref([]);
const selectedVocabulary = ref(null);

onMounted(async () => {});

watch(
  () => adminHomeStore.selectedAdminCategory,
  async (selectedAdminCategory) => {
    if (selectedAdminCategory === "Unit") {
      // Total 조회
      const vocabularyPage =
        await vocabularyService.searchVocabularyResponsePage();
      const totalElements = vocabularyPage.totalElements;

      // Total 만큼 조회하여 단어장 목록을 설정한다.
      vocabularyService
        .searchVocabularyResponsePage({
          page: 0,
          size: totalElements,
        })
        .then((response) => {
          vocabularyList.value = response.content;
          if (!vocabularyList.value.length) {
            selectedVocabulary.value = "No data available";
            return;
          }
          selectedVocabulary.value = vocabularyList.value[0].title;
        })
        .catch((err) => {
          console.error(err);
        });
    }
  }
);
</script>

<!-- 공통 스타일로 분리 예정  -->
<style>
@import url("https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@500&display=swap");

.noto-sans {
  font-family: "Noto Sans KR", sans-serif;
}
</style>
