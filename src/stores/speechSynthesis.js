import { defineStore } from "pinia";
import { ref } from "vue";

export const useSpeechSynthesisStore = defineStore("speechSynthesis", () => {
  const synthDefaultVoiceName = ref(null);
  const synthVoiceNames = ref([]);

  let isInitialized = false;

  const initializeSynthDefaultVoiceName = (voices) => {
    voices.forEach((voice) => {
      if (voice.default) {
        synthDefaultVoiceName.value = voice.name;
        return;
      }
    });
  };

  const initializeSpeechSynthesis = async () => {
    if ("speechSynthesis" in window) {
      const synth = window.speechSynthesis;

      // 음성 데이터가 변경될 때까지 기다리는 Promise 생성
      await new Promise((resolve) => {
        synth.onvoiceschanged = resolve;
      });

      // 음성 데이터 가져오기
      const voices = synth.getVoices();

      // Default로 잡힌 voiceName 설정
      initializeSynthDefaultVoiceName(voices);

      // 음성 데이터의 이름만 추출하여 배열로 변환
      const voiceNames = voices.map((voice) => voice.name);

      // voiceNames에 음성 데이터 이름 배열 설정
      synthVoiceNames.value = voiceNames;
      isInitialized = true;
    } else {
      console.log("SpeechSynthesis API is not supported.");
    }
  };

  const speakText = (synthText) => {
    if (!isInitialized || !synthText) {
      console.error("The state value 'synthText' does not exist.");
      return;
    }

    const utterance = new SpeechSynthesisUtterance(synthText);
    const voices = window.speechSynthesis.getVoices();

    const voice = voices.find((v) => v.name === synthDefaultVoiceName.value);
    utterance.voice = voice;
    window.speechSynthesis.speak(utterance);
  };
  return {
    synthDefaultVoiceName,
    synthVoiceNames,
    initializeSpeechSynthesis,
    speakText,
  };
});
