import { defineStore } from "pinia";
import { ref } from "vue";
import { utils } from "@/common/commonUtils";
import { vocabularyService } from "@/service/vocabularyService";
import { unitService } from "@/service/unitService";

export const useAdminHomeStore = defineStore("adminHome", () => {
  const categories = ref(["Vocabulary", "Unit", "Word"]);

  const selectedCategory = ref("Vocabulary");

  // 단어장(Vocabulary) Pageable
  const vocabularyPage = ref({});

  // 단위(Unit) Pageable
  const unitPage = ref({});

  // 단어(Word) Pageable
  const wordPage = ref({});

  // 단어장(Vocabulary) 셀렉트 박스에 표시될 아이템
  const vocabularyItems = ref([]);

  // 단위(Unit) 셀렉트 박스에 표시될 아이템
  const unitItems = ref([]);

  // 선택한 단어장(Vocabulary) v-model
  const selectedVocabulary = ref({
    title: null,
    value: null,
  });

  // 선택한 단위(Unit) v-model
  const selectedUnit = ref({
    subject: null,
    value: null,
  });

  const initialize = async () => {
    const vocaPageTemp = await vocabularyService.searchVocabularyResponsePage({
      page: 0,
      size: 2000,
    });

    vocabularyItems.value = vocaPageTemp.content.map((v) => {
      return {
        title: v.title,
        value: v.id,
      };
    });

    // 생성한 단어장이 없으면 "No data available"
    if (utils.isEmptyArray(vocaPageTemp.content)) {
      selectedUnit.value.subject = "No data available";
      Object.assign(selectedVocabulary.value, {
        title: "No data available",
        value: null,
      });
      return;
    }

    // 생성한 단어장이 있으면 Default 값으로 가장 최근에 생성한 단어장으로 설정하고,
    const firstVocabulary = vocabularyItems.value[0];

    Object.assign(selectedVocabulary.value, {
      title: firstVocabulary.title,
      value: firstVocabulary.value,
    });

    // 설정한 단어장의 소속된 UnitList를 셀렉트 박스에 설정한다.
    const unitPageTemp = await unitService.searchUnitResponsePage(
      firstVocabulary.value,
      {
        page: 0,
        size: 2000,
      }
    );

    unitItems.value = unitPageTemp.content.map((u) => {
      return {
        subject: u.subject,
        value: u.id,
      };
    });

    // 단어장은 있지만, 생성한 Unit이 없으면 "No data available"
    if (utils.isEmptyArray(unitPageTemp.content)) {
      Object.assign(selectedUnit.value, {
        subject: "No data available",
        value: null,
      });
      return;
    }

    const firstUnit = unitItems.value[0];
    Object.assign(selectedUnit.value, {
      subject: firstUnit.subject,
      value: firstUnit.value,
    });
  };

  return {
    categories,
    selectedCategory,
    vocabularyItems,
    selectedVocabulary,
    unitItems,
    selectedUnit,
    vocabularyPage,
    unitPage,
    wordPage,
    initialize,
  };
});
