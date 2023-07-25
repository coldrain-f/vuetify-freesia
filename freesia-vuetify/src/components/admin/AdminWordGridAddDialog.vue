<template>
  <v-dialog v-model="showDialog" width="550">
    <v-card>
      <template #title>
        <span class="text-primary noto-sans"> ※ 단어 등록 </span>
      </template>
      <template #append>
        <v-btn variant="text" icon="mdi-close" @click="showDialog = false">
        </v-btn>
      </template>
      <v-card-text>
        <v-text-field label="Study Word" v-model="formData.studyWord">
        </v-text-field>
        <v-text-field
          label="Furigana"
          v-model="formData.furigana"
          v-show="props.searchedLanguage == LanguageType.JAPANESE"
        >
        </v-text-field>
        <v-text-field label="Native Word" v-model="formData.nativeWord">
        </v-text-field>
        <v-select
          label="Part Of Speech"
          v-model="formData.partOfSpeech"
          :items="partOfSpeechItems"
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
import { wordService } from "@/service/wordService";
import { computed, reactive, ref } from "vue";

import { useCommonMessageDialogStore } from "@/stores/commonMessageDialog";
import { LanguageType } from "@/common/enum/languageType";

const commonMessageDialogStore = useCommonMessageDialogStore();
const { showCommonMessageDialog } = commonMessageDialogStore;

const props = defineProps({
  modelValue: {
    type: Boolean,
    required: true,
  },
  unitId: {
    type: Number,
  },
  searchedLanguage: {
    type: String,
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

const partOfSpeechItems = ref([
  "명사",
  "대명사",
  "동사",
  "형용사",
  "부사",
  "접속사",
  "전치사",
  "감탄사",
]);

const formData = reactive({
  studyWord: "",
  nativeWord: "",
  furigana: "",
  partOfSpeech: "명사",
});

const clearFormData = () => {
  formData.studyWord = "";
  formData.nativeWord = "";
  formData.furigana = "";
  formData.partOfSpeech = "명사";
};

const onClick = async () => {
  const unitId = props.unitId;
  const { studyWord, nativeWord, furigana, partOfSpeech } = formData;

  try {
    await wordService.register(unitId, {
      studyWord,
      nativeWord,
      furigana,
      partOfSpeech,
    });

    showDialog.value = false;
    showCommonMessageDialog("단어 등록을 완료했습니다.");
    clearFormData();

    emit("success");
  } catch (err) {
    console.error(err);
    showCommonMessageDialog("단어 등록을 실패했습니다.");
  }
};
</script>

<style scoped></style>
