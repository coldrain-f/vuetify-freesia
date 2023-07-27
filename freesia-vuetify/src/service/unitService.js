import { $axios } from "@/common/axios";

export const unitService = {
  /** 단위 목록 페이지 조회 API */
  getPageable: async (vocabularyId = 0, pageable = { page: 0, size: 3 }) => {
    const { page, size } = pageable;
    const response = await $axios.get(
      `/vocabulary/${vocabularyId}/units?page=${page}&size=${size}`
    );
    return response.data;
  },

  /** 단위 등록 API */
  register: async (vocabularyId, formData) => {
    const { subject } = formData;
    const response = await $axios.post(`/vocabulary/${vocabularyId}/units`, {
      subject,
    });
    return response.data;
  },

  /** 단위 삭제 API */
  removeById: async (unitId) => {
    await $axios.delete(`/units/${unitId}`);
  },

  /** 단위 수정 API */
  modifyById: async (unitId, formData) => {
    const { subject } = formData;
    await $axios.patch(`/units/${unitId}`, {
      subject,
    });
  },
};
