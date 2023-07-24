import { $axios } from "@/common/axios";

export const wordService = {
  /** 단어 페이지 조회 API */
  getPageable: async (unitId, pageable = { page: 0, size: 3 }) => {
    const { page, size } = pageable;
    const response = await $axios.get(
      `/units/${unitId}/words?page=${page}&size=${size}`
    );

    return response.data;
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
    const { studyWord, nativeWord, partOfSpeech } = formData;
    await $axios.patch(`/words/${wordId}`, {
      studyWord,
      nativeWord,
      partOfSpeech,
    });
  },

  /** 단어 삭제 API */
  removeById: async (wordId) => {
    await $axios.delete(`/words/${wordId}`);
  },
};
