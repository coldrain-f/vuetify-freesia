<template>
  <v-card flat>
    <v-row class="mt-1">
      <v-col cols="6">
        <v-row>
          <v-col cols="12">
            <p class="ml-5">
              <span class="text-subtitle-2">
                ※ 1/4/7/14 LEARNING
                <v-tooltip activator="parent" location="end">
                  에빙하우스의 망각 곡선을 기반으로 자 <br />
                  연스럽게 단기 기억에서 장기 기억으로 <br />
                  이어질 수 있도록, 당일, 1일 전, 3일 전, 6 <br />
                  일 전, 13일 전 단어를 학습할 수 있도록 <br />
                  설정하는 옵션입니다. <br />
                </v-tooltip>
              </span>
            </p>
          </v-col>
        </v-row>
        <v-radio-group
          inline
          row
          hide-details
          color="primary"
          v-model="learningStore.useLearning"
        >
          <v-col cols="5">
            <v-radio label="적용" :value="true"></v-radio>
          </v-col>
          <v-col cols="6">
            <v-radio label="미적용" :value="false"></v-radio>
          </v-col>
        </v-radio-group>
      </v-col>
      <v-col cols="6">
        <v-row>
          <v-col cols="12">
            <p class="ml-5">
              <span class="text-subtitle-2">
                ※ STYLE
                <v-tooltip activator="parent" location="end">
                  문제를 단어로 출제할지, 뜻으로 출제할지 선택하는 <br />
                  옵션입니다. 영어 단어장인 경우 문제 출제 옵션을 <br />
                  "단어"로 선택하여 시작한다면 영어 단어가 문제로 <br />
                  출제되고 한글 뜻을 입력해서 정답으로 제출하면 됩니다. <br />
                </v-tooltip>
              </span>
            </p>
          </v-col>
        </v-row>
        <v-radio-group
          inline
          row
          hide-details
          color="primary"
          v-model="learningStore.learningStyle"
        >
          <v-col cols="6">
            <v-radio label="단어" value="Word"></v-radio>
          </v-col>
          <v-col cols="6">
            <v-radio label="뜻" value="Meaning"></v-radio>
          </v-col>
        </v-radio-group>
      </v-col>
    </v-row>
    <v-divider></v-divider>

    <v-card-text>
      <v-select
        label="Language"
        variant="underlined"
        class="mb-2"
        :items="['English', 'Japanese']"
        v-model="learningStore.learningLanguage"
        density="compact"
        chips
      />
      <!-- <v-select
        label="TTS"
        variant="underlined"
        v-model="synthStore.synthDefaultVoiceName"
        :items="synthStore.synthVoiceNames"
        density="compact"
        class="mb-2"
      /> -->
      <v-select
        label="Voca"
        v-model="learningStore.learningVocabularyTitle"
        :items="vocabularyOptions"
        variant="underlined"
        density="compact"
        class="mb-2"
      />
      <v-select
        label="Unit"
        v-model="learningStore.learningUnitName"
        :items="unitOptions"
        variant="underlined"
        density="compact"
        class="mb-2"
      />
    </v-card-text>
    <v-card-actions>
      <v-btn
        class="w-100"
        size="default"
        color="primary"
        variant="flat"
        @click="learningStore.isLearningStarted = true"
      >
        <v-icon start icon="mdi-school-outline"></v-icon>
        START
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script setup>
import { useSpeechSynthesisStore } from "@/stores/speechSynthesis";
import { utils } from "@/common/utils";
import { ref, onMounted, watch } from "vue";
import { useLearningStore } from "@/stores/learning";

// Pinia stores
const synthStore = useSpeechSynthesisStore();
const learningStore = useLearningStore();

const vocabularyOptions = ref([]);
const unitOptions = ref([]);

const vocabularyList = ref([]);
const units = ref([]);

// DB에서 조회해오도록 변경 작업 필요.
const fetchVocabularyList = () => {
  return [
    { id: 1, title: "단어가 읽기다 기본편", lang: "en" },
    { id: 2, title: "단어가 읽기다 실력편", lang: "ja" },
  ];
};

// DB에서 조회해오도록 변경 작업 필요.
const fetchUnits = () => {
  return [
    { id: 1, name: "Unit 01 - 일상생활" },
    { id: 2, name: "Unit 02 - 학교" },
  ];
};

// Language 를 감시하고 있다가 해당하는 언어의 TTS Voice를 조회 후
// TTS 음성을 조회한 맨 첫 번째 Voice로 설정한다.
watch(
  () => learningStore.learningLanguage,
  () => {
    const voiceNames = synthStore.getVoiceNamesByLang(
      learningStore.learningLanguage
    );
    synthStore.synthVoiceNames = voiceNames;
    synthStore.synthDefaultVoiceName = voiceNames[0];
  }
);

const loadVocabularyOptions = () => {
  vocabularyList.value.forEach((vocabulary) => {
    vocabularyOptions.value.push(vocabulary.title);
  });
};

const loadUnitOptions = () => {
  units.value.forEach((unit) => {
    unitOptions.value.push(unit.name);
  });
};

const setDefaultSelectedVocabularyOption = () => {
  learningStore.learningVocabularyTitle = utils.isEmptyArray(
    vocabularyList.value
  )
    ? "No data available"
    : vocabularyList.value[0].title;
};

const setDefaultSelectedUnitOption = () => {
  learningStore.learningUnitName = utils.isEmptyArray(units.value)
    ? "No data available"
    : units.value[0].name;
};

const initializeLearningSettings = () => {
  try {
    vocabularyList.value = fetchVocabularyList();
    units.value = fetchUnits();

    loadVocabularyOptions();
    loadUnitOptions();
    setDefaultSelectedVocabularyOption();
    setDefaultSelectedUnitOption();
  } catch (err) {
    console.error("Error occurred during initializing learning settings:", err);
  }
};

onMounted(() => {
  initializeLearningSettings();
});
</script>
