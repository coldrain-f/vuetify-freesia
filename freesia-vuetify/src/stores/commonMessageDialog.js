import { defineStore } from "pinia";
import { ref } from "vue";

// 이름 변경 예정 useCommonStore = useCommonMessageDialogStore
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
