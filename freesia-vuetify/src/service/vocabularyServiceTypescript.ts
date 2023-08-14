import type { PageableParams } from "@/@types/pageableParams";
import type { Vocabulary } from "@/@types/vocabulary";
import type { AxiosInstance } from "axios";
import { $axios } from "@/common/axios";

export default class VocabularyService {
  private axios: AxiosInstance;

  public constructor() {
    this.axios = $axios;
  }

  /**
   * 단어장 검색 API
   *
   * @param {PageableParams}
   * @param {searchCondition}
   * @returns {Vocabulary[]}
   */
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

  /**
   * 단어장 등록 API
   *
   * @param {VocaRegistrationFormData}
   * @returns {number} 등록 완료된 단어장의 고유 번호 - VOCABULARY_ID
   */
  public async register(formData: VocaRegistrationFormData): Promise<number> {
    const response = await this.axios.post<number>("/vocabulary", formData);
    return response.data;
  }

  /**
   * 단어장 번호로 삭제 API

   * @param vocabularyId
   */
  public async deleteById(vocabularyId: number): Promise<void> {
    await this.axios.delete(`/vocabulary/${vocabularyId}`);
  }
}

type SearchCondition = {
  language?: string;
};

type VocaRegistrationFormData = {
  title: string;
  language: string;
};
