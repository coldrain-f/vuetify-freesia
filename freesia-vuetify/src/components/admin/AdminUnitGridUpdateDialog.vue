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
          :model-value="props.selectedUnit.readCount || 0 + '개'"
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
import { computed, reactive, watch } from "vue";

import { useCommonMessageDialogStore } from "@/stores/commonMessageDialog";
import { unitService } from "@/service/unitService";

const commonMessageDialogStore = useCommonMessageDialogStore();
const { showCommonMessageDialog } = commonMessageDialogStore;

const props = defineProps({
  modelValue: {
    type: Boolean,
    required: true,
  },
  selectedUnit: {
    type: Object,
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

const formData = reactive({});

// props를 감시하여 넘어오는 값이 다를 때마다 formData를 갱신해준다.
watch(
  () => props.selectedUnit,
  (selectedUnit) => {
    formData.id = selectedUnit.id;
    formData.subject = selectedUnit.subject;
  },
  { deep: true }
);

const onClick = async () => {
  try {
    const unitId = formData.id;
    await unitService.modifyById(unitId, formData);

    showDialog.value = false;
    showCommonMessageDialog("단위 수정을 완료했습니다.");
    emit("success");
  } catch (err) {
    console.error(err);

    showDialog.value = false;
    showCommonMessageDialog("단위 수정을 실패했습니다.");
  }
};
</script>

<style scoped></style>
