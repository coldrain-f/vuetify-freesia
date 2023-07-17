import { $axios } from "@/common/axios";

export const vocabularyService = {
  /** 단어장 목록 페이지 조회 API */
  getPageable: async (pageable = { page: 0, size: 3 }) => {
    const { page, size } = pageable;
    const response = await $axios.get(`/vocabulary?page=${page}&size=${size}`);
    return response.data;
  },

  /** 단어장 번호로 단건 조회 API */
  findById: async (vocabularyId) => {
    const response = await $axios.get(`/vocabulary/${vocabularyId}`);
    return response.data;
  },

  /** 단어장 등록 API */
  register: async (formData) => {
    const response = await $axios.post("/vocabulary", formData);
    const savedVocaId = response.data;
    return savedVocaId;
  },

  /** 단어장 삭제 API */
  deleteById: async (vocabularyId) => {
    await $axios.delete(`/vocabulary/${vocabularyId}`);
  },

  /** 단어장 수정 API */
  modify: async (formData) => {
    const { id, title } = formData;
    await $axios.patch(`/vocabulary/${id}`, { title });
  },
};
