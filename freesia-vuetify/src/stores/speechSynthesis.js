import { defineStore } from "pinia";
import { ref } from "vue";

export const useSpeechSynthesisStore = defineStore("speechSynthesis", () => {
  const synthDefaultVoiceName = ref(null);
  const synthVoiceNames = ref([]);

  // Synth 전역 설정
  const synth = ref(null);

  let isInitialized = false;

  const initializeCodeMap = (codeMap) => {
    if (!codeMap) {
      console.error("codeMap not found error.");
    }
    codeMap.set("English", "en");
    codeMap.set("Japanese", "ja");
    codeMap.set("Korean", "ko");
  };

  const convertLanguageCode = (lang) => {
    const codeMap = new Map();
    initializeCodeMap(codeMap);
    const findLang = codeMap.get(lang);
    if (!findLang) {
      console.error("lang not found error.");
    }
    return findLang;
  };

  const getVoiceNamesByLang = (lang) => {
    const code = convertLanguageCode(lang);
    const voiceNames = synth.value
      .getVoices()
      .filter((v) => v.lang.startsWith(code))
      .map((v) => v.name);

    if (!voiceNames) {
      console.error("text-to-speech not found.");
      synthVoiceNames.value = "No data available";
      return;
    }
    return voiceNames;
  };

  const initializeSpeechSynthesis = async () => {
    if ("speechSynthesis" in window) {
      synth.value = window.speechSynthesis;

      // 음성 데이터가 변경될 때까지 기다리는 Promise 생성
      await new Promise((resolve) => {
        synth.value.onvoiceschanged = resolve;
      });

      // 음성 데이터 가져오기 -- 임시 제거
      // const voices = synth.value.getVoices();

      // Default로 잡힌 voiceName 설정 -- 임시 제거
      // initializeSynthDefaultVoiceName(voices);

      // 음성 데이터의 이름만 추출하여 배열로 변환 -- 임시 제거
      // const voiceNames = voices.map((voice) => voice.name);

      // voiceNames에 음성 데이터 이름 배열 설정 -- 임시 제거
      // synthVoiceNames.value = voiceNames;

      // 영어를 Default로 설정한다. (영어만 할 경우 주석 해제)
      // const voiceNames = synth.value
      //   .getVoices()
      //   .filter((v) => v.lang.startsWith("en"))
      //   .map((v) => v.name);

      // ALL
      const voiceNames = synth.value.getVoices().map((v) => v.name);

      // 없으면 시스템 Default Voice로 설정
      if (!voiceNames) {
        synth.value.forEach((voice) => {
          if (voice.default) {
            synthDefaultVoiceName.value = voice.name;
          }
        });
        isInitialized = true;
        return;
      }

      synthVoiceNames.value = voiceNames;
      synthDefaultVoiceName.value = voiceNames[0];

      isInitialized = true;
    } else {
      console.log("SpeechSynthesis API is not supported.");
    }
  };

  const speakText = (synthText) => {
    if (!isInitialized || !synthText) {
      //console.error("The state value 'synthText' does not exist.");
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
    getVoiceNamesByLang,
    initializeSpeechSynthesis,
    speakText,
  };
});
