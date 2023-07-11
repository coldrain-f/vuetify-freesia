<template>
  <v-dialog v-model="showDialog" width="550">
    <v-card>
      <template #title>
        <span class="text-error noto-sans"> ※ 유닛 삭제 </span>
      </template>
      <template #append>
        <v-btn variant="text" icon="mdi-close" @click="showDialog = false">
        </v-btn>
      </template>
      <v-card-text>
        <v-text-field
          label="Title"
          :model-value="props.selectedUnit.subject"
          readonly
          append-inner-icon="mdi-read"
        >
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
        <v-alert
          type="error"
          title="Warning"
          text="단위를 삭제하면 소속된 모든 단어들이 삭제됩니다. 반드시 확인 후 삭제해주세요."
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
          <v-btn
            variant="text"
            icon="mdi-close"
            @click="showDeleteConfirmDialog = false"
          >
          </v-btn>
        </template>
        <v-card-text> 정말 삭제하시겠습니까? </v-card-text>
        <v-card-actions class="d-flex justify-end">
          <v-btn color="error"> CONFIRM </v-btn>
          <v-btn @click="showDeleteConfirmDialog = false" class="me-4">
            CANCEL
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
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
  selectedUnit: {
    type: Object,
  },
});

const emit = defineEmits(["update:modelValue"]);

// 혹시나 Grid Data가 선택되지 않은 상태로 Dialog 진입 시 강제로 Dialog를 닫아버린다.
watch(props.selectedUnit, (u) => {
  if (isEmptyObject(u)) {
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
