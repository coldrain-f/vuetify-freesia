<template>
  <v-card flat>
    <v-row>
      <v-col cols="10">
        <v-breadcrumbs :items="breadcrumbsItems">
          <template v-slot:prepend>
            <v-icon size="small" icon="mdi-book-heart" class="mb-1" />
          </template>
          <template v-slot:divider>
            <v-icon icon="mdi-chevron-right"></v-icon>
          </template>
        </v-breadcrumbs>
      </v-col>
      <v-col>
        <v-btn
          class="mt-1"
          icon="mdi-exit-to-app"
          variant="flat"
          @click="learningStore.showLearningTerminationDialog = true"
        ></v-btn>
      </v-col>
    </v-row>
    <v-divider></v-divider>
    <v-card-text>
      <v-row class="noto-sans">
        <v-col cols="10">
          <v-progress-linear
            v-model="progress"
            height="15"
            :color="themeStore.theme"
            class="mb-5 font-weight-bold"
            striped
          >
          </v-progress-linear>
        </v-col>
        <v-col cols="2">
          <p class="text-center">
            <span>{{ currentWordNumber }}/</span>
            <span>{{ totalWordCount }}개</span>
          </p>
        </v-col>
      </v-row>

      <v-row class="noto-sans">
        <v-col cols="4">
          <strong>시도: </strong>
          <span>{{ tryCount }}회</span>
        </v-col>
        <v-col cols="8">
          <strong>틀린 단어: </strong><span>{{ incorrectWordCount }}개</span>
        </v-col>
      </v-row>
      <v-row class="justify-center mb-1">
        <v-col cols="6">
          <v-img
            width="221"
            height="200"
            :aspect-ratio="1"
            :src="currentCatImage"
            alt="image not found."
          >
          </v-img>
        </v-col>
      </v-row>

      <v-row>
        <v-col cols="12">
          <v-text-field
            variant="underlined"
            readonly
            hide-details
            persistent-placeholder
            prepend-icon="mdi-school"
          >
            <span v-if="learningStore.language == LanguageType.ENGLISH">
              <span v-if="learningStore.learningStyle == 'studyWord'">
                {{ currentLearningWord.studyWord }}
              </span>
              <span v-if="learningStore.learningStyle == 'nativeWord'">
                {{ currentLearningWord.nativeWord }}
              </span>
            </span>

            <ruby v-if="learningStore.language == LanguageType.JAPANESE">
              <h6 class="noto-sans-jp text-h6-jp">
                {{ currentLearningWord.studyWord }}
              </h6>

              <rp v-show="showFurigana">(</rp>
              <rt
                class="text-primary noto-sans-jp text-subtitle-2-jp"
                v-show="showFurigana"
              >
                {{ currentLearningWord.furigana }}
              </rt>
              <rp v-show="showFurigana">)</rp>
            </ruby>

            <template v-slot:label>
              <span class="me-2">Study Word</span>
              <v-icon
                v-show="showSuccessIcon"
                icon="mdi-check"
                color="info"
                size="small"
                class="mb-1"
              >
              </v-icon>
              <v-icon
                v-show="showFailIcon"
                icon="mdi-close"
                color="error"
                size="small"
              >
              </v-icon>
            </template>
            <template v-slot:append-inner>
              <v-tooltip text="발음 듣기" location="top">
                <template v-slot:activator="{ props }">
                  <v-btn
                    variant="flat"
                    icon="mdi-volume-high"
                    size="small"
                    @click="handleSpeakText"
                    v-bind="props"
                  >
                  </v-btn>
                </template>
              </v-tooltip>

              <v-tooltip
                text="후리가나"
                location="top"
                v-if="learningStore.language == LanguageType.JAPANESE"
              >
                <template v-slot:activator="{ props }">
                  <v-btn
                    variant="flat"
                    icon="mdi-furigana-horizontal"
                    size="small"
                    v-bind="props"
                    @click="showFurigana = !showFurigana"
                  >
                  </v-btn>
                </template>
              </v-tooltip>
            </template>
          </v-text-field>
        </v-col>
      </v-row>

      <v-row class="mb-2">
        <v-col cols="12">
          <v-text-field
            :hint="maskMeaning"
            label="Native Word"
            ref="inputMeaningField"
            variant="underlined"
            v-model="inputMeaning"
            counter
            @keyup.enter="handleEnter"
            prepend-icon="mdi-shield-sword-outline"
          >
            <template v-slot:append-inner>
              <v-btn variant="text" size="small" icon="mdi-send"> </v-btn>

              <v-tooltip
                text="정답이 표시되는 대신 틀린 단어에 포함됩니다."
                location="top"
              >
                <template v-slot:activator="{ props }">
                  <v-btn
                    variant="flat"
                    size="small"
                    icon="mdi-chat-question"
                    @click="handleHelpButtonClick"
                    v-bind="props"
                  >
                  </v-btn>
                </template>
              </v-tooltip>
            </template>
          </v-text-field>
        </v-col>
      </v-row>

      <v-divider></v-divider>
    </v-card-text>
    <v-card-actions v-if="false"> </v-card-actions>
  </v-card>

  <v-dialog width="auto">
    <v-card>
      <v-card-text></v-card-text>
    </v-card>
  </v-dialog>

  <v-dialog v-model="showLearningTerminationDialog" width="350">
    <v-card>
      <template #title>
        <span class="text-primary">※ Alert</span>
      </template>
      <v-card-text>
        학습을 중간에 종료하면 미학습으로 처리됩니다. 정말 종료하시겠습니까?
      </v-card-text>
      <v-card-actions class="justify-end">
        <v-btn
          color="primary"
          @click="
            () => {
              isLearningStarted = false;
              showLearningTerminationDialog = false;
            }
          "
        >
          CONFIRM
        </v-btn>
        <v-btn @click="showLearningTerminationDialog = false"> CANCEL </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <v-dialog v-model="messageDialog.showDialog" width="auto">
    <v-card title="※ Alarm">
      <template #append>
        <v-btn
          variant="text"
          icon="mdi-close"
          @click="messageDialog.showDialog = false"
        >
        </v-btn>
      </template>
      <v-card-text>
        {{ messageDialog.text }}
      </v-card-text>
      <v-card-actions v-show="messageDialog.showConfirmAction">
        <v-btn
          class="w-100"
          variant="flat"
          color="primary"
          size="small"
          @click="messageDialog.confirmAction()"
        >
          Confirm
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import catGreatImage from "@/assets/images/cat_great_opacity0.png";
import catWhatImage from "@/assets/images/cat_what_opacity0.png";
import catNoImage from "@/assets/images/cat_no_opacity0.png";
import { computed, ref, watch, reactive } from "vue";
import { useSpeechSynthesisStore } from "@/stores/speechSynthesis";
import { useLearningStore } from "@/stores/learning";
import { useThemeStore } from "@/stores/theme";
import { storeToRefs } from "pinia";
import { LanguageType } from "@/common/enum/languageType";

