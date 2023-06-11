import { defineStore } from "pinia";
import { ref } from "vue";

export const useCommonStore = defineStore("common", () => {
  const showSnackbar = ref(false);
  const snackbarText = ref("");

  const showMessageDialog = ref(false);
  const messageDialogText = ref("");

  const VDialogMessage = (text) => {
    showMessageDialog.value = true;
    messageDialogText.value = text;
  };

  return {
    showSnackbar,
    snackbarText,
    showMessageDialog,
    messageDialogText,
    VDialogMessage,
  };
});
