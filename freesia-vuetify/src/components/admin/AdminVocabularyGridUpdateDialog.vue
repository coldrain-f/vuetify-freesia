<template>
  <v-dialog v-model="showDialog" width="550">
    <v-card>
      <template #title>
        <span class="text-info noto-sans"> ※ 단어장 수정 </span>
      </template>
      <template #append>
        <v-btn variant="text" icon="mdi-close" @click="showDialog = false"> </v-btn>
      </template>
      <v-card-text>
        <v-text-field label="Title" v-model="formData.title" required> </v-text-field>
        <v-select
          label="Language"
          :items="languageItems"
          v-model="formData.language"
          item-title="name"
          item-value="name"
          :readonly="!isUnitCountZero()"
          :messages="
            !isUnitCountZero() ? '언어는 소속된 Unit 개수가 0개인 경우에만 변경할 수 있습니다.' : ''
          "
          :class="!isUnitCountZero() ? 'mb-4' : ''"
          :append-inner-icon="!isUnitCountZero() ? 'mdi-read' : 'mdi-menu-down'"
          required
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
import { computed, onMounted, reactive, ref, watch } from "vue";

import { languageService } from "@/service/languageService";
import { vocabularyService } from "@/service/vocabularyService";

import { useCommonMessageDialogStore } from "@/stores/commonMessageDialog";

const commonMessageDialogStore = useCommonMessageDialogStore();
const { showCommonMessageDialog } = commonMessageDialogStore;

const props = defineProps({
  modelValue: {
    type: Boolean,
    required: true,
  },
  selectedVocabulary: {
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

const languageItems = ref([]);

// props를 감시하여 넘어오는 값이 다를 때마다 formData를 갱신해준다.
// reactive를 감시하려면 deep: true를 설정 해줘야 하는듯...
watch(
  () => props.selectedVocabulary,
  (selectedVocabulary) => {
    formData.id = selectedVocabulary.id;
    formData.title = selectedVocabulary.title;
    formData.language = selectedVocabulary.language;
    formData.unitCount = selectedVocabulary.unitCount;
  },
  { deep: true },
);

const isUnitCountZero = () => {
  return props.selectedVocabulary.unitCount == 0;
};

const onClick = async () => {
  try {
    await vocabularyService.modify(formData);

    showDialog.value = false;
    showCommonMessageDialog("단어장 수정을 완료했습니다.");
    emit("success");
  } catch (err) {
    console.error(err);

    showDialog.value = false;
    showCommonMessageDialog("단어장 수정을 실패했습니다.");
  }
};

onMounted(async () => {
  Object.assign(languageItems.value, await languageService.findAll());
});
</script>

<style scoped></style>
