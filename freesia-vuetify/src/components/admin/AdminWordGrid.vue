<template>
  <v-container>
    <v-row>
      <v-col cols="5">
        <v-select
          label="Language"
          variant="underlined"
          density="compact"
          v-model="languageSelectManager.selectedItem"
          :items="languageSelectManager.items"
          item-title="name"
          item-value="name"
          hide-details
          @update:model-value="(changedLanguage) => emit('handleLanguageChange', changedLanguage)"
        >
        </v-select>
      </v-col>
      <v-col cols="7">
        <v-select
          label="Voca"
          variant="underlined"
          density="compact"
          v-model="vocabularySelectManager.selectedItem"
          :items="vocabularySelectManager.items"
          item-title="title"
          item-value="id"
          return-object
          hide-details
          @update:model-value="
            (changedVocabulary) => emit('handleVocabularyChange', changedVocabulary)
          "
        >
        </v-select>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-select
          label="Unit"
          variant="underlined"
          density="compact"
          v-model="unitSelectManager.selectedItem"
          :items="unitSelectManager.items"
          item-title="subject"
          item-value="id"
          return-object
          hide-details
        >
        </v-select>
      </v-col>
    </v-row>
    <v-row class="mt-5">
      <v-col cols="4">
        <v-btn size="small" class="w-100" @click="performSearch">
          <v-icon start icon="mdi-note-search-outline" class="margin-top-1px"> </v-icon>
          SEARCH
        </v-btn>
      </v-col>
      <v-col cols="8" class="text-end">
        <v-btn
          size="small"
          color="primary"
          :disabled="!isSearchPerformed"
          @click="showWordAddDialog = true"
        >
          <v-icon start icon="mdi-note-plus-outline"> </v-icon>
          ADD
        </v-btn>
        <v-btn
          size="small"
          color="info"
          class="ml-2"
          :disabled="isEmptyObject(selectedWord) || !isSearchPerformed"
          @click="showWordUpdateDialog = true"
        >
          <v-icon start icon="mdi-note-edit-outline"> </v-icon>
          UPDATE
        </v-btn>
        <v-btn
          size="small"
          color="error"
          class="ml-2"
          :disabled="isEmptyObject(selectedWord) || !isSearchPerformed"
          @click="showWordDeleteDialog = true"
        >
          <v-icon start icon="mdi-note-remove-outline"> </v-icon>
          DELETE
        </v-btn>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12">
        <v-breadcrumbs
          v-if="isSearchPerformed"
          :items="[
            { title: searchedLanguage, disabled: false },
            { title: searchedVocabulary.title, disabled: false },
            { title: searchedUnit.subject, disabled: false },
          ]"
          :bg-color="themeStore.theme"
          density="comfortable"
          class="noto-sans text-subtitle-2 font-weight-bold"
        >
          <template v-slot:divider>
            <v-icon icon="mdi-chevron-right"></v-icon>
          </template>
        </v-breadcrumbs>
        <ag-grid-vue
          style="width: 100%; height: 312px"
          class="ag-theme-alpine noto-sans"
          :columnDefs="columnDefs"
          :defaultColDef="defaultColDef"
          :rowData="rowData"
          :pagination="true"
          :paginationPageSize="5"
          rowSelection="single"
          @selectionChanged="onSelectionChanged"
          @grid-ready="onGridReady"
        >
        </ag-grid-vue>
      </v-col>
    </v-row>

    <!-- 단어 등록 다이얼로그 -->
    <AdminWordGridAddDialog
      v-model="showWordAddDialog"
      :unitId="searchedUnit.id"
      :searchedLanguage="wordGridManager.searchedLanguage"
      @success="fetchRowData(searchedUnit.id)"
    />

    <!-- 단어 수정 다이얼로그 -->
    <AdminWordGridUpdateDialog
      v-model="showWordUpdateDialog"
      :selectedWord="selectedWord"
      :searchedLanguage="wordGridManager.searchedLanguage"
      @success="fetchRowData(searchedUnit.id)"
    />

    <!-- 단어 삭제 다이얼로그 -->
    <AdminWordGridDeleteDialog
      v-model="showWordDeleteDialog"
      :selectedWord="selectedWord"
      :searchedLanguage="wordGridManager.searchedLanguage"
      @success="fetchRowData(searchedUnit.id)"
    />
  </v-container>
</template>

<script setup>
import AdminWordGridAddDialog from "./AdminWordGridAddDialog.vue";
import AdminWordGridUpdateDialog from "./AdminWordGridUpdateDialog.vue";
import AdminWordGridDeleteDialog from "./AdminWordGridDeleteDialog.vue";

// AG Grid Vue
import { useThemeStore } from "@/stores/theme";
import { AgGridVue } from "ag-grid-vue3";
import { inject, ref, toRefs } from "vue";
import { wordService } from "@/service/wordService";
import { LanguageType } from "@/common/enum/languageType";

