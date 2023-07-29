import { $axios } from "@/common/axios";

export class PlannerService {
  async register(vocabularyId) {
    await $axios.post(`/vocabulary/${vocabularyId}/planner`);
  }

  async findAllByVocabularyId(vocabularyId) {
    const response = await $axios.get(
      `/vocabulary/${vocabularyId}/planner/planner-details`
    );
    return response.data;
  }

  async checkDuplicate(vocabularyId) {
    const response = await $axios.post(
      `/vocabulary/${vocabularyId}/planner/checkDuplicate`
    );
    return response.data;
  }

  async bulkUpdate(plannerDetails) {
    await $axios.patch(`/planner/planner-details`, plannerDetails);
  }
}
