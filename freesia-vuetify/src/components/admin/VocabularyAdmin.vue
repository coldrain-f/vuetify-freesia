<template>
  <v-card flat>
    <v-card-text>
      <v-table class="table-container">
        <thead>
          <tr>
            <th style="width: 55%">Title</th>
            <th style="width: 22.5%">Language</th>
            <th style="width: 22.5%">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="vocabulary in vocabularyPage.content" :key="vocabulary.id">
            <td>{{ vocabulary.title }}</td>
            <td>{{ vocabulary.language }}</td>
            <td>
              <v-btn
                variant="text"
                size="small"
                icon="mdi-trash-can-outline"
                @click="() => openDeleteDialog(vocabulary.id)"
              />
              <v-btn
                variant="text"
                size="small"
                icon="mdi-pencil-outline"
                @click="() => openUpdateDialog(vocabulary.id)"
              />
            </td>
          </tr>
        </tbody>
      </v-table>

      <v-row>
        <v-col cols="12">
          <v-pagination
            :length="vocabularyPage.totalPages"
            v-model="currentPage"
            @update:model-value="handlePageChange"
            rounded="circle"
          />
        </v-col>
      </v-row>
      <v-divider></v-divider>
    </v-card-text>
    <v-card-actions class="mt-5">
      <v-btn
        variant="flat"
        size="default"
        class="w-100"
        :color="themeStore.theme"
        @click="vocaDialogControl.showAddDialog = true"
      >
        <v-icon start icon="mdi-plus"></v-icon>
        Add item
      </v-btn>
    </v-card-actions>
  </v-card>
  <!-- 단어장 삭제 다이얼로그 -->
  <v-dialog v-model="vocaDialogControl.showDeleteDialog" width="500">
    <v-card>
      <template #title>
        <span class="noto-sans text-error"> ※ 단어장 삭제 </span>
      </template>
      <template #append>
        <v-btn
          variant="text"
          icon="mdi-close"
          @click="vocaDialogControl.showDeleteDialog = false"
        >
        </v-btn>
      </template>
      <v-card-text class="mt-5">
        <v-text-field
          readonly
          label="Title"
          v-model="vocabularyDeleteFormData.title"
        />
        <v-select
          label="Language"
          :items="['English', 'Japanese']"
          v-model="vocabularyDeleteFormData.language"
          readonly
        />
        <v-text-field
          readonly
          label="Subunit"
          v-model="vocabularyDeleteFormData.subunit"
        />
      </v-card-text>
      <v-card-actions class="d-flex justify-center">
        <v-btn color="error" style="width: 48%" @click="onClickDeleteButton">
          DELETE
        </v-btn>
        <v-btn
          style="width: 48%"
          @click="vocaDialogControl.showDeleteDialog = false"
        >
          CANCEL
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <!-- 단어장 수정 다이얼로그 -->
  <v-dialog v-model="vocaDialogControl.showUpdateDialog" width="500">
    <v-card>
      <template #title>
        <span class="noto-sans text-info"> ※ 단어장 수정 </span>
      </template>
      <template #append>
        <v-btn
          variant="text"
          icon="mdi-close"
          @click="vocaDialogControl.showUpdateDialog = false"
        >
        </v-btn>
      </template>
      <v-card-text class="mt-5">
        <v-text-field
          append-inner-icon="mdi-file-document-edit-outline"
          label="Title"
          v-model="vocabularyUpdateFormData.title"
        />
        <v-select
          label="Language"
          :items="['English', 'Japanese']"
          v-model="vocabularyUpdateFormData.language"
          readonly
        />
        <v-text-field
          label="Subunit"
          v-model="vocabularyUpdateFormData.subunit"
          readonly
        />
      </v-card-text>
      <v-card-actions class="d-flex justify-center">
        <v-btn color="info" style="width: 48%" @click="onClickUpdateButton">
          UPDATE
        </v-btn>
        <v-btn
          style="width: 48%"
          @click="vocaDialogControl.showUpdateDialog = false"
        >
          CANCEL
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <!-- 단어장 등록 다이얼로그 -->
  <v-dialog v-model="vocaDialogControl.showAddDialog" width="500">
    <v-card>
      <template #title>
        <span class="noto-sans text-primary"> ※ 단어장 등록 </span>
      </template>
      <template #append>
        <v-btn
          variant="text"
          icon="mdi-close"
          @click="vocaDialogControl.showAddDialog = false"
        >
        </v-btn>
      </template>
      <v-card-text class="mt-5">
        <v-text-field label="Title" v-model="vocabularyAddFormData.title" />
        <v-select
          label="Language"
          :items="['English', 'Japanese']"
          v-model="vocabularyAddFormData.language"
        />
      </v-card-text>
      <v-card-actions class="d-flex justify-center">
        <v-btn color="primary" style="width: 48%" @click="onClickAddButton">
          ADD
        </v-btn>
        <v-btn
          style="width: 48%"
          @click="vocaDialogControl.showAddDialog = false"
        >
          CANCEL
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { useThemeStore } from "@/stores/theme";
import { vocabularyService } from "@/service/vocabularyService";

