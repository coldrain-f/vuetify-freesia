<template>
  <v-card flat>
    <v-card-text>
      <v-table class="table-container">
        <thead>
          <tr>
            <th style="width: 32.5%">Study Word</th>
            <th style="width: 32.5%">Native Word</th>
            <th style="width: 10%">POS</th>
            <th style="width: 25%">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="word in wordPage.content" :key="word.id">
            <td>{{ word.studyWord }}</td>
            <td>{{ word.nativeWord }}</td>
            <td>{{ word.partOfSpeech }}</td>
            <td>
              <v-btn
                variant="text"
                size="small"
                icon="mdi-trash-can-outline"
                @click="() => (wordDialogControl.showDeleteDialog = true)"
              />
              <v-btn
                variant="text"
                size="small"
                icon="mdi-pencil-outline"
                @click="() => (wordDialogControl.showUpdateDialog = true)"
              />
            </td>
          </tr>
        </tbody>
      </v-table>

      <v-row>
        <v-col cols="12">
          <v-pagination
            :length="wordPage.totalPages"
            v-model="currentPage"
            rounded="circle"
            @update:model-value="handlePageChange"
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
        @click="() => (wordDialogControl.showAddDialog = true)"
      >
        <v-icon start icon="mdi-plus"></v-icon>
        Add item
      </v-btn>
    </v-card-actions>
  </v-card>

  <!-- 단어 삭제 다이얼로그 -->
  <v-dialog v-model="wordDialogControl.showDeleteDialog" width="500">
    <v-card>
      <template #title>
        <span class="noto-sans text-error"> ※ 단어 삭제 </span>
      </template>
      <template #append>
        <v-btn
          variant="text"
          icon="mdi-close"
          @click="() => (wordDialogControl.showDeleteDialog = false)"
        >
        </v-btn>
      </template>
      <v-card-text class="mt-5">
        <v-text-field readonly label="Study Word" model-value="spice" />
        <v-text-field readonly label="Native Word" model-value="양념" />
        <v-select
          :items="['adj', 'v', 'n']"
          model-value="n"
          label="Part of speech"
          readonly
        />
      </v-card-text>
      <v-card-actions class="d-flex justify-center">
        <v-btn color="error" style="width: 48%"> DELETE </v-btn>
        <v-btn
          style="width: 48%"
          @click="() => (wordDialogControl.showDeleteDialog = false)"
        >
          CANCEL
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <!-- 단어 수정 다이얼로그 -->
  <v-dialog v-model="wordDialogControl.showUpdateDialog" width="500">
    <v-card>
      <template #title>
        <span class="noto-sans text-info"> ※ 단어 수정 </span>
      </template>
      <template #append>
        <v-btn
          variant="text"
          icon="mdi-close"
          @click="() => (wordDialogControl.showUpdateDialog = false)"
        >
        </v-btn>
      </template>
      <v-card-text class="mt-5">
        <v-text-field
          label="Study Word"
          model-value="spice"
          append-inner-icon="mdi-file-document-edit-outline"
        />
        <v-text-field
          label="Native Word"
          model-value="양념"
          append-inner-icon="mdi-file-document-edit-outline"
        />
        <v-select
          append-inner-icon="mdi-file-document-edit-outline"
          model-value="n"
          label="Part of speech"
          :items="['adj', 'v', 'n']"
        />
      </v-card-text>
      <v-card-actions class="d-flex justify-center">
        <v-btn color="info" style="width: 48%"> UPDATE </v-btn>
        <v-btn
          style="width: 48%"
          @click="() => (wordDialogControl.showUpdateDialog = false)"
        >
          CANCEL
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <!-- 단어 등록 다이얼로그 -->
  <v-dialog v-model="wordDialogControl.showAddDialog" width="500">
    <v-card>
      <template #title>
        <span class="noto-sans text-primary"> ※ 단어 등록 </span>
      </template>
      <template #append>
        <v-btn
          variant="text"
          icon="mdi-close"
          @click="() => (wordDialogControl.showAddDialog = false)"
        >
        </v-btn>
      </template>
      <v-card-text class="mt-5">
        <v-text-field label="Study Word" v-model="wordAddFormData.studyWord" />
        <v-text-field
          label="Native Word"
          v-model="wordAddFormData.nativeWord"
        />
        <v-select
          label="Part of speech"
          v-model="wordAddFormData.partOfSpeech"
          :items="['adj', 'v', 'n']"
        />
      </v-card-text>
      <v-card-actions class="d-flex justify-center">
        <v-btn color="primary" style="width: 48%" @click="onClickAddButton">
          ADD
        </v-btn>
        <v-btn
          style="width: 48%"
          @click="() => (wordDialogControl.showAddDialog = false)"
        >
          CANCEL
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { onMounted, reactive, ref, watch } from "vue";
import { useThemeStore } from "@/stores/theme";
import { wordService } from "@/service/wordService";
import { unitService } from "@/service/unitService";
import { useAdminHomeStore } from "@/stores/adminHome";
import { storeToRefs } from "pinia";

const themeStore = useThemeStore();
const adminHomeStore = useAdminHomeStore();

const { selectedUnit, selectedVocabulary, unitItems } =
  storeToRefs(adminHomeStore);

// 단어 다이얼로그 컨트롤
const wordDialogControl = reactive({
  showDeleteDialog: false,
  showUpdateDialog: false,
  showAddDialog: false,
});

// Word Pageable
const wordPage = ref({});

// Pagination Page
const currentPage = ref(1);

// 단어 등록 FormData
const wordAddFormData = reactive({
  studyWord: null,
  nativeWord: null,
  partOfSpeech: "adj",
});

// Pagination PageChange 이벤트 핸들러
const handlePageChange = async (pageNumber) => {
  wordPage.value = await wordService.searchWordResponsePage(
    selectedUnit.value.value,
    {
      page: pageNumber - 1,
      size: 3,
    }
  );
};

// 단어 등록 클릭 이벤트
const onClickAddButton = async () => {
  const savedId = await wordService.registerWord(
    selectedUnit.value.value,
    wordAddFormData
  );

  wordDialogControl.showAddDialog = false;

  wordPage.value = await wordService.searchWordResponsePage(
    selectedUnit.value.value
  );
  currentPage.value = 1;

  Object.assign(wordAddFormData, {
    studyWord: null,
    nativeWord: null,
    partOfSpeech: "adj",
  });

  console.log(`savedId = ${savedId}`);
};

// watch
watch(
  () => selectedUnit.value.value,
  async () => {
    const selectedUnitId = selectedUnit.value.value;
    Object.assign(
      wordPage.value,
      await wordService.searchWordResponsePage(selectedUnitId, {
        page: 0,
        size: 3,
      })
    );
    currentPage.value = 1;
  }
);

watch(
  () => selectedVocabulary.value.value,
  async () => {
    // Unit Select Rendering...
    const unitPage = await unitService.searchUnitResponsePage(
      selectedVocabulary.value.value,
      {
        page: 0,
        size: 2000,
      }
    );
    unitItems.value = unitPage.content.map((u) => {
      return {
        subject: u.subject,
        value: u.id,
      };
    });
    Object.assign(selectedUnit.value, {
      subject: unitItems.value[0].subject,
      value: unitItems.value[0].value,
    });
  }
);

onMounted(async () => {
  if (!selectedUnit.value.value) {
    return;
  }
  wordPage.value = await wordService.searchWordResponsePage(
    selectedUnit.value.value,
    { page: 0, size: 3 }
  );
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
