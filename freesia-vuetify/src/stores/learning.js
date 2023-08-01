import { defineStore } from "pinia";
import { ref } from "vue";

export const useLearningStore = defineStore("learning", () => {
  // 학습 시작 여부
  const isLearningStarted = ref(false);

  // 학습 종료 안내 디아로그 화면을 띄울것인지 여부
  const showLearningTerminationDialog = ref(false);

  // 1/4/7/14 학습법 적용 여부
  const useLearning = ref(true);

  // 학습 스타일 설정 (문제 출제: 학습 단어, 모국 단어)
  const learningStyle = ref("studyWord");

  // 단어
  const words = ref([]);

  // 학습 단어
  const learningWords = ref([]);

  // 학습 언어
  const language = ref("");

  // 플래너 상세 번호
  const plannerDetailId = ref();

  return {
    isLearningStarted,
    showLearningTerminationDialog,
    useLearning,
    learningStyle,
    words,
    language,
    learningWords,
    plannerDetailId,
  };
});