const themeStore = useThemeStore();

// 단어장 Pageable
const vocabularyPage = ref({});

// Pagination Page
const currentPage = ref(1);

// 단어장 다이얼로그 컨트롤
const vocaDialogControl = reactive({
  showDeleteDialog: false,
  showUpdateDialog: false,
  showAddDialog: false,
});

// 단어장 등록 FormData
const vocabularyAddFormData = reactive({
  title: null,
  language: "English",
});

// 단어장 삭제 FormData
const vocabularyDeleteFormData = reactive({
  id: null,
  title: null,
  language: null,
  subunit: 0,
});

// 단어장 수정 FormData
const vocabularyUpdateFormData = reactive({
  id: null,
  title: null,
  language: null,
  subunit: 0,
});

// Pagination PageChange 이벤트 핸들러
const handlePageChange = async (pageNumber) => {
  vocabularyPage.value = await vocabularyService.searchVocabularyResponsePage({
    page: pageNumber - 1,
    size: 3,
  });
};

// 단어장 등록 버튼 클릭 이벤트
const onClickAddButton = async () => {
  try {
    await vocabularyService.registerVocabulary(vocabularyAddFormData);

    vocabularyPage.value =
      await vocabularyService.searchVocabularyResponsePage();

    Object.assign(vocabularyAddFormData, {
      title: null,
      language: "English",
    });
    vocaDialogControl.showAddDialog = false;
    currentPage.value = 1;

    setTimeout(() => {
      // Todo: 공통 Dialog로 Alert을 띄우도록 변경 필요.
      alert("단어장 등록을 성공했습니다.");
    }, 200);
  } catch (err) {
    console.error(err);
    alert("단어장 등록을 실패했습니다.");
  }
};

// 단어장 삭제 다이얼로그 Open
const openDeleteDialog = async (vocabularyId) => {
  Object.assign(
    vocabularyDeleteFormData,
    await vocabularyService.searchOneVocabularyResponse(vocabularyId)
  );
  vocaDialogControl.showDeleteDialog = true;
};

// 단어장 삭제 버튼 클릭 이벤트
const onClickDeleteButton = async () => {
  try {
    await vocabularyService.removeVocabulary(vocabularyDeleteFormData.id);
    vocaDialogControl.showDeleteDialog = false;

    Object.assign(vocabularyDeleteFormData, {
      id: null,
      title: null,
      language: null,
      subunit: 0,
    });

    // 삭제나 수정한 단어장의 페이지를 조회해 본다.
    vocabularyPage.value = await vocabularyService.searchVocabularyResponsePage(
      {
        page: currentPage.value - 1,
        size: 3,
      }
    );

    // 조회해본 페이지가 데이터가 없으면
    if (vocabularyPage.value.content.length <= 0) {
      // 앞 페이지로 이동하고, 조회해본 페이지가 1페이지 라면 앞 페이지가 없으므로 1페이지로 설정한다.
      currentPage.value = currentPage.value === 1 ? 1 : currentPage.value - 1;
    }

    // 조회해본 페이지가 데이터가 있으면 그대로 조회
    await handlePageChange(currentPage.value);

    setTimeout(() => {
      alert("단어장 삭제를 성공했습니다.");
    }, 200);
  } catch (err) {
    console.error(err);
    alert("단어장 삭제를 실패했습니다.");
  }
};

// 단어장 수정 다이얼로그 Open
const openUpdateDialog = async (vocabularyId) => {
  Object.assign(
    vocabularyUpdateFormData,
    await vocabularyService.searchOneVocabularyResponse(vocabularyId)
  );
  vocaDialogControl.showUpdateDialog = true;
};

// 단어장 수정 버튼 클릭 이벤트
const onClickUpdateButton = async () => {
  try {
    await vocabularyService.modifyVocabulary(vocabularyUpdateFormData);
    vocaDialogControl.showUpdateDialog = false;

    await handlePageChange(currentPage.value);

    setTimeout(() => {
      alert("단어장 수정을 성공했습니다.");
    }, 200);
  } catch (err) {
    console.error(err);
    alert("단어장 수정을 실패했습니다.");
  }
};

onMounted(async () => {
  // 단어장 관리 초기 진입 시 단어장 Data Table Rendering.
  vocabularyPage.value = await vocabularyService.searchVocabularyResponsePage();
});
</script>

<style>
.table-container {
  /* 가로, 세로 스크롤 생성 */
  overflow: auto;
  /* 테이블 데이터의 줄 바꿈 제거 */
  white-space: nowrap;

  height: 215px;
  width: 100%;
}
</style>
