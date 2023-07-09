import { defineStore } from "pinia";
import { ref } from "vue";

export const useCommonMessageDialogStore = defineStore(
  "commonMessageDialog",
  () => {
    const showMessageDialog = ref(false);
    const messageDialogText = ref("");

    const showCommonMessageDialog = (message) => {
      showMessageDialog.value = true;
      messageDialogText.value = message;
    };

    return {
      showMessageDialog,
      messageDialogText,
      showCommonMessageDialog,
    };
  }
);
