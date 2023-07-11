<template>
  <v-dialog v-model="showDialog" width="550">
    <v-card>
      <template #title>
        <span class="text-info noto-sans"> ※ 유닛 수정 </span>
      </template>
      <template #append>
        <v-btn variant="text" icon="mdi-close" @click="showDialog = false">
        </v-btn>
      </template>
      <v-card-text>
        <v-text-field label="Subject" v-model="formData.subject" required>
        </v-text-field>
        <v-text-field
          label="Word Count"
          :model-value="props.selectedUnit.wordCount + '개'"
          readonly
          append-inner-icon="mdi-read"
        >
        </v-text-field>
        <v-text-field
          label="Read Count"
          :model-value="props.selectedUnit.readCount + '개'"
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
  selectedUnit: {
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
watch(props.selectedUnit, (u) => {
  // 혹시나 Grid Data가 선택되지 않은 상태로 Dialog 진입 시 강제로 Dialog를 닫아버린다.
  if (isEmptyObject(u)) {
    showDialog.value = false;
    return;
  }

  formData.subject = u.subject;
  formData.wordCount = u.wordCount;
  formData.readCount = u.readCount;
});

const onClick = () => {};
</script>

<style scoped></style>
