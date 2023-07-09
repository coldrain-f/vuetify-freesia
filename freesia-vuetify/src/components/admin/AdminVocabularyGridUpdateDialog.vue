<template>
  <v-dialog v-model="showDialog" width="550">
    <v-card>
      <template #title>
        <span class="text-info noto-sans"> ※ 단어장 수정 </span>
      </template>
      <template #append>
        <v-btn variant="text" icon="mdi-close" @click="showDialog = false">
        </v-btn>
      </template>
      <v-card-text>
        <v-text-field label="Title" v-model="formData.title"> </v-text-field>
        <v-select
          label="Language"
          :items="['English', 'Japanese']"
          v-model="formData.language"
          readonly
        >
        </v-select>
        <v-text-field label="Unit Count" v-model="formData.unitCount">
        </v-text-field>
      </v-card-text>
      <v-card-actions class="d-flex justify-end">
        <v-btn color="info" @click="onClick"> UPDATE </v-btn>
        <v-btn @click="showDialog = false" class="me-4">CANCEL</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { computed, reactive, watch } from "vue";

const props = defineProps({
  modelValue: {
    type: Boolean,
    required: true,
  },
  selectedVocabulary: {
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
  title: null,
  language: null,
  unitCount: null,
});

// props를 감시하여 넘어오는 값이 다를 때마다 formData를 갱신해준다.
watch(props.selectedVocabulary, (v) => {
  // 혹시나 Grid Data가 선택되지 않은 상태로 Dialog 진입 시 강제로 Dialog를 닫아버린다.
  if (isNullVocabulary(v)) {
    showDialog.value = false;
    return;
  }

  Object.assign(formData, {
    title: v.title,
    language: v.language,
    unitCount: v.unitCount,
  });
});

const isNullVocabulary = (v) => {
  return v.title == null || v.language == null || v.unitCount == null;
};

const onClick = () => {
  console.log(`title = ${formData.title}`);
  console.log(`language = ${formData.language}`);
  console.log(`unitCount = ${formData.unitCount}`);
};
</script>

<style scoped></style>
