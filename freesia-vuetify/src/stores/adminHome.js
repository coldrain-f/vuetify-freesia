import { defineStore } from "pinia";
import { ref } from "vue";

export const useAdminHomeStore = defineStore("adminHome", () => {
  const adminCategories = ref(["Vocabulary", "Unit", "Word"]);
  const selectedAdminCategory = ref("Vocabulary");

  const isSubCategory = () => {
    return (
      selectedAdminCategory.value === "Unit" ||
      selectedAdminCategory.value === "Word"
    );
  };

  return {
    adminCategories,
    selectedAdminCategory,
    isSubCategory,
  };
});
