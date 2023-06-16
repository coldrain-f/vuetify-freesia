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
                @click="() => openWordDeleteDialog(word.id)"
              />
              <v-btn
                variant="text"
                size="small"
                icon="mdi-pencil-outline"
                @click="() => openWordUpdateDialog(word.id)"
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
        :disabled="selectedUnit.subject === 'No data available'"
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
        <v-text-field
          readonly
          label="Study Word"
          v-model="wordDeleteFormData.studyWord"
        />
        <v-text-field
          readonly
          label="Native Word"
          v-model="wordDeleteFormData.nativeWord"
        />
        <v-select
          :items="['adj', 'v', 'n']"
          v-model="wordDeleteFormData.partOfSpeech"
          label="Part of speech"
          readonly
        />
      </v-card-text>
      <v-card-actions class="d-flex justify-center">
        <v-btn color="error" style="width: 48%" @click="onClickWordDelete">
          DELETE
        </v-btn>
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
          v-model="wordUpdateFormData.studyWord"
          append-inner-icon="mdi-file-document-edit-outline"
        />
        <v-text-field
          label="Native Word"
          v-model="wordUpdateFormData.nativeWord"
          append-inner-icon="mdi-file-document-edit-outline"
        />
        <v-select
          append-inner-icon="mdi-file-document-edit-outline"
          v-model="wordUpdateFormData.partOfSpeech"
          label="Part of speech"
          :items="['adj', 'v', 'n']"
        />
      </v-card-text>
      <v-card-actions class="d-flex justify-center">
        <v-btn color="info" style="width: 48%" @click="onClickUpdateButton">
          UPDATE
        </v-btn>
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
import { utils } from "@/common/utils";
import { storeToRefs } from "pinia";
import { useCommonStore } from "@/stores/common";

const themeStore = useThemeStore();
const adminHomeStore = useAdminHomeStore();
const commonStore = useCommonStore();

const { VDialogMessage } = commonStore;

const { selectedUnit, selectedVocabulary, unitItems, wordPage } =
  storeToRefs(adminHomeStore);

// 단어 다이얼로그 컨트롤
const wordDialogControl = reactive({
  showDeleteDialog: false,
  showUpdateDialog: false,
  showAddDialog: false,
});

// Pagination Page
const currentPage = ref(1);

// 단어 등록 FormData
const wordAddFormData = reactive({
  studyWord: null,
  nativeWord: null,
  partOfSpeech: "adj",
});

// 단어 수정 FormData
const wordUpdateFormData = reactive({
  studyWord: null,
  nativeWord: null,
  partOfSpeech: "adj",
});

// 단어 삭제 FormData
const wordDeleteFormData = reactive({
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
  try {
    await wordService.registerWord(selectedUnit.value.value, wordAddFormData);

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

    setTimeout(() => {
      VDialogMessage("단어 등록을 완료했습니다.");
    }, 200);
  } catch (err) {
    console.error(err);

    setTimeout(() => {
      VDialogMessage("단어 등록을 실패했습니다.");
    }, 200);
  }
};

// 단어 삭제 다이얼로그 오픈
const openWordDeleteDialog = async (wordId) => {
  Object.assign(
    wordDeleteFormData,
    await wordService.searchOneWordResponse(wordId)
  );

  wordDialogControl.showDeleteDialog = true;
};

// 단어 삭제 클릭 이벤트
const onClickWordDelete = async () => {
  try {
    await wordService.removeWord(wordDeleteFormData.id);

    Object.assign(wordDeleteFormData, {
      studyWord: null,
      nativeWord: null,
      partOfSpeech: "adj",
    });

    wordDialogControl.showDeleteDialog = false;

    // 삭제나 수정한 단어장의 페이지를 조회해 본다.
    wordPage.value = await wordService.searchWordResponsePage(
      selectedUnit.value.value,
      {
        page: currentPage.value - 1,
        size: 3,
      }
    );

    // 조회해본 페이지가 데이터가 없으면
    if (wordPage.value.content.length <= 0) {
      // 앞 페이지로 이동하고, 조회해본 페이지가 1페이지 라면 앞 페이지가 없으므로 1페이지로 설정한다.
      currentPage.value = currentPage.value === 1 ? 1 : currentPage.value - 1;
    }

    // 조회해본 페이지가 데이터가 있으면 그대로 조회
    await handlePageChange(currentPage.value);

    setTimeout(() => {
      VDialogMessage("단어 삭제를 완료했습니다.");
    });
  } catch (err) {
    console.error(err);

    setTimeout(() => {
      VDialogMessage("단어 삭제를 실패했습니다.");
    });
  }
};

// 단어 수정 다이얼로그 오픈
const openWordUpdateDialog = async (wordId) => {
  Object.assign(
    wordUpdateFormData,
    await wordService.searchOneWordResponse(wordId)
  );

  wordDialogControl.showUpdateDialog = true;
};

// 단위 수정 버튼 클릭 이벤트
const onClickUpdateButton = async () => {
  try {
    await wordService.modifyWord(wordUpdateFormData.id, wordUpdateFormData);

    Object.assign(wordUpdateFormData, {
      id: null,
      subject: null,
      wordCount: 0,
    });

    wordDialogControl.showUpdateDialog = false;

    wordPage.value = await wordService.searchWordResponsePage(
      selectedUnit.value.value
    );

    setTimeout(() => {
      VDialogMessage("단어 수정을 완료했습니다.");
    }, 200);
  } catch (err) {
    console.error(err);

    setTimeout(() => {
      VDialogMessage("단어 수정을 실패했습니다.");
    }, 200);
  }
};

// watch
watch(
  () => selectedUnit.value.value,
  async () => {
    // 선택된 단위(Unit)가 없다면 단어 Pageable 초기화
    if (!selectedUnit.value.value) {
      wordPage.value.content = [];
      wordPage.value.totalPages = 0;
      return;
    }
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

    // 선택된 단어장에 생성된 단위(Unit)가 없으면 "No data available"
    if (utils.isEmptyArray(unitPage.content)) {
      Object.assign(selectedUnit.value, {
        subject: "No data available",
        value: null,
      });
      return;
    }

    // 있으면 Default 값으로 맨 첫 번째 단위(Unit)로 설정한다.
    Object.assign(selectedUnit.value, {
      subject: unitItems.value[0].subject,
      value: unitItems.value[0].value,
    });
  }
);

onMounted(async () => {});
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
