import { $axios } from "@/common/axios";
import type { Language } from "@/@types/language";
import type { AxiosInstance } from "axios";

export default class LanguageService {
  private axios: AxiosInstance;

  public constructor() {
    this.axios = $axios;
  }

  public async findAll(): Promise<Language[]> {
    const response = await this.axios.get("/language");
    return response.data as Language[];
  }
}