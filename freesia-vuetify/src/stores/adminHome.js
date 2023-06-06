import { defineStore } from "pinia";
import { ref } from "vue";

export const useAdminHomeStore = defineStore("adminHome", () => {
  const categories = ref(["Vocabulary", "Unit", "Word"]);

  const selectedCategory = ref("Vocabulary");

  const allVocabularyList = ref([]);

  const selectedVocabulary = ref(null);

  const allUnitList = ref([]);

  const selectedUnit = ref(null);

  return {
    categories,
    selectedCategory,
    allVocabularyList,
    selectedVocabulary,
    allUnitList,
    selectedUnit,
  };
});
