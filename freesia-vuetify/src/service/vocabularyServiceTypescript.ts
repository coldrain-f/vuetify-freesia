import type { PageableParams } from "@/@types/pageableParams";
import type { Vocabulary } from "@/@types/vocabulary";
import type { AxiosInstance } from "axios";
import { $axios } from "@/common/axios";

type SearchCondition = {
  language?: string;
};

export default class VocabularyService {
  private axios: AxiosInstance;

  public constructor() {
    this.axios = $axios;
  }

  public async search(
    pageableParams: PageableParams = { page: 0, size: 3 },
    searchCondition: SearchCondition = { language: "English" },
  ): Promise<Vocabulary[]> {
    const response = await this.axios.get<Vocabulary[]>("/vocabulary", {
      params: {
        pageableParams,
        searchCondition,
      },
    });

    return response.data;
  }
}
