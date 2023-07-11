<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <v-select
          label="Voca"
          variant="underlined"
          density="compact"
          :modelValue="{ title: '단어가 읽기다 기본편', vocabularyId: 1 }"
          :items="[
            { title: '단어가 읽기다 기본편', vocabularyId: 1 },
            { title: '단어가 읽기다 실력편', vocabularyId: 2 },
          ]"
          item-title="title"
          item-value="vocabularyId"
          hide-details
          return-object
          persistent-hint
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
          <v-icon start icon="mdi-note-plus-outline" style="margin-top: 1px">
          </v-icon>
          ADD
        </v-btn>
        <v-btn
          size="small"
          color="info"
          class="ml-2"
          :disabled="isEmptyObject(selectedUnit) || !isSearchPerformed"
        >
          <v-icon start icon="mdi-note-edit-outline"> </v-icon>
          UPDATE
        </v-btn>
        <v-btn
          size="small"
          color="error"
          class="ml-2"
          :disabled="isEmptyObject(selectedUnit) || !isSearchPerformed"
        >
          <v-icon start icon="mdi-note-remove-outline"> </v-icon>
          DELETE
        </v-btn>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12">
        <h4 :class="`text-${themeStore.theme}`">【 단어가 읽기다 기본편 】</h4>
        <ag-grid-vue
          style="width: 100%; height: 310px"
          class="ag-theme-alpine"
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

    <AdminUnitGridAddDialog v-model="showUnitAddDialog" />
  </v-container>
</template>

<script setup>
import AdminUnitGridAddDialog from "./AdminUnitGridAddDialog.vue";

// AG Grid Vue
import { useThemeStore } from "@/stores/theme";
import { AgGridVue } from "ag-grid-vue3";
import { commonUtils } from "@/common/commonUtils";
import { inject, ref, toRefs } from "vue";

// Utils
const { isEmptyObject } = commonUtils;

const themeStore = useThemeStore();

const unitGridManager = inject("unitGridManager");

// readonly
const { isSearchPerformed, rowData, selectedUnit } = toRefs(unitGridManager);

const showUnitAddDialog = ref(false);
const showUnitUpdateDialog = ref(false);
const showUnitDeleteDialog = ref(false);

const performSearch = () => {
  unitGridManager.rowData = fetchData();
  unitGridManager.isSearchPerformed = true;
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
    subject: selectedData.subject,
    wordCount: selectedData.wordCount,
    readCount: selectedData.readCount,
  });
};

const defaultColDef = {
  resizable: true,
  sortable: true,
  filter: true,
};

const columnDefs = [
  {
    headerName: "선택",
    width: 70,
    pinned: true,
    headerCheckboxSelection: false, // 헤더 체크박스 disable
    checkboxSelection: true,
    resizable: false,
    sortable: false,
    filter: false,
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
      return params.value + "회독";
    },
  },
  {
    headerName: "등록일",
    field: "createdAt",
    width: 130,
  },
  {
    headerName: "수정일",
    field: "modifiedAt",
    width: 130,
  },
];

const fetchData = () => {
  return [
    {
      subject: "요리",
      wordCount: 0,
      readCount: 0,
      createdAt: new Intl.DateTimeFormat("ko-KR").format(new Date()),
      modifiedAt: new Intl.DateTimeFormat("ko-KR").format(new Date()),
    },
    {
      subject: "일상 1",
      wordCount: 0,
      readCount: 0,
      createdAt: new Intl.DateTimeFormat("ko-KR").format(new Date()),
      modifiedAt: new Intl.DateTimeFormat("ko-KR").format(new Date()),
    },
    {
      subject: "일상 2",
      wordCount: 0,
      readCount: 0,
      createdAt: new Intl.DateTimeFormat("ko-KR").format(new Date()),
      modifiedAt: new Intl.DateTimeFormat("ko-KR").format(new Date()),
    },
    {
      subject: "개인",
      wordCount: 0,
      readCount: 0,
      createdAt: new Intl.DateTimeFormat("ko-KR").format(new Date()),
      modifiedAt: new Intl.DateTimeFormat("ko-KR").format(new Date()),
    },
    {
      subject: "신체",
      wordCount: 0,
      readCount: 0,
      createdAt: new Intl.DateTimeFormat("ko-KR").format(new Date()),
      modifiedAt: new Intl.DateTimeFormat("ko-KR").format(new Date()),
    },
    {
      subject: "취미 1",
      wordCount: 0,
      readCount: 0,
      createdAt: new Intl.DateTimeFormat("ko-KR").format(new Date()),
      modifiedAt: new Intl.DateTimeFormat("ko-KR").format(new Date()),
    },
  ];
};
</script>

<style scoped></style>
