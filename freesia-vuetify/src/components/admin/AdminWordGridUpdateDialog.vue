<template>
  <v-dialog v-model="showDialog" width="550">
    <v-card>
      <template #title>
        <span class="text-info noto-sans"> ※ 단어 수정 </span>
      </template>
      <template #append>
        <v-btn variant="text" icon="mdi-close" @click="showDialog = false">
        </v-btn>
      </template>
      <v-card-text>
        <v-text-field label="Study Word" v-model="formData.studyWord">
        </v-text-field>
        <v-text-field label="Native Word" v-model="formData.nativeWord">
        </v-text-field>
        <v-select
          label="Part Of Speech"
          v-model="formData.partOfSpeech"
          :items="['명사', '형용사', '동사']"
        >
        </v-select>
      </v-card-text>
      <v-card-actions class="d-flex justify-end">
        <v-btn color="info" @click="onClick"> UPDATE </v-btn>
        <v-btn @click="showDialog = false" class="me-4">CANCEL</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { commonUtils } from "@/common/commonUtils";
import { computed, reactive, watch } from "vue";

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

const showDialog = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit("update:modelValue", value);
  },
});

const formData = reactive({
  partOfSpeech: "명사", // Default
});

// props를 감시하여 넘어오는 값이 다를 때마다 formData를 갱신해준다.
watch(props.selectedWord, (w) => {
  // 혹시나 Grid Data가 선택되지 않은 상태로 Dialog 진입 시 강제로 Dialog를 닫아버린다.
  if (isEmptyObject(w)) {
    showDialog.value = false;
    return;
  }

  formData.studyWord = w.studyWord;
  formData.nativeWord = w.nativeWord;
  formData.partOfSpeech = w.partOfSpeech;
});

const onClick = () => {};
</script>

<style scoped></style>
