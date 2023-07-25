<template>
  <v-dialog v-model="showDialog" width="550">
    <v-card>
      <template #title>
        <span class="text-error noto-sans"> ※ 단어 삭제 </span>
      </template>
      <template #append>
        <v-btn variant="text" icon="mdi-close" @click="showDialog = false">
        </v-btn>
      </template>
      <v-card-text>
        <v-text-field
          label="Study Word"
          :model-value="props.selectedWord.studyWord"
          readonly
          append-inner-icon="mdi-read"
        >
        </v-text-field>
        <v-text-field
          label="Furigana"
          :model-value="props.selectedWord.furigana"
          v-show="props.searchedLanguage == LanguageType.JAPANESE"
        >
        </v-text-field>
        <v-text-field
          label="Native Word"
          :model-value="props.selectedWord.nativeWord"
          readonly
          append-inner-icon="mdi-read"
        >
        </v-text-field>
        <v-select
          label="Part Of Speech"
          :model-value="props.selectedWord.partOfSpeech"
          :items="partOfSpeechItems"
        >
        </v-select>
      </v-card-text>
      <v-card-actions class="d-flex justify-end">
        <v-btn color="error" @click="onClick"> DELETE </v-btn>
        <v-btn @click="showDialog = false" class="me-4">CANCEL</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { computed, ref } from "vue";

import { wordService } from "@/service/wordService";

import { useCommonMessageDialogStore } from "@/stores/commonMessageDialog";

import { LanguageType } from "@/common/enum/languageType";

const commonMessageDialogStore = useCommonMessageDialogStore();
const { showCommonMessageDialog } = commonMessageDialogStore;

const props = defineProps({
  modelValue: {
    type: Boolean,
    required: true,
  },
  selectedWord: {
    type: Object,
  },
  searchedLanguage: {
    type: String,
  },
});

const emit = defineEmits(["update:modelValue", "success"]);

const showDialog = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit("update:modelValue", value);
  },
});

const partOfSpeechItems = ref([
  "명사",
  "대명사",
  "동사",
  "형용사",
  "부사",
  "접속사",
  "전치사",
  "감탄사",
]);

const onClick = async () => {
  try {
    const wordId = props.selectedWord.id;
    await wordService.removeById(wordId);

    showDialog.value = false;
    showCommonMessageDialog("단어 삭제를 완료했습니다.");

    emit("success");
  } catch (err) {
    console.error(err);
    showCommonMessageDialog("단어 삭제를 실패했습니다.");
  }
};
</script>

<style scoped></style>
