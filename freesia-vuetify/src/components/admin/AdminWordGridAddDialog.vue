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
          item-title="title"
          item-value="value"
          return-object
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
  { title: "명사", value: "Noun" },
  { title: "대명사", value: "Pronoun" },
  { title: "동사", value: "Verb" },
  { title: "형용사", value: "Adjective" },
  { title: "부사", value: "Adverb" },
  { title: "접속사", value: "Conjunction" },
  { title: "전치사", value: "Preposition" },
  { title: "감탄사", value: "Interjection" },
]);

const formData = reactive({
  studyWord: "",
  nativeWord: "",
  furigana: "",
  partOfSpeech: { title: "명사", value: "Noun" },
});

const clearFormData = () => {
  formData.studyWord = "";
  formData.nativeWord = "";
  formData.furigana = "";
  formData.partOfSpeech = { title: "명사", value: "Noun" };
};

const onClick = async () => {
  const unitId = props.unitId;
  const { studyWord, nativeWord, furigana, partOfSpeech } = formData;

  try {
    await wordService.register(unitId, {
      studyWord,
      nativeWord,
      furigana,
      partOfSpeech: partOfSpeech.value,
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
