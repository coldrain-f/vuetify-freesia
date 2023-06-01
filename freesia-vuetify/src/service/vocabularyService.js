import { $axios } from "@/common/axios";

export const vocabularyService = {
  /** 모든 단어장 조회 API */
  findAll: async () => {
    const response = await $axios.get("/vocabulary");
    return response.data;
  },

  /** 단어장 등록 API */
  registerVocabulary: async (formData) => {
    const response = await $axios.post("/vocabulary", {
      formData,
    });
    return response.data;
  },
};
