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

  const allUnitList = ref([]);

  const selectedUnitSubject = ref(null);
  const selectedUnitId = ref(null);

  const initialize = async () => {
    allVocabularyList.value = await vocabularyService.getAllVocabularyList();

    // 생성한 단어장이 없으면 "No data available"
    if (utils.isEmptyArray(allVocabularyList.value)) {
      selectedVocabularyTitle.value = selectedUnitSubject.value =
        "No data available";
      selectedVocabularyId.value = null;
      return;
    }

    // 생성한 단어장이 있으면 Default 값으로 가장 최근에 생성한 단어장으로 설정하고,
    const firstVocabulary = allVocabularyList.value[0];
    selectedVocabularyTitle.value = firstVocabulary.title;
    selectedVocabularyId.value = firstVocabulary.id;

    // 설정한 단어장의 소속된 UnitList를 셀렉트 박스에 설정한다.
    const unitList = await unitService.getAllUnitList(firstVocabulary.id);
    allUnitList.value = unitList.map((u) => u.subject);

    // 단어장은 있지만, 생성한 Unit이 없으면 "No data available"
    if (utils.isEmptyArray(allUnitList.value)) {
      selectedUnitSubject.value = "No data available";
      return;
    }

    selectedUnitSubject.value = allUnitList.value[0];
  };

  return {
    categories,
    selectedCategory,
    allVocabularyList,
    selectedVocabularyTitle,
    selectedVocabularyId,
    allUnitList,
    selectedUnitSubject,
    selectedUnitId,
    initialize,
  };
});