import _ from "underscore";
import { wordService } from "@/service/wordService";

// Pinia stores
const synthStore = useSpeechSynthesisStore();
const learningStore = useLearningStore();
const themeStore = useThemeStore();

// the action can just be destructured
const { speakText } = synthStore;

const handleSpeakText = () => {
  speakText(currentLearningWord.value.studyWord);
  // if (learningStore.learningStyle == "studyWord") {
  //   speakText(currentLearningWord.value.studyWord);
  // } else if (learningStore.learningStyle == "nativeWord") {
  //   speakText(currentLearningWord.value.nativeWord);
  // }
};

const showFurigana = ref(false);

const { isLearningStarted, showLearningTerminationDialog } =
  storeToRefs(learningStore);

const inputMeaningField = ref(null);

/** 공통 메시지 Dialog - 모든 컴포넌트에서 공용으로 사용하도록 리팩터링 필요 */
const messageDialog = reactive({
  showDialog: false,
  text: null,
  showConfirmAction: true,
  confirmAction: () => {},
});

/** 공통 메시지 Dialog를 띄우는 함수 - 모든 컴포넌트에서 공용으로 사용하도록 리팩터링 필요
 *
 *  isAutoClose, showConfirmAction외 각종 option은 option 객체로 넘기도록 변경 필요.
 * */
