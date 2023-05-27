import { defineStore } from "pinia";
import { ref } from "vue";

export const useAdminHomeStore = defineStore("adminHome", () => {
  const adminCategories = ref(["Vocabulary", "Unit", "Word"]);
  const selectedAdminCategory = ref("Vocabulary");

  return {
    adminCategories,
    selectedAdminCategory,
  };
});
