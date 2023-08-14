import type { Language } from "@/@types/language";
import type { AxiosInstance } from "axios";
import { $axios } from "@/common/axios";

export default class LanguageService {
  private axios: AxiosInstance;

  public constructor() {
    this.axios = $axios;
  }

  public async findAll(): Promise<Language[]> {
    const response = await this.axios.get<Language[]>("/language");
    return response.data;
  }
}