function showDialogMessage(
  text,
  confirmAction,
  isAutoClose = true,
  showConfirmAction = true
) {
  messageDialog.showDialog = true;
  messageDialog.text = text;
  messageDialog.showConfirmAction = showConfirmAction;
  messageDialog.confirmAction = confirmAction
    ? confirmAction
    : () => {
        messageDialog.showDialog = false;
      };

  if (isAutoClose) {
    setTimeout(() => {
      if (messageDialog.showDialog === true) {
        if (messageDialog.confirmAction) {
          messageDialog.confirmAction();
        } else {
          messageDialog.showDialog = false;
        }
      }
    }, 3000);
  }
}

/** 학습 브레드크럼  */
const breadcrumbsItems = ["단어가 읽기다 기본편", "Day 1"];

/** 틀린 단어 목록 */
const incorrectWords = ref([]);

/** 출제 단어 옆 아이콘 표시 여부 */
const showSuccessIcon = ref(false);
const showFailIcon = ref(false);

/** 현재 표시되는 고양이 이미지 */
const currentCatImage = ref(catWhatImage);

/** 사용자가 입력한 단어의 뜻 저장 */
const inputMeaning = ref(null);

/** 시도 횟수 */
const tryCount = ref(0);

/** 총 단어 수 */
const totalWordCount = ref(learningStore.learningWords.length);

/** 현재 학습중인 단어 번호 */
const currentWordNumber = ref(0);

/** 현재 시도에서 틀린 단어 수 */
const incorrectWordCount = computed(() => {
  return incorrectWords.value.length;
});

/** 현재 학습 단어
 *
 * 현재 단어 번호에 해당하는 단어로 설정한다.
 * 마지막인 경우엔 -1을 해서 List의 맨 마지막 단어로 설정한다.
 */
const currentLearningWord = computed(() => {
  return currentWordNumber.value < totalWordCount.value
    ? learningStore.learningWords[currentWordNumber.value]
    : learningStore.learningWords[currentWordNumber.value - 1];
});

/** 학습 진행도 - 백분율 계산
 * 진행도: (현재 진행중인 단어 번호 / 총 단어 수) * 100%
 */
const progress = computed(() => {
  return (currentWordNumber.value / totalWordCount.value) * 100;
});

/** 첫 글자를 제외한 나머지 글자를 '_ '로 마스킹하는 함수 */
const maskMeaning = computed(() => {
  let meaning = "";
  if (learningStore.learningStyle == "studyWord") {
    meaning = currentLearningWord.value.nativeWord;
  } else if (learningStore.learningStyle == "nativeWord") {
    meaning = currentLearningWord.value.studyWord;
  }

  if (meaning.length === 1) {
    return "_";
  }
  let maskedMeaning = meaning[0];
  for (let i = 1; i < meaning.length; i++) {
    maskedMeaning += " _";
  }
  return maskedMeaning;
});

/** 현재 학습중인 단어 번호를 1 증가시키는 함수 */
function incrementCurrentWordNumber() {
  ++currentWordNumber.value;
}

/** 사용자가 입력한 답이 정답인 경우 처리하는 함수 */
function processCorrectAnswer() {
  currentCatImage.value = catGreatImage;
  showSuccessIcon.value = true;

  setTimeout(() => {
    resetCorrectAnswer();
    incrementCurrentWordNumber();
  }, 800);
}

/** 사용자가 입력한 답이 정답이 아닌 경우 처리하는 함수 */
function processIncorrectAnswer() {
  currentCatImage.value = catNoImage;
  showFailIcon.value = true;

  let incorrectWord = "";
  if (learningStore.learningStyle == "studyWord") {
    incorrectWord = currentLearningWord.value.studyWord;
  } else if (learningStore.learningStyle == "nativeWord") {
    incorrectWord = currentLearningWord.value.nativeWord;
  }

  // 틀린 단어 리스트에 추가되어 있지 않다면
  if (!incorrectWords.value.includes(incorrectWord)) {
    // 틀린 단어 리스트에 틀린 단어 추가
    incorrectWords.value.push(incorrectWord);
  }

  setTimeout(() => {
    resetIncorrectAnswer();
  }, 800);
}

