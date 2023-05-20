import { defineStore } from "pinia";
import { ref } from "vue";

export const useLearningStore = defineStore("learning", () => {
  // 학습 시작 여부
  const isLearningStarted = ref(false);

  // 학습 종료 안내 디아로그 화면을 띄울것인지 여부
  const showLearningTerminationDialog = ref(false);

  // 1/4/7/14 학습법 적용 여부
  const useLearning = ref(true);

  // 선택한 학습 Vocabulary
  const learningVocabularyTitle = ref("");

  // 선택한 학습 Unit
  const learningUnitName = ref("");

  return {
    isLearningStarted,
    showLearningTerminationDialog,
    useLearning,
    learningVocabularyTitle,
    learningUnitName,
  };
});
