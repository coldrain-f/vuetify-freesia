<template>
  <v-container>
    <v-row>
      <v-col cols="4">
        <v-btn size="small" class="w-100" @click="performSearch">
          <v-icon start icon="mdi-note-search-outline"> </v-icon>
          SEARCH
        </v-btn>
      </v-col>
      <v-col cols="8" class="text-end">
        <v-btn
          size="small"
          color="primary"
          @click="showAddDialog = true"
          :disabled="!isSearchPerformed"
        >
          <v-icon start icon="mdi-note-plus-outline"> </v-icon>
          ADD
        </v-btn>
        <v-btn
          size="small"
          color="info"
          class="ml-2"
          @click="showUpdateDialog = true"
          :disabled="isEmptyObject(selectedVocabulary) || !isSearchPerformed"
        >
          <v-icon start icon="mdi-note-edit-outline"></v-icon>
          UPDATE
        </v-btn>

        <v-btn
          size="small"
          color="error"
          class="ml-2"
          @click="showDeleteDialog = true"
          :disabled="isEmptyObject(selectedVocabulary) || !isSearchPerformed"
        >
          <v-icon start icon="mdi-note-remove-outline"> </v-icon>
          DELETE
        </v-btn>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12">
        <ag-grid-vue
          style="width: 100%; height: 312px"
          class="ag-theme-alpine noto-sans"
          :columnDefs="columnDefs"
          :rowData="rowData"
          :defaultColDef="defaultColDef"
          :pagination="true"
          :paginationPageSize="5"
          rowSelection="single"
          @selectionChanged="onSelectionChanged"
          @grid-ready="onGridReady"
        >
        </ag-grid-vue>
      </v-col>
    </v-row>

    <!-- 단어장 등록 다이얼로그 -->
    <AdminVocabularyGridAddDialog
      v-model="showAddDialog"
      @success="fetchRowData"
    />

    <!-- 단어장 수정 다이얼로그 -->
    <AdminVocabularyGridUpdateDialog
      v-model="showUpdateDialog"
      :selectedVocabulary="selectedVocabulary"
      @success="fetchRowData"
    />

    <!-- 단어장 삭제 다이얼로그 -->
    <AdminVocabularyGridDeleteDialog
      v-model="showDeleteDialog"
      :selectedVocabulary="selectedVocabulary"
      @success="fetchRowData"
    />
  </v-container>
</template>

<script setup>
import AdminVocabularyGridAddDialog from "./AdminVocabularyGridAddDialog.vue";
import AdminVocabularyGridUpdateDialog from "./AdminVocabularyGridUpdateDialog.vue";
import AdminVocabularyGridDeleteDialog from "./AdminVocabularyGridDeleteDialog.vue";

import { vocabularyService } from "@/service/vocabularyService";

// AG Grid Vue
import { AgGridVue } from "ag-grid-vue3";

import { inject, ref, toRefs } from "vue";

// Utils
import { commonUtils } from "@/common/commonUtils";

const { isEmptyObject } = commonUtils;

const gridApi = ref(null);

const onGridReady = (params) => {
  gridApi.value = params.api;
};

const vocabularyGridManager = inject("vocabularyGridManager");

// reactive 구조 분해 할당 -> readonly
const { isSearchPerformed, rowData, selectedVocabulary } = toRefs(
  vocabularyGridManager
);

const performSearch = async () => {
  await fetchRowData();

  vocabularyGridManager.selectedVocabulary = {};
  vocabularyGridManager.isSearchPerformed = true;
};

const fetchRowData = async () => {
  const vocabularyPageable = await vocabularyService.getPageable({
    page: 0,
    size: 2000,
  });

  vocabularyGridManager.rowData = vocabularyPageable.content;
  vocabularyGridManager.selectedVocabulary = {};
};

// CRUD Dialogs
const showAddDialog = ref(false);
const showUpdateDialog = ref(false);
const showDeleteDialog = ref(false);

const onSelectionChanged = (e) => {
  // 체크했다가 풀었을 경우엔 초기화 처리
  if (e.api.getSelectedNodes().length == 0) {
    vocabularyGridManager.selectedVocabulary = {};
    return;
  }

  const selectedNodes = e.api.getSelectedNodes();

  // 단일 선택 이므로 항상 0번 Index에만 데이터가 있음.
  const selectedData = selectedNodes.map((node) => node.data)[0];

  Object.assign(vocabularyGridManager.selectedVocabulary, {
    id: selectedData.id,
    title: selectedData.title,
    language: selectedData.language,
    unitCount: selectedData.unitCount,
  });
};

const defaultColDef = ref({
  resizable: true,
  sortable: true,
  filter: true,
  floatingFilter: false,
});

const columnDefs = [
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
    resizable: false,
    sortable: false,
    hide: true,
  },
  {
    headerName: "제목",
    field: "title",
    width: 210,
  },
  {
    headerName: "언어",
    field: "language",
    width: 130,
    cellEditor: "agSelectCellEditor",
    cellEditorParams: {
      values: ["English", "Japanese"],
    },
    cellRenderer: (params) => {
      let url = "https://flags.fmcdn.net/data/flags/mini/";

      if (params.value == "English") {
        url += "us.png";
      } else if (params.value == "Japanese") {
        url += "jp.png";
      }

      const flagImage = `<img class="flag" border="0" width="15" height="10" src="${url}">`;
      return flagImage + " " + params.value;
    },
  },
  {
    headerName: "단위 개수",
    field: "unitCount",
    width: 130,
    cellRenderer: (params) => {
      return params.value + "개";
    },
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
];
</script>

<style scoped></style>
