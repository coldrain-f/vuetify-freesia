import { defineStore } from "pinia";
import { ref, type Ref } from "vue";

export const useCommonMessageDialogStore = defineStore("commonMessageDialog", () => {
  const showMessageDialog: Ref<boolean> = ref(false);
  const messageDialogText: Ref<string> = ref("");

  /** 미사용 - 나중에 제거 필요 */
  const showCommonMessageDialog = (message: string) => {
    showMessageDialog.value = true;
    messageDialogText.value = message;
  };

  const resetMessageDialog = (): void => {
    showMessageDialog.value = false;
    messageDialogText.value = "";
  };

  const MessageDialog = (message: string) => {
    resetMessageDialog();
    showMessageDialog.value = true;
    messageDialogText.value = message;
  };

  return {
    showCommonMessageDialog, // 미사용 - 나중에 제거 필요
    showMessageDialog,
    messageDialogText,
    MessageDialog,
  };
});
