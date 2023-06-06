import { $axios } from "@/common/axios";

export const unitService = {
  /** 단위 목록 조회 API */
  searchUnitResponsePage: async (
    vocabularyId,
    pageable = { page: 0, size: 3 }
  ) => {
    const { page, size } = pageable;
    const response = await $axios.get(
      `/vocabulary/${vocabularyId}/units?page=${page}&size=${size}`
    );
    return response.data;
  },

  /** 모든 단위 목록 조회 API */
  getAllUnitList: async (vocabularyId) => {
    const totalElements = await unitService.searchUnitResponsePage(vocabularyId)
      .totalElements;

    // 나중에 response로 받지 않고 바로 .data.content를 하는 경우도 되는지 확인
    const response = await $axios.get(
      `/vocabulary/${vocabularyId}/units?page=0&size=${totalElements}`
    );
    return response.data.content;
  },
};