import { commonUtils } from "@/common/commonUtils";

// Utils
const { isEmptyObject } = commonUtils;

import { useCommonMessageDialogStore } from "@/stores/commonMessageDialog";

const commonMessageDialogStore = useCommonMessageDialogStore();
const { showCommonMessageDialog } = commonMessageDialogStore;

const themeStore = useThemeStore();

const emit = defineEmits(["handleLanguageChange", "handleVocabularyChange"]);

const wordGridManager = inject("wordGridManager");

const languageSelectManager = inject("languageSelectManager");
const vocabularySelectManager = inject("vocabularySelectManager");
const unitSelectManager = inject("unitSelectManager");

// readonly
const {
  isSearchPerformed,
  rowData,
  selectedWord,
  searchedLanguage,
  searchedVocabulary,
  searchedUnit,
} = toRefs(wordGridManager);

const showWordAddDialog = ref(false);
const showWordUpdateDialog = ref(false);
const showWordDeleteDialog = ref(false);

const performSearch = () => {
  if (!vocabularySelectManager.selectedItem.id) {
    showCommonMessageDialog("선택된 단어장이 없습니다.");
    return;
  }
  if (!unitSelectManager.selectedItem.id) {
    showCommonMessageDialog("선택된 유닛이 없습니다.");
    return;
  }
  clearWordGridManagerSelectedWord();

  fetchRowData(unitSelectManager.selectedItem.id);

  wordGridManager.searchedLanguage = languageSelectManager.selectedItem;

  if (wordGridManager.searchedLanguage == LanguageType.ENGLISH) {
    columnDefs.value[4].hide = true;
  } else if (wordGridManager.searchedLanguage == LanguageType.JAPANESE) {
    columnDefs.value[4].hide = false;
  }

  wordGridManager.searchedVocabulary.id = vocabularySelectManager.selectedItem.id;

  wordGridManager.searchedVocabulary.title = vocabularySelectManager.selectedItem.title;

  wordGridManager.searchedUnit.id = unitSelectManager.selectedItem.id;
  wordGridManager.searchedUnit.subject = unitSelectManager.selectedItem.subject;

  isSearchPerformed.value = true;
};

const clearWordGridManagerSelectedWord = () => {
  wordGridManager.selectedWord = {};
};

const onSelectionChanged = (e) => {
  // 체크했다가 풀었을 경우엔 초기화 처리
  if (e.api.getSelectedNodes().length == 0) {
    wordGridManager.selectedWord = {};
    return;
  }

  const selectedNodes = e.api.getSelectedNodes();

  // 단일 선택 이므로 항상 0번 Index에만 데이터가 있음.
  const selectedData = selectedNodes.map((node) => node.data)[0];

  Object.assign(wordGridManager.selectedWord, {
    id: selectedData.id,
    studyWord: selectedData.studyWord,
    nativeWord: selectedData.nativeWord,
    furigana: selectedData.furigana,
    partOfSpeech: selectedData.partOfSpeech,
    incorrectCount: selectedData.incorrectCount,
    correctCount: selectedData.correctCount,
  });
};

const fetchRowData = async (unitId) => {
  const pageableParams = { page: 0, size: 2000 };
  const unitPageable = await wordService.getPageable(unitId, pageableParams);

  wordGridManager.rowData = unitPageable.content;
  wordGridManager.selectedUnit = {};
};

const gridApi = ref(null);

const onGridReady = (params) => {
  gridApi.value = params.api;
};

const defaultColDef = {
  resizable: true,
  sortable: true,
  filter: true,
  floatingFilter: false,
};

const columnDefs = ref([
  {
    headerName: "선택",
    width: 70,
    pinned: "right",
    headerCheckboxSelection: false, // 헤더 체크박스 disable
    checkboxSelection: true,
    resizable: false,
    sortable: false,
    filter: false,
  },
  {
    headerName: "#",
    width: 70,
    filter: false,
    resizable: false,
    valueGetter: (params) => {
      return rowData.value.length - params.node.rowIndex;
    },
  },
  {
    headerName: "ID",
    field: "id",
    width: 70,
    filter: false,
    sortable: false,
    resizable: false,
    hide: true,
  },
  {
    headerName: "학습 단어",
    field: "studyWord",
    width: 155,
  },
  {
    headerName: "후리가나",
    field: "furigana",
    width: 155,
    hide: true,
  },
  {
    headerName: "모국 단어",
    field: "nativeWord",
    width: 155,
  },
  {
    headerName: "품사",
    field: "partOfSpeech",
    width: 130,
  },
  {
    headerName: "등록일",
    field: "createdAt",
    width: 160,
  },
  {
    headerName: "수정일",
    field: "modifiedAt",
    width: 160,
  },
]);
</script>

<style scoped></style>
