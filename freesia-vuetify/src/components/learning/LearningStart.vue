<template>
  <v-card flat>
    <v-breadcrumbs :items="breadcrumbsItems">
      <template v-slot:prepend>
        <v-icon size="small" icon="mdi-book-heart" class="mb-1" />
      </template>
      <template v-slot:divider>
        <v-icon icon="mdi-chevron-right"></v-icon>
      </template>
    </v-breadcrumbs>
    <v-divider></v-divider>
    <v-card-text>
      <v-row>
        <v-col cols="10"
          ><v-progress-linear
            v-model="progress"
            height="15"
            :color="themeStore.theme"
            class="mb-5 font-weight-bold"
            striped
          >
            <!-- <p :class="themeStore.theme === 'dark' ? 'text-white' : ''">
          <span>{{ currentWordNumber }}/</span><span>{{ totalWordCount }}</span>
        </p> -->
          </v-progress-linear>
        </v-col>
        <v-col cols="2">
          <p class="text-center">
            <span>{{ currentWordNumber }}/</span
            ><span>{{ totalWordCount }}개</span>
          </p>
        </v-col>
      </v-row>

      <v-row>
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

      <v-table>
        <thead>
          <tr>
            <th class="font-weight-bold">WORD</th>
            <th class="font-weight-bold">MEANING</th>
            <th class="font-weight-bold">OPTIONS</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td style="width: 35%">
              {{ currentLearningWord.name }}
              <v-icon
                v-show="showSuccessIcon"
                icon="mdi-check"
                color="info"
                class="mb-1"
                size="x-small"
              />

              <v-icon
                v-show="showFailIcon"
                icon="mdi-close"
                color="error"
                size="x-small"
              />
            </td>
            <td style="width: 35%">
              <v-text-field
                :placeholder="maskMeaning"
                :clearable="false"
                variant="underlined"
                ref="inputMeaningField"
                v-model="inputMeaning"
                @keyup.enter="handleEnter"
              >
              </v-text-field>
            </td>
            <td style="width: 30%">
              <span>
                <v-tooltip activator="parent" location="top">
                  정답이 표시되는 대신 틀린 단어에 포함됩니다.
                </v-tooltip>
                <v-btn
                  variant="text"
                  size="small"
                  icon="mdi-chat-question"
                  @click="handleHelpButtonClick"
                >
                </v-btn>
              </span>
              <v-btn
                variant="text"
                size="small"
                icon="mdi-volume-medium"
                @click="speakText(currentLearningWord.name)"
              ></v-btn>
            </td>
          </tr>
        </tbody>
      </v-table>
      <v-divider></v-divider>
    </v-card-text>
    <v-card-actions>
      <v-btn
        variant="flat"
        :color="themeStore.theme"
        class="w-100"
        size="default"
        @click="learningStore.showLearningTerminationDialog = true"
      >
        <v-icon start icon="mdi-exit-to-app"></v-icon>
        BACK
      </v-btn>
    </v-card-actions>
  </v-card>

  <v-dialog width="auto">
    <v-card>
      <v-card-text></v-card-text>
    </v-card>
  </v-dialog>

  <v-dialog v-model="showLearningTerminationDialog" width="auto">
    <v-card>
      <template #title>
        <span>※ Alarm</span>
      </template>
      <v-card-text>정말 처음으로 돌아가시겠습니까?</v-card-text>
      <v-card-actions class="justify-end">
        <v-btn
          color="primary"
          @click="
            () => {
              isLearningStarted = false;
              showLearningTerminationDialog = false;
            }
          "
          >YES</v-btn
        >
        <v-btn color="primary" @click="showLearningTerminationDialog = false"
          >NO</v-btn
        >
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
import _ from "underscore";

// Pinia stores
const synthStore = useSpeechSynthesisStore();
const learningStore = useLearningStore();
const themeStore = useThemeStore();

// the action can just be destructured
const { speakText } = synthStore;

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

/** TODO: DB에서 조회하도록 변경 후 삭제 예정 */
const words = ref([
  { id: 1, name: "spice", meaning: "양념" },
  { id: 2, name: "delicious", meaning: "맛있는" },
  { id: 3, name: "hot", meaning: "뜨거운" },
  { id: 4, name: "ファーストクラス", meaning: "퍼스트클래스" },
]);

/** 학습 브레드크럼  */
const breadcrumbsItems = ["단어가 읽기다 기본편", "Day 1"];

/** 틀린 단어 목록 */
const incorrectWords = ref([]);

/** 학습 단어 목록 */
const learningWords = ref(_.shuffle(words.value));

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
const totalWordCount = ref(learningWords.value.length);

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
    ? learningWords.value[currentWordNumber.value]
    : learningWords.value[currentWordNumber.value - 1];
});

/** 학습 진행도 - 백분율 계산
 * 진행도: (현재 진행중인 단어 번호 / 총 단어 수) * 100%
 */
const progress = computed(() => {
  return (currentWordNumber.value / totalWordCount.value) * 100;
});

/** 첫 글자를 제외한 나머지 글자를 '_ '로 마스킹하는 함수 */
const maskMeaning = computed(() => {
  const meaning = currentLearningWord.value.meaning;
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

  // 이미 틀렸던 단어라면 중복으로 추가하지 않는다.
  if (!incorrectWords.value.includes(currentLearningWord.value.name)) {
    incorrectWords.value.push(currentLearningWord.value.name);
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
  words.value = _.shuffle(incorrectWords.value);
  currentWordNumber.value = 0;
  totalWordCount.value = words.value.length;
  incorrectWords.value = [];
  tryCount.value += 1;
}

/** 정답 입력창에서 엔터키를 눌렀을 경우 이벤트를 처리하는 핸들러 */
function handleEnter() {
  const meaning = currentLearningWord.value.meaning;

  if (meaning === inputMeaning.value) {
    processCorrectAnswer();
  } else {
    processIncorrectAnswer();
  }
}

/** 도움말 버튼을 클릭헀을 경우 이벤트를 처리하는 핸들러 */
function handleHelpButtonClick() {
  inputMeaning.value = currentLearningWord.value.meaning;
  inputMeaningField.value.focus();

  // 이미 클릭했던 단어라면 중복으로 추가하지 않는다.
  if (!incorrectWords.value.includes(currentLearningWord.value.name)) {
    incorrectWords.value.push(currentLearningWord.value.name);
  }
}

/** 학습 진행도가 100%인 경우 처리하는 감시자 */
watch(
  progress,
  () => {
    if (progress.value >= 100.0) {
      if (incorrectWords.value.length <= 0) {
        showDialogMessage(
          "학습을 종료합니다.",
          () => {
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

<!-- 공통 스타일로 분리 예정  -->
<style>
@import url("https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@500&display=swap");

.noto-sans {
  font-family: "Noto Sans KR", sans-serif;
}
</style>
