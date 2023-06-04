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
                @click="onClickDeleteButton(vocabulary.id)"
              ></v-btn>
              <v-btn
                variant="text"
                size="small"
                icon="mdi-pencil-outline"
                @click="vocaDialogControl.showUpdateDialog = true"
              ></v-btn>
            </td>
          </tr>
        </tbody>
      </v-table>

      <v-row>
        <v-col cols="12">
          <v-pagination
            :length="vocabularyPage.totalPages"
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
          model-value="단어가 읽기다 기본편"
        ></v-text-field>
        <v-select
          label="Language"
          :items="['English', 'Japanese']"
          model-value="English"
          readonly
        ></v-select>
        <v-text-field
          readonly
          label="Subunit"
          model-value="40개"
        ></v-text-field>
      </v-card-text>
      <v-card-actions class="d-flex justify-center">
        <v-btn color="error" style="width: 48%"> DELETE </v-btn>
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
          model-value="단어가 읽기다 기본편"
        ></v-text-field>
        <v-select
          label="Language"
          :items="['English', 'Japanese']"
          model-value="English"
          readonly
        ></v-select>
        <v-text-field
          label="Subunit"
          model-value="40개"
          readonly
        ></v-text-field>
      </v-card-text>
      <v-card-actions class="d-flex justify-center">
        <v-btn color="info" style="width: 48%"> UPDATE </v-btn>
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

// 단어장 다이얼로그 컨트롤
const vocaDialogControl = reactive({
  showDeleteDialog: false,
  showUpdateDialog: false,
  showAddDialog: false,
});

const vocabularyAddFormData = reactive({
  title: null,
  language: "English",
});

const vocabularyPage = ref({});

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

    vocaDialogControl.showAddDialog = false;

    // Todo: 공통 Dialog로 Alert을 띄우도록 변경 필요.
    alert("단어장 등록을 성공했습니다.");
  } catch (err) {
    console.error(err);
    alert("단어장 등록을 실패했습니다.");
  }
};

// 단어장 삭제 버튼 클릭 이벤트
const onClickDeleteButton = async (vocabularyId) => {
  vocaDialogControl.showDeleteDialog = true;
};

onMounted(async () => {
  // 단어장 DB 조회 후 Render
  vocabularyPage.value = await vocabularyService.searchVocabularyResponsePage();
});
</script>

<style>
.table-container {
  overflow: auto;
  white-space: nowrap;
  height: 215px;
  width: 100%;
}
</style>
