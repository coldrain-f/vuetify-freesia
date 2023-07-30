<template>
  <div>
    <v-img
      class="border"
      style="cursor: pointer"
      height="375"
      width="500"
      :aspect-ratio="1"
      :src="titleBanner"
      alt="image not found."
      cover
      @click="onClick"
    >
    </v-img>
    <LearningPlannerDialog
      v-model="showPlannerDialog"
      :languageSelect="languageSelect"
      :vocabularySelect="vocabularySelect"
      @handleLanguageChange="
        (changedLanguage) => handleLanguageChange(changedLanguage)
      "
      @handleVocabularyChange="
        (changedVocabulary) => handleVocabularyChange(changedVocabulary)
      "
      @handleClose="handleClose"
    />
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";

import titleBanner from "@/assets/images/mapleBanner2.png";
import LearningPlannerDialog from "./LearningPlannerDialog.vue";
import { LanguageType } from "@/common/enum/languageType";
import { languageService } from "@/service/languageService";
import { vocabularyService } from "@/service/vocabularyService";

const showPlannerDialog = ref(false);

const languageSelect = reactive({
  items: [],
  selectedItem: LanguageType.ENGLISH,
});

const vocabularySelect = reactive({
  items: [],
  selectedItem: {
    id: new Number(),
    title: "",
  },
});

const handleClose = async () => {
  languageSelect.selectedItem = LanguageType.ENGLISH;
  await fetchVocabularySelect(LanguageType.ENGLISH);
};

const handleLanguageChange = async (changedLanguage) => {
  languageSelect.selectedItem = changedLanguage;
  await fetchVocabularySelect(changedLanguage);
};

const handleVocabularyChange = (changedVocabulary) => {
  vocabularySelect.selectedItem = changedVocabulary;
};

const fetchVocabularySelect = async (language) => {
  const pageableParams = {
    page: 0,
    size: 2000,
  };
  const searchCondition = {
    language,
  };

  const vocabularyPageable = await vocabularyService.getPageable(
    pageableParams,
    searchCondition
  );

  vocabularySelect.items = vocabularyPageable.content.map((v) => {
    return {
      id: v.id,
      title: v.title,
    };
  });

  const content = vocabularyPageable.content;
  if (content.length <= 0) {
    handleVocabularyChange({ id: 0, title: "No data available" });
    return;
  }

  handleVocabularyChange({ id: content[0].id, title: content[0].title });
};

const onClick = async () => {
  languageSelect.items = await languageService.findAll();
  await fetchVocabularySelect(LanguageType.ENGLISH);
  showPlannerDialog.value = true;
};
</script>

<style scoped></style>
