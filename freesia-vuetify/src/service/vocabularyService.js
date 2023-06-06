import { $axios } from "@/common/axios";

export const vocabularyService = {
  /** 단어장 목록 페이지 조회 API */
  searchVocabularyResponsePage: async (pageable = { page: 0, size: 3 }) => {
    const { page, size } = pageable;
    const response = await $axios.get(`/vocabulary?page=${page}&size=${size}`);
    return response.data;
  },

  /** 단어장 번호로 단건 조회 API */
  searchOneVocabularyResponse: async (vocabularyId) => {
    const response = await $axios.get(`/vocabulary/${vocabularyId}`);
    return response.data;
  },

  /** 단어장 등록 API */
  registerVocabulary: async (formData) => {
    const { title, language } = formData;
    const response = await $axios.post("/vocabulary", {
      title,
      language,
    });
    return response.data;
  },

  /** 단어장 삭제 API */
  removeVocabulary: async (vocabularyId) => {
    await $axios.delete(`/vocabulary/${vocabularyId}`);
  },

  /** 단어장 수정 API */
  modifyVocabulary: async (formData) => {
    const { id, title } = formData;
    await $axios.patch(`/vocabulary/${id}`, { title });
  },

  /** 단어장 Total 값 반환 */
  getVocabularyTotalELements: async () => {
    const page = await vocabularyService.searchVocabularyResponsePage();
    return page.totalElements;
  },

  /** 모든 단어장 목록 조회  */
  getAllVocabularyList: async () => {
    const page = await vocabularyService.searchVocabularyResponsePage({
      page: 0,
      size: await vocabularyService.searchVocabularyResponsePage()
        .totalElements,
    });
    return page.content;
  },
};
