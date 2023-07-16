<template>
  <v-dialog v-model="showDialog" width="550">
    <v-card>
      <template #title>
        <span class="text-primary noto-sans"> ※ 단어장 등록 </span>
      </template>
      <template #append>
        <v-btn variant="text" icon="mdi-close" @click="showDialog = false">
        </v-btn>
      </template>
      <v-card-text>
        <v-text-field label="Title" v-model="formData.title"> </v-text-field>
        <v-select
          label="Language"
          :items="languageItems"
          v-model="formData.language"
          item-title="name"
          item-value="name"
        >
        </v-select>
      </v-card-text>
      <v-card-actions class="d-flex justify-end">
        <v-btn color="primary" @click="onClick"> ADD </v-btn>
        <v-btn @click="showDialog = false" class="me-4"> CANCEL </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from "vue";

import { languageService } from "@/service/languageService";
import { vocabularyService } from "@/service/vocabularyService";

import { LanguageType } from "@/common/enum/languageType";

import { useCommonMessageDialogStore } from "@/stores/commonMessageDialog";

const commonMessageDialogStore = useCommonMessageDialogStore();
const { showCommonMessageDialog } = commonMessageDialogStore;

const props = defineProps({
  modelValue: {
    type: Boolean,
    required: true,
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
  language: LanguageType.ENGLISH,
});

const languageItems = ref([]);

onMounted(async () => {
  Object.assign(languageItems.value, await languageService.findAll());
});

const clearFormData = () => {
  formData.title = null;
  formData.language = LanguageType.ENGLISH;
};

const onClick = async () => {
  try {
    await vocabularyService.register(formData);
    showDialog.value = false;
    showCommonMessageDialog("단어장 등록을 완료했습니다.");
    clearFormData();
  } catch (err) {
    console.error(err);
    showCommonMessageDialog("단어장 등록을 실패했습니다.");
  }
};
</script>

<style scoped></style>
