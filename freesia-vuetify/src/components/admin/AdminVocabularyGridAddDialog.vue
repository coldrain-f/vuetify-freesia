<template>
  <v-dialog v-model="showDialog" width="550">
    <v-card>
      <template #title>
        <span class="text-primary noto-sans"> ※ 단어장 등록 </span>
      </template>
      <template #append>
        <v-btn variant="text" icon="mdi-close" @click="showDialog = false"> </v-btn>
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
        <v-btn color="primary" @click="handleAddClick"> ADD </v-btn>
        <v-btn @click="showDialog = false" class="me-4"> CANCEL </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref, type Ref } from "vue";
import { useCommonMessageDialogStore } from "@/stores/commonMessageDialog";
import LanguageService from "@/service/languageServiceTypescript";
import VocabularyService from "@/service/vocabularyServiceTypescript";
import type { Language } from "@/@types/language";
import type { VocaRegistrationFormData } from "vocabularyTypes";

/** Props */
interface Props {
  modelValue: boolean;
}

const props = defineProps<Props>();

/** Emits */
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "success"): void;
}>();

/** Services */
const languageService = new LanguageService();
const vocabularyService = new VocabularyService();

/** Stores */
const commonMessageDialogStore = useCommonMessageDialogStore();
const { VAlert } = commonMessageDialogStore;

/** Data */
const formData: VocaRegistrationFormData = reactive({
  title: "",
  language: "English",
});

const languageItems: Ref<Language[]> = ref([]);

/** Computed */
const showDialog = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit("update:modelValue", value);
  },
});

/** Actions */
function resetFormData(): void {
  formData.title = "";
  formData.language = "English";
}

async function handleAddClick(): Promise<void> {
  try {
    await vocabularyService.register(formData);
    resetFormData();
    VAlert("단어장 등록을 완료했습니다.");
    emit("success");
  } catch (err) {
    VAlert("단어장 등록을 실패했습니다.");
    console.error(err);
  }
}

/** Events */
onMounted(async () => {
  languageItems.value = await languageService.findAll();
});
</script>

<style scoped></style>
