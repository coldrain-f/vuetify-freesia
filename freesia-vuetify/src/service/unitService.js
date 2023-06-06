import { $axios } from "@/common/axios";

export const unitService = {
  /** 단위 목록 조회 API */
  searchUnitResponsePage: async (pageable = { page: 0, size: 3 }) => {
    const { page, size } = pageable;
    const response = await $axios.get(`/units?page=${page}&size=${size}`);
    return response.data;
  },
};
