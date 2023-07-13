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
          :model-value="props.selectedWord.partOfSpeech"
          :items="['명사', '형용사', '동사']"
          readonly
          append-inner-icon="mdi-read"
        >
        </v-select>
        <v-row>
          <v-col cols="6">
            <v-text-field
              label="Correct Count"
              :model-value="props.selectedWord.correctCount + '개'"
              readonly
              append-inner-icon="mdi-read"
            >
            </v-text-field>
          </v-col>
          <v-col cols="6">
            <v-text-field
              label="Incorrect Count"
              :model-value="props.selectedWord.incorrectCount + '개'"
              readonly
              append-inner-icon="mdi-read"
            >
            </v-text-field>
          </v-col>
        </v-row>
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

const showDeleteConfirmDialog = ref(false);

const onClick = () => {
  showDeleteConfirmDialog.value = true;
};
</script>

<style scoped></style>
