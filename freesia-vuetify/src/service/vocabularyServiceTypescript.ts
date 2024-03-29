import type { PageableParams } from "@/@types/pageableParams";
import type { AxiosInstance } from "axios";
import { $axios } from "@/common/axios";
import type {
  VocaModificationFormData,
  VocaRegistrationFormData,
  VocaSearchCondition,
  Vocabulary,
} from "vocabularyTypes";

export default class VocabularyService {
  private axios: AxiosInstance;

  public constructor() {
    this.axios = $axios;
  }

  /**
   * 단어장 검색 API
   *
   * @param {PageableParams}
   * @param {VocaSearchCondition}
   * @returns {Vocabulary[]}
   */
  public async search(
    pageableParams: PageableParams = { page: 0, size: 3 },
    searchCondition: VocaSearchCondition = { language: "English" },
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
   * 번호에 해당하는 단어장 삭제 API

   * @param {number}
   */
  public async removeById(vocabularyId: number): Promise<void> {
    await this.axios.delete(`/vocabulary/${vocabularyId}`);
  }

  /**
   * 번호에 해당하는 단어장 수정 API
   *
   * @param {number}
   * @param {VocaModificationFormData}
   */
  public async modifyById(vocabularyId: number, formData: VocaModificationFormData): Promise<void> {
    await $axios.patch(`/vocabulary/${vocabularyId}`, formData);
  }
}
