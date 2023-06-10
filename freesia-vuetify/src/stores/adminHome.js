import { defineStore } from "pinia";
import { ref } from "vue";
import { utils } from "@/common/utils";
import { vocabularyService } from "@/service/vocabularyService";
import { unitService } from "@/service/unitService";

export const useAdminHomeStore = defineStore("adminHome", () => {
  const categories = ref(["Vocabulary", "Unit", "Word"]);

  const selectedCategory = ref("Vocabulary");

  const allVocabularyList = ref([]);

  const selectedVocabularyTitle = ref(null);
  const selectedVocabularyId = ref(null);

  // 선택한 단어장 v-model
  const selectedVocabulary = ref({
    title: null,
    value: null,
  });

  const allUnitList = ref([]);

  const selectedUnitSubject = ref(null);
  const selectedUnitId = ref(null);

  const initialize = async () => {
    const vocaPage = await vocabularyService.searchVocabularyResponsePage({
      page: 0,
      size: 2000,
    });

    allVocabularyList.value = vocaPage.content.map((v) => {
      return {
        title: v.title,
        value: v.id,
      };
    });

    // 생성한 단어장이 없으면 "No data available"
    if (utils.isEmptyArray(allVocabularyList.value)) {
      // Refactor 필요
      selectedUnitSubject.value = "No data available";
      Object.assign(selectedVocabulary.value, {
        title: "No data available",
        value: null,
      });
      return;
    }

    // 생성한 단어장이 있으면 Default 값으로 가장 최근에 생성한 단어장으로 설정하고,
    const firstVocabulary = allVocabularyList.value[0];

    Object.assign(selectedVocabulary.value, {
      title: firstVocabulary.title,
      value: firstVocabulary.value,
    });

    // 설정한 단어장의 소속된 UnitList를 셀렉트 박스에 설정한다.
    const unitList = await unitService.getAllUnitList(firstVocabulary.value);
    allUnitList.value = unitList.map((u) => u.subject);

    // 단어장은 있지만, 생성한 Unit이 없으면 "No data available"
    if (utils.isEmptyArray(allUnitList.value)) {
      selectedUnitSubject.value = "No data available";
      selectedUnitId.value = null;
      return;
    }

    selectedUnitSubject.value = allUnitList.value[0];
    selectedUnitId.value = unitList[0].id;
  };

  return {
    categories,
    selectedCategory,
    allVocabularyList,
    selectedVocabulary,
    selectedVocabularyTitle,
    selectedVocabularyId,
    allUnitList,
    selectedUnitSubject,
    selectedUnitId,
    initialize,
  };
});
