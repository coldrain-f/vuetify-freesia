import { defineStore } from "pinia";
import { ref } from "vue";

export const useLearningStore = defineStore("learning", () => {
  // 학습 시작 여부
  const isLearningStarted = ref(false);

  // 학습 종료 안내 디아로그 화면을 띄울것인지 여부
  const showLearningTerminationDialog = ref(false);

  // 1/4/7/14 학습법 적용 여부
  const useLearning = ref(true);

  // 학습 스타일 설정 (문제 출제: 단어, 뜻)
  const learningStyle = ref("Word");

  // 선택한 학습 언어
  const learningLanguage = ref("English");

  // 선택한 학습 Vocabulary
  const learningVocabulary = ref({ title: null, value: null });

  // 선택한 학습 Unit
  const learningUnitName = ref("");

  return {
    isLearningStarted,
    showLearningTerminationDialog,
    useLearning,
    learningStyle,
    learningLanguage,
    learningVocabulary,
    learningUnitName,
  };
});