/** 사용자가 입력한 답이 정답인 경우 표시했던 데이터를 초기화하는 함수 */
function resetCorrectAnswer() {
  currentCatImage.value = catWhatImage;
  showSuccessIcon.value = false;
  inputMeaning.value = null;

  showFurigana.value = false;
}

/** 사용자가 입력한 답이 정답이 아닌 경우 표시했던 데이터를 초기화하는 함수 */
function resetIncorrectAnswer() {
  currentCatImage.value = catWhatImage;
  showFailIcon.value = false;
}

/** 다음 학습 시작에 필요한 데이터 처리를 진행하는 함수
 *
 * 학습 진행도가 100%를 달성했고 틀린 단어가 1개 이상이라면 시도 횟수가 1 증가하고,
 * 틀린 단어의 목록을 가지고 재학습을 진행할 때 필요한 처리들을 진행한다.
 */
function processNextLearningStart() {
  learningStore.words = _.shuffle(incorrectWords.value);
  currentWordNumber.value = 0;
  totalWordCount.value = learningStore.words.length;
  incorrectWords.value = [];
  tryCount.value += 1;
}

/** 정답 입력창에서 엔터키를 눌렀을 경우 이벤트를 처리하는 핸들러 */
function handleEnter() {
  let meaning = "";
  if (learningStore.learningStyle == "studyWord") {
    meaning = currentLearningWord.value.nativeWord;
  } else if (learningStore.learningStyle == "nativeWord") {
    meaning = currentLearningWord.value.studyWord;
  }

  if (meaning === inputMeaning.value) {
    processCorrectAnswer();
  } else {
    processIncorrectAnswer();
  }
}

/** 도움말 버튼을 클릭헀을 경우 이벤트를 처리하는 핸들러 */
function handleHelpButtonClick() {
  let incorrectWord = "";
  if (learningStore.learningStyle == "studyWord") {
    inputMeaning.value = currentLearningWord.value.nativeWord;
    incorrectWord = currentLearningWord.value.studyWord;
  } else if (learningStore.learningStyle == "nativeWord") {
    inputMeaning.value = currentLearningWord.value.studyWord;
    incorrectWord = currentLearningWord.value.nativeWord;
  }

  inputMeaningField.value.focus();

  // 틀린 단어 리스트에 추가되어 있지 않다면
  if (!incorrectWords.value.includes(incorrectWord)) {
    // 틀린 단어 리스트에 틀린 단어 추가
    incorrectWords.value.push(incorrectWord);
  }
}

// 모니터링
watch(incorrectWords.value, () => {
  console.log(incorrectWords.value);
});

/** 학습 진행도가 100%인 경우 처리하는 감시자 */
watch(
  progress,
  () => {
    if (progress.value >= 100.0) {
      if (incorrectWords.value.length <= 0) {
        showDialogMessage(
          "학습을 종료합니다.",
          async () => {
            await wordService.finish(learningStore.plannerDetailId);
            learningStore.isLearningStarted = false;
            messageDialog.showDialog = false;
          },
          false
        );
      } else {
        showDialogMessage(
          "틀린 단어를 모아 재학습을 진행합니다.",
          () => {
            processNextLearningStart();
            messageDialog.showDialog = false;
          },
          false
        );
      }
    }
  },
  { immediate: true }
);
</script>

<style scoped>
.text-h5-jp {
  font-size: 1.5rem !important;
  font-weight: 400;
  line-height: 2rem;
  letter-spacing: normal !important;
  text-transform: none !important;
}

.text-h6-jp {
  font-size: 1.25rem !important;
  /* font-weight: 500; */
  font-weight: 400;
  line-height: 2rem;
  letter-spacing: 0.0125em !important;
  text-transform: none !important;
}

.text-subtitle-2-jp {
  font-size: 0.875rem !important;
  font-weight: 500;
  line-height: 1.375rem;
  letter-spacing: 0.0071428571em !important;
  text-transform: none !important;
}
</style>
