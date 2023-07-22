<template>
  <v-dialog v-model="showDialog" width="550">
    <v-card>
      <template #title>
        <span class="text-primary noto-sans"> ※ 유닛 등록 </span>
      </template>
      <template #append>
        <v-btn variant="text" icon="mdi-close" @click="showDialog = false">
        </v-btn>
      </template>
      <v-card-text>
        <v-text-field label="Subject" v-model="formData.subject">
        </v-text-field>
      </v-card-text>
      <v-card-actions class="d-flex justify-end">
        <v-btn color="primary" @click="onClick"> ADD </v-btn>
        <v-btn @click="showDialog = false" class="me-4"> CANCEL </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { computed, reactive } from "vue";

import { unitService } from "@/service/unitService";

import { useCommonMessageDialogStore } from "@/stores/commonMessageDialog";

const commonMessageDialogStore = useCommonMessageDialogStore();
const { showCommonMessageDialog } = commonMessageDialogStore;

const props = defineProps({
  modelValue: {
    type: Boolean,
    required: true,
  },
  vocabularyId: {
    type: Number,
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

const formData = reactive({
  subject: null,
});

const clearFormData = () => {
  formData.subject = null;
};

const onClick = async () => {
  try {
    await unitService.register(props.vocabularyId, formData);

    showDialog.value = false;
    clearFormData();
    showCommonMessageDialog("단위 등록을 완료했습니다.");
    emit("success");
  } catch (err) {
    console.error(err);

    showDialog.value = false;
    showCommonMessageDialog("단위 등록을 실패했습니다.");
  }
};
</script>

<style scoped></style>
