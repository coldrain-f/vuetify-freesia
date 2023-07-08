<template>
  <v-row>
    <v-col cols="4">
      <v-btn size="small" color="primary" class="w-100">
        <v-icon start icon="mdi-book-search-outline"></v-icon>
        SEARCH
      </v-btn>
    </v-col>
    <v-col cols="8" class="text-end">
      <v-btn size="small" color="info">
        <v-icon start icon="mdi-book-plus-outline"></v-icon>
        ADD
      </v-btn>
      <v-btn size="small" color="warning" class="ml-2">
        <v-icon start icon="mdi-book-edit-outline"></v-icon>
        UPDATE
      </v-btn>
      <v-btn size="small" color="error" class="ml-2">
        <v-icon start icon="mdi-book-remove-outline"></v-icon>
        DELETE
      </v-btn>
    </v-col>
  </v-row>
  <v-row>
    <v-col cols="12">
      <ag-grid-vue
        style="width: 100%; height: 310px"
        class="ag-theme-alpine"
        :columnDefs="columnDefs"
        :rowData="rowData"
        :defaultColDef="defaultColDef"
        :pagination="true"
        :paginationPageSize="5"
        rowSelection="single"
      >
      </ag-grid-vue>
    </v-col>
  </v-row>
</template>

<script setup>
// AG Grid Vue
import { AgGridVue } from "ag-grid-vue3"; // the AG Grid Vue Component

import { ref } from "vue";

const defaultColDef = ref({
  sortable: false,
  filter: false,
});

const columnDefs = [
  {
    headerName: "선택",
    width: 70,
    pinned: true,
    headerCheckboxSelection: false, // 헤더 체크박스 disable
    checkboxSelection: true,
    sortable: false,
  },
  {
    headerName: "제목",
    field: "title",
    width: 210,
    resizable: true,
    sortable: true,
    filter: true,
  },
  {
    headerName: "언어",
    field: "language",
    width: 120,
    resizable: true,
    sortable: true,
    filter: true,
    cellEditor: "agSelectCellEditor",
    cellEditorParams: {
      values: ["English", "Japanese"],
    },
  },
  {
    headerName: "단위 개수",
    field: "unitCount",
    width: 130,
    resizable: true,
    sortable: true,
    filter: true,
  },
];

const rowData = ref([
  {
    title: "단어가 읽기다 기본편",
    language: "English",
    unitCount: "0개",
  },
  { title: "단어가 읽기다 실력편", language: "English", unitCount: "1개" },
  { title: "일본어 JLPT N5급", language: "Japanese", unitCount: "2개" },
]);
</script>

<style scoped></style>