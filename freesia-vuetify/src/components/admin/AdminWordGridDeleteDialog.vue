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
          label="Native Word"
          :model-value="props.selectedWord.nativeWord"
          readonly
          append-inner-icon="mdi-read"
        >
        </v-text-field>
        <v-select
          label="Part Of Speech"
          :modal-value="props.selectedWord.partOfSpeech"
          :items="partOfSpeechItems"
          item-title="title"
          item-value="value"
          return-object
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
import { commonUtils } from "@/common/commonUtils";
import { computed, ref, watch } from "vue";

const { isEmptyObject } = commonUtils;

const props = defineProps({
  modelValue: {
    type: Boolean,
    required: true,
  },
  selectedWord: {
    type: Object,
  },
});

const emit = defineEmits(["update:modelValue"]);

// 혹시나 Grid Data가 선택되지 않은 상태로 Dialog 진입 시 강제로 Dialog를 닫아버린다.
watch(props.selectedWord, (w) => {
  if (isEmptyObject(w)) {
    showDialog.value = false;
    return;
  }
});

const showDialog = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit("update:modelValue", value);
  },
});

const partOfSpeechItems = ref([
  { title: "명사", value: "Noun" },
  { title: "대명사", value: "Pronoun" },
  { title: "동사", value: "Verb" },
  { title: "형용사", value: "Adjective" },
  { title: "부사", value: "Adverb" },
  { title: "접속사", value: "Conjunction" },
  { title: "전치사", value: "Preposition" },
  { title: "감탄사", value: "Interjection" },
]);

const showDeleteConfirmDialog = ref(false);

const onClick = () => {
  showDeleteConfirmDialog.value = true;
};
</script>

<style scoped></style>
