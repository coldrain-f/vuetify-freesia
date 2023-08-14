import type { Language } from "@/@types/language";
import type { AxiosInstance } from "axios";
import { $axios } from "@/common/axios";

export default class LanguageService {
  private axios: AxiosInstance;

  public constructor() {
    this.axios = $axios;
  }

  /**
   * DB에 등록 된 모든 언어 조회 API
   *
   * @returns {Language[]}
   */
  public async findAll(): Promise<Language[]> {
    const response = await this.axios.get<Language[]>("/language");
    return response.data;
  }
}
