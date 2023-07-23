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
          @update:model-value="
            (changedLanguage) => emit('handleLanguageChange', changedLanguage)
          "
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
        >
        </v-select>
      </v-col>
    </v-row>
    <v-row class="mt-5">
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
          :disabled="!isSearchPerformed"
          @click="showUnitAddDialog = true"
        >
          <v-icon start icon="mdi-note-plus-outline"> </v-icon>
          ADD
        </v-btn>
        <v-btn
          size="small"
          color="info"
          class="ml-2"
          :disabled="isEmptyObject(selectedUnit) || !isSearchPerformed"
          @click="showUnitUpdateDialog = true"
        >
          <v-icon start icon="mdi-note-edit-outline"> </v-icon>
          UPDATE
        </v-btn>
        <v-btn
          size="small"
          color="error"
          class="ml-2"
          :disabled="isEmptyObject(selectedUnit) || !isSearchPerformed"
          @click="showUnitDeleteDialog = true"
        >
          <v-icon start icon="mdi-note-remove-outline"> </v-icon>
          DELETE
        </v-btn>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12">
        <div :class="`text-${themeStore.theme} mb-1`" v-if="isSearchPerformed">
          <p class="noto-sans">
            <span class="me-2">【 </span>
            <span> {{ searchedLanguage }} </span>
            <span class="ms-2"> 】</span>
            <span class="ms-2 me-2"> 》</span>
            <span class="me-2">【 </span>
            <span class="me-2">{{ searchedVocabulary.title }}</span>
            <span> 】</span>
          </p>
        </div>
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

    <!-- 단위 등록 다이얼로그 -->
    <AdminUnitGridAddDialog
      v-model="showUnitAddDialog"
      :vocabularyId="searchedVocabulary.id"
      @success="fetchRowData(searchedVocabulary.id)"
    />

    <!-- 단위 수정 다이얼로그 -->
    <AdminUnitGridUpdateDialog
      v-model="showUnitUpdateDialog"
      :selectedUnit="selectedUnit"
      @success="fetchRowData(searchedVocabulary.id)"
    />

    <!-- 단위 삭제 다이얼로그 -->
    <AdminUnitGridDeleteDialog
      v-model="showUnitDeleteDialog"
      :selectedUnit="selectedUnit"
      @success="fetchRowData(searchedVocabulary.id)"
    />
  </v-container>
</template>

<script setup>
import AdminUnitGridAddDialog from "./AdminUnitGridAddDialog.vue";
import AdminUnitGridUpdateDialog from "./AdminUnitGridUpdateDialog.vue";
import AdminUnitGridDeleteDialog from "./AdminUnitGridDeleteDialog.vue";

// AG Grid Vue
import { AgGridVue } from "ag-grid-vue3";

import { commonUtils } from "@/common/commonUtils";
import { useThemeStore } from "@/stores/theme";
import { inject, ref, toRefs } from "vue";

import { unitService } from "@/service/unitService";

// Utils
const { isEmptyObject } = commonUtils;

const themeStore = useThemeStore();

const emit = defineEmits(["handleLanguageChange"]);

const unitGridManager = inject("unitGridManager");

const languageSelectManager = inject("languageSelectManager");
const vocabularySelectManager = inject("vocabularySelectManager");

// readonly
const {
  isSearchPerformed,
  rowData,
  selectedUnit,
  searchedVocabulary,
  searchedLanguage,
} = toRefs(unitGridManager);

const showUnitAddDialog = ref(false);
const showUnitUpdateDialog = ref(false);
const showUnitDeleteDialog = ref(false);

const performSearch = async () => {
  await fetchRowData(vocabularySelectManager.selectedItem.id);

  unitGridManager.searchedLanguage = languageSelectManager.selectedItem;

  unitGridManager.searchedVocabulary.title =
    vocabularySelectManager.selectedItem.title;

  unitGridManager.searchedVocabulary.id =
    vocabularySelectManager.selectedItem.id;

  unitGridManager.isSearchPerformed = true;
};

const fetchRowData = async (vocabularyId) => {
  const pageableParams = { page: 0, size: 2000 };

  const unitPageable = await unitService.getPageable(
    vocabularyId,
    pageableParams
  );

  unitGridManager.rowData = unitPageable.content;
  unitGridManager.selectedUnit = {};
};

const gridApi = ref(null);

const onGridReady = (params) => {
  gridApi.value = params.api;
};

const onSelectionChanged = (e) => {
  // 체크했다가 풀었을 경우엔 초기화 처리
  if (e.api.getSelectedNodes().length == 0) {
    unitGridManager.selectedUnit = {};
    return;
  }

  const selectedNodes = e.api.getSelectedNodes();

  // 단일 선택 이므로 항상 0번 Index에만 데이터가 있음.
  const selectedData = selectedNodes.map((node) => node.data)[0];

  Object.assign(unitGridManager.selectedUnit, {
    id: selectedData.id,
    subject: selectedData.subject,
    wordCount: selectedData.wordCount,
    readCount: selectedData.readCount,
  });
};

const defaultColDef = {
  resizable: true,
  sortable: true,
  filter: true,
  floatingFilter: false,
};

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
    resizable: false,
    filter: false,
    valueGetter: (params) => {
      return rowData.value.length - params.node.rowIndex;
    },
  },
  {
    headerName: "ID",
    field: "id",
    width: 70,
    resizable: false,
    sortable: false,
    filter: false,
    hide: true,
  },
  {
    headerName: "주제",
    field: "subject",
    width: 164,
  },
  {
    headerName: "단어 개수",
    field: "wordCount",
    width: 116,
    cellRenderer: (params) => {
      return params.value + "개";
    },
  },
  {
    headerName: "회독수",
    field: "readCount",
    width: 116,
    cellRenderer: (params) => {
      if (!params.value) {
        return "0회독";
      }
      return params.value + "회독";
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
