<template>
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
        rowSelection="multiple"
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
    headerCheckboxSelection: true, // 헤더 체크박스 enable
    checkboxSelection: true,
    sortable: false,
  },
  {
    headerName: "상태",
    field: "status",
    pinned: true,
    width: 70,
  },
  {
    headerName: "제목",
    field: "title",
    width: 206,
    resizable: true,
    sortable: true,
    filter: true,
    editable: true,
  },
  {
    headerName: "언어",
    field: "language",
    width: 120,
    sortable: true,
    filter: true,
    editable: true,
    cellEditor: "agSelectCellEditor",
    cellEditorParams: {
      values: ["English", "Japanese"],
    },
  },
];

const rowData = ref([
  {
    title: "단어가 읽기다 기본편",
    language: "English",
    status: "",
  },
  { title: "단어가 읽기다 실력편", language: "English", status: "" },
  { title: "일본어 JLPT N5급", language: "Japanese", status: "" },
]);
</script>

<style scoped></style>
