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
        <v-text-field label="Title" v-model="formData.title" required>
        </v-text-field>
        <v-select
          label="Language"
          :items="['English', 'Japanese']"
          v-model="formData.language"
          :readonly="!isUnitCountZero()"
          required
          :messages="
            !isUnitCountZero()
              ? '언어는 소속된 Unit 개수가 0개인 경우에만 변경할 수 있습니다.'
              : ''
          "
          :class="!isUnitCountZero() ? 'mb-4' : ''"
          :append-inner-icon="!isUnitCountZero() ? 'mdi-read' : 'mdi-menu-down'"
        >
        </v-select>
        <v-text-field
          label="Unit Count"
          :model-value="props.selectedVocabulary.unitCount + '개'"
          readonly
          append-inner-icon="mdi-read"
        >
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
import { commonUtils } from "@/common/commonUtils";
import { computed, reactive, watch } from "vue";

const { isEmptyObject } = commonUtils;

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

const formData = reactive({});

// props를 감시하여 넘어오는 값이 다를 때마다 formData를 갱신해준다.
watch(props.selectedVocabulary, (v) => {
  // 혹시나 Grid Data가 선택되지 않은 상태로 Dialog 진입 시 강제로 Dialog를 닫아버린다.
  if (isEmptyObject(v)) {
    showDialog.value = false;
    return;
  }

  formData.title = v.title;
  formData.language = v.language;
  formData.unitCount = v.unitCount;
});

const isUnitCountZero = () => {
  return props.selectedVocabulary.unitCount == 0;
};

const onClick = () => {
  console.log(`title = ${formData.title}`);
  console.log(`language = ${formData.language}`);
  console.log(`unitCount = ${formData.unitCount}`);
};
</script>

<style scoped></style>
