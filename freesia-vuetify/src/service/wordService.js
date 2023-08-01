import { $axios } from "@/common/axios";

export const wordService = {
  /** 단어 페이지 조회 API */
  getPageable: async (unitId = 0, pageable = { page: 0, size: 3 }) => {
    const { page, size } = pageable;
    const response = await $axios.get(
      `/units/${unitId}/words?page=${page}&size=${size}`
    );

    return response.data;
  },

  /** 학습 단어 조회 API */
  findLearningWords: async (unitList) => {
    const response = await $axios.get(`/words/learning`, {
      params: {
        unitList: unitList.join(","),
      },
    });
    return response.data;
  },

  finish: async (plannerDetailId) => {
    await $axios.post(`/words/learning/${plannerDetailId}/finished`);
  },

  /** 단어 등록 API */
  register: async (unitId, formData) => {
    const { studyWord, nativeWord, furigana, partOfSpeech } = formData;
    const response = await $axios.post(`/units/${unitId}/words`, {
      studyWord,
      nativeWord,
      furigana,
      partOfSpeech,
    });

    return response.data;
  },

  /** 단어 수정 API */
  modifyById: async (wordId, formData) => {
    const { studyWord, nativeWord, furigana, partOfSpeech } = formData;
    await $axios.patch(`/words/${wordId}`, {
      studyWord,
      nativeWord,
      furigana,
      partOfSpeech,
    });
  },

  /** 단어 삭제 API */
  removeById: async (wordId) => {
    await $axios.delete(`/words/${wordId}`);
  },
};
