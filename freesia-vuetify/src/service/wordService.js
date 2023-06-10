import { $axios } from "@/common/axios";

export const wordService = {
  /** 단어 등록 API */
  registerWord: async (unitId, formData) => {
    const { studyWord, nativeWord, partOfSpeech } = formData;

    const response = await $axios.post(`/units/${unitId}/words`, {
      studyWord,
      nativeWord,
      partOfSpeech,
    });
    return response.data;
  },

  /** 단어 페이지 조회 API */
  searchWordResponsePage: async (unitId, pageable = { page: 0, size: 3 }) => {
    const { page, size } = pageable;
    const response = await $axios.get(
      `/units/${unitId}/words?page=${page}&size=${size}`
    );
    return response.data;
  },
};
