<template>
  <v-dialog v-model="showDialog" width="550">
    <v-card>
      <template #title>
        <span class="text-error noto-sans"> ※ 단어장 삭제 </span>
      </template>
      <template #append>
        <v-btn variant="text" icon="mdi-close" @click="showDialog = false"> </v-btn>
      </template>
      <v-card-text>
        <v-text-field
          label="Title"
          :model-value="props.selectedVocabulary.title"
          readonly
          append-inner-icon="mdi-read"
        >
        </v-text-field>
        <v-select
          label="Language"
          :items="['English', 'Japanese']"
          :model-value="props.selectedVocabulary.language"
          append-inner-icon="mdi-read"
          readonly
        >
        </v-select>
        <v-text-field
          label="Unit Count"
          :model-value="props.selectedVocabulary.unitCount + '개'"
          readonly
          append-inner-icon="mdi-read"
        >
        </v-text-field>
        <v-alert
          type="error"
          icon="mdi-material-design"
          title="Warning"
          border="start"
          prominent
          closable
          variant="tonal"
          text="단어장을 삭제하면 소속된 모든 유닛과 단어들이 삭제됩니다. 반드시 확인 후 삭제해주세요."
        >
        </v-alert>
      </v-card-text>
      <v-card-actions class="d-flex justify-end">
        <v-btn color="error" @click="onClick"> DELETE </v-btn>
        <v-btn @click="showDialog = false" class="me-4">CANCEL</v-btn>
      </v-card-actions>
    </v-card>

    <!-- 삭제 재확인 Dialog -->
    <v-dialog v-model="showDeleteConfirmDialog" width="350">
      <v-card>
        <template #title>
          <span class="text-error noto-sans"> ※ Alert </span>
        </template>
        <template #append>
          <v-btn variant="text" icon="mdi-close" @click="showDeleteConfirmDialog = false"> </v-btn>
        </template>
        <v-card-text> 정말 삭제하시겠습니까? </v-card-text>
        <v-card-actions class="d-flex justify-end">
          <v-btn color="error" @click="onDeleteVocabulary"> CONFIRM </v-btn>
          <v-btn class="me-4"> CANCEL </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-dialog>
</template>

<script setup lang="ts">
import { computed, ref } from "vue";

import { vocabularyService } from "@/service/vocabularyService";

import { useCommonMessageDialogStore } from "@/stores/commonMessageDialog";
import { Vocabulary } from "vocabularyTypes";

const commonMessageDialogStore = useCommonMessageDialogStore();
const { showCommonMessageDialog } = commonMessageDialogStore;

/** Props */
interface Props {
  modelValue: boolean;
  selectedVocabulary: Vocabulary;
}

const props = defineProps<Props>();

const emit = defineEmits(["update:modelValue", "success"]);

const showDialog = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit("update:modelValue", value);
  },
});

const showDeleteConfirmDialog = ref(false);

const onClick = () => {
  showDeleteConfirmDialog.value = true;
};

const closeAllDialogs = () => {
  showDeleteConfirmDialog.value = false;
  showDialog.value = false;
};

const onDeleteVocabulary = async () => {
  try {
    const vocabularyId = props.selectedVocabulary.id;
    await vocabularyService.deleteById(vocabularyId);

    closeAllDialogs();
    showCommonMessageDialog("단어장 삭제를 완료했습니다.");

    emit("success");
  } catch (err) {
    console.error(err);

    closeAllDialogs();
    showCommonMessageDialog("단어장 삭제를 실패했습니다.");
  }
};
</script>

<style scoped></style>
