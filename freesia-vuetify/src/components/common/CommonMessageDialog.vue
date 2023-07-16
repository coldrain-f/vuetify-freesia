<!-- 
  - 공통 메시지 다이얼로그
  
  사용법: 
  import { useCommonMessageDialogStore } from "@/stores/commonMessageDialog";
  
  const commonMessageDialogStore = useCommonMessageDialogStore();
  const { showCommonMessageDialog } = commonMessageDialogStore;

  showCommonMessageDialog("Hello World!")
 -->
<template>
  <v-dialog v-model="showMessageDialog" width="350">
    <v-card>
      <template #title>
        <span class="text-primary"> ※ Alert </span>
      </template>
      <template #append>
        <v-btn
          variant="text"
          icon="mdi-close"
          @click="showMessageDialog = false"
        >
        </v-btn>
      </template>
      <v-card-text>
        <span class="noto-sans">
          {{ messageDialogText }}
        </span>
      </v-card-text>
      <v-card-actions class="d-flex justify-end">
        <v-btn color="primary" @click="showMessageDialog = false" class="w-100">
          CONFIRM
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { useCommonMessageDialogStore } from "@/stores/commonMessageDialog";
import { storeToRefs } from "pinia";

const commonMessageDialogStore = useCommonMessageDialogStore();

const { showMessageDialog, messageDialogText } = storeToRefs(
  commonMessageDialogStore
);
</script>

<style scoped></style>
