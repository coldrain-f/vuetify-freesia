import { $axios } from "@/common/axios";

export const languageService = {
  findAll: async () => {
    const response = await $axios.get("/language");
    return response.data;
  },
};
